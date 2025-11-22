package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WindowCasingBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    public static final BooleanProperty BOTTOM_TOGGLE = BooleanProperty.create("bottom_toggle");

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
                .setValue(BOTTOM_TOGGLE, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
                .setValue(TOP, true)
                .setValue(BOTTOM, true)
                .setValue(LEFT, true)
                .setValue(RIGHT, true)
                .setValue(BOTTOM_TOGGLE, false);
        return updateConnections(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return updateConnections(state, level, pos);
    }

    private boolean isConnected(LevelAccessor level, BlockPos pos, Direction facing) {
        BlockState other = level.getBlockState(pos);
        return other.getBlock() instanceof WindowCasingBlock && other.getValue(FACING) == facing;
    }

    private boolean hasSameBelow(LevelAccessor level, BlockPos pos, Direction facing) {
        BlockState below = level.getBlockState(pos.below());
        return below.getBlock() instanceof WindowCasingBlock && below.getValue(FACING) == facing;
    }

    private BlockState updateConnections(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction facing = state.getValue(FACING);

        boolean north = isConnected(level, pos.north(), facing);
        boolean east = isConnected(level, pos.east(), facing);
        boolean south = isConnected(level, pos.south(), facing);
        boolean west = isConnected(level, pos.west(), facing);
        boolean up = isConnected(level, pos.above(), facing);

        boolean bottom = state.getValue(BOTTOM);
        if (!state.getValue(BOTTOM_TOGGLE)) {
            bottom = !hasSameBelow(level, pos, facing);
        }

        boolean top;
        boolean left;
        boolean right;

        if (facing == Direction.NORTH) {
            top = !up;
            left = !east;
            right = !west;
        } else if (facing == Direction.SOUTH) {
            top = !up;
            left = !west;
            right = !east;
        } else if (facing == Direction.EAST) {
            top = !up;
            left = !south;
            right = !north;
        } else {
            top = !up;
            left = !north;
            right = !south;
        }

        return state
                .setValue(TOP, top)
                .setValue(LEFT, left)
                .setValue(RIGHT, right)
                .setValue(BOTTOM, bottom);
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(stack.getItem() instanceof AxeItem)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (hit.getDirection() != state.getValue(FACING)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        double hitY = hit.getLocation().y - pos.getY();
        if (hitY >= 0.5) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        boolean newBottom = !state.getValue(BOTTOM);

        BlockState newState = state
                .setValue(BOTTOM, newBottom)
                .setValue(BOTTOM_TOGGLE, true);

        if (!level.isClientSide) {
            level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.levelEvent(2001, pos, Block.getId(state));
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide);
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
            if (shape.isEmpty()) shape = NORTH_PLANE;
        } else if (facing == Direction.SOUTH) {
            if (state.getValue(LEFT)) shape = Shapes.or(shape, SOUTH_LEFT);
            if (state.getValue(RIGHT)) shape = Shapes.or(shape, SOUTH_RIGHT);
            if (state.getValue(TOP)) shape = Shapes.or(shape, SOUTH_TOP);
            if (state.getValue(BOTTOM)) shape = Shapes.or(shape, SOUTH_BOTTOM);
            if (shape.isEmpty()) shape = SOUTH_PLANE;
        } else if (facing == Direction.EAST) {
            if (state.getValue(LEFT)) shape = Shapes.or(shape, EAST_LEFT);
            if (state.getValue(RIGHT)) shape = Shapes.or(shape, EAST_RIGHT);
            if (state.getValue(TOP)) shape = Shapes.or(shape, EAST_TOP);
            if (state.getValue(BOTTOM)) shape = Shapes.or(shape, EAST_BOTTOM);
            if (shape.isEmpty()) shape = EAST_PLANE;
        } else if (facing == Direction.WEST) {
            if (state.getValue(LEFT)) shape = Shapes.or(shape, WEST_LEFT);
            if (state.getValue(RIGHT)) shape = Shapes.or(shape, WEST_RIGHT);
            if (state.getValue(TOP)) shape = Shapes.or(shape, WEST_TOP);
            if (state.getValue(BOTTOM)) shape = Shapes.or(shape, WEST_BOTTOM);
            if (shape.isEmpty()) shape = WEST_PLANE;
        }

        return shape;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, TOP, BOTTOM, LEFT, RIGHT, BOTTOM_TOGGLE);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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
        list.add(Component.translatable("tooltip.hearth_and_timber.window_casing.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        list.add(Component.empty());
        list.add(Component.translatable("tooltip.hearth_and_timber.window_casing.info_1").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }
}