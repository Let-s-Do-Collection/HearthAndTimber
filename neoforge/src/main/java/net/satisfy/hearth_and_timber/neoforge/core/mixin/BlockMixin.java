package net.satisfy.hearth_and_timber.neoforge.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(Block.class)
public class BlockMixin {

    @WrapOperation(method = "shouldRenderFace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;skipRendering(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"))
    private static boolean skipHiddenFacesForMimic(BlockState instance, BlockState adjacent, Direction direction, Operation<Boolean> original, @Local(argsOnly = true) BlockGetter level, @Local(argsOnly = true, ordinal = 1) BlockPos adjPos) {
        if (adjacent.is(ObjectRegistry.TIMBER_FRAME) || adjacent.is(ObjectRegistry.TIMBER_CROSS_FRAME) || adjacent.is(ObjectRegistry.TIMBER_DIAGONAL_FRAME) || adjacent.is(ObjectRegistry.TIMBER_GRID_FRAME)) {
            var entity = level.getBlockEntity(adjPos, EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get());
            if (entity.isPresent() && Objects.nonNull(entity.get().getMimicState()) && !entity.get().getMimicState().isAir()) {
                return original.call(instance, entity.get().getMimicState(), direction);
            }
        }
        return original.call(instance, adjacent, direction);
    }
}
