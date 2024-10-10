package rbasamoyai.createbigcannons.effects.particles.smoke;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class SmokeShellSmokeParticleData implements ParticleOptions, ICustomParticleDataWithSprite<SmokeShellSmokeParticleData> {

	public static final Codec<SmokeShellSmokeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, SmokeShellSmokeParticleData::new));

	@SuppressWarnings("deprecation")
	public static final Deserializer<SmokeShellSmokeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public SmokeShellSmokeParticleData fromNetwork(ParticleType<SmokeShellSmokeParticleData> type, FriendlyByteBuf buf) {
            float scale = buf.readFloat();
            return new SmokeShellSmokeParticleData(scale);
        }

        @Override
        public SmokeShellSmokeParticleData fromCommand(ParticleType<SmokeShellSmokeParticleData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float scale = reader.readFloat();
            return new SmokeShellSmokeParticleData(scale);
        }
    };

	private final float scale;

	public SmokeShellSmokeParticleData(float scale) {
		this.scale = scale;
	}

	public SmokeShellSmokeParticleData() {
		this(0);
	}

	public float scale() {
		return this.scale;
	}

	@Override
	public ParticleType<?> getType() {
		return CBCParticleTypes.SMOKE_SHELL_SMOKE.get();
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.scale);
	}

	@Override
	public String writeToString() {
		return String.format("%f", this.scale);
	}

	@Override
	public Deserializer<SmokeShellSmokeParticleData> getDeserializer() {
		return DESERIALIZER;
	}

	@Override
	public Codec<SmokeShellSmokeParticleData> getCodec(ParticleType<SmokeShellSmokeParticleData> type) {
		return CODEC;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public SpriteParticleRegistration<SmokeShellSmokeParticleData> getMetaFactory() {
		return SmokeShellSmokeParticle.Provider::new;
	}

}

