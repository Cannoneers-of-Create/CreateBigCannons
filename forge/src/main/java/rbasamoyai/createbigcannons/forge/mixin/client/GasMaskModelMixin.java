package rbasamoyai.createbigcannons.forge.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.goggles.GogglesModel;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskModel;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

@Mixin(GasMaskModel.class)
public class GasMaskModelMixin extends GogglesModel {

	GasMaskModelMixin(BakedModel template) { super(template); }

	@Override
	public BakedModel applyTransform(ItemTransforms.TransformType cameraTransformType, PoseStack matrices, boolean lefthanded) {
		if (cameraTransformType == ItemTransforms.TransformType.HEAD)
			return CBCBlockPartials.GAS_MASK.get().applyTransform(cameraTransformType, matrices, lefthanded);
		return super.applyTransform(cameraTransformType, matrices, lefthanded);
	}

}
