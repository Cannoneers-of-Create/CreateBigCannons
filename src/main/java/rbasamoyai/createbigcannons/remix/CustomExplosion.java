package rbasamoyai.createbigcannons.remix;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;

public interface CustomExplosion {

	default void playLocalSound(Level level, double x, double y, double z) {
	}

	default void editBlock(Level level, BlockPos pos, BlockState blockState, FluidState fluidState, float power) {
	}

    void setCanDamageTerrain(boolean canDamageTerrain);

    boolean canDamageTerrain();

    float getEntityRadius();

	void sendExplosionToClient(ServerPlayer player);
	Explosion.BlockInteraction getBlockInteraction();

	abstract class Impl extends Explosion implements CustomExplosion {
		protected final Level level;
		protected final double x;
		protected final double y;
		protected final double z;
		protected final float blockSize;
        protected final float entitySize;
		protected final BlockInteraction interaction;
        protected boolean canDamageTerrain = true;

		public Impl(Level level, @Nullable Entity source, @Nullable DamageSource damageSource,
					@Nullable ExplosionDamageCalculator calculator, double toBlowX, double toBlowY, double toBlowZ, float blockRadius,
					float entityRadius, boolean fire, BlockInteraction interaction) {
			super(level, source, damageSource, calculator, toBlowX, toBlowY, toBlowZ, blockRadius, fire, interaction,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE); // These last three arguments are unused.
			this.level = level;
			this.x = toBlowX;
			this.y = toBlowY;
			this.z = toBlowZ;
			this.blockSize = blockRadius;
            this.entitySize = entityRadius;
			this.interaction = interaction;
		}

		public Impl(Level level, ClientboundCBCExplodePacket packet) {
			super(level, null, packet.x(), packet.y(), packet.z(), packet.blockPower(), false, BlockInteraction.DESTROY, packet.toBlow());
			this.level = level;
			this.x = packet.x();
			this.y = packet.y();
			this.z = packet.z();
			this.blockSize = packet.blockPower();
            this.entitySize = packet.entityPower();
			this.interaction = BlockInteraction.DESTROY;
		}

        @Override
        public void setCanDamageTerrain(boolean canDamageTerrain) {
            this.canDamageTerrain = canDamageTerrain;
        }

        @Override
        public boolean canDamageTerrain() {
            return this.canDamageTerrain;
        }

		@Override
		public void finalizeExplosion(boolean spawnParticles) {
			super.finalizeExplosion(false);

			if (spawnParticles)
				this.spawnParticles();
		}

        @Override public float getEntityRadius() { return this.entitySize; }

        protected void spawnParticles() {
		}

		@Override public BlockInteraction getBlockInteraction() { return this.interaction; }
	}

    class CustomDamageCalculator extends ExplosionDamageCalculator {
        @Override
        public float getEntityDamageAmount(Explosion explosion, Entity entity) {
            float f = explosion instanceof CustomExplosion customExplosion ? customExplosion.getEntityRadius() : explosion.radius();
            f *= 2;
            Vec3 vec3 = explosion.center();
            double d0 = Math.sqrt(entity.distanceToSqr(vec3)) / (double)f;
            double d1 = (1.0 - d0) * (double)Explosion.getSeenPercent(vec3, entity);
            return (float)((d1 * d1 + d1) / 2.0 * 7.0 * (double)f + 1.0);
        }
    }

}
