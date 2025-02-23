package rbasamoyai.createbigcannons.compat.computercraft;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.detail.VanillaDetailRegistries;
import rbasamoyai.createbigcannons.compat.computercraft.details.FuzeItemDetails;
import rbasamoyai.createbigcannons.compat.computercraft.details.FuzedBlockItemDetails;
import rbasamoyai.createbigcannons.compat.computercraft.peripherals.BasinFoundryPeripheral;
import rbasamoyai.createbigcannons.compat.computercraft.peripherals.CannonCastPeripheral;
import rbasamoyai.createbigcannons.compat.computercraft.peripherals.CannonMountPeripheral;
import rbasamoyai.createbigcannons.compat.computercraft.peripherals.FixedCannonMountPeripheral;
import rbasamoyai.createbigcannons.compat.computercraft.peripherals.FuzedProjectilePeripheral;

public class ComputerCraftLoader {
	public static void loadCommon(){
		ComputerCraftAPI.registerGenericSource(new CannonMountPeripheral());
		ComputerCraftAPI.registerGenericSource(new CannonCastPeripheral());
		ComputerCraftAPI.registerGenericSource(new BasinFoundryPeripheral());

		ComputerCraftAPI.registerGenericSource(new FixedCannonMountPeripheral());
		ComputerCraftAPI.registerGenericSource(new FuzedProjectilePeripheral());
		addDetails();
	}

	public static void addDetails(){
		VanillaDetailRegistries.ITEM_STACK.addProvider(new FuzedBlockItemDetails());
		VanillaDetailRegistries.ITEM_STACK.addProvider(new FuzeItemDetails());
	}
}
