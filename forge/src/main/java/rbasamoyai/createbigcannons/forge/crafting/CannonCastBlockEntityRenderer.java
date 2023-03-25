package rbasamoyai.createbigcannons.forge.crafting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.fluids.FluidStack;
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
