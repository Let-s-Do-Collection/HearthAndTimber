package net.satisfy.hearth_and_timber.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.satisfy.hearth_and_timber.HearthAndTimber;

import java.util.Optional;

public class HearthAndTimberFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HearthAndTimber.init();

        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(HearthAndTimber.MOD_ID);
        modContainer.ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(
                HearthAndTimber.identifier("connected_timber"),
                container,
                ResourcePackActivationType.ALWAYS_ENABLED
        ));
    }
}
