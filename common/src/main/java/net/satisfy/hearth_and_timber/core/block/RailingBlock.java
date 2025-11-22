package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RailingBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<RailingBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BlockState.CODEC.fieldOf("base_state").forGetter(b -> b.baseState), propertiesCodec()).apply(i, RailingBlock::new));
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape[] INNER_LEFT_SHAPES = new VoxelShape[4];
    protected static final VoxelShape[] INNER_RIGHT_SHAPES = new VoxelShape[4];
    protected static final VoxelShape[] FULL_SHAPES = new VoxelShape[4];
    protected static final VoxelShape[] OUTER_LEFT_SHAPES = new VoxelShape[4];
    protected static final VoxelShape[] OUTER_RIGHT_SHAPES = new VoxelShape[4];
    public static final EnumProperty<Size> SIZE = EnumProperty.create("size", Size.class);

    private final Block base;
    protected final BlockState baseState;

    static {
        double[][] full = new double[][]{
                {0.75, 0.0, 0.8125, 0.9375, 0.75, 1.0},
                {0.0625, 0.0, 0.8125, 0.25, 0.75, 1.0},
                {0.40625, 0.0, 0.8125, 0.59375, 0.75, 1.0},
                {0.0, 0.75, 0.75, 1.0, 1.0, 1.0}
        };
        double[][] outer = new double[][]{
                {0.0, 0.75, 0.0, 0.25, 1.0, 0.25}
        };
        double[][] inner = new double[][]{
                {0.75, 0.0, 0.0, 0.9375, 0.75, 0.1875},
                {0.0, 0.0, 0.75, 0.1875, 0.75, 0.9375},
                {0.0625, 0.0, 0.0625, 0.25, 0.75, 0.25},
                {0.40625, 0.0, 0.0, 0.59375, 0.75, 0.1875},
                {0.0, 0.0, 0.40625, 0.1875, 0.75, 0.59375},
                {0.0, 0.75, 0.0, 1.0, 1.0, 0.25},
                {0.0, 0.75, 0.25, 0.25, 1.0, 1.0}
        };

        for (int f = 0; f < 4; f++) {
            FULL_SHAPES[f] = buildRotated(full, f);
            int r = rotFromSouth(f);
            INNER_LEFT_SHAPES[f] = buildRotated(inner, r);
            INNER_RIGHT_SHAPES[f] = buildRotated(inner, (r + 1) & 3);
            OUTER_LEFT_SHAPES[f] = buildRotated(outer, r);
            OUTER_RIGHT_SHAPES[f] = buildRotated(outer, r);
        }

        int s = Direction.SOUTH.get2DDataValue();
        int n = Direction.NORTH.get2DDataValue();
        int w = Direction.WEST.get2DDataValue();
        int e = Direction.EAST.get2DDataValue();
        OUTER_LEFT_SHAPES[e] = buildRotated(outer, (rotFromSouth(e) + 2) & 3);
        OUTER_RIGHT_SHAPES[w] = buildRotated(outer, (rotFromSouth(w) + 1) & 3);
        OUTER_RIGHT_SHAPES[n] = buildRotated(outer, (rotFromSouth(n) + 1) & 3);
        OUTER_RIGHT_SHAPES[e] = buildRotated(outer, (rotFromSouth(e) + 1) & 3);
        OUTER_RIGHT_SHAPES[s] = buildRotated(outer, (rotFromSouth(s) + 1) & 3);
    }

    private static int rotFromSouth(int facingIndex) {
        if (facingIndex == 0) return 2;
        if (facingIndex == 1) return 3;
        if (facingIndex == 2) return 0;
        return 1;
    }

    private static VoxelShape buildRotated(double[][] boxes, int rot) {
        VoxelShape shape = Shapes.empty();
        for (double[] b : boxes) {
            double[] t = rotateBox(b, rot);
            shape = Shapes.join(shape, Shapes.box(t[0], t[1], t[2], t[3], t[4], t[5]), BooleanOp.OR);
        }
        return shape;
    }

    private static double[] rotateBox(double[] b, int rot) {
        double x1 = b[0], y1 = b[1], z1 = b[2], x2 = b[3], y2 = b[4], z2 = b[5];
        if (rot == 0) return new double[]{x1, y1, z1, x2, y2, z2};
        if (rot == 1) return new double[]{1.0 - z2, y1, x1, 1.0 - z1, y2, x2};
        if (rot == 2) return new double[]{1.0 - x2, y1, 1.0 - z2, 1.0 - x1, y2, 1.0 - z1};
        return new double[]{z1, y1, 1.0 - x2, z2, y2, 1.0 - x1};
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        int f = state.getValue(FACING).get2DDataValue();
        StairsShape s = state.getValue(SHAPE);
        Size size = state.getValue(SIZE);

        if (size == Size.HALF) {
            if (s == StairsShape.INNER_LEFT || s == StairsShape.INNER_RIGHT) return rotateInnerHalfShape(f, s);
            if (s == StairsShape.OUTER_LEFT || s == StairsShape.OUTER_RIGHT) return rotateOuterHalfShape(f, s);
            return rotateHalfShape(f);
        }

        if (size == Size.QUARTER) {
            if (s == StairsShape.INNER_LEFT || s == StairsShape.INNER_RIGHT) return rotateInnerQuarterShape(f, s);
            if (s == StairsShape.OUTER_LEFT || s == StairsShape.OUTER_RIGHT) return rotateOuterQuarterShape(f, s);
            return rotateQuarterShape(f);
        }

        if (s == StairsShape.OUTER_LEFT) return OUTER_LEFT_SHAPES[f];
        if (s == StairsShape.OUTER_RIGHT) return OUTER_RIGHT_SHAPES[f];
        if (s == StairsShape.INNER_LEFT) return INNER_LEFT_SHAPES[f];
        if (s == StairsShape.INNER_RIGHT) return INNER_RIGHT_SHAPES[f];

        return FULL_SHAPES[f];
    }

    private static VoxelShape rotateHalfShape(int rot) {
        double[][] boxes = {
                {0.75, 0, 0, 0.9375, 0.5, 0.1875},
                {0.0625, 0, 0, 0.25, 0.5, 0.1875},
                {0.40625, 0, 0, 0.59375, 0.5, 0.1875},
                {0, 0.5, 0, 1, 0.75, 0.25}
        };
        return buildRotated(boxes, (rot + 2) & 3);
    }

    private static VoxelShape rotateQuarterShape(int rot) {
        double[][] boxes = {
                {0.75, 0, 0, 0.9375, 0.25, 0.1875},
                {0.0625, 0, 0, 0.25, 0.25, 0.1875},
                {0.40625, 0, 0, 0.59375, 0.25, 0.1875},
                {0, 0.25, 0, 1, 0.5, 0.25}
        };
        return buildRotated(boxes, (rot + 2) & 3);
    }

    private static VoxelShape rotateInnerHalfShape(int rot, StairsShape s) {
        double[][] boxes = {
                {0.75, 0, 0, 0.9375, 0.5, 0.1875},
                {0, 0, 0.75, 0.1875, 0.5, 0.9375},
                {0.0625, 0, 0.0625, 0.25, 0.5, 0.25},
                {0.40625, 0, 0, 0.59375, 0.5, 0.1875},
                {0, 0, 0.40625, 0.1875, 0.5, 0.59375},
                {0, 0.5, 0, 1, 0.75, 0.25},
                {0, 0.5, 0.25, 0.25, 0.75, 1}
        };
        int baseRot = rotFromSouth(rot);
        int finalRot = s == StairsShape.INNER_RIGHT ? (baseRot + 1) & 3 : baseRot;
        return buildRotated(boxes, finalRot);
    }

    private static VoxelShape rotateInnerQuarterShape(int rot, StairsShape s) {
        double[][] boxes = {
                {0.75, 0, 0, 0.9375, 0.25, 0.1875},
                {0, 0, 0.75, 0.1875, 0.25, 0.9375},
                {0.0625, 0, 0.0625, 0.25, 0.25, 0.25},
                {0.40625, 0, 0, 0.59375, 0.25, 0.1875},
                {0, 0, 0.40625, 0.1875, 0.25, 0.59375},
                {0, 0.25, 0, 1, 0.5, 0.25},
                {0, 0.25, 0.25, 0.25, 0.5, 1}
        };
        int baseRot = rotFromSouth(rot);
        int finalRot = s == StairsShape.INNER_RIGHT ? (baseRot + 1) & 3 : baseRot;
        return buildRotated(boxes, finalRot);
    }

    private static VoxelShape rotateOuterHalfShape(int rot, StairsShape s) {
        double[][] boxes = {
                {0, 0.5, 0, 0.25, 0.75, 0.25}
        };
        int baseRot = rotFromSouth(rot);
        int finalRot = s == StairsShape.OUTER_RIGHT ? (baseRot + 1) & 3 : baseRot;
        return buildRotated(boxes, finalRot);
    }

    private static VoxelShape rotateOuterQuarterShape(int rot, StairsShape s) {
        double[][] boxes = {
                {0, 0.25, 0, 0.25, 0.5, 0.25}
        };
        int baseRot = rotFromSouth(rot);
        int finalRot = s == StairsShape.OUTER_RIGHT ? (baseRot + 1) & 3 : baseRot;
        return buildRotated(boxes, finalRot);
    }

    public @NotNull MapCodec<? extends RailingBlock> codec() {
        return CODEC;
    }

    public RailingBlock(BlockState baseState, BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, false).setValue(SIZE, Size.FULL));
        this.base = baseState.getBlock();
        this.baseState = baseState;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof AxeItem) {
            if (!level.isClientSide) {
                VoxelShape shape = getShape(state, level, pos, CollisionContext.empty());
                AABB box = shape.bounds();
                double cx = pos.getX() + box.minX + (box.maxX - box.minX) / 2.0;
                double cy = pos.getY() + box.minY + (box.maxY - box.minY) / 2.0;
                double cz = pos.getZ() + box.minZ + (box.maxZ - box.minZ) / 2.0;

                ((ServerLevel) level).sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, state),
                        cx, cy, cz,
                        12, 0.1, 0.1, 0.1, 0.05
                );
                level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            Size next = nextSize(state.getValue(SIZE));
            level.setBlock(pos, state.setValue(SIZE, next), Block.UPDATE_ALL_IMMEDIATE);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    public float getExplosionResistance() {
        return this.base.getExplosionResistance();
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        FluidState fluid = ctx.getLevel().getFluidState(pos);
        BlockState state = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        return state.setValue(SHAPE, getStairsShape(state, ctx.getLevel(), pos));
    }

    protected @NotNull BlockState updateShape(BlockState state, Direction dir, BlockState adj, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return dir.getAxis().isHorizontal() ? state.setValue(SHAPE, getStairsShape(state, level, pos)) : super.updateShape(state, dir, adj, level, pos, pos2);
    }

    private static StairsShape getStairsShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction f = state.getValue(FACING);
        BlockState fwd = level.getBlockState(pos.relative(f));
        if (isCompatibleStair(fwd)) {
            Direction f2 = fwd.getValue(FACING);
            if (f2.getAxis() != f.getAxis() && canTakeShape(state, level, pos, f2.getOpposite())) {
                if (f2 == f.getCounterClockWise()) return StairsShape.OUTER_LEFT;
                return StairsShape.OUTER_RIGHT;
            }
        }
        BlockState back = level.getBlockState(pos.relative(f.getOpposite()));
        if (isCompatibleStair(back)) {
            Direction f3 = back.getValue(FACING);
            if (f3.getAxis() != f.getAxis() && canTakeShape(state, level, pos, f3)) {
                if (f3 == f.getCounterClockWise()) return StairsShape.INNER_LEFT;
                return StairsShape.INNER_RIGHT;
            }
        }
        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        BlockState s = level.getBlockState(pos.relative(dir));
        return !isCompatibleStair(s) || s.getValue(FACING) != state.getValue(FACING);
    }

    public static boolean isCompatibleStair(BlockState state) {
        return state.getBlock() instanceof RailingBlock;
    }

    protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        Direction f = state.getValue(FACING);
        StairsShape sh = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                if (f.getAxis() == Axis.Z) {
                    return switch (sh) {
                        case INNER_LEFT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
                break;
            case FRONT_BACK:
                if (f.getAxis() == Axis.X) {
                    return switch (sh) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
        }
        return super.mirror(state, mirror);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING, SHAPE, WATERLOGGED, SIZE);
    }

    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }


    public enum Size implements StringRepresentable {
        FULL, HALF, QUARTER;

        public @NotNull String getSerializedName() {
            return name().toLowerCase();
        }
    }

    private static Size nextSize(Size s) {
        if (s == Size.FULL) return Size.HALF;
        if (s == Size.HALF) return Size.QUARTER;
        return Size.FULL;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        int beige = 0xF5DEB3;
        int gold = 0xFFD700;
        if (!Screen.hasShiftDown()) {
            Component key = Component.literal("[SHIFT]").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(gold)));
            list.add(Component.translatable("tooltip.hearth_and_timber.tooltip_information.hold", key).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
            return;
        }
        list.add(Component.translatable("tooltip.hearth_and_timber.railing.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }
}