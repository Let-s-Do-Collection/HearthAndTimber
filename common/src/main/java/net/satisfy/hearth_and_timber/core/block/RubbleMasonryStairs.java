package net.satisfy.hearth_and_timber.core.block;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.hearth_and_timber.core.block.entity.RubbleMasonryBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RubbleMasonryStairs extends StairBlock implements EntityBlock, IRubbleMasonry {
    private final Supplier<Block> nextBlock;
    private final Supplier<Block> previousBlock;

    public RubbleMasonryStairs(Supplier<BlockState> baseState, Properties properties, Supplier<Block> nextBlock, Supplier<Block> previousBlock) {
        super(baseState.get(), properties);
        this.nextBlock = nextBlock;
        this.previousBlock = previousBlock;
    }

    @Override
    public Supplier<Block> getNextBlock() {
        return nextBlock;
    }

    @Override
    public Supplier<Block> getPreviousBlock() {
        return previousBlock;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack heldStack, BlockState blockState, Level level, BlockPos position, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return handleInteraction(blockState, level, position, player, hand, hitResult);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos position, BlockState blockState) {
        return new RubbleMasonryBlockEntity(position, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;
        return (serverLevel, blockPosition, state, blockEntity) -> {
            if (blockEntity instanceof RubbleMasonryBlockEntity rubbleEntity) {
                RubbleMasonryBlockEntity.tick((ServerLevel) serverLevel, blockPosition, state, rubbleEntity);
            }
        };
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<net.minecraft.network.chat.Component> tooltipList, TooltipFlag tooltipFlag) {
        IRubbleMasonry.super.appendHoverText(itemStack, tooltipContext, tooltipList, tooltipFlag);
    }
}