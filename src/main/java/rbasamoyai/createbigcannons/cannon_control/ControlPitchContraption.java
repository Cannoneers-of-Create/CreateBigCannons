package rbasamoyai.createbigcannons.cannon_control;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

public interface ControlPitchContraption {

    boolean isAttachedTo(AbstractContraptionEntity entity);
    void attach(PitchOrientedContraptionEntity poce);
    void onStall();

    void disassemble();

    BlockPos getDismountPositionForContraption(PitchOrientedContraptionEntity poce);

    BlockState getControllerState();

    interface Block extends ControlPitchContraption {
        BlockPos getControllerBlockPos();
    }

}
