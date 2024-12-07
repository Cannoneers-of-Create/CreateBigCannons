package rbasamoyai.createbigcannons.forge.mixin;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

@Mixin(AbstractCannonProjectile.class)
public abstract class AbstractCannonProjectileMixin extends Projectile implements IEntityAdditionalSpawnData {

	@Shadow public abstract void baseWriteSpawnData(FriendlyByteBuf buf);
	@Shadow public abstract void baseReadSpawnData(FriendlyByteBuf buf);

	protected AbstractCannonProjectileMixin(EntityType<? extends AbstractCannonProjectile> entityType, Level level) {
		super(entityType, level);
	}

	@Nonnull
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override public void writeSpawnData(FriendlyByteBuf buf) { this.baseWriteSpawnData(buf); }
	@Override public void readSpawnData(FriendlyByteBuf buf) { this.baseReadSpawnData(buf); }

}
