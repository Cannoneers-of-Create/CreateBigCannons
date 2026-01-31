package rbasamoyai.createbigcannons.effects.particles.impacts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.foundation.particle.ICustomParticleData;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public record DebrisMatterParticleData(boolean deflect, boolean forceDisplay, BlockState blockState) implements ParticleOptions,
	ICustomParticleData<DebrisMatterParticleData> {

	private static final MapCodec<DebrisMatterParticleData> CODEC = RecordCodecBuilder.mapCodec(i -> i
		.group(Codec.BOOL.fieldOf("deflect")
			.forGetter(data -> data.deflect),
		Codec.BOOL.fieldOf("force_display")
			.forGetter(data -> data.forceDisplay),
		BlockState.CODEC.fieldOf("block_state")
			.forGetter(data -> data.blockState))
		.apply(i, DebrisMatterParticleData::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, DebrisMatterParticleData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL, DebrisMatterParticleData::deflect,
        ByteBufCodecs.BOOL, DebrisMatterParticleData::forceDisplay,
        CatnipStreamCodecs.BLOCK_STATE, DebrisMatterParticleData::blockState,
        DebrisMatterParticleData::new);

	public DebrisMatterParticleData() { this(false, false, Blocks.AIR.defaultBlockState()); }

	@Override public MapCodec<DebrisMatterParticleData> getCodec(ParticleType<DebrisMatterParticleData> type) { return CODEC; }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, DebrisMatterParticleData> getStreamCodec() {
        return STREAM_CODEC;
    }

    @OnlyIn(Dist.CLIENT)
	@Override
	public ParticleProvider<DebrisMatterParticleData> getFactory() {
		return new DebrisMatterParticle.Provider();
	}

	@Override public ParticleType<?> getType() { return CBCParticleTypes.DEBRIS_MATTER.get(); }

}
