package net.satisfy.hearth_and_timber.fabric;

import net.fabricmc.api.ModInitializer;
import net.satisfy.hearth_and_timber.HearthAndTimber;

public class HearthAndTimberFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HearthAndTimber.init();
    }
}
