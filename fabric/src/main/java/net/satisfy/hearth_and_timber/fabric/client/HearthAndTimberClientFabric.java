package net.satisfy.hearth_and_timber.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.client.HearthAndTimberClient;
import net.satisfy.hearth_and_timber.fabric.client.renderer.block.FoundationTexturedModel;

public class HearthAndTimberClientFabric implements ClientModInitializer {
    public static final ResourceLocation FOUNDATION_STRAIGHT = HearthAndTimber.identifier("block/foundation_block");
    public static final ResourceLocation FOUNDATION_INNER = HearthAndTimber.identifier("block/foundation_block_inner");
    public static final ResourceLocation FOUNDATION_OUTER = HearthAndTimber.identifier("block/foundation_block_outer");

    @Override
    public void onInitializeClient() {
        HearthAndTimberClient.onInitializeClient();
        HearthAndTimberClient.preInitClient();
        registerFoundationModelHandler();
    }

    private static void registerFoundationModelHandler() {
        ModelLoadingPlugin.register(context ->
                context.modifyModelAfterBake().register((bakedModel, ctx) -> {
                    ResourceLocation id = ctx.resourceId() != null
                            ? ctx.resourceId()
                            : (ctx.topLevelId() != null ? ctx.topLevelId().id() : null);
                    if (id != null && isFoundationModel(id)) {
                        return bakedModel == null ? null : new FoundationTexturedModel(bakedModel);
                    }
                    return bakedModel;
                })
        );
    }

    private static boolean isFoundationModel(ResourceLocation id) {
        return id.equals(FOUNDATION_STRAIGHT)
                || id.equals(FOUNDATION_INNER)
                || id.equals(FOUNDATION_OUTER);
    }
}