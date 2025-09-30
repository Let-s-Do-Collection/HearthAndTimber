package net.satisfy.hearth_and_timber.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class SlidingHayloftDoorBlockEntity extends BlockEntity {
    private boolean open;
    private long animStart;
    private boolean animForward;

    public SlidingHayloftDoorBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.SLIDING_HAYLOFT_DOOR_BLOCK_ENTITY.get(), pos, state);
    }

    public void setOpen(boolean v) {
        long now = System.currentTimeMillis();
        float p = getSlide();
        open = v;
        animForward = v;
        if (animForward) {
            animStart = now - (long) (p * 1000);
        } else {
            animStart = now - (long) ((1f - p) * 1000);
        }
        setChanged();
        if (level != null && !level.isClientSide) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }


    public boolean isOpen() {
        return open;
    }

    public float getSlide() {
        long now = System.currentTimeMillis();
        float t = (now - animStart) / 1000f;
        if (t < 0f) t = 0f;
        if (t > 1f) t = 1f;
        return animForward ? t : 1f - t;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        open = tag.getBoolean("open");
        animStart = tag.getLong("animStart");
        animForward = tag.getBoolean("animForward");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putBoolean("open", open);
        tag.putLong("animStart", animStart);
        tag.putBoolean("animForward", animForward);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SlidingHayloftDoorBlockEntity be) {
    }
}
