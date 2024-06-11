package rbasamoyai.createbigcannons.effects.particles.smoke;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class QuickFiringBreechSmokeParticleData implements ParticleOptions, ICustomParticleData<QuickFiringBreechSmokeParticleData> {

	private static final Deserializer<QuickFiringBreechSmokeParticleData> DESERIALIZER = new Deserializer<>() {
        @Override
        public QuickFiringBreechSmokeParticleData fromCommand(ParticleType<QuickFiringBreechSmokeParticleData> particleType, StringReader reader) {
            return new QuickFiringBreechSmokeParticleData();
        }

        @Override
        public QuickFiringBreechSmokeParticleData fromNetwork(ParticleType<QuickFiringBreechSmokeParticleData> particleType, FriendlyByteBuf buffer) {
            return new QuickFiringBreechSmokeParticleData();
        }
    };

	private static final Codec<QuickFiringBreechSmokeParticleData> CODEC = Codec.unit(QuickFiringBreechSmokeParticleData::new);

	@Override public Deserializer<QuickFiringBreechSmokeParticleData> getDeserializer() { return DESERIALIZER; }
	@Override public Codec<QuickFiringBreechSmokeParticleData> getCodec(ParticleType<QuickFiringBreechSmokeParticleData> type) { return CODEC; }

	@Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<QuickFiringBreechSmokeParticleData> getFactory() {
		return new QuickFiringBreechSmokeParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.QUICK_FIRING_BREECH_SMOKE.get(); }
	@Override public void writeToNetwork(FriendlyByteBuf buffer) {}
	@Override public String writeToString() { return ""; }

}
