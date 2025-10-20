package net.satisfy.hearth_and_timber.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.block.TimberFrameBlock;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class TimberFrameBlockEntity extends BlockEntity {
    private BlockState held;

    public TimberFrameBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get(), pos, state);
    }

    public BlockState getHeldBlock() {
        return held;
    }

    public void setHeldBlock(BlockState state) {
        this.held = state;
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
        if (held != null) {
            BlockState.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), held).result().ifPresent(n -> tag.put("Held", n));
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("Held")) {
            BlockState.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag.get("Held")).result().ifPresent(s -> this.held = s);
        } else {
            this.held = null;
        }
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
