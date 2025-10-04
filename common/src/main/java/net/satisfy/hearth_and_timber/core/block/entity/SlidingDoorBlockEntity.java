package net.satisfy.hearth_and_timber.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.block.SlidingBarnDoorBlock;
import net.satisfy.hearth_and_timber.core.block.SlidingHayloftDoorBlock;
import net.satisfy.hearth_and_timber.core.block.SlidingStableDoorBlock;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.registry.SoundEventRegistry;
import org.jetbrains.annotations.NotNull;

public class SlidingDoorBlockEntity extends BlockEntity {
    private boolean open;
    private long animStart;
    private boolean animForward;
    private boolean playedOpenSound;
    private boolean playedCloseSound;
    private boolean allowInteractionSounds;
    private String wood = "spruce";
    private boolean reinforced;

    public SlidingDoorBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.SLIDING_DOOR_BLOCK_ENTITY.get(), pos, state);
        allowInteractionSounds = false;
    }

    public boolean isReinforced() {
        return reinforced;
    }

    public void setReinforced(boolean value) {
        if (this.reinforced != value) {
            this.reinforced = value;
            setChanged();
            if (level != null && !level.isClientSide) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void setOpen(boolean value, boolean playSound) {
        long now = System.currentTimeMillis();
        float current = getSlide();
        open = value;
        animForward = value;
        if (animForward) animStart = now - (long) (current * 12000f);
        else animStart = now - (long) ((1f - current) * 12000f);
        if (playSound) {
            allowInteractionSounds = true;
            if (animForward) { playedOpenSound = false; playedCloseSound = true; }
            else { playedCloseSound = false; playedOpenSound = true; }
        } else {
            allowInteractionSounds = false;
            playedOpenSound = true;
            playedCloseSound = true;
        }
        setChanged();
        if (level != null && !level.isClientSide) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public static void serverTick(Level level, BlockPos position, BlockState state, SlidingDoorBlockEntity be) {
        if (!(level instanceof ServerLevel server)) return;
        float slide = be.getSlide();
        if (be.allowInteractionSounds && be.animForward && !be.playedOpenSound) {
            float progress = be.getSlide();
            float pitch = 0.8f + progress * 0.6f + server.random.nextFloat() * 0.2f;
            float volume = 0.6f + progress * 0.8f;
            server.playSound(null, position, SoundEventRegistry.SLIDING_DOOR_OPEN.get(), SoundSource.BLOCKS, volume, pitch);
            be.playedOpenSound = true;
        }
        if (be.allowInteractionSounds && !be.animForward && !be.playedCloseSound) {
            float progress = 1f - be.getSlide();
            float pitch = 0.8f + progress * 0.6f + server.random.nextFloat() * 0.2f;
            float volume = 0.6f + progress * 0.8f;
            server.playSound(null, position, SoundEventRegistry.SLIDING_DOOR_CLOSE.get(), SoundSource.BLOCKS, volume, pitch);
            be.playedCloseSound = true;
        }
        if (slide > 0f && slide < 1f && (server.getGameTime() & 3L) == 0L) {
            float distance = slide * (26f / 16f);
            Direction facing = getFacing(state);
            boolean right = isRightHinge(state);
            Direction lateral = right ? facing.getClockWise() : facing.getCounterClockWise();
            double offsetX = 0.0;
            double offsetZ = 0.0;
            switch (lateral) {
                case NORTH -> offsetZ -= distance;
                case SOUTH -> offsetZ += distance;
                case WEST -> offsetX -= distance;
                case EAST -> offsetX += distance;
            }
            double forwardOffset = (1.0 / 16.0) * distance;
            switch (facing) {
                case NORTH -> offsetZ -= forwardOffset;
                case SOUTH -> offsetZ += forwardOffset;
                case WEST -> offsetX -= forwardOffset;
                case EAST -> offsetX += forwardOffset;
            }
            double particleX = position.getX() + 0.5 + offsetX;
            double particleY = position.getY() + 0.02;
            double particleZ = position.getZ() + 0.2 + offsetZ;
            BlockState below = server.getBlockState(position.below());
            server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, below), particleX, particleY, particleZ, 4, 0.02, 0.0, 0.02, 0.0);
        }
        if (!be.animForward && slide <= 0f) be.playedOpenSound = false;
        if (be.animForward && slide >= 1f) be.playedCloseSound = false;
    }

    private static Direction getFacing(BlockState state) {
        Block b = state.getBlock();
        if (b instanceof SlidingBarnDoorBlock) return state.getValue(SlidingBarnDoorBlock.FACING);
        if (b instanceof SlidingHayloftDoorBlock) return state.getValue(SlidingHayloftDoorBlock.FACING);
        if (b instanceof SlidingStableDoorBlock) return state.getValue(SlidingStableDoorBlock.FACING);
        return Direction.NORTH;
    }
    private static boolean isRightHinge(BlockState state) {
        Block b = state.getBlock();
        if (b instanceof SlidingBarnDoorBlock) {
            SlidingBarnDoorBlock.HingeSide h = state.getValue(SlidingBarnDoorBlock.HINGE);
            return h == SlidingBarnDoorBlock.HingeSide.RIGHT;
        }
        if (b instanceof SlidingHayloftDoorBlock) {
            SlidingHayloftDoorBlock.HingeSide h = state.getValue(SlidingHayloftDoorBlock.HINGE);
            return h == SlidingHayloftDoorBlock.HingeSide.RIGHT;
        }
        if (b instanceof SlidingStableDoorBlock) {
            SlidingStableDoorBlock.HingeSide h = state.getValue(SlidingStableDoorBlock.HINGE);
            return h == SlidingStableDoorBlock.HingeSide.RIGHT;
        }
        return false;
    }

    public float getSlide() {
        long now = System.currentTimeMillis();
        float t = (now - animStart) / 1000f;
        if (t < 0f) t = 0f;
        if (t > 1f) t = 1f;
        return animForward ? t : 1f - t;
    }

    public String getWood() {
        return wood;
    }

    public void setWood(String value) {
        if (value != null && !value.isEmpty() && !value.equals(this.wood)) {
            this.wood = value;
            setChanged();
            if (level != null && !level.isClientSide) level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        open = tag.getBoolean("open");
        animStart = tag.getLong("animStart");
        animForward = tag.getBoolean("animForward");
        playedOpenSound = tag.getBoolean("playedOpenSound");
        playedCloseSound = tag.getBoolean("playedCloseSound");
        if (tag.contains("wood")) wood = tag.getString("wood");
        reinforced = tag.getBoolean("reinforced");
        allowInteractionSounds = false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putBoolean("open", open);
        tag.putLong("animStart", animStart);
        tag.putBoolean("animForward", animForward);
        tag.putBoolean("playedOpenSound", playedOpenSound);
        tag.putBoolean("playedCloseSound", playedCloseSound);
        tag.putString("wood", wood);
        tag.putBoolean("reinforced", reinforced);
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
