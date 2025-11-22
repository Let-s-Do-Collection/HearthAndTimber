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

    private static final VoxelShape TOP_CAP_SHAPE = Block.box(0, 14, 0, 16, 16, 16);
    private static final VoxelShape OUTLINE_FULL_SHAPE = Shapes.block();
    private static final VoxelShape OUTLINE_HANGING_SHAPE = Block.box(0, 8, 0, 16, 16, 16);
    private static final VoxelShape BELOW_BLOCK_SHAPE = Shapes.block().move(0, -1, 0);

    public FrameworkBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(0.5F)
                .noOcclusion()
                .sound(SoundType.WOOD));
        registerDefaultState(stateDefinition.any()
                .setValue(DISTANCE, 7)
                .setValue(WATERLOGGED, false)
                .setValue(BOTTOM, false)
                .setValue(STAGE, Stage.TOP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, WATERLOGGED, BOTTOM, STAGE);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return context.getItemInHand().is(asItem());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        LevelAccessor level = context.getLevel();
        int distance = getDistance(level, clickedPos);
        boolean isWaterlogged = level.getFluidState(clickedPos).getType() == Fluids.WATER;
        boolean isBottom = isBottom(level, clickedPos, distance);
        Stage stage = resolveStage(level, clickedPos);
        return defaultBlockState()
                .setValue(WATERLOGGED, isWaterlogged)
                .setValue(DISTANCE, distance)
                .setValue(BOTTOM, isBottom)
                .setValue(STAGE, stage);
    }

    @Override
    protected void onPlace(BlockState state, net.minecraft.world.level.Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 1);
        }
        return state.setValue(STAGE, resolveStage(level, pos));
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        int distance = getDistance(level, pos);
        boolean isBottom = isBottom(level, pos, distance);
        Stage stage = resolveStage(level, pos);
        BlockState updatedState = state
                .setValue(DISTANCE, distance)
                .setValue(BOTTOM, isBottom)
                .setValue(STAGE, stage);

        if (updatedState.getValue(DISTANCE) == 7) {
            if (state.getValue(DISTANCE) == 7) {
                FallingBlockEntity.fall(level, pos, updatedState);
            } else {
                level.destroyBlock(pos, true);
            }
        } else if (updatedState != state) {
            level.setBlock(pos, updatedState, 3);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return getDistance(level, pos) < 7;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(STAGE) == Stage.HANGING) {
            return OUTLINE_HANGING_SHAPE;
        }
        return OUTLINE_FULL_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context.isAbove(Shapes.block(), pos, true) && !context.isDescending()) {
            return TOP_CAP_SHAPE;
        }
        if (state.getValue(DISTANCE) != 0 && state.getValue(BOTTOM) && context.isAbove(BELOW_BLOCK_SHAPE, pos, true)) {
            return Block.box(0, 0, 0, 16, 2, 16);
        }
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(state);
    }

    private boolean isBottom(BlockGetter level, BlockPos pos, int distance) {
        if (distance <= 0) {
            return false;
        }
        BlockState belowState = level.getBlockState(pos.below());
        return !(belowState.getBlock() instanceof FrameworkBlock);
    }

    public static int getDistance(BlockGetter level, BlockPos pos) {
        BlockPos.MutableBlockPos belowPos = pos.mutable().move(Direction.DOWN);
        BlockState belowState = level.getBlockState(belowPos);
        int distance = 7;

        if (belowState.getBlock() instanceof FrameworkBlock) {
            distance = belowState.getValue(DISTANCE);
        } else if (belowState.isFaceSturdy(level, belowPos, Direction.UP)) {
            return 0;
        }

        for (Direction horizontalDirection : Plane.HORIZONTAL) {
            BlockState neighborState = level.getBlockState(belowPos.setWithOffset(pos, horizontalDirection));
            if (neighborState.getBlock() instanceof FrameworkBlock) {
                distance = Math.min(distance, neighborState.getValue(DISTANCE) + 1);
                if (distance == 1) {
                    break;
                }
            }
        }

        return distance;
    }

    private Stage resolveStage(LevelAccessor level, BlockPos pos) {
        boolean hasFrameworkAbove = level.getBlockState(pos.above()).getBlock() instanceof FrameworkBlock;
        boolean hasAirBelow = level.isEmptyBlock(pos.below());
        if (hasFrameworkAbove) {
            return Stage.SUPPORT;
        }
        if (hasAirBelow) {
            return Stage.HANGING;
        }
        return Stage.TOP;
    }

    public enum Stage implements StringRepresentable {
        SUPPORT("support"),
        TOP("top"),
        HANGING("hanging");

        private final String serializedName;

        Stage(String serializedName) {
            this.serializedName = serializedName;
        }

        @Override
        public @NotNull String getSerializedName() {
            return serializedName;
        }
    }
}
