package net.satisfy.hearth_and_timber.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.satisfy.hearth_and_timber.client.HearthAndTimberClient;
import net.satisfy.hearth_and_timber.client.TimberHelper;
import net.satisfy.hearth_and_timber.fabric.client.renderer.block.ConnectingTimberModel;
import net.satisfy.hearth_and_timber.fabric.client.renderer.block.FoundationTexturedModel;

import java.util.Objects;


public class HearthAndTimberClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HearthAndTimberClient.onInitializeClient();
        HearthAndTimberClient.preInitClient();
        registerFoundationModelHandler();
    }

    private static void registerFoundationModelHandler() {
        ModelLoadingPlugin.register(context -> context.modifyModelAfterBake().register((bakedModel, ctx) -> {
            if (Objects.nonNull(ctx.resourceId()) && Objects.nonNull(bakedModel)) {
                if (TimberHelper.isFoundation(ctx.resourceId())) return new FoundationTexturedModel(bakedModel);
                if (TimberHelper.isConnecting(ctx.resourceId())) return new ConnectingTimberModel(bakedModel);
            }
            return bakedModel;
        }));
    }
}
