package net.satisfy.hearth_and_timber.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

public class BathtubBlockEntity extends BlockEntity {
    private int total;
    private int progress;
    private int clientProgress;
    private boolean filling;

    public BathtubBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BATHTUB_BLOCK_ENTITY.get(), pos, state);
    }

    public void startFilling(int totalTicks) {
        total = totalTicks;
        progress = 0;
        clientProgress = 0;
        filling = true;
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BathtubBlockEntity be) {
        if (!be.filling || be.total <= 0) return;
        be.progress++;
        be.setChanged();
        if (be.progress % 10 == 0) level.playSound(null, pos, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.25f, 0.9f + level.random.nextFloat() * 0.2f);
        if (be.progress % 20 == 0) level.playSound(null, pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.BLOCKS, 0.35f, 0.95f + level.random.nextFloat() * 0.1f);
        if (be.progress % 5 == 0) level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        if (be.progress >= be.total) {
            be.progress = be.total;
            be.filling = false;
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.7f, 1.0f);
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }
    }

    public static void clientTick(BathtubBlockEntity be) {
        if (be.clientProgress < be.progress) be.clientProgress++;
    }

    public float getFillRatio(float partial) {
        if (total <= 0) return 0f;
        float cp = clientProgress;
        float sp = progress;
        float p = cp + (sp - cp) * Mth.clamp(partial, 0f, 1f);
        return Mth.clamp(p / (float) total, 0f, 1f);
    }

    public boolean isFilling() {
        return filling;
    }

    public boolean isFilled() {
        return total > 0 && progress >= total;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("total", total);
        tag.putInt("progress", progress);
        tag.putBoolean("filling", filling);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        total = tag.getInt("total");
        progress = tag.getInt("progress");
        filling = tag.getBoolean("filling");
        clientProgress = progress;
    }

    public boolean canDrainPercent(float percent) {
        if (total <= 0) return false;
        int amt = Mth.floor(total * percent + 0.5f);
        return amt > 0 && progress >= amt;
    }

    public boolean drainPercent(float percent) {
        if (total <= 0) return false;
        int amt = Mth.floor(total * percent + 0.5f);
        if (amt <= 0 || progress < amt) return false;
        progress -= amt;
        if (progress == 0) {
            filling = false;
        }
        setChanged();
        Level l = level;
        if (l != null) l.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        return true;
    }

    @Override
    public void setChanged() {
        Level l = level;
        if (l instanceof ServerLevel s && !l.isClientSide()) {
            Packet<ClientGamePacketListener> pkt = getUpdatePacket();
            for (ServerPlayer p : GeneralUtil.tracking(s, worldPosition)) p.connection.send(pkt);
        }
        super.setChanged();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }
}
