package rbasamoyai.createbigcannons.compat.computercraft;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.PeripheralType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;

public class CannonMountPeripheral implements GenericPeripheral {
	@Override
	public String id() {
		return new ResourceLocation(CreateBigCannons.MOD_ID, "cannon_mount").toString();
	}

	@LuaFunction(mainThread = true)
	public double getPitch(CannonMountBlockEntity ent){
		return ent.getDisplayPitch();
	}

	@LuaFunction(mainThread = true)
	public double getYaw(CannonMountBlockEntity ent){
		return ent.getYaw();
	}

	@LuaFunction(mainThread = true)
	public boolean isAssembled(CannonMountBlockEntity ent){
		return ent.isRunning();
	}

	@LuaFunction(mainThread = true)
	public double getMaximumDepression(CannonMountBlockEntity ent) {
		if (ent.getContraption() == null || !ent.getContraption().isAlive()) {
			return 0;
		}
		return ent.getContraption().maximumDepression();
	}

	@LuaFunction(mainThread = true)
	public double getMaximumElevation(CannonMountBlockEntity ent) {
		if (ent.getContraption() == null || !ent.getContraption().isAlive()) {
			return 0;
		}
		return ent.getContraption().maximumElevation();
	}

	@LuaFunction(mainThread = true)
	public String getDirection(CannonMountBlockEntity ent) {
		return ent.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toString();
	}

	@LuaFunction(mainThread = true)
	public int getInitialYaw(CannonMountBlockEntity ent) {
		return switch (ent.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toString()) {
			case "east" -> 90;
			case "south" -> 180;
			case "west" -> 270;
			default -> 0;
		};
	}

	@LuaFunction(mainThread = true)
	public double getCannonWeight(CannonMountBlockEntity ent) {
		return ent.calculateCannonStressApplied();
	}

	@LuaFunction(mainThread = true)
	public double getCannonStress(CannonMountBlockEntity ent, double rpm) {
		return ent.calculateCannonStressApplied()*rpm;
	}

	@LuaFunction(mainThread = true)
	public double getCannonStress(CannonMountBlockEntity ent) {
		double weight = ent.calculateCannonStressApplied();
		return weight * ent.getYawInterface().getSpeed() + weight * ent.getPitchInterface().getSpeed();
	}
}
