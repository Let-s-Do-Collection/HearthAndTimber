package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FrameworkBlock extends Block implements SimpleWaterloggedBlock {
    public static final IntegerProperty DISTANCE = BlockStateProperties.STABILITY_DISTANCE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;
    public static final EnumProperty<Stage> STAGE = EnumProperty.create("stage", Stage.class);

    private static final VoxelShape SHAPE_TOP_CAP = Block.box(0, 14, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_OUTLINE_FULL = Shapes.block();
    private static final VoxelShape SHAPE_OUTLINE_HANGING = Block.box(0, 8, 0, 16, 16, 16);
    private static final VoxelShape SHAPE_BELOW_BLOCK = Shapes.block().move(0, -1, 0);

    public FrameworkBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(0.5F).noOcclusion().sound(SoundType.WOOD));
        registerDefaultState(stateDefinition.any().setValue(DISTANCE, 7).setValue(WATERLOGGED, false).setValue(BOTTOM, false).setValue(STAGE, Stage.TOP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(DISTANCE, WATERLOGGED, BOTTOM, STAGE);
    }

    @Override
    public boolean canBeReplaced(BlockState s, BlockPlaceContext ctx) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos p = ctx.getClickedPos();
        LevelAccessor l = ctx.getLevel();
        int d = getDistance(l, p);
        return defaultBlockState()
                .setValue(WATERLOGGED, l.getFluidState(p).getType() == Fluids.WATER)
                .setValue(DISTANCE, d)
                .setValue(BOTTOM, isBottom(l, p, d))
                .setValue(STAGE, resolveStage(l, p));
    }

    @Override
    protected void onPlace(BlockState s, net.minecraft.world.level.Level l, BlockPos p, BlockState old, boolean moved) {
        if (!l.isClientSide) l.scheduleTick(p, this, 1);
    }

    @Override
    protected BlockState updateShape(BlockState s, Direction dir, BlockState ns, LevelAccessor l, BlockPos p, BlockPos np) {
        if (s.getValue(WATERLOGGED)) l.scheduleTick(p, Fluids.WATER, Fluids.WATER.getTickDelay(l));
        if (!l.isClientSide()) l.scheduleTick(p, this, 1);
        return s.setValue(STAGE, resolveStage(l, p));
    }

    @Override
    protected void tick(BlockState s, ServerLevel l, BlockPos p, RandomSource r) {
        int d = getDistance(l, p);
        BlockState u = s.setValue(DISTANCE, d).setValue(BOTTOM, isBottom(l, p, d)).setValue(STAGE, resolveStage(l, p));
        if (u.getValue(DISTANCE) == 7) {
            if (s.getValue(DISTANCE) == 7) {
                FallingBlockEntity.fall(l, p, u);
            } else {
                l.destroyBlock(p, true);
            }
        } else if (u != s) {
            l.setBlock(p, u, 3);
        }
    }

    @Override
    protected boolean canSurvive(BlockState s, LevelReader l, BlockPos p) {
        return getDistance(l, p) < 7;
    }

    @Override
    public VoxelShape getShape(BlockState s, BlockGetter g, BlockPos p, CollisionContext c) {
        if (s.getValue(STAGE) == Stage.HANGING) return SHAPE_OUTLINE_HANGING;
        return SHAPE_OUTLINE_FULL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState s, BlockGetter g, BlockPos p, CollisionContext c) {
        if (c.isAbove(Shapes.block(), p, true) && !c.isDescending()) return SHAPE_TOP_CAP;
        if (s.getValue(DISTANCE) != 0 && s.getValue(BOTTOM) && c.isAbove(SHAPE_BELOW_BLOCK, p, true))
            return Block.box(0, 0, 0, 16, 2, 16);
        return Shapes.empty();
    }

    @Override
    public VoxelShape getInteractionShape(BlockState s, BlockGetter g, BlockPos p) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState s, BlockGetter g, BlockPos p) {
        return Shapes.block();
    }

    @Override
    public FluidState getFluidState(BlockState s) {
        return s.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(s);
    }

    private boolean isBottom(BlockGetter g, BlockPos p, int d) {
        return d > 0 && !(g.getBlockState(p.below()).getBlock() instanceof FrameworkBlock);
    }

    public static int getDistance(BlockGetter g, BlockPos p) {
        BlockPos.MutableBlockPos m = p.mutable().move(Direction.DOWN);
        BlockState b = g.getBlockState(m);
        int d = 7;
        if (b.getBlock() instanceof FrameworkBlock) {
            d = b.getValue(DISTANCE);
        } else if (b.isFaceSturdy(g, m, Direction.UP)) {
            return 0;
        }
        for (Direction dir : Plane.HORIZONTAL) {
            BlockState s = g.getBlockState(m.setWithOffset(p, dir));
            if (s.getBlock() instanceof FrameworkBlock) {
                d = Math.min(d, s.getValue(DISTANCE) + 1);
                if (d == 1) break;
            }
        }
        return d;
    }

    private Stage resolveStage(LevelAccessor l, BlockPos p) {
        boolean hasAbove = l.getBlockState(p.above()).getBlock() instanceof FrameworkBlock;
        boolean airBelow = l.isEmptyBlock(p.below());
        if (hasAbove) return Stage.SUPPORT;
        if (airBelow) return Stage.HANGING;
        return Stage.TOP;
    }

    public enum Stage implements StringRepresentable {
        SUPPORT("support"),
        TOP("top"),
        HANGING("hanging");

        private final String n;

        Stage(String n) {
            this.n = n;
        }

        @Override
        public @NotNull String getSerializedName() {
            return n;
        }
    }
}
