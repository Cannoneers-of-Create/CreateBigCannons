package rbasamoyai.createbigcannons.compat.curios;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.CreateBigCannons;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

// Adapted from GogglesCurioRenderer
public class GasMaskCurioRenderer implements ICurioRenderer {

	public static final ModelLayerLocation LAYER = new ModelLayerLocation(CreateBigCannons.resource("gas_mask"), "gas_mask");

	private final HumanoidModel<LivingEntity> model;

	public GasMaskCurioRenderer(ModelPart part) {
		this.model = new HumanoidModel<>(part);
	}

	@Override
	public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext,
			PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light,
			float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		// Prepare values for transformation
		this.model.setupAnim(slotContext.entity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.model.prepareMobModel(slotContext.entity(), limbSwing, limbSwingAmount, partialTicks);
		ICurioRenderer.followHeadRotations(slotContext.entity(), this.model.head);

		// Translate and rotate with our head
		matrixStack.pushPose();
		matrixStack.translate(this.model.head.x / 16.0, this.model.head.y / 16.0, this.model.head.z / 16.0);
		matrixStack.mulPose(Axis.YP.rotation(this.model.head.yRot));
		matrixStack.mulPose(Axis.XP.rotation(this.model.head.xRot));

		// Translate and scale to our head
		matrixStack.translate(0, -0.25, 0);
		matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
		matrixStack.scale(0.625f, 0.625f, 0.625f);

		// Render
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getItemRenderer().renderStatic(stack, ItemDisplayContext.HEAD, light, OverlayTexture.NO_OVERLAY,
			matrixStack, renderTypeBuffer, minecraft.level, 0);
		matrixStack.popPose();
	}

	public static MeshDefinition mesh() {
		CubeListBuilder builder = new CubeListBuilder();
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		mesh.getRoot().addOrReplaceChild("head", builder, PartPose.ZERO);
		return mesh;
	}

}
