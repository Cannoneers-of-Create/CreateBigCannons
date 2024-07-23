package rbasamoyai.createbigcannons.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CBCCfgComputerCraft extends ConfigBase {
	public final ConfigBool enableCheatingPeripheralFunctions = b(false, "enableCheatingPeripheralFunctions", Comments.enableCheatingPeripheralFunctions);

	@Override public String getName() { return "computercraft"; }

	private static class Comments {
		static String enableCheatingPeripheralFunctions = "Enable some cheating functions for peripherals.";
	}
}
