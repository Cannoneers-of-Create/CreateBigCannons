package rbasamoyai.createbigcannons.compat.computercraft.peripheral.cannon_mount;

import dan200.computercraft.api.lua.LuaFunction;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;

public class CheatingCannonMountPeripheral extends CannonMountPeripheral{
	public CheatingCannonMountPeripheral(String type, CannonMountBlockEntity tileEntity) {
		super(type, tileEntity);
	}

	@LuaFunction(mainThread = true)
	public final void setPitch(double value){
		if(this.isRunning())
			this.tileEntity.setPitch((float) value);
	}

	@LuaFunction(mainThread = true)
	public final void setYaw(double value){
		if(this.isRunning())
			this.tileEntity.setYaw((float) value);
	}
}
