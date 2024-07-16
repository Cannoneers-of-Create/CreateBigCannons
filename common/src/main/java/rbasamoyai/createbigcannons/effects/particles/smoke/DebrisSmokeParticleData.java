package rbasamoyai.createbigcannons.effects.particles.smoke;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleDataWithSprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record DebrisSmokeParticleData(float scale) implements ParticleOptions, ICustomParticleDataWithSprite<DebrisSmokeParticleData> {

	private static final Deserializer<DebrisSmokeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public DebrisSmokeParticleData fromCommand(ParticleType<DebrisSmokeParticleData> particleType, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
            return new DebrisSmokeParticleData(reader.readFloat());
        }

        @Override
        public DebrisSmokeParticleData fromNetwork(ParticleType<DebrisSmokeParticleData> particleType, FriendlyByteBuf buffer) {
            return new DebrisSmokeParticleData(buffer.readFloat());
        }
    };

	private static final Codec<DebrisSmokeParticleData> CODEC = RecordCodecBuilder.create(i -> i
		.group(Codec.FLOAT.fieldOf("scale")
			.forGetter(data -> data.scale))
		.apply(i, DebrisSmokeParticleData::new));

	public DebrisSmokeParticleData() { this(0); }

	@Override public Deserializer<DebrisSmokeParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<DebrisSmokeParticleData> getCodec(ParticleType<DebrisSmokeParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleEngine.SpriteParticleRegistration<DebrisSmokeParticleData> getMetaFactory() {
		return DebrisSmokeParticle.Provider::new;
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.DEBRIS_SMOKE.get(); }
	@Override public void writeToNetwork(FriendlyByteBuf buffer) { buffer.writeFloat(this.scale); }
	@Override public String writeToString() { return String.format("%f", this.scale); }

}
