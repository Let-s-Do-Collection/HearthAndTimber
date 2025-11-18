package net.satisfy.hearth_and_timber.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;

public interface ExtendedLevelChunk {
    enum MimicType {
        FOUNDATION, TIMBER, CROSS, GRID, DIAGONAL;

        public static MimicType forBlock(Block blk) {
            if (blk.equals(ObjectRegistry.TIMBER_FRAME.get())) return TIMBER;
            if (blk.equals(ObjectRegistry.TIMBER_CROSS_FRAME.get())) return CROSS;
            if (blk.equals(ObjectRegistry.TIMBER_GRID_FRAME.get())) return GRID;
            if (blk.equals(ObjectRegistry.TIMBER_DIAGONAL_FRAME.get())) return DIAGONAL;
            return FOUNDATION;
        }
    }

    default BlockState heandtim$getMimicState(MimicType mimicType, BlockPos pos) {
        throw new AssertionError("Method not implemented");
    }
}
