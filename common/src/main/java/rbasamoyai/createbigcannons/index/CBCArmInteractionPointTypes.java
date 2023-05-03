package rbasamoyai.createbigcannons.index;

import com.simibubi.create.content.logistics.block.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.logistics.block.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechPoint;

import java.util.function.Function;

public class CBCArmInteractionPointTypes {

	public static final QuickfiringBreechType QUICKFIRING_BREECH = register("quickfiring_breech", QuickfiringBreechType::new);

	private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
		T type = factory.apply(CreateBigCannons.resource(id));
		ArmInteractionPointType.register(type);
		return type;
	}

	public static class QuickfiringBreechType extends ArmInteractionPointType {
		public QuickfiringBreechType(ResourceLocation id) {
			super(id);
		}

		@Override
		public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
			return CBCBlocks.CANNON_MOUNT.has(state) && level.getBlockEntity(pos) instanceof CannonMountBlockEntity;
		}

		@Nullable
		@Override
		public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
			return new QuickfiringBreechPoint(this, level, pos, state);
		}
	}

	public static void register() {}

}
