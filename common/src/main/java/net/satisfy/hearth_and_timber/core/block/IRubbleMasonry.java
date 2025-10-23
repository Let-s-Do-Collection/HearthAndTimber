package net.satisfy.hearth_and_timber.core.block;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;

public interface IRubbleMasonry {
    Supplier<Block> getNextBlock();
    Supplier<Block> getPreviousBlock();

    default ItemInteractionResult handleInteraction(BlockState blockState, Level level, BlockPos position, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (heldStack.is(Items.IRON_PICKAXE) || heldStack.is(Items.STONE_PICKAXE) || heldStack.is(Items.DIAMOND_PICKAXE) || heldStack.is(Items.NETHERITE_PICKAXE) || heldStack.is(Items.GOLDEN_PICKAXE)) {
            Block nextBlock = getNextBlock() == null ? null : getNextBlock().get();
            if (nextBlock != null) {
                if (!level.isClientSide) {
                    level.setBlock(position, copyMatching(blockState, nextBlock.defaultBlockState()), 3);
                }
                player.swing(hand);
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (heldStack.is(ObjectRegistry.QUICKLIME.get().asItem())) {
            Block previousBlock = getPreviousBlock() == null ? null : getPreviousBlock().get();
            if (previousBlock != null) {
                BlockState targetState = copyMatching(blockState, previousBlock.defaultBlockState());
                if (!level.isClientSide) {
                    level.setBlock(position, targetState, 3);
                    if (!player.getAbilities().instabuild) heldStack.shrink(1);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    default void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltipList, TooltipFlag tooltipFlag) {
        int beige = 0xF5DEB3;
        int gold = 0xFFD700;
        if (!Screen.hasShiftDown()) {
            Component key = Component.literal("[SHIFT]").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(gold)));
            tooltipList.add(Component.translatable("tooltip.hearth_and_timber.tooltip_information.hold", key).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
            return;
        }
        tooltipList.add(Component.translatable("tooltip.hearth_and_timber.rubble.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        tooltipList.add(Component.empty());
        tooltipList.add(Component.translatable("tooltip.hearth_and_timber.rubble.info_1").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }

    static BlockState copyMatching(BlockState fromState, BlockState toState) {
        for (Property<?> property : toState.getProperties()) {
            if (fromState.hasProperty(property)) {
                toState = setUnchecked(toState, property, fromState.getValue(property));
            }
        }
        return toState;
    }

    static <T extends Comparable<T>> BlockState setUnchecked(BlockState state, Property<T> property, Object value) {
        return state.setValue(property, property.getValueClass().cast(value));
    }
}
