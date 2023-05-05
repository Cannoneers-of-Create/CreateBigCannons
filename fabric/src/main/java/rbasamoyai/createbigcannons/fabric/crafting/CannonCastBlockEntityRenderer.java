package rbasamoyai.createbigcannons.fabric.crafting;

import com.jozufozu.flywheel.core.virtual.VirtualEmptyBlockGetter;
import com.jozufozu.flywheel.fabric.model.DefaultLayerFilteringBakedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import io.github.fabricators_of_create.porting_lib.render.virtual.FixedLightBakedModel;
import io.github.fabricators_of_create.porting_lib.render.virtual.TranslucentBakedModel;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntityRenderer;

import java.util.Random;

public class CannonCastBlockEntityRenderer extends AbstractCannonCastBlockEntityRenderer {

	public CannonCastBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderFluidBox(AbstractCannonCastBlockEntity cast, float width, float height, MultiBufferSource buffers, PoseStack stack, int light) {
		if (!(cast instanceof CannonCastBlockEntity castc)) return;
		FluidStack fstack = castc.fluid.getFluid();
		if (!fstack.isEmpty()) FluidRenderer.renderFluidBox(fstack, 0, 0, 0, width, height, width, buffers, stack, light, false);
	}

	@Override
	protected void renderPreview(PoseStack ms, float alpha, VertexConsumer vCons, BlockState state, int light, BlockPos pos) {
		BakedModel model = this.dispatcher.getBlockModel(state);
		model = DefaultLayerFilteringBakedModel.wrap(model);
		model = FixedLightBakedModel.wrap(model, light);
		model = TranslucentBakedModel.wrap(model, () -> alpha);
		this.dispatcher.getModelRenderer()
				.tesselateBlock(VirtualEmptyBlockGetter.INSTANCE, model, state, pos, ms, vCons, false, RandomSource.create(), 42L, OverlayTexture.NO_OVERLAY);
	}


}
