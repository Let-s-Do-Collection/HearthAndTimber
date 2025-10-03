package net.satisfy.hearth_and_timber.client.renderer.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.hearth_and_timber.HearthAndTimber;

@SuppressWarnings("unused")
public class SlidingBarnDoorModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(HearthAndTimber.identifier("sliding_barn_door"), "main");
	private final ModelPart SlidingBarnDoor;
	private float slide;
	private int sign;

	public SlidingBarnDoorModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.SlidingBarnDoor = root.getChild("SlidingBarnDoor");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition SlidingBarnDoor = partdefinition.addOrReplaceChild("SlidingBarnDoor", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -48.0F, 6.0F, 32.0F, 48.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	public void configure(float progress, boolean hingeLeft) {
		this.slide = progress;
		this.sign = hingeLeft ? -1 : 1;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int j, int k) {
		poseStack.pushPose();
		poseStack.translate(sign * slide, 0.0F, 0.0F);
		SlidingBarnDoor.render(poseStack, vertexConsumer, light, j, k);
		poseStack.popPose();
	}
}
