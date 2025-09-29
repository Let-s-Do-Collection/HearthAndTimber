package net.satisfy.hearth_and_timber.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.Fluids;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.client.renderer.model.BathtubModel;
import net.satisfy.hearth_and_timber.core.block.BathtubBlock;
import net.satisfy.hearth_and_timber.core.block.entity.BathtubBlockEntity;

public class BathtubRenderer implements BlockEntityRenderer<BathtubBlockEntity> {
    private static final ResourceLocation TEXTURE = HearthAndTimber.identifier("textures/entity/bathtub.png");
    private final BathtubModel model;

    public BathtubRenderer(BlockEntityRendererProvider.Context ctx) {
        this.model = new BathtubModel(ctx.bakeLayer(BathtubModel.LAYER_LOCATION));
    }

    @Override
    public void render(BathtubBlockEntity be, float partial, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        if (be.getBlockState().getValue(BathtubBlock.PART) != BedPart.HEAD) return;

        pose.pushPose();
        pose.translate(1.5, 1.5, 0.5);
        pose.mulPose(Axis.YN.rotationDegrees(be.getBlockState().getValue(BathtubBlock.FACING).toYRot()));
        pose.mulPose(Axis.XP.rotationDegrees(180));
        var vc = buffers.getBuffer(model.renderType(TEXTURE));
        model.renderToBuffer(pose, vc, light, OverlayTexture.NO_OVERLAY, -1);
        pose.popPose();

        if (!(be.getBlockState().getValue(BathtubBlock.FILLING) || be.getBlockState().getValue(BathtubBlock.FILLED))) return;

        float ratio = be.getBlockState().getValue(BathtubBlock.FILLED) ? 1f : be.getFillRatio(partial);
        if (ratio < 0f) ratio = 0f;
        if (ratio > 1f) ratio = 1f;

        float base = 0.475f;
        float height = 0.46f;
        float eps = 0.0015f;

        float yMax = base + height * ratio - eps;

        float xMin = 2f / 16f + eps;
        float xMax = 14f / 16f - eps;
        float zMin = -9f / 16f + eps;
        float zMax = 19f / 16f - eps;

        pose.pushPose();
        pose.translate(0.8, 0.0, 0.5);
        pose.mulPose(Axis.YN.rotationDegrees(be.getBlockState().getValue(BathtubBlock.FACING).toYRot()));
        pose.translate(-0.5, 0.0, -0.5);
        FluidRenderer.renderFluidBox(FluidStack.create(Fluids.WATER, 100L), xMin, base + eps, zMin, xMax, yMax, zMax, buffers, pose, light, false, be.getLevel(), be.getBlockPos());
        pose.popPose();
    }



}
