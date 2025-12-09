package net.satisfy.hearth_and_timber;

import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.hearth_and_timber.core.registry.*;

import java.lang.reflect.Method;

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

        if (Platform.isModLoaded("everycomp")) {
            try {
                Class<?> compatInitClass = Class.forName("net.satisfy.hearth_and_timber.core.compat.everycomp.HearthAndTimberEveryCompatInit");
                Method initMethod = compatInitClass.getMethod("init");
                initMethod.invoke(null);
            } catch (ReflectiveOperationException ignored) {}
        }
    }
}