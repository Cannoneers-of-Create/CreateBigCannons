package rbasamoyai.createbigcannons.compat.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;

public class FixedCannonMountPeripheral implements GenericPeripheral {
	@Override
	public String id() {
		return CreateBigCannons.MOD_ID+"fixed_cannon_mount";
	}

	@LuaFunction(mainThread = true)
	public boolean isAssembled(FixedCannonMountBlockEntity ent){
		return ent.isRunning();
	}
}
