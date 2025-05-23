package rbasamoyai.createbigcannons.effects.particles.smoke;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class QuickFiringBreechSmokeParticleData implements ParticleOptions, ICustomParticleData<QuickFiringBreechSmokeParticleData> {

	private static final MapCodec<QuickFiringBreechSmokeParticleData> CODEC = MapCodec.unit(QuickFiringBreechSmokeParticleData::new);

    private static final StreamCodec<RegistryFriendlyByteBuf, QuickFiringBreechSmokeParticleData> STREAM_CODEC = StreamCodec.unit(new QuickFiringBreechSmokeParticleData());

	@Override public MapCodec<QuickFiringBreechSmokeParticleData> getCodec(ParticleType<QuickFiringBreechSmokeParticleData> type) { return CODEC; }

    @Override public StreamCodec<? super RegistryFriendlyByteBuf, QuickFiringBreechSmokeParticleData> getStreamCodec() { return STREAM_CODEC; }

    @Environment(EnvType.CLIENT)
	@Override
	public ParticleProvider<QuickFiringBreechSmokeParticleData> getFactory() {
		return new QuickFiringBreechSmokeParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.QUICK_FIRING_BREECH_SMOKE.get(); }

}
