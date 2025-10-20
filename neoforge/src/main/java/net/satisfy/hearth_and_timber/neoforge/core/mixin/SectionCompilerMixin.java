package net.satisfy.hearth_and_timber.neoforge.core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;
import net.satisfy.hearth_and_timber.client.TimberHelper;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(value = SectionCompiler.class)
public abstract class SectionCompilerMixin {
    @Shadow
    @Final
    private BlockRenderDispatcher blockRenderer;

    @Shadow
    protected abstract BufferBuilder getOrBeginLayer(Map<RenderType, BufferBuilder> map, SectionBufferBuilderPack arg, RenderType arg2);

    @Inject(method = "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;Lnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V", shift = At.Shift.AFTER))
    public void tryAgain(
            SectionPos sectionPos, RenderChunkRegion renderChunkRegion,
            VertexSorting vertexSorting, SectionBufferBuilderPack sectionBufferBuilderPack,
            List<AddSectionGeometryEvent.AdditionalSectionRenderer> additionalRenderers,
            CallbackInfoReturnable<SectionCompiler.Results> cir, @Local BlockState blockState,
            @Local PoseStack poseStack, @Local Map<RenderType, BufferBuilder> map,
            @Local(ordinal = 2) BlockPos blockPos, @Local RandomSource randomSource
    ) {
        if (Objects.nonNull(blockState) && TimberHelper.isConnecting(blockState)) {
            renderChunkRegion.getBlockEntity(blockPos, EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get()).ifPresent(entity -> {
                BlockState contained = entity.getHeldBlock();
                if (Objects.isNull(contained)) return;
                RenderType renderType = ItemBlockRenderTypes.getChunkRenderType(contained);

                BufferBuilder bufferBuilder = this.getOrBeginLayer(map, sectionBufferBuilderPack, renderType);
                this.blockRenderer.renderBatched(contained, blockPos, renderChunkRegion, poseStack, bufferBuilder, true, randomSource);
            });
        }
    }
}
