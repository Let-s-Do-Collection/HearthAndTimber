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
import org.joml.Matrix4f;

public class SlidingHayloftDoorRenderer implements BlockEntityRenderer<SlidingHayloftDoorBlockEntity> {
    private static final ResourceLocation TEXTURE = HearthAndTimber.identifier("textures/entity/sliding_doors/spruce.png");
    private final SlidingHayloftDoorModel model;

    public SlidingHayloftDoorRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SlidingHayloftDoorModel(context.bakeLayer(SlidingHayloftDoorModel.LAYER_LOCATION));
    }

    @Override
    public void render(SlidingHayloftDoorBlockEntity be, float partialTick, PoseStack ps, MultiBufferSource buffers, int light, int overlay) {
        if (!(be.getBlockState().getBlock() instanceof SlidingHayloftDoorBlock)) return;
        if (be.getBlockState().getValue(SlidingHayloftDoorBlock.PART) != SlidingHayloftDoorBlock.Quarter.BL) return;

        ps.pushPose();
        Direction dir = be.getBlockState().getValue(SlidingHayloftDoorBlock.FACING);
        SlidingHayloftDoorBlock.HingeSide hinge = be.getBlockState().getValue(SlidingHayloftDoorBlock.HINGE);

        switch (hinge) {
            case LEFT -> {
                switch (dir) {
                    case EAST -> ps.translate(1.3125, 1.5, 0.5);
                    case WEST -> ps.translate(-0.3125, 1.5, 0.5);
                    case NORTH -> ps.translate(0.5, 1.5, -0.3125);
                    case SOUTH -> ps.translate(0.5, 1.5, 1.3125);
                }
            }
            case RIGHT -> {
                switch (dir) {
                    case EAST -> ps.translate(1.3125, 1.5, 1.5);
                    case WEST -> ps.translate(-0.3125, 1.5, -0.5);
                    case NORTH -> ps.translate(1.5, 1.5, -0.3125);
                    case SOUTH -> ps.translate(-0.5, 1.5, 1.3125);
                }
            }
        }

        ps.mulPose(Axis.YN.rotationDegrees(dir.toYRot()));
        ps.mulPose(Axis.XP.rotationDegrees(180));

        float p = be.getSlide();
        Direction lateral = hinge == SlidingHayloftDoorBlock.HingeSide.RIGHT ? dir.getClockWise() : dir.getCounterClockWise();
        float dx = 0f;
        float dz = 0f;
        switch (lateral) {
            case NORTH -> dz = -p;
            case SOUTH -> dz = p;
            case WEST -> dx = -p;
            case EAST -> dx = p;
        }
        Matrix4f m = new Matrix4f().translate(dx, 0f, dz);
        ps.last().pose().mul(m);

        var vc = buffers.getBuffer(model.renderType(TEXTURE));
        model.renderToBuffer(ps, vc, light, OverlayTexture.NO_OVERLAY, -1);
        ps.popPose();
    }
}
