package net.satisfy.hearth_and_timber.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HearthAndTimberClient {

    public static void onInitializeClient() {


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
