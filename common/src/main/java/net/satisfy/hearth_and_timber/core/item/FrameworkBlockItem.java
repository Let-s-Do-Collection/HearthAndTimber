package net.satisfy.hearth_and_timber.core.item;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ScaffoldingBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FrameworkBlockItem extends ScaffoldingBlockItem {
    private final Supplier<Block> extensionBlock;

    public FrameworkBlockItem(Block block, Supplier<Block> extensionBlock, Item.Properties properties) {
        super(block, properties);
        this.extensionBlock = extensionBlock;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Direction clickedFace = context.getClickedFace();
        BlockPos supportPos = context.getClickedPos();
        Level level = context.getLevel();
        BlockPos targetPos = supportPos.relative(clickedFace);

        boolean isSidePlacement = clickedFace.getAxis().isHorizontal();
        boolean hasAirBelow = level.isEmptyBlock(targetPos.below());

        if (isSidePlacement && hasAirBelow) {
            BlockPlaceContext placeContext = new BlockPlaceContext(context);
            BlockState targetState = level.getBlockState(targetPos);
            if (!targetState.canBeReplaced(placeContext)) {
                return InteractionResult.FAIL;
            }

            if (!level.isClientSide) {
                BlockState extensionState = extensionBlock.get().defaultBlockState();
                level.setBlock(targetPos, extensionState, 3);
                Player player = context.getPlayer();
                ItemStack stack = context.getItemInHand();
                if (player == null || !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }
}