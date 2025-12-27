package rbasamoyai.createbigcannons.multiloader;

import java.util.function.Supplier;

import net.createmod.catnip.platform.CatnipServices;

public class EnvExecute {

	public static void executeOnClient(Supplier<Runnable> sup) {
        CatnipServices.PLATFORM.executeOnClientOnly(sup);
    }

}
