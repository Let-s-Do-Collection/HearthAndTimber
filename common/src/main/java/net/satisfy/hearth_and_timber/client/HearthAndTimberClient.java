package net.satisfy.hearth_and_timber.client;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;

import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class HearthAndTimberClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(), FRAMEWORK.get(), CATTLEGRID.get(), RUSTIC_WARDROBE.get()
        );

        registerStorageTypeRenderers();
        registerBlockEntityRenderer();
         }

    public static void registerEntityRenderers() {
        }


    public static void preInitClient() {
        registerEntityRenderers();
        registerEntityModelLayer();
    }

    public static void registerEntityModelLayer() {
    }

    public static void registerStorageTypeRenderers() {
       }

    public static void registerBlockEntityRenderer() {
      }
}
