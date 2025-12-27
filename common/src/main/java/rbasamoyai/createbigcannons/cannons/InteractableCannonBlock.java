package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.content.contraptions.Contraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

public interface InteractableCannonBlock {

	boolean onInteractWhileAssembled(Player player, BlockPos localPos, Direction side, InteractionHand hand, Level level,
									 Contraption contraption, BlockEntity be, StructureBlockInfo info, PitchOrientedContraptionEntity entity);

}
