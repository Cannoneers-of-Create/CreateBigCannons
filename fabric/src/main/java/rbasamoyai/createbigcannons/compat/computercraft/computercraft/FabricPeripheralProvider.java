package rbasamoyai.createbigcannons.compat.computercraft.computercraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.compat.computercraft.peripheral.cannon_mount.CannonMountPeripheral;
import rbasamoyai.createbigcannons.compat.computercraft.peripheral.cannon_mount.CheatingCannonMountPeripheral;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class FabricPeripheralProvider implements IPeripheralProvider {

	@Nullable
	@Override
	public IPeripheral getPeripheral(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
		if(level.getBlockEntity(blockPos) instanceof CannonMountBlockEntity cannonMountBlockEntity) {
			return CBCConfigs.SERVER.computercraft.enableCheatingPeripheralFunctions.get() ?
				new CheatingCannonMountPeripheral("cannon_mount", cannonMountBlockEntity)
				:
				new CannonMountPeripheral("cannon_mount", cannonMountBlockEntity);
		}
		return null;
	}
}
