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
        ModelLoadingPlugin.register(context -> context.modifyModelAfterBake().register((bakedModel, ctx) -> {
            ResourceLocation id = ctx.resourceId();
            if (id == null) {
                return bakedModel;
            }

            String path = id.getPath();

            boolean isFoundationPart =
                    path.startsWith("block/timber_foundation")
                            || path.startsWith("block/timber_base_skirt")
                            || path.startsWith("block/timber_base_trim");

            boolean isFramePlaceholder =
                    path.startsWith("block/timber_frame")
                            || path.startsWith("block/timber_grid_frame")
                            || path.startsWith("block/timber_diagonal_frame")
                            || path.startsWith("block/timber_cross_frame")
                            || path.startsWith("block/timber_stairs")
                            || path.equals("item/timber_frame")
                            || path.equals("item/timber_grid_frame")
                            || path.equals("item/timber_diagonal_frame")
                            || path.equals("item/timber_cross_frame")
                            || path.equals("item/timber_stairs");

            if (isFoundationPart || isFramePlaceholder) {
                return bakedModel == null ? null : new FoundationTexturedModel(bakedModel, isFramePlaceholder);
            }

            return bakedModel;
        }));
    }

}