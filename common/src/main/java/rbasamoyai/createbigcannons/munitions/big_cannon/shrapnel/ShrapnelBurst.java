package rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.PartialBlockDamageManager;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;

public class ShrapnelBurst extends CBCProjectileBurst {

	public ShrapnelBurst(EntityType<? extends ShrapnelBurst> entityType, Level level) { super(entityType, level); }

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide) {
			ParticleOptions trailParticle = this.getTrailParticle();
			if (trailParticle != null) {
				for (SubProjectile subProjectile : this.subProjectiles) {
					this.level().addParticle(trailParticle, this.getX() + subProjectile.displacement()[0],
						this.getY() + subProjectile.displacement()[1], this.getZ() + subProjectile.displacement()[2], 0, 0, 0);
				}
			}
		}
	}

	@Override
	protected void onSubProjectileHitEntity(EntityHitResult result, SubProjectile subProjectile) {
		EntityDamagePropertiesComponent properties = this.getProperties().damage();
		Entity entity = result.getEntity();
		if (properties == null || properties.ignoresInvulnerability())
			entity.invulnerableTime = 0;
		float damage = properties == null ? 0 : properties.entityDamage();
		entity.hurt(this.getDamageSource(), damage);
		if (properties == null || !properties.rendersInvulnerable())
			entity.invulnerableTime = 0;
	}

	@Override
	protected void onSubProjectileHitBlock(BlockHitResult result, SubProjectile subProjectile) {
		super.onSubProjectileHitBlock(result, subProjectile);

		BlockPos pos = result.getBlockPos();
		BlockState state = this.level().getChunk(pos).getBlockState(pos);
		if (!this.level().isClientSide && state.getDestroySpeed(this.level(), pos) != -1 && this.canDestroyBlock(state)) {
			Vec3 curVel = new Vec3(subProjectile.velocity()[0], subProjectile.velocity()[1], subProjectile.velocity()[2]);
			double curPom = this.getProperties().ballistics().durabilityMass() * curVel.length();
			double toughness = BlockArmorPropertiesHandler.getProperties(state).toughness(this.level(), state, pos, true);
			BlockPos pos1 = pos.immutable();
			CreateBigCannons.BLOCK_DAMAGE.damageBlock(pos1, (int) Math.min(curPom, toughness), state, this.level(), PartialBlockDamageManager::voidBlock);
		}
		if (this.level() instanceof ServerLevel slevel) {
			ParticleOptions options = new BlockParticleOption(ParticleTypes.BLOCK, state);
			for (ServerPlayer player : slevel.players()) {
				if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 1024d)
					slevel.sendParticles(player, options, true, pos.getX(), pos.getY(), pos.getZ(), 20, 0.4, 2, 0.4, 1);
			}
		}
		SoundType type = state.getSoundType();
		this.level().playLocalSound(pos.getX(), pos.getY(), pos.getZ(), type.getBreakSound(), SoundSource.NEUTRAL,
			type.getVolume() * 2, type.getPitch(), false);
	}

	protected boolean canDestroyBlock(BlockState state) { return true; }

	@Nullable public ParticleOptions getTrailParticle() { return ParticleTypes.SMOKE; }

	@Override public double getSubProjectileWidth() { return 0.8; }
	@Override public double getSubProjectileHeight() { return 0.8; }

	protected DamageSource getDamageSource() {
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(CBCDamageTypes.SHRAPNEL),
			this.getProperties().damage().ignoresEntityArmor());
	}

}
