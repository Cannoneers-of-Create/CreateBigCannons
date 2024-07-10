package rbasamoyai.createbigcannons.equipment.manual_loading;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;

public interface HandloadingTool {

	void onUseOnCannon(Player player, Level level, BlockPos startPos, Direction face, MountedBigCannonContraption contraption);

}
