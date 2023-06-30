package rbasamoyai.createbigcannons.forge.mixin;

import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.Shadow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.fluids.FluidAttributes;
import javax.annotation.Nonnull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;

@Mixin(CBCFlowingFluid.class)
public abstract class CBCFlowingFluidMixin extends FlowingFluid {

	@Shadow @Final protected ResourceLocation stillTex;
	@Shadow @Final protected ResourceLocation flowingTex;
	@Shadow @Final protected int color;
	@Shadow @Final protected SoundEvent fillSound;
	@Shadow @Final protected SoundEvent emptySound;

	@Nonnull
	@Override
	protected FluidAttributes createAttributes() {
		return FluidAttributes.builder(this.stillTex, this.flowingTex)
				.color(this.color)
				.sound(this.fillSound, this.emptySound)
				.build(this);
	}

}
