package rbasamoyai.createbigcannons.index;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.CannonMountPoint;

public class CBCArmInteractionPointTypes {

	public static final CannonMountType CANNON_MOUNT = register("cannon_mount", CannonMountType::new);

	private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
		T type = factory.apply(CreateBigCannons.resource(id));
		ArmInteractionPointType.register(type);
		return type;
	}

	public static class CannonMountType extends ArmInteractionPointType {
		public CannonMountType(ResourceLocation id) {
			super(id);
		}

		@Override
		public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
			if (CBCBlocks.CANNON_MOUNT.has(state))
				return level.getBlockEntity(pos) instanceof CannonMountBlockEntity;
			if (CBCBlocks.FIXED_CANNON_MOUNT.has(state))
				return level.getBlockEntity(pos) instanceof FixedCannonMountBlockEntity;
			return false;
		}

		@Nullable
		@Override
		public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
			return new CannonMountPoint(this, level, pos, state);
		}
	}

	public static void register() {
	}

}
