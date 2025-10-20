package net.satisfy.hearth_and_timber.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.client.HearthAndTimberClient;
import net.satisfy.hearth_and_timber.client.TimberHelper;
import net.satisfy.hearth_and_timber.neoforge.client.renderer.block.ConnectingTimberModel;
import net.satisfy.hearth_and_timber.neoforge.client.renderer.block.FoundationTexturedModel;

@EventBusSubscriber(modid = HearthAndTimber.MOD_ID, value = Dist.CLIENT)
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
        e.getModels().entrySet().stream()
                .filter(entry ->
                        TimberHelper.isFoundation(entry.getKey().id().withPrefix("block/")))
                .forEach(entry -> e.getModels().put(entry.getKey(),
                        new FoundationTexturedModel(entry.getValue(), (q, s) -> true)));
        e.getModels().entrySet().stream()
                .filter(entry ->
                        TimberHelper.isConnecting(entry.getKey().id().withPrefix("block/")))
                .forEach(entry -> e.getModels().put(entry.getKey(),
                        new ConnectingTimberModel(entry.getValue())));
    }
}
