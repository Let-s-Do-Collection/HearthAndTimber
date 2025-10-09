package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.core.block.entity.WindowCasingBlockEntity;
import org.jetbrains.annotations.NotNull;

public class WindowCasingBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    public static final BooleanProperty FLOWER_POT = BooleanProperty.create("flower_pot");


    private static final VoxelShape POT_N = Block.box(5, 0, 9, 11, 6, 15);
    private static final VoxelShape POT_S = Block.box(5, 0, 1, 11, 6, 7);
    private static final VoxelShape POT_E = Block.box(1, 0, 5, 7, 6, 11);
    private static final VoxelShape POT_W = Block.box(9, 0, 5, 15, 6, 11);

    private static final VoxelShape NORTH_PLANE = Block.box(0, 0, 14, 16, 16, 16);
    private static final VoxelShape SOUTH_PLANE = Block.box(0, 0, 0, 16, 16, 2);
    private static final VoxelShape EAST_PLANE = Block.box(0, 0, 0, 2, 16, 16);
    private static final VoxelShape WEST_PLANE = Block.box(14, 0, 0, 16, 16, 16);

    private static final VoxelShape NORTH_LEFT = Block.box(14, 0, 14, 16, 16, 16);
    private static final VoxelShape NORTH_RIGHT = Block.box(0, 0, 14, 2, 16, 16);
    private static final VoxelShape NORTH_TOP = Block.box(0, 14, 14, 16, 16, 16);
    private static final VoxelShape NORTH_BOTTOM = Block.box(0, 0, 14, 16, 2, 16);

    private static final VoxelShape SOUTH_LEFT = Block.box(0, 0, 0, 2, 16, 2);
    private static final VoxelShape SOUTH_RIGHT = Block.box(14, 0, 0, 16, 16, 2);
    private static final VoxelShape SOUTH_TOP = Block.box(0, 14, 0, 16, 16, 2);
    private static final VoxelShape SOUTH_BOTTOM = Block.box(0, 0, 0, 16, 2, 2);

    private static final VoxelShape EAST_LEFT = Block.box(0, 0, 14, 2, 16, 16);
    private static final VoxelShape EAST_RIGHT = Block.box(0, 0, 0, 2, 16, 2);
    private static final VoxelShape EAST_TOP = Block.box(0, 14, 0, 2, 16, 16);
    private static final VoxelShape EAST_BOTTOM = Block.box(0, 0, 0, 2, 2, 16);

    private static final VoxelShape WEST_LEFT = Block.box(14, 0, 0, 16, 16, 2);
    private static final VoxelShape WEST_RIGHT = Block.box(14, 0, 14, 16, 16, 16);
    private static final VoxelShape WEST_TOP = Block.box(14, 14, 0, 16, 16, 16);
    private static final VoxelShape WEST_BOTTOM = Block.box(14, 0, 0, 16, 2, 16);

    public WindowCasingBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(TOP, true)
                .setValue(BOTTOM, true)
                .setValue(LEFT, true)
                .setValue(RIGHT, true)
                .setValue(FLOWER_POT, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState base = defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        return updateConnections(base, context.getLevel(), context.getClickedPos());
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return updateConnections(state, level, currentPos);
    }

    private boolean isConnected(LevelAccessor level, BlockPos pos, Direction facing) {
        BlockState other = level.getBlockState(pos);
        return other.getBlock() instanceof WindowCasingBlock && other.getValue(FACING) == facing;
    }

    private BlockState updateConnections(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        boolean northConnected = isConnected(level, pos.north(), facing);
        boolean eastConnected = isConnected(level, pos.east(), facing);
        boolean southConnected = isConnected(level, pos.south(), facing);
        boolean westConnected = isConnected(level, pos.west(), facing);
        boolean upConnected = isConnected(level, pos.above(), facing);
        boolean downConnected = isConnected(level, pos.below(), facing);
        boolean top;
        boolean bottom;
        boolean left;
        boolean right;
        if (facing == Direction.NORTH) {
            top = !upConnected;
            bottom = !downConnected;
            left = !eastConnected;
            right = !westConnected;
        } else if (facing == Direction.SOUTH) {
            top = !upConnected;
            bottom = !downConnected;
            left = !westConnected;
            right = !eastConnected;
        } else if (facing == Direction.EAST) {
            top = !upConnected;
            bottom = !downConnected;
            left = !southConnected;
            right = !northConnected;
        } else {
            top = !upConnected;
            bottom = !downConnected;
            left = !northConnected;
            right = !southConnected;
        }
        return state.setValue(TOP, top).setValue(BOTTOM, bottom).setValue(LEFT, left).setValue(RIGHT, right);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, TOP, BOTTOM, LEFT, RIGHT, FLOWER_POT);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(Items.FLOWER_POT)) {
            if (state.getValue(FLOWER_POT)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            if (hit.getDirection() != state.getValue(FACING)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            if (!state.getValue(BOTTOM)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            double y = hit.getLocation().y - pos.getY();
            if (y >= 0.5) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(FLOWER_POT, true), Block.UPDATE_CLIENTS);
                if (!player.getAbilities().instabuild) stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        if (state.getValue(FLOWER_POT) && stack.is(ItemTags.SMALL_FLOWERS)) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof WindowCasingBlockEntity wc && wc.getFlower().isEmpty()) {
                    ItemStack copy = stack.copy();
                    copy.setCount(1);
                    wc.setFlower(copy);
                    wc.setChanged();
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        VoxelShape shape = Shapes.empty();
        if (facing == Direction.NORTH) {
            if (state.getValue(LEFT)) shape = Shapes.or(shape, NORTH_LEFT);
            if (state.getValue(RIGHT)) shape = Shapes.or(shape, NORTH_RIGHT);
            if (state.getValue(TOP)) shape = Shapes.or(shape, NORTH_TOP);
            if (state.getValue(BOTTOM)) shape = Shapes.or(shape, NORTH_BOTTOM);
            if (state.getValue(FLOWER_POT)) shape = Shapes.or(shape, POT_N);
            if (shape.isEmpty()) shape = NORTH_PLANE;
        } else if (facing == Direction.SOUTH) {
            if (state.getValue(LEFT)) shape = Shapes.or(shape, SOUTH_LEFT);
            if (state.getValue(RIGHT)) shape = Shapes.or(shape, SOUTH_RIGHT);
            if (state.getValue(TOP)) shape = Shapes.or(shape, SOUTH_TOP);
            if (state.getValue(BOTTOM)) shape = Shapes.or(shape, SOUTH_BOTTOM);
            if (state.getValue(FLOWER_POT)) shape = Shapes.or(shape, POT_S);
            if (shape.isEmpty()) shape = SOUTH_PLANE;
        } else if (facing == Direction.EAST) {
            if (state.getValue(LEFT)) shape = Shapes.or(shape, EAST_LEFT);
            if (state.getValue(RIGHT)) shape = Shapes.or(shape, EAST_RIGHT);
            if (state.getValue(TOP)) shape = Shapes.or(shape, EAST_TOP);
            if (state.getValue(BOTTOM)) shape = Shapes.or(shape, EAST_BOTTOM);
            if (state.getValue(FLOWER_POT)) shape = Shapes.or(shape, POT_E);
            if (shape.isEmpty()) shape = EAST_PLANE;
        } else if (facing == Direction.WEST) {
            if (state.getValue(LEFT)) shape = Shapes.or(shape, WEST_LEFT);
            if (state.getValue(RIGHT)) shape = Shapes.or(shape, WEST_RIGHT);
            if (state.getValue(TOP)) shape = Shapes.or(shape, WEST_TOP);
            if (state.getValue(BOTTOM)) shape = Shapes.or(shape, WEST_BOTTOM);
            if (state.getValue(FLOWER_POT)) shape = Shapes.or(shape, POT_W);
            if (shape.isEmpty()) shape = WEST_PLANE;
        }
        return shape;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WindowCasingBlockEntity(pos, state);
    }
}