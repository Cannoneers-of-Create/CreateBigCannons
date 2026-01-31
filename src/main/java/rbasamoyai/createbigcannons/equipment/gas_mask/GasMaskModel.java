package rbasamoyai.createbigcannons.equipment.gas_mask;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.goggles.GogglesModel;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class GasMaskModel extends GogglesModel {

	public GasMaskModel(BakedModel template) {
		super(template);
	}

    @Override
    public BakedModel applyTransform(ItemDisplayContext cameraItemDisplayContext, PoseStack mat, boolean leftHanded) {
        if (cameraItemDisplayContext == ItemDisplayContext.HEAD)
            return CBCBlockPartials.GAS_MASK.get().applyTransform(cameraItemDisplayContext, mat, leftHanded);
        return super.applyTransform(cameraItemDisplayContext, mat, leftHanded);
    }

}
