package rbasamoyai.createbigcannons.cannon_control;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;

public interface ControlPitchContraption {

    boolean isAttachedTo(AbstractContraptionEntity entity);
    void attach(AbstractPitchOrientedContraptionEntity poce);
    void onStall();

    void disassemble();

    BlockPos getDismountPositionForContraption(AbstractPitchOrientedContraptionEntity poce);

    BlockState getControllerState();

    default void cacheRecoilVector(Vec3 vector) {}

    interface Block extends ControlPitchContraption {
        BlockPos getControllerBlockPos();
    }

}
