package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.contraptions.particle.ICustomParticleDataWithSprite;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class FluidBlobParticleData implements ParticleOptions, ICustomParticleDataWithSprite<FluidBlobParticleData> {

	public static final Codec<FluidBlobParticleData> CODEC = RecordCodecBuilder.create(i -> i
			.group(Codec.FLOAT.fieldOf("scale").forGetter(FluidBlobParticleData::scale),
					EndFluidStack.CODEC.fieldOf("fluid").forGetter(FluidBlobParticleData::fluid))
			.apply(i, FluidBlobParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<FluidBlobParticleData> DESERIALIZER = new Deserializer<>() {
		@Override
		public FluidBlobParticleData fromNetwork(ParticleType<FluidBlobParticleData> type, FriendlyByteBuf buf) {
			return new FluidBlobParticleData(buf.readFloat(), EndFluidStack.readBuf(buf));
		}

		@Override
		public FluidBlobParticleData fromCommand(ParticleType<FluidBlobParticleData> type, StringReader reader) throws CommandSyntaxException {
			// TODO: Read from command
			reader.expect(' ');
			return new FluidBlobParticleData(reader.readFloat(), new EndFluidStack(Fluids.WATER, 1, new CompoundTag()));
		}
	};

	private final float scale;
	private final EndFluidStack fluid;

	public FluidBlobParticleData(float scale, EndFluidStack fluid) {
		this.scale = scale;
		this.fluid = fluid;
	}

	public float scale() { return this.scale; }
	public EndFluidStack fluid() { return this.fluid; }

	@Override public ParticleType<?> getType() { return CBCParticleTypes.FLUID_BLOB.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale);
		this.fluid.writeBuf(buf);
	}

	@Override
	public String writeToString() {
		return String.format("%f %s", this.scale, RegisteredObjects.getKeyOrThrow(this.fluid.fluid()));
	}

	@Override public Deserializer<FluidBlobParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<FluidBlobParticleData> getCodec(ParticleType<FluidBlobParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<FluidBlobParticleData> getMetaFactory() {
		return FluidBlobParticle.Provider::new;
	}

}
