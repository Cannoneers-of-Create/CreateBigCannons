package rbasamoyai.createbigcannons.mixin.client;

import net.minecraft.client.Camera;

import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraAccessor {

	@Invoker void callSetPosition(Vec3 pos);

}
