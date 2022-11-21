package rbasamoyai.createbigcannons.cannonmount;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;

public interface ControlPitchContraption {

    boolean isAttachedTo(AbstractContraptionEntity entity);
    void attach(PitchOrientedContraptionEntity poce);
    void onStall();

    void disassemble();

    public interface Block extends ControlPitchContraption {
        BlockPos getControllerBlockPos();
    }

}
