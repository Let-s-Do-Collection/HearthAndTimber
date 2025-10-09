package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.core.block.entity.FoundationBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FireplaceCorniceBlock extends WindowCasingBlock implements EntityBlock {
    public static final DirectionProperty FACING = WindowCasingBlock.FACING;
    public static final BooleanProperty TOP = WindowCasingBlock.TOP;
    public static final BooleanProperty BOTTOM = WindowCasingBlock.BOTTOM;
    public static final BooleanProperty LEFT = WindowCasingBlock.LEFT;
    public static final BooleanProperty RIGHT = WindowCasingBlock.RIGHT;
    public static final BooleanProperty APPLIED = BooleanProperty.create("applied");

    public FireplaceCorniceBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TOP, false).setValue(BOTTOM, false).setValue(LEFT, false).setValue(RIGHT, false).setValue(APPLIED, false).setValue(WATERLOGGED, false));
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TOP, BOTTOM, LEFT, RIGHT, APPLIED, WATERLOGGED);
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
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
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
}
