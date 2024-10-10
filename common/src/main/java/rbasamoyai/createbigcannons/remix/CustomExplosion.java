package rbasamoyai.createbigcannons.remix;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;

public interface CustomExplosion {

	default void playLocalSound(Level level, double x, double y, double z) {
	}

	default void editBlock(Level level, BlockPos pos, BlockState blockState, FluidState fluidState, float power) {
	}

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
					boolean fire, Level.ExplosionInteraction interaction) {
			super(level, source, damageSource, calculator, toBlowX, toBlowY, toBlowZ, radius, fire, convertToExplosionBlockInteraction(level, interaction));
			this.level = level;
			this.x = toBlowX;
			this.y = toBlowY;
			this.z = toBlowZ;
			this.size = radius;
			this.interaction = convertToExplosionBlockInteraction(level, interaction);
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

		protected void spawnParticles() {
		}

		@Override public BlockInteraction getBlockInteraction() { return this.interaction; }
	}

	static Explosion.BlockInteraction convertToExplosionBlockInteraction(Level level, Level.ExplosionInteraction levelExplosionInteraction) {
		return switch(levelExplosionInteraction) {
			case NONE -> Explosion.BlockInteraction.KEEP;
			case BLOCK -> getDestroyType(level, GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
			case MOB -> level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
				? getDestroyType(level, GameRules.RULE_MOB_EXPLOSION_DROP_DECAY)
				: Explosion.BlockInteraction.KEEP;
			case TNT -> getDestroyType(level, GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
		};
	}

	static Explosion.BlockInteraction getDestroyType(Level level, GameRules.Key<GameRules.BooleanValue> gameRule) {
		return level.getGameRules().getBoolean(gameRule) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
	}

}
