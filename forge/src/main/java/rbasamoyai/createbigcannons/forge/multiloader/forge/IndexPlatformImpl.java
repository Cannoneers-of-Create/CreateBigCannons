package rbasamoyai.createbigcannons.forge.multiloader.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.forge.crafting.CannonCastBlockEntity;
import rbasamoyai.createbigcannons.forge.crafting.CannonDrillBlockEntity;

public class IndexPlatformImpl {

	public static boolean isFakePlayer(Player player) { return player instanceof FakePlayer; }

	public static AbstractCannonDrillBlockEntity makeDrill(BlockEntityType<? extends AbstractCannonDrillBlockEntity> type, BlockPos pos, BlockState state) {
		return new CannonDrillBlockEntity(type, pos, state);
	}

	public static AbstractCannonCastBlockEntity makeCast(BlockEntityType<? extends AbstractCannonCastBlockEntity> type, BlockPos pos, BlockState state) {
		return new CannonCastBlockEntity(type, pos, state);
	}

}
