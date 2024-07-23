package rbasamoyai.createbigcannons.compat.computercraft.peripheral.cannon_mount;

import com.simibubi.create.content.contraptions.AssemblyException;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;

public class CannonMountPeripheral implements IPeripheral {
	public final String type ;
	public final CannonMountBlockEntity tileEntity;

	public final Level level;

	public final BlockPos worldPosition;

	public CannonMountPeripheral(String type, CannonMountBlockEntity tileEntity) {
		this.type = type;
		this.tileEntity = tileEntity;
		this.level = tileEntity.getLevel();
		this.worldPosition = tileEntity.getBlockPos();
	}

	@NotNull
	@Override
	public String getType() {
		return type;
	}

	@Override
	public boolean equals(@Nullable IPeripheral iPeripheral) {
		return iPeripheral == this;
	}

	@Override
	public Object getTarget() {
		return this.tileEntity;
	}

	@LuaFunction(mainThread = true)
	public final Object assemble() throws LuaException {
		if(!this.tileEntity.isRunning()) {
			try {
				this.tileEntity.assemble();
			} catch (AssemblyException e) {
				throw new LuaException(e.toString());
			}
			return true;
		}
		return false;
	}

	@LuaFunction(mainThread = true)
	public final Object disassemble() {
		if(this.tileEntity.isRunning()) {
			this.tileEntity.disassemble();
			this.tileEntity.sendData();
			return true;
		}
		return false;
	}

	@LuaFunction(mainThread = true)
	public final void fire() {
		if(this.tileEntity.isRunning() && this.tileEntity.getContraption() != null) {
			this.tileEntity.getContraption().tryFiringShot();
		}
	}

	@LuaFunction(mainThread = true)
	public final boolean isRunning(){
		return this.tileEntity.isRunning();
	}

	@LuaFunction
	public final double getPitch() {
		return this.tileEntity.getPitch();
	}

	@LuaFunction
	public final double getYaw() {
		return this.tileEntity.getYaw();
	}
}
