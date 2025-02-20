package rbasamoyai.createbigcannons.compat.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.foundry.BasinFoundryBlockEntity;

public class BasinFoundryPeripheral implements GenericPeripheral {

	@Override
	public String id() {
		return CreateBigCannons.MOD_ID+":basin_foundry";
	}

	@LuaFunction(mainThread = true)
	public double getMeltingTime(BasinFoundryBlockEntity ent){
		return ent.meltingTime;
	}

	@LuaFunction(mainThread = true)
	public double getRecipeCooldown(BasinFoundryBlockEntity ent){
		return ent.recipeCooldown;
	}

	@LuaFunction(mainThread = true)
	public boolean isRunning(BasinFoundryBlockEntity ent){
		return ent.isRunning();
	}
}
