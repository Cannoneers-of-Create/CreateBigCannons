package rbasamoyai.createbigcannons.crafting.casting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;

public record InvalidCastingError(BlockPos pos, EndFluidStack fluidStack, CannonCastShape shape) {

    public static final MapCodec<InvalidCastingError> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlockPos.CODEC.fieldOf("Position").forGetter(InvalidCastingError::pos),
        EndFluidStack.CODEC.fieldOf("Fluid").forGetter(InvalidCastingError::fluidStack),
        CBCRegistries.cannonCastShapes().byNameCodec().fieldOf("CastShape").forGetter(InvalidCastingError::shape)
    ).apply(instance, InvalidCastingError::new));

	public MutableComponent getMessage() {
		MutableComponent fluidText = getFluidStackText(this.fluidStack);
		MutableComponent shapeText = Component.translatable(Util.makeDescriptionId("cast_shape", CBCRegistries.cannonCastShapes().getKey(this.shape)));
		return Component.translatable("exception.createbigcannons.casting", this.pos.getX(), this.pos.getY(), this.pos.getZ(), fluidText, shapeText);
	}

    public static MutableComponent getFluidStackText(EndFluidStack efstack) {
        FluidStack stack = efstack.isEmpty() ? FluidStack.EMPTY : new FluidStack(Holder.direct(efstack.fluid()), efstack.amount(), efstack.components().asPatch());
        return stack.getHoverName().copy();
    }

}
