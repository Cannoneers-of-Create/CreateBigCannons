package rbasamoyai.createbigcannons.cannons.autocannon.breech;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class AutocannonBreechRenderer extends SmartBlockEntityRenderer<AbstractAutocannonBreechBlockEntity> {

	public AutocannonBreechRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(AbstractAutocannonBreechBlockEntity breech, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.renderSafe(breech, partialTicks, ms, buffer, light, overlay);
		if (Backend.canUseInstancing(breech.getLevel())) return;

		BlockState state = breech.getBlockState();
		Direction facing = state.getValue(AutocannonBreechBlock.FACING);

		ms.pushPose();

		if (state.getValue(AutocannonBreechBlock.HANDLE)) {
			if (breech.getSeatColor() != null) {
				CachedBufferer.partialFacing(CBCBlockPartials.autocannonSeatFor(breech.getSeatColor()), state, facing)
					.rotateCentered(Vector3f.YP.rotationDegrees(facing.getAxis().isVertical() ? 180 : 0))
					.light(light)
					.renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
			}
		} else {
			Vector3f normal = facing.step();
			normal.mul(breech.getAnimateOffset(partialTicks) * -0.5f);
			CachedBufferer.partialFacing(getPartialModelForState(breech), state, facing)
				.translate(normal)
				.rotateCentered(Vector3f.YP.rotationDegrees(facing.getAxis().isVertical() ? 180 : 0))
				.light(light)
				.renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
		}

		ms.popPose();
	}

	private static PartialModel getPartialModelForState(AbstractAutocannonBreechBlockEntity breech) {
		return breech.getBlockState().getBlock() instanceof AutocannonBlock cBlock
			? CBCBlockPartials.autocannonEjectorFor(cBlock.getAutocannonMaterial())
			: CBCBlockPartials.CAST_IRON_AUTOCANNON_EJECTOR;
	}

}
