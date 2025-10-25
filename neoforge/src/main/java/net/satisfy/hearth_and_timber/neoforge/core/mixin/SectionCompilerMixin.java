package net.satisfy.hearth_and_timber.neoforge.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.Objects;

@Mixin(SectionCompiler.class)
public abstract class SectionCompilerMixin {

    @Shadow
    @Final
    private BlockRenderDispatcher blockRenderer;

    @Shadow
    protected abstract BufferBuilder getOrBeginLayer(Map<RenderType, BufferBuilder> map, SectionBufferBuilderPack pack, RenderType layer);

    @WrapOperation(method = "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;Lnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V"))
    private void renderMimicAfterOriginal(BlockRenderDispatcher dispatcher, BlockState state, BlockPos pos, BlockAndTintGetter getter, PoseStack poseStack, VertexConsumer consumer, boolean checkSides, RandomSource random, ModelData modelData, RenderType layer, Operation<Void> original, @Local RenderChunkRegion region, @Local SectionBufferBuilderPack pack, @Local Map<RenderType, BufferBuilder> map) {
        original.call(dispatcher, state, pos, getter, poseStack, consumer, checkSides, random, modelData, layer);
        if (state.is(ObjectRegistry.TIMBER_FRAME)
                || state.is(ObjectRegistry.TIMBER_CROSS_FRAME)
                || state.is(ObjectRegistry.TIMBER_DIAGONAL_FRAME)
                || state.is(ObjectRegistry.TIMBER_GRID_FRAME)) {
            region.getBlockEntity(pos, EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get()).ifPresent(be -> {
                BlockState mimic = be.getMimicState();
                if (Objects.isNull(mimic) || mimic.isAir()) return;
                RenderType mimicLayer = ItemBlockRenderTypes.getChunkRenderType(mimic);
                BufferBuilder buf = this.getOrBeginLayer(map, pack, mimicLayer);
                this.blockRenderer.renderBatched(mimic, pos, region, poseStack, buf, true, random, modelData, mimicLayer);
            });
        }
    }
}
