package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.satisfy.hearth_and_timber.HearthAndTimber;

public class SoundEventRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> SLIDING_DOOR_OPEN = SOUND_EVENTS.register("sliding_door_open", () -> SoundEvent.createVariableRangeEvent(HearthAndTimber.identifier("sliding_door_open")));
    public static final RegistrySupplier<SoundEvent> SLIDING_DOOR_CLOSE = SOUND_EVENTS.register("sliding_door_close", () -> SoundEvent.createVariableRangeEvent(HearthAndTimber.identifier("sliding_door_close")));

    public static void init() {
        SOUND_EVENTS.register();
    }
}