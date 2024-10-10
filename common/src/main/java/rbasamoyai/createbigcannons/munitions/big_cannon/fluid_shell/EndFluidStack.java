package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record EndFluidStack(Fluid fluid, int amount, CompoundTag data) {

	public static EndFluidStack EMPTY = new EndFluidStack(Fluids.EMPTY, 0, new CompoundTag());

	private static final Codec<Fluid> FLUID_CODEC =
			ResourceLocation.CODEC.comapFlatMap(EndFluidStack::read, CBCRegistryUtils::getFluidLocation).stable();
	public static final Codec<EndFluidStack> CODEC = RecordCodecBuilder.create(i -> i
			.group(FLUID_CODEC.fieldOf("fluid").forGetter(EndFluidStack::fluid),
					Codec.INT.fieldOf("amount").forGetter(EndFluidStack::amount),
					CompoundTag.CODEC.fieldOf("data").forGetter(EndFluidStack::data))
			.apply(i, EndFluidStack::new));

	public CompoundTag writeTag(CompoundTag tag) {
		tag.putString("Fluid", CBCRegistryUtils.getFluidLocation(this.fluid).toString());
		tag.putInt("FluidAmount", this.amount);
		tag.put("FluidTag", this.data);
		return tag;
	}

	public static EndFluidStack readTag(CompoundTag tag) {
		Fluid fluid = CBCRegistryUtils.getFluid(CBCUtils.location(tag.getString("Fluid")));
		int amount = tag.getInt("FluidAmount");
		CompoundTag data = tag.getCompound("FluidTag");
		return new EndFluidStack(fluid, amount, data);
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeResourceLocation(CBCRegistryUtils.getFluidLocation(this.fluid))
		.writeVarInt(this.amount)
		.writeNbt(this.data);
	}

	public static EndFluidStack readBuf(FriendlyByteBuf buf) {
		return new EndFluidStack(CBCRegistryUtils.getFluid(buf.readResourceLocation()), buf.readVarInt(), buf.readNbt());
	}

	public boolean isEmpty() {
		return this == EMPTY || this.fluid == Fluids.EMPTY || this.fluid == null || this.amount < 1;
	}

	public EndFluidStack copy(int newAmount) {
		return new EndFluidStack(this.fluid, newAmount, this.data.copy());
	}

	public EndFluidStack copy() { return this.copy(this.amount); }

	public static DataResult<Fluid> read(ResourceLocation location) {
		try {
			return DataResult.success(CBCRegistryUtils.getFluid(location));
		} catch (Exception var2) {
			return DataResult.error(() -> "Not a valid fluid id: " + location + " " + var2.getMessage());
		}
	}

}
