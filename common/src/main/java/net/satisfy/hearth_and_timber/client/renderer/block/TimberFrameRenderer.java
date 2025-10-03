package net.satisfy.hearth_and_timber.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;

public class TimberFrameRenderer implements BlockEntityRenderer<TimberFrameBlockEntity> {

    public TimberFrameRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TimberFrameBlockEntity be, float pt, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int overlay) {
        if (be.getLevel() == null) return;
        BlockState held = be.getHeldBlock();
        if (held == null || held.isAir()) return;

        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Vec3 center = Vec3.atCenterOf(be.getBlockPos());
        Vec3 dir = center.subtract(cam);

        float eps = -0.00125F;
        double ax = Math.abs(dir.x);
        double ay = Math.abs(dir.y);
        double az = Math.abs(dir.z);

        poseStack.pushPose();
        if (ax >= ay && ax >= az) {
            poseStack.translate(Math.signum(dir.x) * eps, 0F, 0F);
        } else if (ay >= ax && ay >= az) {
            poseStack.translate(0F, Math.signum(dir.y) * eps, 0F);
        } else {
            poseStack.translate(0F, 0F, Math.signum(dir.z) * eps);
        }

        brd.renderBatched(held, be.getBlockPos(), be.getLevel(), poseStack, buffers.getBuffer(RenderType.cutoutMipped()), false, RandomSource.create(be.getBlockPos().asLong())
        );
        poseStack.popPose();
    }
}
