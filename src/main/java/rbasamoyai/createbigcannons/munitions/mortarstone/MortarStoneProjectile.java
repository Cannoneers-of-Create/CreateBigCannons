package rbasamoyai.createbigcannons.munitions.mortarstone;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class MortarStoneProjectile extends AbstractCannonProjectile {

    private boolean tooManyCharges = false;

    public MortarStoneProjectile(EntityType<? extends MortarStoneProjectile> type, Level level) {
        super(type, level);
        this.setBreakthroughPower((byte) 1);
    }

    @Override
    public void tick() {
        if (this.tooManyCharges) {
            if (this.level instanceof ServerLevel slevel) {
                slevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getRenderedBlockState()), this.getX(), this.getY(), this.getZ(), 40, 0.1f, 0.1f, 0.1f, 0.01d);
            }
            SoundType soundType = this.getRenderedBlockState().getSoundType();
            this.playSound(soundType.getBreakSound(), soundType.getPitch() * 0.5f, soundType.getVolume() * 0.75f);
            this.discard();
            return;
        }
        super.tick();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level.isClientSide) {
            Vec3 hitLoc = result.getLocation();

            if (this.getBreakthroughPower() <= 0) {
                this.level.explode(null, hitLoc.x, hitLoc.y, hitLoc.z,
                        CBCConfigs.SERVER.munitions.mortarStonePower.getF(),
                        CBCConfigs.SERVER.munitions.damageRestriction.get() == CBCCfgMunitions.GriefState.NO_DAMAGE ? Explosion.BlockInteraction.NONE : Explosion.BlockInteraction.DESTROY);
                this.tooManyCharges = true;
            }
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
        return CBCBlocks.MORTAR_STONE.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
    }

    @Override
    public void setChargePower(float power) {
        float maxCharges = CBCConfigs.SERVER.munitions.maxMortarStoneCharges.getF();
        this.tooManyCharges = maxCharges >= 0 && power > maxCharges;
    }

    @Override protected float getGravity() { return -0.025f; }

}
