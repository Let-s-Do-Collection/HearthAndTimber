package net.satisfy.hearth_and_timber.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;
import org.joml.Vector3f;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class TimberHelper {

    public static boolean isFoundation(ResourceLocation id) {
        return id.getNamespace().equals(HearthAndTimber.MOD_ID) && (
            id.getPath().startsWith("block/timber_foundation") ||
            id.getPath().startsWith("block/timber_base_skirt") ||
            id.getPath().startsWith("block/timber_base_trim")
        );
    }

    public static boolean isConnecting(ResourceLocation id) {
        return id.getNamespace().equals(HearthAndTimber.MOD_ID) && (
                id.getPath().startsWith("block/timber_frame") ||
                id.getPath().startsWith("block/timber_cross_frame") ||
                id.getPath().startsWith("block/timber_diagonal_frame") ||
                id.getPath().startsWith("block/timber_grid_frame")
        );
    }

    public static boolean isConnecting(BlockState state) {
        return state.is(ObjectRegistry.TIMBER_FRAME) ||
               state.is(ObjectRegistry.TIMBER_CROSS_FRAME) ||
               state.is(ObjectRegistry.TIMBER_DIAGONAL_FRAME) ||
               state.is(ObjectRegistry.TIMBER_GRID_FRAME);
    }

    public static Direction getPer1(Direction dir) {
        return switch (dir) {
            case DOWN, UP -> Direction.WEST;

            case NORTH -> Direction.EAST;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
            case EAST -> Direction.SOUTH;
        };
    }

    public static Direction getPer2(Direction dir) {
        return switch (dir) {
            case DOWN -> Direction.SOUTH;
            case UP -> Direction.NORTH;
            case NORTH, SOUTH, WEST, EAST -> Direction.UP;
        };
    }

    public static int getCardinals(BlockAndTintGetter world, BlockState state, BlockPos pos, Direction face) {

        Pair<Direction, Direction> axis = new Pair<>(getPer1(face), getPer2(face)); // LEFT, UP
        Function<Vec3i, Boolean> exists = (vec) -> world.getBlockState(pos.offset(vec))
                .is(state.getBlock());

        Vec3i left = axis.getA().getNormal();
        Vec3i right = left.multiply(-1);
        Vec3i up = axis.getB().getNormal();
        Vec3i down = up.multiply(-1);

        AtomicInteger result = new AtomicInteger();
        // right, left, above, below, top right, top left, bottom right, bottom left
        List.of(
                exists.apply(right),
                exists.apply(left),
                exists.apply(up),
                exists.apply(down),
                exists.apply(right.offset(up)),
                exists.apply(left.offset(up)),
                exists.apply(right.offset(down)),
                exists.apply(left.offset(down))
        ).forEach(b -> result.updateAndGet(v -> (v << 1) | (b ? 1 : 0)));
        return result.get();
    }

    // TODO: Got this from ChatGPT, please rewrite at some point
    public static boolean exists(Vec3i vec, int packed) {
        int index = -1;
        int x = Integer.signum(vec.getX());
        int y = Integer.signum(vec.getY());
        int z = Integer.signum(vec.getZ());

        // cardinals
        if (x != 0 && y == 0 && z == 0) {
            index = (x > 0 ? 0 : 3);
        } else if (y != 0 && x == 0 && z == 0) {
            index = (y > 0 ? 1 : 4);
        } else if (z != 0 && x == 0 && y == 0) {
            index = (z > 0 ? 2 : 5);
        }

        // corners
        else if (x != 0 && y != 0 && z != 0) {
            int ix = (x > 0 ? 0 : 4);
            int iy = (y > 0 ? 0 : 2);
            int iz = (z > 0 ? 1 : 0);
            index = 6 + ix + iy + iz;
        }

        // edges
        else if ((x != 0 && y != 0 && z == 0) || (x != 0 && y == 0 && z != 0) || (x == 0 && y != 0 && z != 0)) {
            for (int i = 0; i < 8; i++) {
                int mx = (i < 4 ? 1 : -1);
                Vec3i primary = (i % 4 < 2 ? new Vec3i(1,0,0) : new Vec3i(0,1,0));
                int sx = (i % 2) * 2 - 1;
                Vec3i v = new Vec3i(1,0,0).multiply(mx).offset(primary.multiply(sx));
                if (Integer.signum(v.getX()) == x && Integer.signum(v.getY()) == y && Integer.signum(v.getZ()) == z) {
                    index = 14 + i;
                    break;
                }
            }
        }

        if (index == -1) return false; // not one of the encoded positions

        return ((packed >> (21 - index)) & 1) == 1;
    }


    public static int[] getFusionTexUV(int adjacents) {
        int cardinals = adjacents >> 4;
        int diags = adjacents & 15;

        if (cardinals == 0) return new int[]{0, 0}; // single block

        if (cardinals == 4) return new int[]{3, 0};
        if (cardinals == 2) return new int[]{0, 3};
        if (cardinals == 8) return new int[]{1, 0};
        if (cardinals == 1) return new int[]{0, 1};

        if (cardinals == 12) return new int[]{2, 0};
        if (cardinals == 3) return new int[]{0, 2};
        if (cardinals == 6) {
            if ((diags & 4) == 4) return new int[]{3, 3};
            return new int[]{5, 1};
        }
        if (cardinals == 10) {
            if ((diags & 8) == 8) return new int[]{1, 3};
            return new int[]{4, 1};
        }
        if (cardinals == 9) {
            if ((diags & 2) == 2) return new int[]{1, 1};
            return new int[]{4, 0};
        }
        if (cardinals == 5) {
            if ((diags & 1) == 1) return new int[]{3, 1};
            return new int[]{5, 0};
        }
        if ((cardinals & 6) == 0) {
            if ((diags & 10) == 10) return new int[]{1, 2};
            if ((diags & 8) == 8) return new int[]{4, 2};
            if ((diags & 2) == 2) return new int[]{6, 2};
            return new int[]{6, 0};
        }
        if ((cardinals & 2) == 0) {
            if ((diags & 3) == 3) return new int[]{2, 1};
            if ((diags & 1) == 1) return new int[]{7, 2};
            if ((diags & 2) == 2) return new int[]{5, 2};
            return new int[]{7, 0};
        }
        if ((cardinals & 8) == 0) {
            if ((diags & 5) == 5) return new int[]{3, 2};
            if ((diags & 4) == 4) return new int[]{7, 3};
            if ((diags & 1) == 1) return new int[]{5, 3};
            return new int[]{7, 1};
        }
        if ((cardinals & 1) == 0) {
            if ((diags & 12) == 12) return new int[]{2, 3};
            if ((diags & 4) == 4) return new int[]{4, 3};
            if ((diags & 8) == 8) return new int[]{6, 3};
            return new int[]{6, 1};
        }

        if (diags == 15) return new int[]{2, 2};
        if (diags == 11) return new int[]{7, 5};
        if (diags == 7) return new int[]{6, 5};
        if (diags == 14) return new int[]{7, 4};
        if (diags == 13) return new int[]{6, 4};

        if (diags == 9) return new int[]{0, 4};
        if (diags == 6) return new int[]{0, 5};
        if (diags == 3) return new int[]{3, 4};
        if (diags == 5) return new int[]{3, 5};
        if (diags == 12) return new int[]{2, 5};
        if (diags == 10) return new int[]{1, 2};

        if ((diags & 4) == 4) return new int[]{5, 5};
        if ((diags & 8) == 8) return new int[]{4, 5};
        if ((diags & 2) == 2) return new int[]{6, 2};
        if ((diags & 1) == 1) return new int[]{5, 4};
        return new int[]{1, 4};

    }

    public static int getIntermediate(BlockGetter world, BlockPos pos, BlockState toMatch) {
        int output = 0;
        Vec3i x = new Vec3i(1, 0, 0);
        Vec3i y = new Vec3i(0, 1, 0);
        Vec3i z = new Vec3i(0, 0, 1);
        Function<Vec3i, Boolean> exists = (vec) -> world.getBlockState(pos.offset(vec))
                .is(toMatch.getBlock());
        // cardinals
        for (int i = 0; i < 6; i++) {
            output = output << 1 | (exists.apply(
                    List.of(x, y, z).get(i % 3).multiply(i < 3 ? 1 : -1)
            ) ? 1 : 0);
        }
        // corners
        for (int i = 0; i < 8; i++) {
            output = output << 1 | (exists.apply(
                    x.multiply(i < 4 ? 1 : -1)
                    .offset(y.multiply(i % 4 < 2 ? 1 : -1))
                    .offset(z.multiply((i % 2) * 2 - 1))
            ) ? 1 : 0);
        }
        // edges
        for (int i = 0; i < 8; i++) {
            output = output << 1 | (exists.apply(
                    x.multiply(i < 4 ? 1 : -1).offset(
                            (i % 4 < 2 ? x : y).multiply((i % 2) * 2 - 1)
                    )
            ) ? 1 : 0);
        }
        return output;
    }


}
