package net.satisfy.hearth_and_timber.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
        ModelLoadingPlugin.register(context -> {
            context.modifyModelAfterBake().register(((bakedModel, ctx) -> {
                ResourceLocation id = ctx.resourceId();
                if (id == null) return bakedModel;
                String path = id.getPath();
                boolean isFoundationPart = path.startsWith("block/timber_foundation") || path.startsWith("block/timber_base_skirt") || path.startsWith("block/timber_base_trim");
                boolean isFramePlaceholder = path.matches("(?:block|item)/(?:timber_frame|timber_grid_frame|timber_diagonal_frame|timber_cross_frame|placeholder)");
                if ((isFoundationPart || isFramePlaceholder)) {
                    return bakedModel == null ? null : new FoundationTexturedModel(bakedModel, isFramePlaceholder);
                }
                return bakedModel;
            }));


        });

    }

}