package rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.CachedBufferer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class DropMortarShellRenderer extends EntityRenderer<DropMortarShellProjectile> {

	public DropMortarShellRenderer(EntityRendererProvider.Context context) { super(context); }

	@Override
	public void render(DropMortarShellProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack,
					   MultiBufferSource buffers, int packedLight) {
		Vec3 vel = entity.getOrientation();
		if (vel.lengthSqr() < 1e-4d)
			vel = new Vec3(0, -1, 0);

		poseStack.pushPose();
		if (vel.horizontalDistanceSqr() > 1e-4d) {
			Vec3 horizontal = new Vec3(vel.x, 0, vel.z).normalize();
			poseStack.mulPoseMatrix(CBCUtils.mat4x4fFacing(vel.normalize(), horizontal));
			poseStack.mulPoseMatrix(CBCUtils.mat4x4fFacing(horizontal, new Vec3(0, 0, -1)));
		} else {
			poseStack.mulPoseMatrix(CBCUtils.mat4x4fFacing(vel.normalize(), new Vec3(0, 0, -1)));
		}

		CachedBufferer.partial(CBCBlockPartials.DROP_MORTAR_SHELL_FLYING, CBCBlocks.DROP_MORTAR_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH))
			.light(packedLight)
			.renderInto(poseStack, buffers.getBuffer(RenderType.cutout()));

		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
	}

	@Override public ResourceLocation getTextureLocation(DropMortarShellProjectile entity) { return null; }

}
