package rbasamoyai.createbigcannons.multiloader.forge;

import java.util.function.Supplier;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class EnvExecuteImpl {

	public static void executeOnClient(Supplier<Runnable> sup) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, sup);
	}

}
