package net.satisfy.hearth_and_timber.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class TimberFrameBlockEntity extends BlockEntity {
    private BlockState mimicState;

    public TimberFrameBlockEntity(BlockPos blockPos, BlockState state) {
        super(EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get(), blockPos, state);
    }

    public BlockState getMimicState() {
        return mimicState;
    }

    public void setMimicState(BlockState newMimicState) {
        if (level == null) {
            mimicState = newMimicState;
            return;
        }
        mimicState = newMimicState;
        setChanged();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(this);
            serverLevel.getChunkSource().chunkMap
                    .getPlayers(serverLevel.getChunkAt(worldPosition).getPos(), false)
                    .forEach(player -> player.connection.send(packet));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if (mimicState != null) {
            compoundTag.put("Mimic", NbtUtils.writeBlockState(mimicState));
        }
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        if (compoundTag.contains("Mimic")) {
            mimicState = NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), compoundTag.getCompound("Mimic"));
        } else {
            mimicState = null;
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        if (mimicState != null) {
            tag.put("Mimic", NbtUtils.writeBlockState(mimicState));
        }
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}