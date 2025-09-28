package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.satisfy.hearth_and_timber.HearthAndTimber;

import java.util.function.Supplier;

public class MobEffectRegistry {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.MOB_EFFECT);
    private static final Registrar<MobEffect> MOB_EFFECTS_REGISTRAR = MOB_EFFECTS.getRegistrar();

    private static RegistrySupplier<MobEffect> registerEffect(String name, Supplier<MobEffect> effect) {
        if (Platform.isNeoForge()) {
            return MOB_EFFECTS.register(name, effect);
        }
        return MOB_EFFECTS_REGISTRAR.register(HearthAndTimber.identifier(name), effect);
    }

    public static void init() {
        MOB_EFFECTS.register();
    }

    public static Holder<MobEffect> holder(RegistrySupplier<MobEffect> supplier) {
        return BuiltInRegistries.MOB_EFFECT.getResourceKey(supplier.get())
                .flatMap(BuiltInRegistries.MOB_EFFECT::getHolder)
                .orElseThrow();
    }

    public static MobEffectInstance inst(RegistrySupplier<MobEffect> supplier, int duration) {
        return new MobEffectInstance(holder(supplier), duration);
    }

    static {
    }
}
