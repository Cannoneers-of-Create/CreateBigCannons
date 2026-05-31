package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.createmod.catnip.codecs.CatnipCodecUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public record EndFluidStack(Fluid fluid, int amount, PatchedDataComponentMap components) {

	public static EndFluidStack EMPTY = new EndFluidStack(Fluids.EMPTY, 0, new PatchedDataComponentMap(DataComponentMap.EMPTY));

	public static final Codec<EndFluidStack> CODEC = RecordCodecBuilder.create(i -> i
			.group(CBCRegistryUtils.getFluidRegistry().byNameCodec().fieldOf("fluid").forGetter(EndFluidStack::fluid),
					Codec.INT.fieldOf("amount").forGetter(EndFluidStack::amount),
                    DataComponentPatch.CODEC.optionalFieldOf("data", DataComponentPatch.EMPTY).forGetter(stack -> stack.components.asPatch()))
			.apply(i, EndFluidStack::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, EndFluidStack> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.registry(CBCRegistryUtils.getFluidRegistryKey()), EndFluidStack::fluid,
        ByteBufCodecs.VAR_INT, EndFluidStack::amount,
        DataComponentPatch.STREAM_CODEC, fs -> fs.components.asPatch(),
        EndFluidStack::new);

    public EndFluidStack(Fluid fluid, int amount, DataComponentPatch patch) {
        this(fluid, amount, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
    }

	public CompoundTag writeTag(CompoundTag tag, HolderLookup.Provider registries) {
		tag.putString("Fluid", CBCRegistryUtils.getFluidLocation(this.fluid).toString());
		tag.putInt("FluidAmount", this.amount);
        tag.put("FluidData", CatnipCodecUtils.encode(DataComponentPatch.CODEC, registries, this.components.asPatch()).orElse(new CompoundTag()));
		return tag;
	}

	public static EndFluidStack readTag(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.isEmpty())
            return EMPTY;
		Fluid fluid = CBCRegistryUtils.getFluid(CBCUtils.location(tag.getString("Fluid")));
		int amount = tag.getInt("FluidAmount");
        DataComponentPatch dataPatch = CatnipCodecUtils.decode(DataComponentPatch.CODEC, registries, tag.get("FluidData")).orElse(DataComponentPatch.EMPTY);
		return new EndFluidStack(fluid, amount, dataPatch);
	}

	public boolean isEmpty() {
		return this == EMPTY || this.fluid == Fluids.EMPTY || this.fluid == null || this.amount < 1;
	}

	public EndFluidStack copy(int newAmount) {
		return new EndFluidStack(this.fluid, newAmount, this.components.copy());
	}

	public EndFluidStack copy() { return this.copy(this.amount); }

}
