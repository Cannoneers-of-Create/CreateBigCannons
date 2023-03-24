package rbasamoyai.createbigcannons.multiloader.forge;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

public class EnvExecuteImpl {

	public static void executeOnClient(Supplier<Runnable> sup) { DistExecutor.unsafeRunWhenOn(Dist.CLIENT, sup); }

}
