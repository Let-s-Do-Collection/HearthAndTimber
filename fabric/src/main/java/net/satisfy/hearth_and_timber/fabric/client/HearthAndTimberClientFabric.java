package net.satisfy.hearth_and_timber.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.satisfy.hearth_and_timber.client.HearthAndTimberClient;
public class HearthAndTimberClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HearthAndTimberClient.preInitClient();
        HearthAndTimberClient.onInitializeClient();
    }
}
