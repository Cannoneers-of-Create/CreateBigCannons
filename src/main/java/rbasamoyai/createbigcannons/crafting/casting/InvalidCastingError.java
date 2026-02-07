package rbasamoyai.createbigcannons.crafting.casting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public record InvalidCastingError(BlockPos pos, Fluid fluid, CannonCastShape shape) {

    public static final MapCodec<InvalidCastingError> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlockPos.CODEC.fieldOf("Position").forGetter(InvalidCastingError::pos),
        CBCRegistryUtils.getFluidRegistry().byNameCodec().fieldOf("Fluid").forGetter(InvalidCastingError::fluid),
        CBCRegistries.cannonCastShapes().byNameCodec().fieldOf("CastShape").forGetter(InvalidCastingError::shape)
    ).apply(instance, InvalidCastingError::new));

	public MutableComponent getMessage() {
		MutableComponent fluidText = Component.translatable(Util.makeDescriptionId("fluid", CBCRegistryUtils.getFluidLocation(this.fluid)));
		MutableComponent shapeText = Component.translatable(Util.makeDescriptionId("cast_shape", CBCRegistries.cannonCastShapes().getKey(this.shape)));
		return Component.translatable("exception.createbigcannons.casting", this.pos.getX(), this.pos.getY(), this.pos.getZ(), fluidText, shapeText);
	}

}
