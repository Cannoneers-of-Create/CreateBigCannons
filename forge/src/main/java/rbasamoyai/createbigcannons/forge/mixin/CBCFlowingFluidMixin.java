package rbasamoyai.createbigcannons.forge.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.foundation.data.CreateRegistrate;

import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.Shadow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;

@Mixin(CBCFlowingFluid.class)
public abstract class CBCFlowingFluidMixin extends FlowingFluid {
	@Shadow @Final protected ResourceLocation stillTex;
	@Shadow @Final protected ResourceLocation flowingTex;
	@Shadow @Final protected int color;
	@Shadow
	@Final
	protected SoundEvent fillSound;
	@Shadow
	@Final
	protected SoundEvent emptySound;

	@Override
	public FluidType getFluidType() {
		return CreateRegistrate.defaultFluidType(FluidType.Properties.create().sound(SoundActions.BUCKET_FILL, this.fillSound).sound(SoundActions.BUCKET_EMPTY, this.emptySound), stillTex, flowingTex);
	}
}
