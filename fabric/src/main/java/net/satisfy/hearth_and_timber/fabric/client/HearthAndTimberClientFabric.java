package net.satisfy.hearth_and_timber.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.hearth_and_timber.client.HearthAndTimberClient;
import net.satisfy.hearth_and_timber.fabric.client.renderer.block.FoundationTexturedModel;

public class HearthAndTimberClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HearthAndTimberClient.onInitializeClient();
        HearthAndTimberClient.preInitClient();
        registerFoundationModelHandler();
    }

    private static void registerFoundationModelHandler() {
        ModelLoadingPlugin.register(context ->
                context.modifyModelAfterBake().register((bakedModel, ctx) -> {
                    ResourceLocation id = ctx.resourceId();
                    if (id != null && isTexturableModel(id)) {
                        return bakedModel == null ? null : new FoundationTexturedModel(bakedModel);
                    }
                    return bakedModel;
                })
        );
    }

    private static boolean isTexturableModel(ResourceLocation id) {
        String p = id.getPath();
        if (p.startsWith("block/timber_foundation")) return true;
        if (p.startsWith("block/timber_base_skirt")) return true;
        if (p.startsWith("block/timber_base_trim")) return true;
        return false;
    }
}
