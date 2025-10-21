package net.satisfy.hearth_and_timber.core.block.entity;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.block.IRubbleMasonry;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;

public class RubbleMasonryBlockEntity extends BlockEntity {
    private int brushProgress;
    private UUID brushingPlayerId;
    private boolean brushingActive;

    public RubbleMasonryBlockEntity(BlockPos position, BlockState blockState) {
        super(EntityTypeRegistry.RUBBLE_MASONRY_BLOCK_ENTITY.get(), position, blockState);
    }

    public void startBrushing(Player player) {
        brushingActive = true;
        brushingPlayerId = player.getUUID();
    }

    public static void tick(ServerLevel level, BlockPos position, BlockState blockState, RubbleMasonryBlockEntity blockEntity) {
        if (!blockEntity.brushingActive) return;

        Player brushingPlayer = blockEntity.brushingPlayerId == null ? null : level.getPlayerByUUID(blockEntity.brushingPlayerId);
        if (brushingPlayer == null) {
            blockEntity.resetBrushing();
            return;
        }

        boolean holdingBrush = brushingPlayer.getMainHandItem().is(Items.BRUSH) || brushingPlayer.getOffhandItem().is(Items.BRUSH);
        boolean withinReach = brushingPlayer.distanceToSqr(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5) <= 25.0;

        if (!holdingBrush || !withinReach) {
            blockEntity.resetBrushing();
            return;
        }

        blockEntity.brushProgress++;
        if (blockEntity.brushProgress >= 60) {
            if (blockState.getBlock() instanceof IRubbleMasonry brushableBlock) {
                brushableBlock.onBrushingFinished(level, position, blockState);
            }
            blockEntity.resetBrushing();
        }
    }

    private void resetBrushing() {
        brushingActive = false;
        brushingPlayerId = null;
        brushProgress = 0;
    }
}