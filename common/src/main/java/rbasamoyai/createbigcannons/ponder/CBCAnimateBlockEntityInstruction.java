package rbasamoyai.createbigcannons.ponder;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.instruction.AnimateBlockEntityInstruction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AnimatedAutocannon;

public class CBCAnimateBlockEntityInstruction extends AnimateBlockEntityInstruction {

	protected CBCAnimateBlockEntityInstruction(BlockPos location,
											   float totalDelta, int ticks, BiConsumer<PonderWorld, Float> setter,
											   Function<PonderWorld, Float> getter) {
		super(location, totalDelta, ticks, setter, getter);
	}

	public static CBCAnimateBlockEntityInstruction cannonMountPitch(BlockPos location, float angle, int ticks) {
		return new CBCAnimateBlockEntityInstruction(location, angle, ticks,
			(level, t) -> castIfPresent(level, location, CannonMountBlockEntity.class)
				.ifPresent(mount -> mount.setPitch(t)),
			level -> castIfPresent(level, location, CannonMountBlockEntity.class).map(mount -> mount.getPitchOffset(1.0f)).orElse(0.0f));
	}

	public static CBCAnimateBlockEntityInstruction cannonMountYaw(BlockPos location, float angle, int ticks) {
		return new CBCAnimateBlockEntityInstruction(location, angle, ticks,
			(level, t) -> castIfPresent(level, location, CannonMountBlockEntity.class)
				.ifPresent(mount -> mount.setYaw(t)),
			level -> castIfPresent(level, location, CannonMountBlockEntity.class).map(mount -> mount.getYawOffset(1.0f)).orElse(0.0f));
	}

	public static CBCAnimateBlockEntityInstruction autocannon(BlockPos location, int ticks) {
		return new CBCAnimateBlockEntityInstruction(location, ticks, ticks,
			(level, t) -> castIfPresent(level, location, AnimatedAutocannon.class)
				.ifPresent(AnimatedAutocannon::incrementAnimationTicks),
			level -> castIfPresent(level, location, AnimatedAutocannon.class).map(autocannon -> (float) autocannon.getAnimationTicks()).orElse(0.0f));
	}

	private static <T> Optional<T> castIfPresent(PonderWorld level, BlockPos pos, Class<T> clazz) {
		BlockEntity be = level.getBlockEntity(pos);
		return clazz.isInstance(be) ? Optional.of(clazz.cast(be)) : Optional.empty();
	}

}
