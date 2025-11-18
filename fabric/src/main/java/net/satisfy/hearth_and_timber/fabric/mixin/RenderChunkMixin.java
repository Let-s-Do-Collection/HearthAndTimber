package net.satisfy.hearth_and_timber.fabric.mixin;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.satisfy.hearth_and_timber.client.TimberHelper;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.client.ExtendedRenderChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Mixin(RenderChunk.class)
public class RenderChunkMixin implements ExtendedRenderChunk {

    @SuppressWarnings("unchecked")
    @Unique Map<BlockPos, BlockState>[] mimicStates = new Object2ObjectOpenHashMap[] {
            new Object2ObjectOpenHashMap<BlockPos, BlockState>(), // any foundation
            new Object2ObjectOpenHashMap<BlockPos, BlockState>(), // timber
            new Object2ObjectOpenHashMap<BlockPos, BlockState>(), // cross
            new Object2ObjectOpenHashMap<BlockPos, BlockState>(), // grid
            new Object2ObjectOpenHashMap<BlockPos, BlockState>() // diagonal
    };

    @Override
    public BlockState heandtim$getMimicState(TimberHelper.MimicType mimicType, BlockPos pos) {
        return mimicStates[mimicType.ordinal()].get(pos);
    }

    @Inject(method = "setBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    public void captureMimicState(BlockEntity blockEntity, CallbackInfo ci) {
        if (blockEntity.getType().equals(EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get())) {
            mimicStates[
                    TimberHelper.MimicType.forBlock(blockEntity.getBlockState().getBlock()).ordinal()
            ].put(blockEntity.getBlockPos(), Objects.requireNonNullElseGet(
                    ((TimberFrameBlockEntity) blockEntity).getMimicState(), Blocks.AIR::defaultBlockState
            ));
        }
    }

    @Inject(method = "removeBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;"))
    public void deleteMimicState(BlockPos blockPos, CallbackInfo ci) {
        Arrays.stream(mimicStates)
                .filter(m -> m.get(blockPos) != null).findFirst()
                .ifPresent(m -> m.remove(blockPos));
    }
}
