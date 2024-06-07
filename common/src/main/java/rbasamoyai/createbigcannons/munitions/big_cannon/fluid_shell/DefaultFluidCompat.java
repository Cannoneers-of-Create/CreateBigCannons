package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import com.simibubi.create.AllFluids;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobEffectRegistry.OnHitBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobEffectRegistry.OnHitEntity;

public class DefaultFluidCompat {

	public static void registerMinecraftBlobEffects() {
		FluidBlobEffectRegistry.registerHitEntity(Fluids.WATER, DefaultFluidCompat::waterHitEntity);
		FluidBlobEffectRegistry.registerHitEntity(Fluids.LAVA, DefaultFluidCompat::lavaHitEntity);

		FluidBlobEffectRegistry.registerHitBlock(Fluids.WATER, DefaultFluidCompat::waterHitBlock);
		FluidBlobEffectRegistry.registerHitBlock(Fluids.LAVA, DefaultFluidCompat::lavaHitBlock);
	}

	public static void registerCreateBlobEffects() {
		FluidBlobEffectRegistry.registerHitBlock(AllFluids.POTION.get(), DefaultFluidCompat::potionHitBlock);
		FluidBlobEffectRegistry.registerHitEntity(AllFluids.POTION.get(), DefaultFluidCompat::potionHitEntity);
	}

	public static void waterHitEntity(OnHitEntity.Context context) {
		Entity entity = context.result().getEntity();
		entity.clearFire();
		if (!context.level().isClientSide)
			douseFire(entity.blockPosition(), context.burst(), context.level());
	}

	public static void lavaHitEntity(OnHitEntity.Context context) {
		Entity entity = context.result().getEntity();
		entity.setSecondsOnFire(100);
		if (!context.level().isClientSide)
			spawnFire(entity.blockPosition(), context.burst(), context.level());
	}

	public static void potionHitEntity(OnHitEntity.Context context) {
		if (!context.level().isClientSide)
			spawnAreaEffectCloud(context.result().getEntity().blockPosition(), context.burst(), context.level());
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

	public static void douseFire(BlockPos root, FluidBlobBurst blob, Level level) {
		float chance = FluidBlobBurst.getBlockAffectChance();
		AABB bounds = blob.getAreaOfEffect(root);
		BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
		BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
		for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
			if (chance == 0 || level.getRandom().nextFloat() > chance) continue;
			BlockState state = level.getBlockState(pos);
			if (state.is(BlockTags.FIRE)) {
				level.removeBlock(pos, false);
			} else if (AbstractCandleBlock.isLit(state)) {
				AbstractCandleBlock.extinguish(null, state, level, pos);
			} else if (CampfireBlock.isLitCampfire(state)) {
				level.levelEvent(null, 1009, pos, 0);
				CampfireBlock.dowse(blob.getOwner(), level, pos, state);
				level.setBlockAndUpdate(pos, state.setValue(CampfireBlock.LIT, false));
			}
		}
	}

	public static void spawnFire(BlockPos root, FluidBlobBurst blob, Level level) {
		float chance = FluidBlobBurst.getBlockAffectChance();
		AABB bounds = blob.getAreaOfEffect(root);
		BlockPos pos1 = new BlockPos(Math.floor(bounds.minX), Math.floor(bounds.minY), Math.floor(bounds.minZ));
		BlockPos pos2 = new BlockPos(Math.floor(bounds.maxX), Math.floor(bounds.maxY), Math.floor(bounds.maxZ));
		for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
			if (chance > 0 && level.getRandom().nextFloat() <= chance && level.isEmptyBlock(pos)) {
				level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
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
