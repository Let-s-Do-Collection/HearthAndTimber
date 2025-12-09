package net.satisfy.hearth_and_timber.core.compat.everycomp;

import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;

public final class HearthAndTimberEveryCompatInit {
    public static void init() {
        EveryCompatAPI.registerModule(new HearthAndTimberWoodGoodModule("hearth_and_timber"));
    }
}