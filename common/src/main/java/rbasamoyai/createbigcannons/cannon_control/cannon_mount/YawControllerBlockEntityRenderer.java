package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class YawControllerBlockEntityRenderer extends KineticBlockEntityRenderer {

	public YawControllerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(KineticBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (Backend.canUseInstancing(te.getLevel())) return;

		BlockState state = te.getBlockState();
		ms.pushPose();

		VertexConsumer solidBuf = buffer.getBuffer(RenderType.solid());
		SuperByteBuffer yawShaftRender = CachedBufferer.partialFacing(CBCBlockPartials.YAW_SHAFT, state, Direction.UP);
		renderRotatingBuffer(te, yawShaftRender, ms, solidBuf, light);

		SuperByteBuffer inputShaftRender = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, Direction.DOWN);
		renderRotatingBuffer(te, inputShaftRender, ms, solidBuf, light);

		ms.popPose();
	}

}
