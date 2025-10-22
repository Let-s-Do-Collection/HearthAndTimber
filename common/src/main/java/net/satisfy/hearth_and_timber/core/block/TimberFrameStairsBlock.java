package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
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

public class TimberFrameStairsBlock extends StairBlock implements EntityBlock, SimpleWaterloggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty APPLIED = BooleanProperty.create("applied");

    public TimberFrameStairsBlock(BlockState baseShapeState, BlockBehaviour.Properties properties) {
        super(baseShapeState, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(APPLIED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(APPLIED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState placed = super.getStateForPlacement(context);
        return placed == null ? null : placed.setValue(APPLIED, false);
    }

    protected @NotNull BlockState updateShape(BlockState state, net.minecraft.core.Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
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

    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof PickaxeItem) {
            if (!state.getValue(APPLIED)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TimberFrameBlockEntity foundation) {
                BlockState mimic = foundation.getMimicState();
                if (mimic != null && !mimic.isAir()) {
                    Block.popResource(level, pos, new ItemStack(mimic.getBlock()));
                    foundation.setMimicState(null);
                    level.setBlock(pos, state.setValue(APPLIED, false), 3);
                    if (level instanceof ServerLevel server) server.levelEvent(2001, pos, Block.getId(mimic));
                }
            }
            return ItemInteractionResult.CONSUME;
        }
        if (state.getValue(APPLIED)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!(stack.getItem() instanceof BlockItem blockItem)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        BlockState mimic = blockItem.getBlock().defaultBlockState();
        if (!canAccept(level, pos, mimic)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TimberFrameBlockEntity foundation) {
            foundation.setMimicState(mimic);
            level.setBlock(pos, state.setValue(APPLIED, true), 3);
            if (level instanceof ServerLevel server) server.levelEvent(2001, pos, Block.getId(mimic));
        }
        return ItemInteractionResult.CONSUME;
    }

    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TimberFrameBlockEntity foundation) {
            BlockState mimic = foundation.getMimicState();
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
}