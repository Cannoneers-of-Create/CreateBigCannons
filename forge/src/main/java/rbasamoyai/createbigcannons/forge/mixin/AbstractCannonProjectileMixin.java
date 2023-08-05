package rbasamoyai.createbigcannons.forge.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import javax.annotation.Nonnull;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

@Mixin(AbstractCannonProjectile.class)
public abstract class AbstractCannonProjectileMixin extends Projectile implements IEntityAdditionalSpawnData {

	protected AbstractCannonProjectileMixin(EntityType<? extends AbstractCannonProjectile> entityType, Level level) {
		super(entityType, level);
	}

	@Nonnull
	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buf) {
		Vec3 vel = this.getDeltaMovement();
		buf.writeFloat(this.getXRot())
			.writeFloat(this.getYRot())
			.writeDouble(vel.x)
			.writeDouble(vel.y)
			.writeDouble(vel.z);
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buf) {
		this.setXRot(buf.readFloat());
		this.setYRot(buf.readFloat());
		this.setDeltaMovement(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

}
