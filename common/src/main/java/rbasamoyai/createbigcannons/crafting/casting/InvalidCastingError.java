package rbasamoyai.createbigcannons.crafting.casting;

import javax.annotation.Nullable;

import com.simibubi.create.foundation.utility.Components;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record InvalidCastingError(BlockPos pos, Fluid fluid, CannonCastShape shape) {

	public static void write(CompoundTag tag, @Nullable InvalidCastingError error) {
		if (error == null) return;
		CompoundTag errorTag = new CompoundTag();
		errorTag.put("Position", NbtUtils.writeBlockPos(error.pos));
		errorTag.putString("Fluid", CBCRegistryUtils.getFluidLocation(error.fluid).toString());
		errorTag.putString("CastShape", CBCRegistries.CANNON_CAST_SHAPES.getKey(error.shape).toString());
		tag.put("CastingError", errorTag);
	}

	@Nullable
	public static InvalidCastingError read(CompoundTag tag) {
		if (!tag.contains("CastingError", Tag.TAG_COMPOUND)) return null;
		CompoundTag errorTag = tag.getCompound("CastingError");
		if (!errorTag.contains("Position", Tag.TAG_COMPOUND)) return null;
		BlockPos pos = NbtUtils.readBlockPos(errorTag.getCompound("Position"));
		if (!errorTag.contains("Fluid", Tag.TAG_STRING)) return null;
		Fluid fluid = CBCRegistryUtils.getFluid(CBCUtils.location(errorTag.getString("Fluid")));
		if (!errorTag.contains("CastShape", Tag.TAG_STRING)) return null;
		CannonCastShape shape = CBCRegistries.CANNON_CAST_SHAPES.get(CBCUtils.location(errorTag.getString("CastShape")));
		return new InvalidCastingError(pos, fluid, shape);
	}

	public MutableComponent getMessage() {
		MutableComponent fluidText = Components.translatable(Util.makeDescriptionId("fluid",  CBCRegistryUtils.getFluidLocation(this.fluid)));
		MutableComponent shapeText = Components.translatable(Util.makeDescriptionId("cast_shape", CBCRegistries.CANNON_CAST_SHAPES.getKey(this.shape)));
		return Components.translatable("exception.createbigcannons.casting", this.pos.getX(), this.pos.getY(), this.pos.getZ(), fluidText, shapeText);
	}

}
