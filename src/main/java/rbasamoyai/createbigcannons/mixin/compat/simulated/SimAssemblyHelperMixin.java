package rbasamoyai.createbigcannons.mixin.compat.simulated;

import java.util.Collection;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.StructureTransform;

import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.simulated_team.simulated.util.SimAssemblyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(SimAssemblyHelper.class)
public class SimAssemblyHelperMixin {

    @WrapMethod(method = "disassembleAndAddCreateContraptions")
    private static void createbigcannons$disassembleAndAddCreateContraptions(Level level, BoundingBox3ic assemblyBounds,
                                                                             Collection<BlockPos> blocks, boolean passGluesBack,
                                                                             List<AABB> collectedGlues, Operation<Void> original) {
        original.call(level, assemblyBounds, blocks, passGluesBack, collectedGlues);
        // Adapted from disassembleAndAddCreateContraptions --ritchie
        AABB assemblyBoundsD = new AABB(assemblyBounds.minX(), assemblyBounds.minY(), assemblyBounds.minZ(),
            assemblyBounds.maxX() + 1, assemblyBounds.maxY() + 1, assemblyBounds.maxZ() + 1);
        List<PitchOrientedContraptionEntity> intersectingPoces = level.getEntitiesOfClass(PitchOrientedContraptionEntity.class, assemblyBoundsD.inflate(2.0));
        for (PitchOrientedContraptionEntity poce : intersectingPoces) {
            if (!(poce.getController() instanceof ControlPitchContraption.Block blockController))
                continue; // Do not accept carriages, e.g.
            BlockPos controllerPos = blockController.getControllerBlockPos();
            if (!blocks.contains(controllerPos))
                continue;
            // Disassemble contraption and add blocks
            Contraption contraption = poce.getContraption();
            StructureTransform transform = poce.makeStructureTransform();
            for (BlockPos contraptionBlock : contraption.getBlocks().keySet())
                blocks.add(transform.apply(contraptionBlock));
            // No superglue
            poce.disassemble();
            blockController.disassemble();
            blockController.markForReassembly();
        }
    }

}
