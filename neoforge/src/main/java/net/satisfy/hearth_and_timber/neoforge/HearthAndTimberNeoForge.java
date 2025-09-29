package net.satisfy.hearth_and_timber.neoforge;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.satisfy.hearth_and_timber.HearthAndTimber;

@Mod(HearthAndTimber.MOD_ID)
public class HearthAndTimberNeoForge {
    
    public HearthAndTimberNeoForge(ModContainer modContainer) {
          HearthAndTimber.init();
    }
}
