package rbasamoyai.createbigcannons.index;

import javax.annotation.Nullable;

import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountExtensionBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.YawControllerBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.CannonMountPoint;

public class CBCArmInteractionPointTypes {
    static {
        register("cannon_mount", new CannonMountType());
    }
    private static <T extends ArmInteractionPointType> void register(String name, T type) {
        Registry.register(CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE, CreateBigCannons.resource(name), type);
    }

	public static class CannonMountType extends ArmInteractionPointType {
		@Override
		public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
			if (CBCBlocks.CANNON_MOUNT.has(state))
				return level.getBlockEntity(pos) instanceof CannonMountBlockEntity;
			if (CBCBlocks.FIXED_CANNON_MOUNT.has(state))
				return level.getBlockEntity(pos) instanceof FixedCannonMountBlockEntity;
			if (CBCBlocks.CANNON_MOUNT_EXTENSION.has(state))
				return level.getBlockEntity(pos) instanceof CannonMountExtensionBlockEntity;
			if (CBCBlocks.YAW_CONTROLLER.has(state))
				return level.getBlockEntity(pos) instanceof YawControllerBlockEntity;
			return false;
		}

		@Nullable
		@Override
		public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
			return new CannonMountPoint(this, level, pos, state);
		}
	}

    public static void init() {
    }

}
