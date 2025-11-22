package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimberFrameBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final MapCodec<TimberFrameBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BlockState.CODEC.fieldOf("base_state").forGetter(block -> block.baseState), propertiesCodec()).apply(instance, TimberFrameBlock::new));
    public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");
    public static final BooleanProperty APPLIED = BooleanProperty.create("applied");
    private final Block base;
    protected final BlockState baseState;

    public TimberFrameBlock(BlockState baseState, BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(APPLIED, false));
        this.base = baseState.getBlock();
        this.baseState = baseState;
    }

    public @NotNull MapCodec<? extends TimberFrameBlock> codec() {
        return CODEC;
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    public float getExplosionResistance() {
        return this.base.getExplosionResistance();
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState fluid = context.getLevel().getFluidState(pos);
        return this.defaultBlockState().setValue(WATERLOGGED, fluid.getType() == Fluids.WATER).setValue(APPLIED, false);
    }

    protected @NotNull BlockState updateShape(BlockState state, net.minecraft.core.Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, APPLIED);
    }

    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TimberFrameBlockEntity(pos, state);
    }

    private static boolean canAccept(BlockGetter level, BlockPos pos, BlockState candidate) {
        if (candidate == null || candidate.isAir()) return false;
        if (candidate.getBlock() instanceof EntityBlock) return false;
        if (candidate.getRenderShape() != RenderShape.MODEL) return false;
        return Block.isShapeFullBlock(candidate.getShape(level, pos));
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
                    BlockState newState = state.setValue(APPLIED, false);
                    level.setBlock(blockPos, newState, 3);
                    level.sendBlockUpdated(blockPos, state, newState, Block.UPDATE_ALL);
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
            BlockState newState = state.setValue(APPLIED, true);
            level.setBlock(blockPos, newState, 3);
            level.sendBlockUpdated(blockPos, state, newState, Block.UPDATE_ALL);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.levelEvent(2001, blockPos, Block.getId(mimicState));
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TimberFrameBlockEntity timberFrameBlockEntity) {
            BlockState mimic = timberFrameBlockEntity.getMimicState();
            if (mimic != null && !mimic.isAir() && !player.isCreative()) {
                Block.popResource(level, pos, new ItemStack(mimic.getBlock()));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
        return state;
    }

    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return InteractionResult.PASS;
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
        list.add(Component.translatable("tooltip.hearth_and_timber.timber_frame_full.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        list.add(Component.empty());
        list.add(Component.translatable("tooltip.hearth_and_timber.timber_frame.info_1").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }
}