package net.satisfy.hearth_and_timber.neoforge.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.client.TimberHelper;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(Block.class)
public class BlockMixin {

    @WrapOperation(method = "shouldRenderFace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;skipRendering(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"))
    private static boolean noInternalFacesBitte(BlockState instance, BlockState adjacent, Direction direction, Operation<Boolean> original, @Local(argsOnly = true) BlockGetter level, @Local(argsOnly = true, ordinal = 1) BlockPos adjPos) {
        // first block check so we don't do unnecessary load
        if (TimberHelper.isConnecting(adjacent)) {
            var e = level.getBlockEntity(adjPos, EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get());
            if (e.isPresent() && Objects.nonNull(e.get().getHeldBlock())
                && !e.get().getHeldBlock().isAir()
            ) return original.call(instance, e.get().getHeldBlock(), direction);
        }
        return original.call(instance, adjacent, direction);
    }

}
