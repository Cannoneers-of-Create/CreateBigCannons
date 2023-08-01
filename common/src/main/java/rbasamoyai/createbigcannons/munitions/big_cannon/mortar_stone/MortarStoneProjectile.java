package rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone;

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
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;

public class MortarStoneProjectile extends AbstractBigCannonProjectile {

    private boolean tooManyCharges = false;

    public MortarStoneProjectile(EntityType<? extends MortarStoneProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        if (this.tooManyCharges) {
            if (this.level instanceof ServerLevel slevel) {
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
    protected void onImpact(HitResult result, boolean stopped) {
        super.onImpact(result, stopped);
        if (!this.level.isClientSide) {
            Vec3 hitLoc = result.getLocation();
            this.level.explode(null, this.indirectArtilleryFire(), null, hitLoc.x, hitLoc.y, hitLoc.z,
                    (float) this.getProperties().explosivePower(), false,
                    CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
            this.tooManyCharges = true;
        }
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
        float maxCharges = CBCConfigs.SERVER.munitions.maxMortarStoneCharges.getF();
        this.tooManyCharges = maxCharges >= 0 && power > maxCharges;
    }

}
