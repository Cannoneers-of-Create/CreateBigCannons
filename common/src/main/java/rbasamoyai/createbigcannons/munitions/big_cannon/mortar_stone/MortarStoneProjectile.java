package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

import javax.annotation.Nonnull;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class MortarStoneProjectile extends AbstractBigCannonProjectile {

    private boolean tooManyCharges = false;

    public MortarStoneProjectile(EntityType<? extends MortarStoneProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        if (this.tooManyCharges) {
            if (this.level() instanceof ServerLevel slevel) {
                slevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getRenderedBlockState()), this.getX(), this.getY(), this.getZ(), 40, 0.1f, 0.1f, 0.1f, 0.01d);
            }
            SoundType soundType = this.getRenderedBlockState().getSoundType();
            this.playSound(soundType.getBreakSound(), soundType.getVolume() * 0.5f, soundType.getPitch() * 0.75f);
            this.discard();
            return;
        }
        super.tick();
    }

    @Override
    protected boolean onImpact(HitResult hitResult, ImpactResult impactResult, ProjectileContext projectileContext) {
        super.onImpact(hitResult, impactResult, projectileContext);
        if (!this.level().isClientSide) {
            Vec3 hitLoc = hitResult.getLocation();
			MortarStoneExplosion explosion = new MortarStoneExplosion(this.level(), null, this.indirectArtilleryFire(false),
				hitLoc.x, hitLoc.y, hitLoc.z, this.getAllProperties().explosion().explosivePower(),
				CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
			CreateBigCannons.handleCustomExplosion(this.level(), explosion);
        }
		return true;
    }

	@Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.tooManyCharges = tag.getBoolean("TooManyCharges");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("TooManyCharges", this.tooManyCharges);
    }

    @Override
    public BlockState getRenderedBlockState() {
        return CBCBlocks.MORTAR_STONE_PROJECTILE.getDefaultState();
    }

    @Override
    public void setChargePower(float power) {
        float maxCharges = this.getAllProperties().maxCharges();
        this.tooManyCharges = maxCharges >= 0 && power > maxCharges;
    }

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
		return this.getAllProperties().bigCannonProperties();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	protected MortarStoneProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.MORTAR_STONE.getPropertiesOf(this);
	}

}
