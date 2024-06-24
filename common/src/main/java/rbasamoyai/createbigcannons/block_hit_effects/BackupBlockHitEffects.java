package rbasamoyai.createbigcannons.block_hit_effects;

import com.simibubi.create.AllFluids;

import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.effects.particles.impacts.DebrisMatterParticleData;
import rbasamoyai.createbigcannons.effects.particles.impacts.DebrisSmokeBurstParticleData;
import rbasamoyai.createbigcannons.effects.particles.splashes.ProjectileSplashParticleData;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;

public class BackupBlockHitEffects {

	public static void backupSolidEffect(Level level, BlockState blockState, boolean deflect, boolean forceDisplay, double x, double y, double z,
										 double dx, double dy, double dz, ProjectileHitEffect projectileEffect) {
		SoundType type = blockState.getSoundType();
		level.addParticle(new DebrisMatterParticleData(deflect, forceDisplay, blockState), forceDisplay, x, y, z, dx, dy, dz);
		if (deflect) {
			level.playLocalSound(x, y, z, type.getBreakSound(), SoundSource.BLOCKS, 2, type.getPitch(), false);
		} else if (projectileEffect.effectMultiplier() >= 1) {
			level.addParticle(new DebrisSmokeBurstParticleData(), forceDisplay, x, y, z, dx, dy, dz);
			CBCSoundEvents.PROJECTILE_IMPACT.playAt(level, x, y, z, 3f, 1.0f + level.random.nextFloat() * 0.4f, false);
		}
	}

	public static void backupFluidEffect(Level level, BlockState blockState, boolean deflect, boolean forceDisplay, double x,
										 double y, double z, double dx, double dy, double dz, ProjectileHitEffect projectileEffect) {
		if (dy < Math.sqrt(dx * dx + dy * dy + dz * dz) * 0.15)
			return;
		float r;
		float g;
		float b;
		float light;
		double velScale;
		Vec3 vel = new Vec3(dx, dy, dz);
		float volume = projectileEffect.volume();
		if (blockState.is(Blocks.WATER)) {
			r = 0.92f;
			g = 0.97f;
			b = 1f;
			light = 0;
			velScale = 0.5;
			float pitch = modifyPitch(0.31f, projectileEffect, level.random.nextFloat() * 0.3f);
			CBCSoundEvents.PROJECTILE_SPLASH.playAt(level, x, y, z, volume, pitch, false);
		} else if (blockState.is(Blocks.LAVA)) {
			r = 1f;
			g = 0.35f;
			b = 0.05f;
			light = 1;
			velScale = 0.35;
			float pitch = modifyPitch(0.5f, projectileEffect, level.random.nextFloat() * 0.3f);
			CBCSoundEvents.HOT_PROJECTILE_SPLASH.playAt(level, x, y, z, volume, pitch, false);
		} else if (isFluid(blockState, AllFluids.HONEY.get())) {
			r = 0.95f;
			g = 0.67f;
			b = 0.07f;
			light = 0;
			velScale = 0.2;
			float pitch = modifyPitch(0, projectileEffect, level.random.nextFloat() * 0.3f);
			CBCSoundEvents.PROJECTILE_SPLASH.playAt(level, x, y, z, volume, pitch, false);
		} else if (isFluid(blockState, AllFluids.CHOCOLATE.get())) {
			r = 0.56f;
			g = 0.25f;
			b = 0.21f;
			light = 0;
			velScale = 0.4;
			float pitch = modifyPitch(0.31f, projectileEffect, level.random.nextFloat() * 0.3f);
			CBCSoundEvents.PROJECTILE_SPLASH.playAt(level, x, y, z, volume, pitch, false);
		} else if (isFluid(blockState, CBCFluids.MOLTEN_CAST_IRON.get())) {
			r = 0.84f;
			g = 0.43f;
			b = 0.24f;
			light = 0.25f;
			velScale = 0.2;
			float pitch = modifyPitch(0.5f, projectileEffect, level.random.nextFloat() * 0.3f);
			CBCSoundEvents.HOT_PROJECTILE_SPLASH.playAt(level, x, y, z, volume, pitch, false);
		} else if (isFluid(blockState, CBCFluids.MOLTEN_BRONZE.get())) {
			r = 0.78f;
			g = 0.51f;
			b = 0.31f;
			light = 0.25f;
			velScale = 0.2;
			float pitch = modifyPitch(0.5f, projectileEffect, level.random.nextFloat() * 0.3f);
			CBCSoundEvents.HOT_PROJECTILE_SPLASH.playAt(level, x, y, z, volume, pitch, false);
		} else if (isFluid(blockState, CBCFluids.MOLTEN_STEEL.get())) {
			r = 0.42f;
			g = 0.43f;
			b = 0.40f;
			light = 0.25f;
			velScale = 0.2;
			float pitch = modifyPitch(0.5f, projectileEffect, level.random.nextFloat() * 0.3f);
			CBCSoundEvents.HOT_PROJECTILE_SPLASH.playAt(level, x, y, z, volume, pitch, false);
		} else if (isFluid(blockState, CBCFluids.MOLTEN_NETHERSTEEL.get())) {
			r = 0.51f;
			g = 0.37f;
			b = 0.36f;
			light = 0.25f;
			velScale = 0.2;
			float pitch = modifyPitch(0.5f, projectileEffect, level.random.nextFloat() * 0.3f);
			CBCSoundEvents.HOT_PROJECTILE_SPLASH.playAt(level, x, y, z, volume, pitch, false);
		}
		else {
			return;
		}
		level.addParticle(new ProjectileSplashParticleData(r, g, b, light), true, x, y, z, dx * velScale * 0.5f, dy * velScale, dz * velScale * 0.5f);
	}

	private static float modifyPitch(float base, ProjectileHitEffect effect, float modifier) {
		float f = Mth.clamp(effect.getPitch(base), 0, 2);
		return Mth.clamp(f + modifier, 0, 2);
	}

	private static boolean isFluid(BlockState state, Fluid fluid) {
		return state.getFluidState().getType().isSame(fluid);
	}

	private BackupBlockHitEffects() {}

}
