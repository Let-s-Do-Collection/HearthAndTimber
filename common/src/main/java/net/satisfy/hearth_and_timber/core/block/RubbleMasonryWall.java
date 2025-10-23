package net.satisfy.hearth_and_timber.core.block;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class RubbleMasonryWall extends WallBlock implements IRubbleMasonry {
    private final Supplier<Block> nextBlock;
    private final Supplier<Block> previousBlock;

    public RubbleMasonryWall(Properties properties, Supplier<Block> nextBlock, Supplier<Block> previousBlock) {
        super(properties);
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
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipList, TooltipFlag tooltipFlag) {
        IRubbleMasonry.super.appendHoverText(itemStack, tooltipContext, tooltipList, tooltipFlag);
    }
}
