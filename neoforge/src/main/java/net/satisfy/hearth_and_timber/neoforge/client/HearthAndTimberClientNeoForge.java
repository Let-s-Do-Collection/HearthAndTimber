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
    public static void wrapTimberFoundation(ModelEvent.ModifyBakingResult e) {
        Map<ModelResourceLocation, BakedModel> models = e.getModels();
        for (var entry : models.entrySet()) {
            ResourceLocation loc = entry.getKey().id();
            if (!HearthAndTimber.MOD_ID.equals(loc.getNamespace())) continue;
            if (loc.getPath().contains("timber_foundation")) {
                models.put(entry.getKey(), new FoundationTexturedModel(entry.getValue(), (q, s) -> true));
            }
        }
    }

    @SubscribeEvent
    public static void wrapFireplaceCornice(ModelEvent.ModifyBakingResult e) {
        Map<ModelResourceLocation, BakedModel> models = e.getModels();
        for (var entry : models.entrySet()) {
            ResourceLocation loc = entry.getKey().id();
            if (!HearthAndTimber.MOD_ID.equals(loc.getNamespace())) continue;
            if (loc.getPath().contains("fireplace_cornice")) {
                models.put(entry.getKey(), new FoundationTexturedModel(entry.getValue(), (q, s) -> q.getTintIndex() == 1));
            }
        }
    }

}
