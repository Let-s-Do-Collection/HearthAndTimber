package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
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
import net.satisfy.hearth_and_timber.core.block.entity.FoundationBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FireplaceCorniceBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty LEFT = BooleanProperty.create("left");
    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
    public static final BooleanProperty APPLIED = BooleanProperty.create("applied");

    public FireplaceCorniceBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(TOP, false)
                .setValue(BOTTOM, false)
                .setValue(LEFT, false)
                .setValue(RIGHT, false)
                .setValue(APPLIED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, TOP, BOTTOM, LEFT, RIGHT, APPLIED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean water = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, water)
                .setValue(TOP, false)
                .setValue(BOTTOM, false)
                .setValue(LEFT, false)
                .setValue(RIGHT, false)
                .setValue(APPLIED, false);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return state;
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FoundationBlockEntity(pos, state);
    }

    private static boolean canAccept(BlockGetter level, BlockPos pos, BlockState state) {
        if (state == null || state.isAir()) return false;
        if (state.getBlock() instanceof EntityBlock) return false;
        if (state.getRenderShape() != RenderShape.MODEL) return false;
        return Block.isShapeFullBlock(state.getShape(level, pos));
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FoundationBlockEntity fbe) {
            BlockState mimic = fbe.getMimicState();
            if (mimic != null && !mimic.isAir() && !player.isCreative()) {
                Block.popResource(level, pos, new ItemStack(mimic.getBlock()));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
        return state;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Item item = stack.getItem();
        if (item instanceof PickaxeItem) {
            if (!state.getValue(APPLIED)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FoundationBlockEntity fbe) {
                BlockState mimic = fbe.getMimicState();
                if (mimic != null && !mimic.isAir()) {
                    Block.popResource(level, pos, new ItemStack(mimic.getBlock()));
                }
                BlockState newState = state.setValue(APPLIED, false);
                level.setBlock(pos, newState, 3);
                fbe.setMimicState(null);
                if (level instanceof ServerLevel server) {
                    server.sendBlockUpdated(pos, state, newState, 3);
                    server.levelEvent(2001, pos, Block.getId(mimic == null ? Blocks.AIR.defaultBlockState() : mimic));
                }
            }
            return ItemInteractionResult.CONSUME;
        }
        if (state.getValue(APPLIED)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!(item instanceof BlockItem blockItem)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        BlockState mimic = blockItem.getBlock().defaultBlockState();
        if (!canAccept(level, pos, mimic)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FoundationBlockEntity fbe) {
            BlockState newState = state.setValue(APPLIED, true);
            fbe.setMimicState(mimic);
            level.setBlock(pos, newState, 3);
            if (level instanceof ServerLevel server) {
                server.sendBlockUpdated(pos, state, newState, 3);
                server.levelEvent(2001, pos, Block.getId(mimic));
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, List<Component> list, TooltipFlag flag) {
        int beige = 0xF5DEB3;
        int gold = 0xFFD700;
        if (!Screen.hasShiftDown()) {
            Component key = Component.literal("[SHIFT]").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(gold)));
            list.add(Component.translatable("tooltip.hearth_and_timber.tooltip_information.hold", key).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
            return;
        }
        list.add(Component.translatable("tooltip.hearth_and_timber.timber_frame.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        list.add(Component.empty());
        list.add(Component.translatable("tooltip.hearth_and_timber.timber_frame.info_1").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        Direction f = state.getValue(FACING);
        boolean top = state.getValue(TOP);
        boolean bottom = state.getValue(BOTTOM);
        boolean left = state.getValue(LEFT);
        boolean right = state.getValue(RIGHT);
        VoxelShape shape = Shapes.empty();
        if (f == Direction.SOUTH) {
            if (left) shape = Shapes.or(shape, box(0, 0, 0, 4, 16, 4));
            if (right) shape = Shapes.or(shape, box(12, 0, 0, 16, 16, 4));
            if (top) shape = Shapes.or(shape, box(0, 11, 0, 16, 16, 5));
            if (bottom) shape = Shapes.or(shape, box(0, 0, 1, 16, 10, 2));
        } else if (f == Direction.NORTH) {
            if (left) shape = Shapes.or(shape, box(12, 0, 12, 16, 16, 16));
            if (right) shape = Shapes.or(shape, box(0, 0, 12, 4, 16, 16));
            if (top) shape = Shapes.or(shape, box(0, 11, 11, 16, 16, 16));
            if (bottom) shape = Shapes.or(shape, box(0, 0, 14, 16, 10, 15));
        } else if (f == Direction.EAST) {
            if (left) shape = Shapes.or(shape, box(0, 0, 12, 4, 16, 16));
            if (right) shape = Shapes.or(shape, box(0, 0, 0, 4, 16, 4));
            if (top) shape = Shapes.or(shape, box(0, 11, 0, 5, 16, 16));
            if (bottom) shape = Shapes.or(shape, box(1, 0, 0, 2, 10, 16));
        } else if (f == Direction.WEST) {
            if (left) shape = Shapes.or(shape, box(12, 0, 0, 16, 16, 4));
            if (right) shape = Shapes.or(shape, box(12, 0, 12, 16, 16, 16));
            if (top) shape = Shapes.or(shape, box(11, 11, 0, 16, 16, 16));
            if (bottom) shape = Shapes.or(shape, box(14, 0, 0, 15, 10, 16));
        }
        return shape;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return getShape(state, level, pos, ctx);
    }
}