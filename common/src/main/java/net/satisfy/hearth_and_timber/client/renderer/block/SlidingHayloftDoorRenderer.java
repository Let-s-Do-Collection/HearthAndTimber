package net.satisfy.hearth_and_timber.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.client.renderer.model.SlidingBarnDoorModel;
import net.satisfy.hearth_and_timber.client.renderer.model.SlidingHayloftDoorModel;
import net.satisfy.hearth_and_timber.core.block.SlidingBarnDoorBlock;
import net.satisfy.hearth_and_timber.core.block.SlidingHayloftDoorBlock;
import net.satisfy.hearth_and_timber.core.block.entity.SlidingDoorBlockEntity;

public class SlidingHayloftDoorRenderer implements BlockEntityRenderer<SlidingDoorBlockEntity> {
    private final SlidingHayloftDoorModel hayloftModel;
    private final SlidingBarnDoorModel barnModel;

    public SlidingHayloftDoorRenderer(BlockEntityRendererProvider.Context context) {
        this.hayloftModel = new SlidingHayloftDoorModel(context.bakeLayer(SlidingHayloftDoorModel.LAYER_LOCATION));
        this.barnModel = new SlidingBarnDoorModel(context.bakeLayer(SlidingBarnDoorModel.LAYER_LOCATION));
    }

    private static boolean isHayloft(BlockState state) {
        return state.getBlock() instanceof SlidingHayloftDoorBlock;
    }

    private static boolean isBarn(BlockState state) {
        return state.getBlock() instanceof SlidingBarnDoorBlock;
    }

    private static boolean isBottomLeft(BlockState state) {
        if (isHayloft(state)) return state.getValue(SlidingHayloftDoorBlock.PART) == SlidingHayloftDoorBlock.Quarter.BL;
        if (isBarn(state)) return state.getValue(SlidingBarnDoorBlock.PART) == SlidingBarnDoorBlock.Quarter.BL;
        return false;
    }

    private static Direction getFacing(BlockState state) {
        if (isHayloft(state)) return state.getValue(SlidingHayloftDoorBlock.FACING);
        return state.getValue(SlidingBarnDoorBlock.FACING);
    }

    private static boolean isRightHinge(BlockState state) {
        if (isHayloft(state)) return state.getValue(SlidingHayloftDoorBlock.HINGE) == SlidingHayloftDoorBlock.HingeSide.RIGHT;
        return state.getValue(SlidingBarnDoorBlock.HINGE) == SlidingBarnDoorBlock.HingeSide.RIGHT;
    }

    private ResourceLocation textureFor(SlidingDoorBlockEntity be) {
        String wood = be.getWood();
        if (be.isReinforced()) {
            return HearthAndTimber.identifier("textures/entity/sliding_doors/" + wood + "_reinforced.png");
        }
        return HearthAndTimber.identifier("textures/entity/sliding_doors/" + wood + ".png");
    }

    @Override
    public void render(SlidingDoorBlockEntity be, float pt, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        BlockState state = be.getBlockState();
        if (!isHayloft(state) && !isBarn(state)) return;
        if (!isBottomLeft(state)) return;

        poseStack.pushPose();

        Direction facing = getFacing(state);
        boolean right = isRightHinge(state);

        if (isHayloft(state)) {
            switch (right ? SlidingHayloftDoorBlock.HingeSide.RIGHT : SlidingHayloftDoorBlock.HingeSide.LEFT) {
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
        } else {
            switch (right ? SlidingBarnDoorBlock.HingeSide.RIGHT : SlidingBarnDoorBlock.HingeSide.LEFT) {
                case LEFT -> {
                    switch (facing) {
                        case EAST -> poseStack.translate(1.375, 1.5, 0.5);
                        case WEST -> poseStack.translate(-0.375, 1.5, 0.5);
                        case NORTH -> poseStack.translate(0.5, 1.5, -0.375);
                        case SOUTH -> poseStack.translate(0.5, 1.5, 1.375);
                    }
                }
                case RIGHT -> {
                    switch (facing) {
                        case EAST -> poseStack.translate(1.375, 1.5, 1.5);
                        case WEST -> poseStack.translate(-0.375, 1.5, -0.5);
                        case NORTH -> poseStack.translate(1.5, 1.5, -0.375);
                        case SOUTH -> poseStack.translate(-0.5, 1.5, 1.375);
                    }
                }
            }
        }

        float distance = be.getSlide() * (26f / 16f);
        Direction lateral = right ? facing.getClockWise() : facing.getCounterClockWise();
        double offsetX = 0.0;
        double offsetZ = 0.0;
        switch (lateral) {
            case NORTH -> offsetZ -= distance;
            case SOUTH -> offsetZ += distance;
            case WEST -> offsetX -= distance;
            case EAST -> offsetX += distance;
        }
        double forwardOffset = (1.0 / 16.0) * distance;
        switch (facing) {
            case NORTH -> offsetZ -= forwardOffset;
            case SOUTH -> offsetZ += forwardOffset;
            case WEST -> offsetX -= forwardOffset;
            case EAST -> offsetX += forwardOffset;
        }
        poseStack.translate(offsetX, 0.0, offsetZ);

        poseStack.mulPose(Axis.YN.rotationDegrees(facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        if (right) {
            poseStack.translate(1.0, 0.0, 0.0);
            poseStack.scale(-1.0F, 1.0F, 1.0F);
        }

        if (isBarn(state)) {
            var vc = buffers.getBuffer(hayloftModel.renderType(textureFor(be)));
            barnModel.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, -1);
        } else {
            var vc = buffers.getBuffer(hayloftModel.renderType(textureFor(be)));
            hayloftModel.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, -1);
        }

        poseStack.popPose();
    }
}
