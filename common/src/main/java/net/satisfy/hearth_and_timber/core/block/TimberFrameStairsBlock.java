package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimberFrameStairsBlock extends StairBlock implements EntityBlock, SimpleWaterloggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty APPLIED = BooleanProperty.create("applied");

    public TimberFrameStairsBlock(BlockState baseShapeState, BlockBehaviour.Properties properties) {
        super(baseShapeState, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(APPLIED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(APPLIED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState placedState = super.getStateForPlacement(context);
        return placedState == null ? null : placedState.setValue(APPLIED, false);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, net.minecraft.core.Direction direction, BlockState neighborState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return super.updateShape(state, direction, neighborState, levelAccessor, blockPos, neighborPos);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new TimberFrameBlockEntity(blockPos, state);
    }

    private static boolean canAccept(BlockGetter blockGetter, BlockPos blockPos, BlockState candidateState) {
        if (candidateState == null || candidateState.isAir()) return false;
        if (candidateState.getBlock() instanceof EntityBlock) return false;
        if (candidateState.getRenderShape() != RenderShape.MODEL) return false;
        return Block.isShapeFullBlock(candidateState.getShape(blockGetter, blockPos));
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        if (itemStack.getItem() instanceof PickaxeItem) {
            if (!state.getValue(APPLIED)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof TimberFrameBlockEntity timberFrameBlockEntity) {
                BlockState mimicState = timberFrameBlockEntity.getMimicState();
                if (mimicState != null && !mimicState.isAir()) {
                    Block.popResource(level, blockPos, new ItemStack(mimicState.getBlock()));
                    timberFrameBlockEntity.setMimicState(null);
                    level.setBlock(blockPos, state.setValue(APPLIED, false), 3);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.levelEvent(2001, blockPos, Block.getId(mimicState));
                    }
                }
            }
            return ItemInteractionResult.CONSUME;
        }
        if (state.getValue(APPLIED)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!(itemStack.getItem() instanceof BlockItem blockItem)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        BlockState mimicState = blockItem.getBlock().defaultBlockState();
        if (!canAccept(level, blockPos, mimicState)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof TimberFrameBlockEntity timberFrameBlockEntity) {
            timberFrameBlockEntity.setMimicState(mimicState);
            level.setBlock(blockPos, state.setValue(APPLIED, true), 3);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.levelEvent(2001, blockPos, Block.getId(mimicState));
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof TimberFrameBlockEntity timberFrameBlockEntity) {
            BlockState mimicState = timberFrameBlockEntity.getMimicState();
            if (mimicState != null && !mimicState.isAir() && !player.isCreative()) {
                Block.popResource(level, blockPos, new ItemStack(mimicState.getBlock()));
            }
        }
        super.playerWillDestroy(level, blockPos, state, player);
        return state;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos blockPos, Player player, BlockHitResult hitResult) {
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> components, TooltipFlag tooltipFlag) {
        int beige = 0xF5DEB3;
        int gold = 0xFFD700;
        if (!Screen.hasShiftDown()) {
            Component key = Component.literal("[SHIFT]").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(gold)));
            components.add(Component.translatable("tooltip.hearth_and_timber.tooltip_information.hold", key).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
            return;
        }
        components.add(Component.translatable("tooltip.hearth_and_timber.timber_frame.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        components.add(Component.empty());
        components.add(Component.translatable("tooltip.hearth_and_timber.timber_frame_full.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        components.add(Component.empty());
        components.add(Component.translatable("tooltip.hearth_and_timber.timber_frame.info_1").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }
}