package net.satisfy.hearth_and_timber.neoforge.client;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.client.HearthAndTimberClient;
import net.satisfy.hearth_and_timber.neoforge.client.renderer.block.FoundationTexturedModel;

import java.util.Map;

@EventBusSubscriber(modid = HearthAndTimber.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class HearthAndTimberClientNeoForge {

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        HearthAndTimberClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        HearthAndTimberClient.onInitializeClient();
    }

    @SubscribeEvent
    public static void wrapTimberFoundation(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> models = event.getModels();
        for (var entry : models.entrySet()) {
            ResourceLocation id = entry.getKey().id();
            if (id == null || !HearthAndTimber.MOD_ID.equals(id.getNamespace())) continue;

            String path = id.getPath();
            boolean isFoundationPart =
                    path.startsWith("block/timber_foundation") || path.startsWith("timber_foundation") ||
                            path.startsWith("block/timber_base_skirt") || path.startsWith("timber_base_skirt") ||
                            path.startsWith("block/timber_base_trim")  || path.startsWith("timber_base_trim");

            boolean isFramePlaceholder =
                    path.startsWith("block/timber_frame")          || path.startsWith("timber_frame") ||
                            path.startsWith("block/timber_grid_frame")     || path.startsWith("timber_grid_frame") ||
                            path.startsWith("block/timber_diagonal_frame") || path.startsWith("timber_diagonal_frame") ||
                            path.startsWith("block/timber_cross_frame")    || path.startsWith("timber_cross_frame");

            if (isFoundationPart || isFramePlaceholder) {
                BakedModel original = entry.getValue();
                if (original == null) continue;
                models.put(entry.getKey(), new FoundationTexturedModel(original, isFramePlaceholder));
            }
        }
    }
}