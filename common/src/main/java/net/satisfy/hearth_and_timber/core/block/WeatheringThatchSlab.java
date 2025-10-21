package net.satisfy.hearth_and_timber.core.block;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class WeatheringThatchSlab extends SlabBlock implements IWeatheringThatch {
    private final Supplier<Block> nextBlock;

    public WeatheringThatchSlab(Properties properties, Supplier<Block> nextBlock) {
        super(properties);
        this.nextBlock = nextBlock;
    }

    @Override
    public Supplier<Block> getNextBlock() {
        return nextBlock;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return getNextBlock() != null;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        tickWeathering(state, level, pos, random);
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return handleInteraction(state, level, pos, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        IWeatheringThatch.super.appendHoverText(stack, context, list, flag);
    }
}