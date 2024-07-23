package rbasamoyai.createbigcannons.compat.computercraft;

import dan200.computercraft.api.ComputerCraftAPI;

public class ComputerCraftCompat {
	public static void init() {
		ComputerCraftAPI.registerPeripheralProvider(new ForgePeripheralProvider());
	}
}
