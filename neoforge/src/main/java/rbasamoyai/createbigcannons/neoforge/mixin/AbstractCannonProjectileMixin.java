package rbasamoyai.createbigcannons.neoforge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

@Mixin(AbstractCannonProjectile.class)
public abstract class AbstractCannonProjectileMixin extends Projectile implements IEntityWithComplexSpawn {

	@Shadow public abstract void baseWriteSpawnData(RegistryFriendlyByteBuf buf);
	@Shadow public abstract void baseReadSpawnData(RegistryFriendlyByteBuf buf);

	protected AbstractCannonProjectileMixin(EntityType<? extends AbstractCannonProjectile> entityType, Level level) {
		super(entityType, level);
	}

	@Override public void writeSpawnData(RegistryFriendlyByteBuf buf) { this.baseWriteSpawnData(buf); }
	@Override public void readSpawnData(RegistryFriendlyByteBuf buf) { this.baseReadSpawnData(buf); }

}
