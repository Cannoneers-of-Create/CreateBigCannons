package rbasamoyai.createbigcannons.cannon_control;

import javax.annotation.Nullable;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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

	@Nullable default ResourceLocation getTypeId() { return null; }

	default void onRecoil(Vec3 vector, AbstractContraptionEntity cannon) {
	}

	interface Block extends ControlPitchContraption {
		BlockPos getControllerBlockPos();
	}

}
