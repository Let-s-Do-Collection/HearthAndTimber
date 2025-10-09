package net.satisfy.hearth_and_timber.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.satisfy.hearth_and_timber.core.block.WindowCasingBlock;
import net.satisfy.hearth_and_timber.core.block.entity.WindowCasingBlockEntity;

public class WindowCasingRenderer implements BlockEntityRenderer<WindowCasingBlockEntity> {
    public WindowCasingRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(WindowCasingBlockEntity be, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        ItemStack stack = be.getFlower();
        if (stack.isEmpty() || be.getLevel() == null) return;
        Block block = Block.byItem(stack.getItem());
        BlockState flowerState = block.defaultBlockState();
        if (flowerState.isAir()) return;

        pose.pushPose();
        Direction facing = be.getBlockState().getValue(WindowCasingBlock.FACING);
        switch (facing) {
            case NORTH -> pose.translate(0.1, 0.2, 0.3);
            case SOUTH -> pose.translate(0.1, 0.2, -0.1);
            case EAST  -> pose.translate(-0.1, 0.2, 0.1);
            case WEST  -> pose.translate(0.35, 0.2, 0.1);
            default    -> pose.translate(0.5, 0.2, 0.5);
        }
        pose.scale(0.75f, 0.75f, 0.75f);

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        dispatcher.renderSingleBlock(flowerState, pose, buffers, light, overlay);
        pose.popPose();
    }
}