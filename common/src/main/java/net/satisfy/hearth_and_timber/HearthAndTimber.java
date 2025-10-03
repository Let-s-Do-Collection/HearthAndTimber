package net.satisfy.hearth_and_timber;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.hearth_and_timber.core.registry.*;

public class HearthAndTimber {
    public static final String MOD_ID = "hearth_and_timber";

    public static ResourceLocation identifier(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void init() {
        ObjectRegistry.init();
        EntityTypeRegistry.init();
        TabRegistry.init();
        SoundEventRegistry.init();
    }
}