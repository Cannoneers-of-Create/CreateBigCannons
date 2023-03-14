package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.contraptions.particle.ICustomParticleDataWithSprite;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class FluidBlobParticleData implements ParticleOptions, ICustomParticleDataWithSprite<FluidBlobParticleData> {

	public static final Codec<FluidBlobParticleData> CODEC = RecordCodecBuilder.create(i -> i
			.group(Codec.FLOAT.fieldOf("scale").forGetter(FluidBlobParticleData::scale),
					FluidStack.CODEC.fieldOf("fluid").forGetter(FluidBlobParticleData::fluid))
			.apply(i, FluidBlobParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<FluidBlobParticleData> DESERIALIZER = new Deserializer<FluidBlobParticleData>() {
		@Override
		public FluidBlobParticleData fromNetwork(ParticleType<FluidBlobParticleData> type, FriendlyByteBuf buf) {
			return new FluidBlobParticleData(buf.readFloat(), FluidStack.fromBuffer(buf));
		}

		@Override
		public FluidBlobParticleData fromCommand(ParticleType<FluidBlobParticleData> type, StringReader reader) throws CommandSyntaxException {
			// TODO: Read from command
			reader.expect(' ');
			return new FluidBlobParticleData(reader.readFloat(), new FluidStack(Fluids.WATER, 1));
		}
	}

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
		FluidStack.toBuffer(buf);
	}

	@Override
	public String writeToString() {
		return String.format("%f %s", this.scale, RegisteredObjects.getKeyOrThrow(this.fluid.getFluid()).toString());
	}

	@Override
	public Deserializer<FluidBlobParticleData> getDeserializer() {
		return null;
	}

	@Override
	public Codec<FluidBlobParticleData> getCodec(ParticleType<FluidBlobParticleData> type) {
		return null;
	}

	@Override
	public ParticleEngine.SpriteParticleRegistration<FluidBlobParticleData> getMetaFactory() {
		return FluidBlobParticle.Provider::new;
	}
}
