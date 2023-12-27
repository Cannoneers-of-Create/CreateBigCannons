package rbasamoyai.createbigcannons.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(Entity.class)
public abstract class EntityClientMixin {

	@Unique private final Entity self = (Entity) (Object) this;

	@Inject(method = "turn", at = @At("HEAD"), cancellable = true)
	public void createbigcannons$turn(double yaw, double pitch, CallbackInfo ci) {
		Minecraft mc = Minecraft.getInstance();
		boolean playerIsCamera = mc.getCameraEntity() == null;
		if (playerIsCamera && this.self.getVehicle() instanceof PitchOrientedContraptionEntity poce) {
			float crot = poce.getRotationCoefficient();
			float dxr = (float) pitch * crot;
			float dyr = (float) yaw * crot;
			this.self.setXRot(this.self.getXRot() + dxr);
			this.self.setYRot(this.self.getYRot() + dyr);

			float e = poce.maximumDepression();
			float d = -poce.maximumElevation();
			this.self.setXRot(Mth.clamp(this.self.getXRot(), d, e));
			this.self.xRotO += dxr;
			this.self.yRotO += dyr;
			this.self.xRotO = Mth.clamp(this.self.xRotO, d, e);

			self.getVehicle().onPassengerTurned(this.self);
			if (ci.isCancellable()) ci.cancel();
		}
	}

}
