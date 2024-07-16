package rbasamoyai.createbigcannons.block_hit_effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import rbasamoyai.createbigcannons.block_hit_effects.ProjectileParticleEffectModifiers.Modifier.Context;
import rbasamoyai.createbigcannons.effects.particles.impacts.GlassBurstParticleData;
import rbasamoyai.createbigcannons.effects.particles.impacts.LeafBurstParticleData;
import rbasamoyai.createbigcannons.effects.particles.impacts.SparkBurstParticleData;
import rbasamoyai.createbigcannons.effects.particles.impacts.SplinterBurstParticleData;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;

public class DefaultParticleModifiers {

	private static ParticleOptions modifySparkBurst(Context context) {
		ParticleOptions options = context.particle();
		if (!(options instanceof SparkBurstParticleData data))
			return options;
		return new SparkBurstParticleData(data.color(), data.deflect(), getCount(data.count(), context));
	}

	private static ParticleOptions modifySplinterBurst(Context context) {
		ParticleOptions options = context.particle();
		if (!(options instanceof SplinterBurstParticleData data) || !data.blockState().isAir())
			return options;
		return new SplinterBurstParticleData(context.blockState(), getCount(data.count(), context));
	}

	private static ParticleOptions modifyGlassBurst(Context context) {
		ParticleOptions options = context.particle();
		if (!(options instanceof GlassBurstParticleData data) || !data.blockState().isAir())
			return options;
		return new GlassBurstParticleData(context.blockState(), getCount(data.count(), context));
	}

	private static ParticleOptions modifyLeafBurst(Context context) {
		ParticleOptions options = context.particle();
		if (!(options instanceof LeafBurstParticleData data) || !data.blockState().isAir())
			return options;
		return new LeafBurstParticleData(context.blockState(), getCount(data.count(), context));
	}

	private static int getCount(int baseCount, Context context) {
		return Mth.ceil(baseCount * context.effect().effectMultiplier());
	}

	public static void register() {
		ProjectileParticleEffectModifiers.register(CBCParticleTypes.SPARK_BURST.get(), DefaultParticleModifiers::modifySparkBurst);
		ProjectileParticleEffectModifiers.register(CBCParticleTypes.SPLINTER_BURST.get(), DefaultParticleModifiers::modifySplinterBurst);
		ProjectileParticleEffectModifiers.register(CBCParticleTypes.GLASS_BURST.get(), DefaultParticleModifiers::modifyGlassBurst);
		ProjectileParticleEffectModifiers.register(CBCParticleTypes.LEAF_BURST.get(), DefaultParticleModifiers::modifyLeafBurst);
	}

}
