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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Mth;
import net.satisfy.hearth_and_timber.core.block.BathtubBlock;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

public class BathtubBlockEntity extends BlockEntity {
    private int total;
    private int progress;
    private int clientProgress;

    public BathtubBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.BATHTUB_BLOCK_ENTITY.get(), pos, state);
    }

    public void startFilling(int totalTicks) {
        this.total = totalTicks;
        this.progress = 0;
        this.clientProgress = 0;
        setChanged();
    }

    public void abortFilling() {
        total = 0;
        progress = 0;
        clientProgress = 0;
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BathtubBlockEntity be) {
        if (!state.getValue(BathtubBlock.FILLING) || be.total <= 0) return;

        be.progress++; be.setChanged();

        if (be.progress % 10 == 0)
            level.playSound(null, pos, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.25f, 0.9f + level.random.nextFloat() * 0.2f);

        if (be.progress % 20 == 0)
            level.playSound(null, pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.BLOCKS, 0.35f, 0.95f + level.random.nextFloat() * 0.1f);

        if (be.progress % 5 == 0) level.sendBlockUpdated(pos, state, state, 3);

        if (be.progress >= be.total) {
            level.setBlock(pos, state.setValue(BathtubBlock.FILLING, false).setValue(BathtubBlock.FILLED, true), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.7f, 1.0f);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BathtubBlockEntity be) {
        if (be.clientProgress < be.progress) be.clientProgress++;
    }

    public float getFillRatio(float partial) {
        if (total <= 0) return 0f;
        float cp = clientProgress;
        float sp = progress;
        float p = cp + (sp - cp) * Mth.clamp(partial, 0f, 1f);
        return Mth.clamp(p / (float) total, 0f, 1f);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("total", total);
        tag.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        total = tag.getInt("total");
        progress = tag.getInt("progress");
        clientProgress = progress;
    }

    @Override
    public void setChanged() {
        Level level = this.level;
        if (level instanceof ServerLevel serverLevel && !level.isClientSide()) {
            Packet<ClientGamePacketListener> pkt = getUpdatePacket();
            for (ServerPlayer p : GeneralUtil.tracking(serverLevel, worldPosition)) {
                p.connection.send(pkt);
            }
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
