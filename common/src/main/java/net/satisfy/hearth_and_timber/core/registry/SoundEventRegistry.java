package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.satisfy.hearth_and_timber.HearthAndTimber;

public class SoundEventRegistry {

    private static final Registrar<SoundEvent> SOUND_EVENTS = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.SOUND_EVENT).getRegistrar();

    public static void init() {}

    private static RegistrySupplier<SoundEvent> create(String name) {
        ResourceLocation id = HearthAndTimber.identifier(name);
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(id));
    }
}