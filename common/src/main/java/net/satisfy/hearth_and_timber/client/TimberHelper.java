package net.satisfy.hearth_and_timber.client;

import net.minecraft.resources.ResourceLocation;
import net.satisfy.hearth_and_timber.HearthAndTimber;

public class TimberHelper {
    public static boolean isFoundation(ResourceLocation id) {
        return id.getNamespace().equals(HearthAndTimber.MOD_ID) && (
            id.getPath().startsWith("block/timber_foundation") ||
            id.getPath().startsWith("block/timber_base_skirt") ||
            id.getPath().startsWith("block/timber_base_trim")
        );
    }

    public static boolean isFrame(ResourceLocation id) {
        return id.getNamespace().equals(HearthAndTimber.MOD_ID) && (
                id.getPath().startsWith("block/timber_frame") ||
                id.getPath().startsWith("block/timber_cross_frame") ||
                id.getPath().startsWith("block/timber_diagonal_frame") ||
                id.getPath().startsWith("block/timber_grid_frame")
        );
    }
}