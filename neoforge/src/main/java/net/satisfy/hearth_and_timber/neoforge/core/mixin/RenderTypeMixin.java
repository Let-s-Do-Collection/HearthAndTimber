package net.satisfy.hearth_and_timber.neoforge.core.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.hearth_and_timber.client.HearthAndTimberRTs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(RenderType.class)
public class RenderTypeMixin {

    @Shadow public static ImmutableList<RenderType> CHUNK_BUFFER_LAYERS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void moreChunkRenderTypes(CallbackInfo ci) {
        List<RenderType> chunk_layers = new ArrayList<>(CHUNK_BUFFER_LAYERS);
        // prevent neo crash
        HearthAndTimberRTs.DAMN_YOU_MOJANG.chunkLayerId = chunk_layers.size();

        chunk_layers.add(HearthAndTimberRTs.DAMN_YOU_MOJANG);
        CHUNK_BUFFER_LAYERS = ImmutableList.copyOf(chunk_layers);
    }

}
