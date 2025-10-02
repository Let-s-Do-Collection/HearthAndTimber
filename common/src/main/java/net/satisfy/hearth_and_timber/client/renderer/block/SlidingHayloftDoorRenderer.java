package net.satisfy.hearth_and_timber.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.client.renderer.model.SlidingHayloftDoorModel;
import net.satisfy.hearth_and_timber.core.block.SlidingHayloftDoorBlock;
import net.satisfy.hearth_and_timber.core.block.entity.SlidingHayloftDoorBlockEntity;

public class SlidingHayloftDoorRenderer implements BlockEntityRenderer<SlidingHayloftDoorBlockEntity> {
    private ResourceLocation textureFor(SlidingHayloftDoorBlockEntity be) {
        String wood = be.getWood();
        if (be.isReinforced()) {
            return HearthAndTimber.identifier("textures/entity/barn_doors/" + wood + "_reinforced.png");
        }
        return HearthAndTimber.identifier("textures/entity/barn_doors/" + wood + ".png");
    }

    private final SlidingHayloftDoorModel model;

    public SlidingHayloftDoorRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SlidingHayloftDoorModel(context.bakeLayer(SlidingHayloftDoorModel.LAYER_LOCATION));
    }

    @Override
    public void render(SlidingHayloftDoorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!(blockEntity.getBlockState().getBlock() instanceof SlidingHayloftDoorBlock)) return;
        if (blockEntity.getBlockState().getValue(SlidingHayloftDoorBlock.PART) != SlidingHayloftDoorBlock.Quarter.BL) return;

        poseStack.pushPose();
        Direction facing = blockEntity.getBlockState().getValue(SlidingHayloftDoorBlock.FACING);
        SlidingHayloftDoorBlock.HingeSide hingeSide = blockEntity.getBlockState().getValue(SlidingHayloftDoorBlock.HINGE);

        switch (hingeSide) {
            case LEFT -> {
                switch (facing) {
                    case EAST -> poseStack.translate(1.3125, 1.5, 0.5);
                    case WEST -> poseStack.translate(-0.3125, 1.5, 0.5);
                    case NORTH -> poseStack.translate(0.5, 1.5, -0.3125);
                    case SOUTH -> poseStack.translate(0.5, 1.5, 1.3125);
                }
            }
            case RIGHT -> {
                switch (facing) {
                    case EAST -> poseStack.translate(1.3125, 1.5, 1.5);
                    case WEST -> poseStack.translate(-0.3125, 1.5, -0.5);
                    case NORTH -> poseStack.translate(1.5, 1.5, -0.3125);
                    case SOUTH -> poseStack.translate(-0.5, 1.5, 1.3125);
                }
            }
        }

        float distance = blockEntity.getSlide() * (26f / 16f);
        Direction lateral = hingeSide == SlidingHayloftDoorBlock.HingeSide.RIGHT ? facing.getClockWise() : facing.getCounterClockWise();
        double offsetX = 0.0;
        double offsetZ = 0.0;
        switch (lateral) {
            case NORTH -> offsetZ -= distance;
            case SOUTH -> offsetZ += distance;
            case WEST  -> offsetX -= distance;
            case EAST  -> offsetX += distance;
        }
        double forwardOffset = (1.0 / 16.0) * distance;
        switch (facing) {
            case NORTH -> offsetZ -= forwardOffset;
            case SOUTH -> offsetZ += forwardOffset;
            case WEST  -> offsetX -= forwardOffset;
            case EAST  -> offsetX += forwardOffset;
        }
        poseStack.translate(offsetX, 0.0, offsetZ);

        poseStack.mulPose(Axis.YN.rotationDegrees(facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        if (hingeSide == SlidingHayloftDoorBlock.HingeSide.RIGHT) {
            poseStack.translate(1.0, 0.0, 0.0);
            poseStack.scale(-1.0F, 1.0F, 1.0F);
        }

        var vertexConsumer = bufferSource.getBuffer(model.renderType(textureFor(blockEntity)));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
        poseStack.popPose();
    }

}
