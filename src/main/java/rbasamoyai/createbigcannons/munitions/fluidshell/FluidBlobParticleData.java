package rbasamoyai.createbigcannons.munitions.fluidshell;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.utility.RegisteredObjects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.CBCParticleTypes;

public class FluidBlobParticleData implements ParticleOptions {

	public static final Codec<FluidBlobParticleData> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.FLOAT.fieldOf("scale").forGetter(FluidBlobParticleData::scale),
				FluidStack.CODEC.fieldOf("fluid").forGetter(FluidBlobParticleData::fluid))
				.apply(instance, FluidBlobParticleData::new);
	});
	
	@SuppressWarnings("deprecation")
	public static final ParticleOptions.Deserializer<FluidBlobParticleData> DESERIALIZER = new Deserializer<FluidBlobParticleData>() {
		@Override
		public FluidBlobParticleData fromNetwork(ParticleType<FluidBlobParticleData> type, FriendlyByteBuf buf) {
			return new FluidBlobParticleData(buf.readFloat(), buf.readFluidStack());
		}
		
		@Override
		public FluidBlobParticleData fromCommand(ParticleType<FluidBlobParticleData> type, StringReader reader) throws CommandSyntaxException {
			// TODO: Read from command
			reader.expect(' ');
			return new FluidBlobParticleData(reader.readFloat(), new FluidStack(Fluids.WATER, 1));
		}
	};
	
	public static final float MAX_SCALE = 6.0f;
	
	private final float scale;
	private final FluidStack fluid;
	
	public FluidBlobParticleData(float scale, FluidStack fluid) {
		this.scale = scale;
		this.fluid = fluid;
	}
	
	public float scale() { return this.scale; }
	public FluidStack fluid() { return this.fluid; }
	
	@Override public ParticleType<?> getType() { return CBCParticleTypes.FLUID_BLOB.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale);
		buf.writeFluidStack(this.fluid);
	}

	@Override
	public String writeToString() {
		return String.format("%f %s", this.scale, RegisteredObjects.getKeyOrThrow(this.fluid.getFluid()).toString());
	}

}
