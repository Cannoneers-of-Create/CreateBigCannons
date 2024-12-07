package rbasamoyai.createbigcannons.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import io.github.fabricators_of_create.porting_lib.entity.IEntityAdditionalSpawnData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

@Mixin(AbstractCannonProjectile.class)
public abstract class AbstractCannonProjectileMixin extends Projectile implements IEntityAdditionalSpawnData {

	@Shadow public abstract void baseWriteSpawnData(FriendlyByteBuf buf);
	@Shadow public abstract void baseReadSpawnData(FriendlyByteBuf buf);

	protected AbstractCannonProjectileMixin(EntityType<? extends AbstractCannonProjectile> entityType, Level level) {
		super(entityType, level);
	}

	@Override public void writeSpawnData(FriendlyByteBuf buf) { this.baseWriteSpawnData(buf); }
	@Override public void readSpawnData(FriendlyByteBuf buf) { this.baseReadSpawnData(buf); }

}
