package net.satisfy.hearth_and_timber.neoforge;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.neoforge.core.config.HearthAndTimberNeoForgeConfig;

import java.util.Objects;

@Mod(HearthAndTimber.MOD_ID)
public class HearthAndTimberNeoForge {
    
    public HearthAndTimberNeoForge(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, HearthAndTimberNeoForgeConfig.COMMON_CONFIG);
        Objects.requireNonNull(modContainer.getEventBus()).addListener(HearthAndTimberNeoForgeConfig::onLoad);
        modContainer.getEventBus().addListener(HearthAndTimberNeoForgeConfig::onReload);
        HearthAndTimber.init();
    }
}
