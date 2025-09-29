package net.satisfy.hearth_and_timber.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.satisfy.hearth_and_timber.core.block.WardrobeBlock;
import net.satisfy.hearth_and_timber.core.block.entity.WardrobeBlockEntity;
import org.jetbrains.annotations.NotNull;

public class WardrobeRenderer implements BlockEntityRenderer<WardrobeBlockEntity>, RenderLayerParent<ArmorStand, HumanoidModel<ArmorStand>> {
    private final HumanoidModel<ArmorStand> baseModel;
    private final HumanoidArmorLayer<ArmorStand, HumanoidModel<ArmorStand>, HumanoidModel<ArmorStand>> armorLayer;
    private ArmorStand dummy;

    public WardrobeRenderer(BlockEntityRendererProvider.Context ctx) {
        this.baseModel = new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER));
        this.armorLayer = new HumanoidArmorLayer<>(
                this,
                new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(ctx.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                Minecraft.getInstance().getModelManager()
        );
    }

    @Override
    public void render(WardrobeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (be.getLevel() == null) return;
        if (dummy == null || dummy.level() != be.getLevel()) {
            dummy = new ArmorStand(EntityType.ARMOR_STAND, be.getLevel());
            dummy.setNoBasePlate(true);
            dummy.setInvisible(true);
        }

        dummy.setItemSlot(EquipmentSlot.HEAD, be.getItem(WardrobeBlockEntity.SLOT_HEAD));
        dummy.setItemSlot(EquipmentSlot.CHEST, be.getItem(WardrobeBlockEntity.SLOT_CHEST));
        dummy.setItemSlot(EquipmentSlot.LEGS, be.getItem(WardrobeBlockEntity.SLOT_LEGS));
        dummy.setItemSlot(EquipmentSlot.FEET, be.getItem(WardrobeBlockEntity.SLOT_FEET));

        poseStack.pushPose();
        poseStack.translate(0.5, 1.6, 0.5);
        switch (be.getBlockState().getValue(WardrobeBlock.FACING)) {
            case SOUTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(270));
        }
        poseStack.scale(0.9f, -0.9f, -0.9f);
        armorLayer.render(poseStack, buffer, packedLight, dummy, 0, 0, partialTicks, 0, 0, 0);
        poseStack.popPose();
    }

    @Override
    public @NotNull HumanoidModel<ArmorStand> getModel() {
        return baseModel;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(ArmorStand entity) {
        return ResourceLocation.withDefaultNamespace("textures/entity/armorstand/wood.png");
    }
}
