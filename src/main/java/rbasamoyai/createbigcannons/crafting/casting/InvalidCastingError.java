package rbasamoyai.createbigcannons.crafting.casting;

import javax.annotation.Nullable;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record InvalidCastingError(BlockPos pos, EndFluidStack fluid, CannonCastShape shape) {

	public static void write(CompoundTag tag, @Nullable InvalidCastingError error) {
		if (error == null) return;
		CompoundTag errorTag = new CompoundTag();
		errorTag.put("Position", NbtUtils.writeBlockPos(error.pos));
		errorTag.put("Fluid", error.fluid.writeTag(new CompoundTag()));
		errorTag.putString("CastShape", CBCRegistries.cannonCastShapes().getKey(error.shape).toString());
		tag.put("CastingError", errorTag);
	}

	@Nullable
	public static InvalidCastingError read(CompoundTag tag) {
		if (!tag.contains("CastingError", Tag.TAG_COMPOUND)) return null;
		CompoundTag errorTag = tag.getCompound("CastingError");
		if (!errorTag.contains("Position", Tag.TAG_COMPOUND)) return null;
		BlockPos pos = NbtUtils.readBlockPos(errorTag.getCompound("Position"));
		if (!errorTag.contains("Fluid", Tag.TAG_COMPOUND)) return null;
		EndFluidStack fluid = EndFluidStack.readTag(errorTag.getCompound("Fluid"));
		if (!errorTag.contains("CastShape", Tag.TAG_STRING)) return null;
		CannonCastShape shape = CBCRegistries.cannonCastShapes().get(CBCUtils.location(errorTag.getString("CastShape")));
		return new InvalidCastingError(pos, fluid, shape);
	}

	public MutableComponent getMessage() {
		MutableComponent fluidText = IndexPlatform.getFluidStackText(this.fluid);
		MutableComponent shapeText = Component.translatable(Util.makeDescriptionId("cast_shape", CBCRegistries.cannonCastShapes().getKey(this.shape)));
		return Component.translatable("exception.createbigcannons.casting", this.pos.getX(), this.pos.getY(), this.pos.getZ(), fluidText, shapeText);
	}

}
