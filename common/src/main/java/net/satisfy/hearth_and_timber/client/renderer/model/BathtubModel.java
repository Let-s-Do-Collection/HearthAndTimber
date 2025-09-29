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
public class BathtubModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(HearthAndTimber.identifier("bathtub"), "main");
	private final ModelPart bathtub;

	public BathtubModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.bathtub = root.getChild("bathtub");
		ModelPart cubes = this.bathtub.getChild("cubes");
	}


	public static LayerDefinition getTexturedModelData() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bathtub = partdefinition.addOrReplaceChild("bathtub", CubeListBuilder.create(), PartPose.offset(7.0F, 24.0F, -19.0F));

		PartDefinition cubes = bathtub.addOrReplaceChild("cubes", CubeListBuilder.create().texOffs(32, 0).addBox(5.0F, -2.0F, 0.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(2.0F, 6.0F, -13.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(2.0F, 6.0F, -29.0F, 16.0F, 8.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(2.0F, 0.0F, -13.0F, 3.0F, 6.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(2.0F, 0.0F, -29.0F, 3.0F, 6.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(26, 12).addBox(15.0F, 0.0F, -13.0F, 3.0F, 6.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(26, 12).addBox(15.0F, 0.0F, -29.0F, 3.0F, 6.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(16, 19).addBox(5.0F, 0.0F, 0.0F, 10.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(15.0F, -2.0F, -28.0F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(2, 2).addBox(15.0F, -2.0F, -12.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(9.0F, -4.0F, -27.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(9.0F, -2.0F, -27.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(12.0F, -3.0F, -27.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(6.0F, -3.0F, -27.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-17.0F, -14.0F, 24.0F));

		PartDefinition cube_r1 = cubes.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -15.5F, 2.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(2, 2).addBox(-1.0F, -1.0F, 0.5F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -1.0F, -13.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r2 = cubes.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 19).addBox(-5.0F, -3.0F, -1.5F, 10.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, 3.0F, -27.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r3 = cubes.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 0).addBox(5.0F, -2.0F, 0.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, 0.0F, -26.0F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int j, int k) {
		bathtub.render(poseStack, vertexConsumer, light, j, k);
	}
}