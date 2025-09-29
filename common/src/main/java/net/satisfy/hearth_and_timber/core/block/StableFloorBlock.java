package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class StableFloorBlock extends Block {
    public static final MapCodec<StableFloorBlock> CODEC = simpleCodec(StableFloorBlock::new);
    public static final IntegerProperty WEAR = IntegerProperty.create("wear", 0, 27);
    public static final BooleanProperty SEALED = BooleanProperty.create("sealed");
    private Supplier<Block> trampled;
    private Supplier<Block> straw;

    public StableFloorBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WEAR, 0).setValue(SEALED, false));
    }

    public void setTrampled(Supplier<Block> s) {
        trampled = s;
    }

    public void setStraw(Supplier<Block> s) {
        straw = s;
    }

    @Override
    public @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(WEAR, SEALED);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity && !state.getValue(SEALED)) {
            long t = level.getGameTime();
            if (((t + pos.asLong()) & 7L) == 0L) {
                RandomSource r = level.getRandom();
                if (r.nextFloat() < 0.15f) {
                    int w = state.getValue(WEAR);
                    if (w >= 26 && trampled != null) {
                        level.setBlock(pos, trampled.get().defaultBlockState(), 3);
                    } else {
                        level.setBlock(pos, state.setValue(WEAR, Math.min(27, w + 1)), 2);
                    }
                }
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return super.getStateForPlacement(ctx);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction dir, BlockState other, LevelAccessor level, BlockPos pos, BlockPos fromPos) {
        if (!state.getValue(SEALED) && straw != null && other.is(Blocks.HAY_BLOCK)) {
            level.scheduleTick(pos, this, 1);
        }
        return super.updateShape(state, dir, other, level, pos, fromPos);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(SEALED) || straw == null) return;
        tryStrawTransform(level, pos);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(SEALED);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (straw == null || state.getValue(SEALED)) return;
        tryStrawTransform(level, pos);
    }

    private void tryStrawTransform(ServerLevel level, BlockPos pos) {
        List<BlockPos> hayPositions = new ArrayList<>();
        for (Direction d : Direction.values()) {
            BlockPos p = pos.relative(d);
            if (level.getBlockState(p).is(Blocks.HAY_BLOCK)) hayPositions.add(p);
        }
        for (BlockPos hay : hayPositions) {
            List<BlockPos> strawList = new ArrayList<>();
            List<BlockPos> candidate = new ArrayList<>();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos n = hay.offset(dx, 0, dz);
                    BlockState s = level.getBlockState(n);
                    if (s.is(straw.get())) strawList.add(n);
                    else if (s.is(this)) candidate.add(n);
                }
            }
            int quota = 3 - strawList.size();
            if (quota <= 0) continue;
            candidate.sort(Comparator.comparingLong(BlockPos::asLong));
            for (int i = 0; i < Math.min(quota, candidate.size()); i++) {
                if (candidate.get(i).equals(pos)) {
                    level.setBlock(pos, straw.get().defaultBlockState(), 3);
                    return;
                }
            }
        }
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.player.Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof ShovelItem || stack.getItem() instanceof HoeItem) {
            boolean sealed = state.getValue(SEALED);
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(SEALED, !sealed), 3);
            } else {
                RandomSource r = level.getRandom();
                for (int i = 0; i < 12; i++) {
                    double x = pos.getX() + 0.5 + (r.nextDouble() - 0.5) * 0.4;
                    double y = pos.getY() + 1.02;
                    double z = pos.getZ() + 0.5 + (r.nextDouble() - 0.5) * 0.4;
                    double vx = (r.nextDouble() - 0.5) * 0.06;
                    double vy = 0.15 + r.nextDouble() * 0.2;
                    double vz = (r.nextDouble() - 0.5) * 0.06;
                    level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), x, y, z, vx, vy, vz);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
