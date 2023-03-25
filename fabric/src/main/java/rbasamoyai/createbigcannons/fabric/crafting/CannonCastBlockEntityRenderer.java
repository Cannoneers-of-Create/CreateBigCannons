package rbasamoyai.createbigcannons.fabric.crafting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntityRenderer;

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

}
