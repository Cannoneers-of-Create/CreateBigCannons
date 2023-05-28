package rbasamoyai.createbigcannons.forge.mixin;

import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.Shadow;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;

@Mixin(CBCFlowingFluid.class)
public abstract class CBCFlowingFluidMixin extends FlowingFluid {
	@Shadow
	@Final
	protected SoundEvent fillSound;
	@Shadow
	@Final
	protected SoundEvent emptySound;

	@NotNull
	protected FluidType createAttributes() {
		return new FluidType(
			FluidType.Properties.create().sound(SoundActions.BUCKET_FILL, this.fillSound).sound(SoundActions.BUCKET_EMPTY, this.emptySound)
		);
	}

}
