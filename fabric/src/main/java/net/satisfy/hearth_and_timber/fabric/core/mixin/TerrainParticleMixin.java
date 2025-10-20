package net.satisfy.hearth_and_timber.fabric.core.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;

@Mixin(TerrainParticle.class)
public abstract class TerrainParticleMixin extends TextureSheetParticle {

    protected TerrainParticleMixin(ClientLevel clientLevel, double d, double e, double f) {
        super(clientLevel, d, e, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At("TAIL"))
    public void switchTimberSprite(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, BlockState blockState, BlockPos blockPos, CallbackInfo ci) {
        BlockState held;
        Optional<TimberFrameBlockEntity> maybe = clientLevel.getBlockEntity(blockPos, EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get());
        if (maybe.isEmpty()) maybe = clientLevel.getBlockEntity(blockPos.below(), EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get());
        if (maybe.isPresent()) held = maybe.get().getHeldBlock(); else return;

        if (Objects.isNull(held) || held.isAir() || RandomSource.create().nextDouble() > 0.3) return;

        this.setSprite(Minecraft.getInstance().getBlockRenderer()
                .getBlockModelShaper().getParticleIcon(held)
        );
    }

}
