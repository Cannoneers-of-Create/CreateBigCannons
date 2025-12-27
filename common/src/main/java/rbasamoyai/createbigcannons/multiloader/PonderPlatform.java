package rbasamoyai.createbigcannons.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nullable;

public class PonderPlatform {

	@ExpectPlatform
	public static void drain(BlockEntity be, int count, @Nullable Direction dir) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void fillWith(BlockEntity be, Fluid fluid, int count, @Nullable Direction dir) {
		throw new AssertionError();
	}

}
