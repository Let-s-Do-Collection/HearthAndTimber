package net.satisfy.hearth_and_timber.neoforge.core.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.hearth_and_timber.client.HearthAndTimberRTs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderType.class)
public class RenderTypeMixin {

    @Shadow
    @Final
    @Mutable
    public static ImmutableList<RenderType> CHUNK_BUFFER_LAYERS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void moreChunkRenderTypes(CallbackInfo ci) {
        List<RenderType> layers = new ArrayList<>(CHUNK_BUFFER_LAYERS);
        HearthAndTimberRTs.FOUNDATION_LAYER.chunkLayerId = layers.size();
        layers.add(HearthAndTimberRTs.FOUNDATION_LAYER);
        CHUNK_BUFFER_LAYERS = ImmutableList.copyOf(layers);
    }
}
