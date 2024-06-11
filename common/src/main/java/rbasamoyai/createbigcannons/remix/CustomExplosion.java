package rbasamoyai.createbigcannons.remix;

import javax.annotation.Nullable;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;

public interface CustomExplosion {

	void playLocalSound(Level level, double x, double y, double z);
	void sendExplosionToClient(ServerPlayer player);
	Explosion.BlockInteraction getBlockInteraction();

	abstract class Impl extends Explosion implements CustomExplosion {
		protected final Level level;
		protected final double x;
		protected final double y;
		protected final double z;
		protected final float size;
		protected final BlockInteraction interaction;

		public Impl(Level level, @Nullable Entity source, @Nullable DamageSource damageSource,
					@Nullable ExplosionDamageCalculator calculator, double toBlowX, double toBlowY, double toBlowZ, float radius,
					boolean fire, BlockInteraction interaction) {
			super(level, source, damageSource, calculator, toBlowX, toBlowY, toBlowZ, radius, fire, interaction);
			this.level = level;
			this.x = toBlowX;
			this.y = toBlowY;
			this.z = toBlowZ;
			this.size = radius;
			this.interaction = interaction;
		}

		public Impl(Level level, ClientboundCBCExplodePacket packet) {
			super(level, null, packet.x(), packet.y(), packet.z(), packet.power(), packet.toBlow());
			this.level = level;
			this.x = packet.x();
			this.y = packet.y();
			this.z = packet.z();
			this.size = packet.power();
			this.interaction = BlockInteraction.DESTROY;
		}

		@Override
		public void finalizeExplosion(boolean spawnParticles) {
			super.finalizeExplosion(false);

			if (spawnParticles)
				this.spawnParticles();
		}

		protected abstract void spawnParticles();

		@Override public BlockInteraction getBlockInteraction() { return this.interaction; }
	}

}
