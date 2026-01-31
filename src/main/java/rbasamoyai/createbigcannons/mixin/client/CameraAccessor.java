package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;

@Mixin(Camera.class)
public interface CameraAccessor {

	@Invoker void callSetPosition(Vec3 pos);

}
