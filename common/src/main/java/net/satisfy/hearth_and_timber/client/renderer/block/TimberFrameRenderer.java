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
    private final BlockRenderDispatcher brd;

    public TimberFrameRenderer(BlockEntityRendererProvider.Context context) {
        this.brd = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(TimberFrameBlockEntity be, float pt, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int overlay) {
        if (be.getLevel() == null) return;
        BlockState held = be.getHeldBlock();
        if (held == null || held.isAir()) return;

        Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Vec3 center = Vec3.atCenterOf(be.getBlockPos());
        float dist = (float) cam.distanceTo(center);
        float eps = Math.min(0.02f, 0.002f + Math.max(0f, dist - 8f) * 0.00025f);
        float shrink = 15.998f / 16f;

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(shrink, shrink, shrink);
        poseStack.translate(-0.5, -0.5, -0.5);

        Vec3 dir = center.subtract(cam).normalize();
        poseStack.translate(dir.x * eps, dir.y * eps, dir.z * eps);

        brd.renderBatched(held, be.getBlockPos(), be.getLevel(), poseStack, buffers.getBuffer(RenderType.cutoutMipped()), false, RandomSource.create(be.getBlockPos().asLong()));
        poseStack.popPose();
    }


    @Override
    public int getViewDistance() {
        return Minecraft.getInstance().options.renderDistance().get() * 16;
    }
}