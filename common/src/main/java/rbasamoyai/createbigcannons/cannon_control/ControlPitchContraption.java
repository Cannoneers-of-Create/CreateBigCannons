package rbasamoyai.createbigcannons.cannon_control;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

public interface ControlPitchContraption {

	boolean isAttachedTo(AbstractContraptionEntity entity);

	void attach(PitchOrientedContraptionEntity poce);

	void onStall();

	void disassemble();

	BlockPos getDismountPositionForContraption(PitchOrientedContraptionEntity poce);

	BlockState getControllerState();

	default void cacheRecoilVector(Vec3 vector, AbstractContraptionEntity cannon) {
	}

	interface Block extends ControlPitchContraption {
		BlockPos getControllerBlockPos();
	}

}
