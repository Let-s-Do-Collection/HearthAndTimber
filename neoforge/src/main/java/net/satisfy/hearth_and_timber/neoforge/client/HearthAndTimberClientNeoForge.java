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
    public static final ResourceLocation FOUNDATION_STRAIGHT = HearthAndTimber.identifier("block/foundation_block");
    public static final ResourceLocation FOUNDATION_INNER = HearthAndTimber.identifier("block/foundation_block_inner");
    public static final ResourceLocation FOUNDATION_OUTER = HearthAndTimber.identifier("block/foundation_block_outer");

    @SubscribeEvent
    public static void beforeClientSetup(RegisterEvent event) {
        HearthAndTimberClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        HearthAndTimberClient.onInitializeClient();
    }

    @SubscribeEvent
    public static void onModelModify(ModelEvent.ModifyBakingResult event) {
        System.out.println("[FOUNDATION] ModifyBakingResult fired!");
        Map<ModelResourceLocation, BakedModel> models = event.getModels();
        for (var entry : models.entrySet()) {
            ModelResourceLocation id = entry.getKey();
            ResourceLocation loc = id.id();
            if (isFoundationModel(loc)) {
                System.out.println("[FOUNDATION] Replaced model for " + loc);
                BakedModel original = entry.getValue();
                models.put(id, new FoundationTexturedModel(original));
            }
        }
    }

    private static boolean isFoundationModel(ResourceLocation id) {
        return id.equals(FOUNDATION_STRAIGHT) || id.equals(FOUNDATION_INNER) || id.equals(FOUNDATION_OUTER);
    }
}