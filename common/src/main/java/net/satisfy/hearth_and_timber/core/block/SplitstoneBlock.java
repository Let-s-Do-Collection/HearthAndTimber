package net.satisfy.hearth_and_timber.core.block;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.NotNull;

public class SplitstoneBlock extends Block {
    private final Supplier<Block> pathBlock;

    public SplitstoneBlock(Properties properties, Supplier<Block> pathBlock) {
        super(properties);
        this.pathBlock = pathBlock;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof PickaxeItem) {
            if (!level.isClientSide) {
                level.levelEvent(player, 2001, pos, Block.getId(state));
                level.playSound(null, pos, SoundEvents.DEEPSLATE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(pos, pathBlock.get().defaultBlockState(), 3);
            } else {
                RandomSource r = level.getRandom();
                for (int i = 0; i < 12; i++) {
                    double x = pos.getX() + 0.5 + (r.nextDouble() - 0.5) * 0.4;
                    double y = pos.getY() + 1.02;
                    double z = pos.getZ() + 0.5 + (r.nextDouble() - 0.5) * 0.4;
                    double vx = (r.nextDouble() - 0.5) * 0.08;
                    double vy = 0.25 + r.nextDouble() * 0.25;
                    double vz = (r.nextDouble() - 0.5) * 0.08;
                    level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), x, y, z, vx, vy, vz);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
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
        list.add(Component.translatable("tooltip.hearth_and_timber.splitstone.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }
}
