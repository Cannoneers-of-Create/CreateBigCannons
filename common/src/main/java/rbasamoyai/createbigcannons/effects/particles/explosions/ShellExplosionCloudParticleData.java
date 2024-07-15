package rbasamoyai.createbigcannons.effects.particles.explosions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record ShellExplosionCloudParticleData(float scale, boolean isPlume) implements ParticleOptions,
	ICustomParticleData<ShellExplosionCloudParticleData> {

	private static final Deserializer<ShellExplosionCloudParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public ShellExplosionCloudParticleData fromCommand(ParticleType<ShellExplosionCloudParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			float scale = reader.readFloat();
			reader.expect(' ');
			boolean isPlume = reader.readBoolean();
            return new ShellExplosionCloudParticleData(scale, isPlume);
        }

        @Override
        public ShellExplosionCloudParticleData fromNetwork(ParticleType<ShellExplosionCloudParticleData> particleType, FriendlyByteBuf buffer) {
            return new ShellExplosionCloudParticleData(buffer.readFloat(), buffer.readBoolean());
        }
    };

	private static final Codec<ShellExplosionCloudParticleData> CODEC = RecordCodecBuilder.create(i -> i.group(
		Codec.FLOAT.fieldOf("scale").forGetter(ShellExplosionCloudParticleData::scale),
		Codec.BOOL.fieldOf("isPlume").forGetter(ShellExplosionCloudParticleData::isPlume)
	).apply(i, ShellExplosionCloudParticleData::new));

	public ShellExplosionCloudParticleData() { this(0, false); }

	@Override public Deserializer<ShellExplosionCloudParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<ShellExplosionCloudParticleData> getCodec(ParticleType<ShellExplosionCloudParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<ShellExplosionCloudParticleData> getFactory() {
		return new ShellExplosionCloudParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.SHELL_EXPLOSION_CLOUD.get(); }

	@Override
	public void writeToNetwork(FriendlyByteBuf buffer) {
		buffer.writeFloat(this.scale)
			.writeBoolean(this.isPlume);
	}

	@Override
	public String writeToString() {
		return String.format("%f %b", this.scale, this.isPlume);
	}

}
