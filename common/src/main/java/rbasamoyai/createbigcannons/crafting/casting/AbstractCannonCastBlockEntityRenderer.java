package rbasamoyai.createbigcannons.crafting.casting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public abstract class AbstractCannonCastBlockEntityRenderer extends SafeBlockEntityRenderer<AbstractCannonCastBlockEntity> {

	protected final BlockRenderDispatcher dispatcher;

	protected AbstractCannonCastBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.dispatcher = context.getBlockRenderDispatcher();
	}

	@Override
	protected void renderSafe(AbstractCannonCastBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		if (!te.canRenderCastModel()) return;
		BlockState state = te.getBlockState();

		ms.pushPose();

		CannonCastShape shape = te.getRenderedSize();
		if (shape != null) {
			CachedBufferer.partial(CBCBlockPartials.cannonCastFor(shape), state)
				.light(light)
				.renderInto(ms, buffer.getBuffer(RenderType.solid()));
		}

		if (te.isController()) {
			LerpedFloat levelLerped = te.getFluidLevel();
			if (levelLerped != null && shape != null) {
				float level = levelLerped.getValue(partialTicks);

				boolean flag = shape.isLarge();
				float width = flag ? 2.875f : 0.875f;
				float height = ((float) te.height - 0.125f) * level;

				if (height > 0.0625f) {
					ms.pushPose();
					float f = flag ? -15 / 16f : 1 / 16f;
					ms.translate(f, 0.0625f, f);
					this.renderFluidBox(te, width, height, buffer, ms, light);
					ms.popPose();
				}
			}

			BlockPos base = te.getBlockPos();
			LerpedFloat castProgressLerped = te.getCastingLevel();
			if (castProgressLerped != null) {
				float alpha = castProgressLerped.getValue(partialTicks);
				for (int l = 0; l < te.resultPreview.size(); ++l) {
					int light1 = te.hasLevel() ? LevelRenderer.getLightColor(te.getLevel(), base.above(l)) : light;
					ms.pushPose();
					ms.translate(0, l, 0);
					this.renderPreview(ms, alpha, buffer.getBuffer(Sheets.translucentItemSheet()), te.resultPreview.get(l), light1, base.above(l));
					ms.popPose();
				}
			}
		}

		ms.popPose();
	}

	protected abstract void renderFluidBox(AbstractCannonCastBlockEntity cast, float width, float height, MultiBufferSource buffers, PoseStack stack, int light);

	protected abstract void renderPreview(PoseStack ms, float alpha, VertexConsumer vCons, BlockState state, int light, BlockPos pos);

}
