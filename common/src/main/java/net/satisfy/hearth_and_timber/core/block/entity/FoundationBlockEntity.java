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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class FoundationBlockEntity extends BlockEntity {
    private BlockState mimicState;

    public FoundationBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.FOUNDATION_BLOCK_ENTITY.get(), pos, state);
    }

    public BlockState getMimicState() {
        return mimicState;
    }

    private static BooleanProperty findApplied(BlockState state) {
        for (Property<?> p : state.getProperties()) {
            if (p instanceof BooleanProperty bp && p.getName().equals("applied")) return bp;
        }
        return null;
    }

    public void setMimicState(BlockState newMimicState) {
        if (level == null) {
            mimicState = newMimicState;
            return;
        }
        mimicState = newMimicState;
        setChanged();
        if (level instanceof ServerLevel server) {
            server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(this);
            server.getChunkSource().chunkMap
                    .getPlayers(server.getChunkAt(worldPosition).getPos(), false)
                    .forEach(p -> p.connection.send(packet));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (mimicState != null) tag.put("Mimic", NbtUtils.writeBlockState(mimicState));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("Mimic")) mimicState = NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), tag.getCompound("Mimic"));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        if (mimicState != null) tag.put("Mimic", NbtUtils.writeBlockState(mimicState));
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
