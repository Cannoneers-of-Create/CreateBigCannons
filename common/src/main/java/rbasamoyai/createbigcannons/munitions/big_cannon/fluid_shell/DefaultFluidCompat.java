package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import com.simibubi.create.AllFluids;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.effects.particles.explosions.FluidCloudParticleData;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobEffectRegistry.OnFluidShellExplode;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobEffectRegistry.OnHitBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobEffectRegistry.OnHitEntity;

public class DefaultFluidCompat {

	public static void registerMinecraftBlobEffects() {
		FluidBlobEffectRegistry.registerHitEntity(Fluids.WATER, DefaultFluidCompat::waterHitEntity);
		FluidBlobEffectRegistry.registerHitEntity(Fluids.LAVA, DefaultFluidCompat::lavaHitEntity);

		FluidBlobEffectRegistry.registerHitBlock(Fluids.WATER, DefaultFluidCompat::waterHitBlock);
		FluidBlobEffectRegistry.registerHitBlock(Fluids.LAVA, DefaultFluidCompat::lavaHitBlock);

		FluidBlobEffectRegistry.registerFluidShellExplosionEffect(Fluids.WATER, DefaultFluidCompat::waterFluidShellExplode);
		FluidBlobEffectRegistry.registerFluidShellExplosionEffect(Fluids.LAVA, DefaultFluidCompat::lavaFluidShellExplode);
	}

	public static void registerCreateBlobEffects() {
		FluidBlobEffectRegistry.registerHitBlock(AllFluids.POTION.get(), DefaultFluidCompat::potionHitBlock);
		FluidBlobEffectRegistry.registerHitEntity(AllFluids.POTION.get(), DefaultFluidCompat::potionHitEntity);
		FluidBlobEffectRegistry.registerFluidShellExplosionEffect(AllFluids.POTION.get(), DefaultFluidCompat::potionFluidShellExplode);
	}

	public static void waterHitEntity(OnHitEntity.Context context) {
		Entity entity = context.result().getEntity();
		entity.clearFire();
	}

	public static void lavaHitEntity(OnHitEntity.Context context) {
		Entity entity = context.result().getEntity();
		if (entity.fireImmune())
			return;
		entity.setSecondsOnFire(100);
		if (entity.hurt(DamageSource.ON_FIRE, 4.0F))
			entity.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + entity.level.random.nextFloat() * 0.4F);
	}

	public static void potionHitEntity(OnHitEntity.Context context) {
		Entity entity = context.result().getEntity();
		if (!(entity instanceof LivingEntity living))
			return;
		CompoundTag tag = context.burst().getFluidStack().data();
		for (MobEffectInstance effect : PotionUtils.getAllEffects(tag))
			living.addEffect(new MobEffectInstance(effect));
	}

	public static void waterHitBlock(OnHitBlock.Context context) {
		if (!context.level().isClientSide)
			douseFire(context.result().getBlockPos().relative(context.result().getDirection()), context.burst(), context.level());
	}

	public static void lavaHitBlock(OnHitBlock.Context context) {
		if (!context.level().isClientSide)
			spawnFire(context.result().getBlockPos().relative(context.result().getDirection()), context.burst(), context.level());
	}

	public static void potionHitBlock(OnHitBlock.Context context) {
		if (!context.level().isClientSide)
			spawnAreaEffectCloud(context.result().getBlockPos().relative(context.result().getDirection()), context.burst(), context.level());
	}

	public static void waterFluidShellExplode(OnFluidShellExplode.Context context) {
		Level level = context.level();
		double x = context.x();
		double y = context.y();
		double z = context.z();
		CBCSoundEvents.WATER_FLUID_RELEASE.playAt(level, x, y, z, 2.5f, 0.15f + level.random.nextFloat(0.3f), false);
		CBCSoundEvents.FLUID_SHELL_EXPLOSION.playAt(level, x, y, z, 3, 0.9f + level.random.nextFloat(0.1f), false);
		level.addParticle(new FluidCloudParticleData(), x, y, z, 0, 0, 0);
	}

	public static void lavaFluidShellExplode(OnFluidShellExplode.Context context) {
		Level level = context.level();
		double x = context.x();
		double y = context.y();
		double z = context.z();
		CBCSoundEvents.LAVA_FLUID_RELEASE.playAt(level, x, y, z, 2.25f, 1.0f + level.random.nextFloat(0.1f), false);
		CBCSoundEvents.FLUID_SHELL_EXPLOSION.playAt(level, x, y, z, 3, 0.9f + level.random.nextFloat(0.1f), false);
		level.addParticle(new FluidCloudParticleData(), x, y, z, 0, 0, 0);
	}

	public static void potionFluidShellExplode(OnFluidShellExplode.Context context) {
		Level level = context.level();
		double x = context.x();
		double y = context.y();
		double z = context.z();
		CBCSoundEvents.POTION_FLUID_RELEASE.playAt(level, x, y, z, 3, 0.9f + level.random.nextFloat(0.1f), false);
		CBCSoundEvents.FLUID_SHELL_EXPLOSION.playAt(level, x, y, z, 3, 0.9f + level.random.nextFloat(0.1f), false);
		level.addParticle(new FluidCloudParticleData(), x, y, z, 0, 0, 0);
	}

	public static void douseFire(BlockPos root, FluidBlobBurst blob, Level level) {
		float chance = FluidBlobBurst.getBlockAffectChance();
		if (chance == 0)
			return;
		AABB bounds = blob.getAreaOfEffect(root);
		BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
		BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
		for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
			if (level.getRandom().nextFloat() > chance) continue;
			BlockState state = level.getBlockState(pos);
			if (state.is(BlockTags.FIRE)) {
				level.removeBlock(pos, false);
			} else if (AbstractCandleBlock.isLit(state)) {
				AbstractCandleBlock.extinguish(null, state, level, pos);
			} else if (CampfireBlock.isLitCampfire(state)) {
				level.levelEvent(null, 1009, pos, 0);
				CampfireBlock.dowse(blob.getOwner(), level, pos, state);
				level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
			} else if (CandleCakeBlock.isLit(state)) {
				AbstractCandleBlock.extinguish(null, state, level, pos);
			}
		}
	}

	public static void spawnFire(BlockPos root, FluidBlobBurst blob, Level level) {
		float chance = FluidBlobBurst.getBlockAffectChance();
		if (chance == 0)
			return;
		AABB bounds = blob.getAreaOfEffect(root);
		BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
		BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
		for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
			if (level.getRandom().nextFloat() > chance)
				continue;
			BlockState state = level.getBlockState(pos);
			if (level.isEmptyBlock(pos)) {
				level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
			} else if (CandleBlock.canLight(state) || CampfireBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
				level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
				level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
				level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
			}
		}
	}

	public static void spawnAreaEffectCloud(BlockPos pos, FluidBlobBurst blob, Level level) {
		CompoundTag tag = blob.getFluidStack().data();

		AreaEffectCloud aec = EntityType.AREA_EFFECT_CLOUD.create(level);
		aec.setPos(Vec3.atCenterOf(pos));
		aec.setRadius(blob.getBlobSize() * 2);
		aec.setRadiusOnUse(-0.5f);
		aec.setWaitTime(10);
		aec.setRadiusPerTick(-aec.getRadius() / (float) aec.getDuration());
		aec.setPotion(PotionUtils.getPotion(tag));

		for (MobEffectInstance effect : PotionUtils.getAllEffects(tag)) {
			aec.addEffect(new MobEffectInstance(effect));
		}

		aec.setFixedColor(PotionUtils.getColor(PotionUtils.getAllEffects(tag)) | 0xff000000);
		level.addFreshEntity(aec);
	}

}
