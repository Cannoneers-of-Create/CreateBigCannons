package rbasamoyai.createbigcannons.compat.computercraft;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import org.jetbrains.annotations.NotNull;

import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.compat.computercraft.peripheral.cannon_mount.CannonMountPeripheral;
import rbasamoyai.createbigcannons.compat.computercraft.peripheral.cannon_mount.CheatingCannonMountPeripheral;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class ForgePeripheralProvider implements IPeripheralProvider {
	@NotNull
	@Override
	public LazyOptional<IPeripheral> getPeripheral(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull Direction direction) {
		if(level.getBlockEntity(blockPos) instanceof CannonMountBlockEntity cannonMountBlockEntity) {
			return CBCConfigs.SERVER.computercraft.enableCheatingPeripheralFunctions.get() ?
				LazyOptional.of(() -> new CheatingCannonMountPeripheral("cannon_mount", cannonMountBlockEntity))
				:
				LazyOptional.of(() -> new CannonMountPeripheral("cannon_mount", cannonMountBlockEntity));
		}
		return LazyOptional.empty();
	}
}
