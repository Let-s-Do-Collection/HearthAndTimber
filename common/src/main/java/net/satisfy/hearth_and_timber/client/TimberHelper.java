package net.satisfy.hearth_and_timber.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;

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

    public enum MimicType {
        FOUNDATION, TIMBER, CROSS, GRID, DIAGONAL;

        public static MimicType forBlock(Block blk) {
            if (blk.equals(ObjectRegistry.TIMBER_FRAME.get())) return TIMBER;
            if (blk.equals(ObjectRegistry.TIMBER_CROSS_FRAME.get())) return CROSS;
            if (blk.equals(ObjectRegistry.TIMBER_GRID_FRAME.get())) return GRID;
            if (blk.equals(ObjectRegistry.TIMBER_DIAGONAL_FRAME.get())) return DIAGONAL;
            return FOUNDATION;
        }
    }
}