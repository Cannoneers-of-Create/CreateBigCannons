package rbasamoyai.createbigcannons.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.material.Fluid;

public class FluidPlatform {
	@ExpectPlatform
	public static Fluid getMilk() { throw new AssertionError();}
}
