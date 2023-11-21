package rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.render.CachedBufferer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class DropMortarShellRenderer extends EntityRenderer<DropMortarShellProjectile> {

	public DropMortarShellRenderer(EntityRendererProvider.Context context) { super(context); }

	@Override
	public void render(DropMortarShellProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack,
					   MultiBufferSource buffers, int packedLight) {
		poseStack.pushPose();

		Quaternion q = Vector3f.YP.rotationDegrees(entity.getViewYRot(partialTicks) + 180.0f);
		Quaternion q1 = Vector3f.XP.rotationDegrees(entity.getViewXRot(partialTicks) - 90.0f);
		q.mul(q1);

		poseStack.translate(0.0d, 0.4d, 0.0d);
		poseStack.mulPose(q);

		CachedBufferer.partial(CBCBlockPartials.DROP_MORTAR_SHELL_FLYING, CBCBlocks.DROP_MORTAR_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH))
			.light(packedLight)
			.renderInto(poseStack, buffers.getBuffer(RenderType.cutout()));

		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
	}

	@Override public ResourceLocation getTextureLocation(DropMortarShellProjectile entity) { return null; }

}
