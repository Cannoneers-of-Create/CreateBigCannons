package rbasamoyai.createbigcannons.forge.mixin;

import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.Shadow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.fluids.FluidAttributes;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCFlowingFluid;

@Mixin(CBCFlowingFluid.class)
public abstract class CBCFlowingFluidMixin extends FlowingFluid {

	@Shadow @Final protected ResourceLocation stillTex;
	@Shadow @Final protected ResourceLocation flowingTex;
	@Shadow @Final protected int color = 0x00ffffff;
	@Shadow @Final protected SoundEvent fillSound;
	@Shadow @Final protected SoundEvent emptySound;
	@Shadow @Final protected String translationKey;

	@NotNull
	@Override
	protected FluidAttributes createAttributes() {
		return FluidAttributes.builder(this.stillTex, this.flowingTex)
				.color(this.color)
				.sound(this.fillSound, this.emptySound)
				.translationKey(this.translationKey)
				.build(this);
	}

}
