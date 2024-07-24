package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.jozufozu.flywheel.light.LightVolume;
import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.jozufozu.flywheel.util.box.ImmutableBox;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.remix.HasCannonLightingVolume;

@Mixin(LightVolume.class)
public class LightVolumeAccessor implements HasCannonLightingVolume {

	@Unique
	private AbstractMountedCannonContraption createbigcannons$cannonContraption = null;

	@Override
	public void createbigcannons$setCannonContraption(AbstractMountedCannonContraption contraption) {
		this.createbigcannons$cannonContraption = contraption;
	}

	@ModifyExpressionValue(method = "initialize",
		at = @At(value = "INVOKE", target = "Lcom/jozufozu/flywheel/light/LightVolume;getVolume()Lcom/jozufozu/flywheel/util/box/ImmutableBox;"),
		remap = false)
	private ImmutableBox createbigcannons$getVolume(ImmutableBox original) {
		if (this.createbigcannons$cannonContraption != null && this.createbigcannons$cannonContraption.entity != null) {
			GridAlignedBB bb = GridAlignedBB.from(this.createbigcannons$cannonContraption.createBoundsFromExtensionLengths());
			bb.translate(this.createbigcannons$cannonContraption.entity.blockPosition());
			return bb;
		}
		return original;
	}

}
