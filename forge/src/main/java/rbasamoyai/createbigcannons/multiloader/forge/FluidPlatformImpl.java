package rbasamoyai.createbigcannons.multiloader.forge;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;

public class FluidPlatformImpl {
	public static Fluid getMilk() {
		return ForgeMod.MILK.get();
	}
}
