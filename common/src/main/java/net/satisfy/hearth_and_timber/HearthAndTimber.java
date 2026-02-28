package net.satisfy.hearth_and_timber;

import com.google.common.reflect.Reflection;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;
import net.satisfy.hearth_and_timber.core.registry.TabRegistry;

public class HearthAndTimber {
    public static final String MOD_ID = "hearth_and_timber";

    public static ResourceLocation identifier(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void init() {
        Reflection.initialize(
                ObjectRegistry.class,
                EntityTypeRegistry.class,
                TabRegistry.class
        );
    }
}