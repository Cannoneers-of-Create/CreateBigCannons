package rbasamoyai.createbigcannons.fabric.multiloader.fabric;

import io.github.fabricators_of_create.porting_lib.fake_players.FakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.fabric.crafting.CannonDrillBlockEntity;

public class IndexPlatformImpl {

	public static boolean isFakePlayer(Player player) { return player instanceof FakePlayer; }

	public static AbstractCannonDrillBlockEntity makeDrill(BlockEntityType<? extends AbstractCannonDrillBlockEntity> type, BlockPos pos, BlockState state) {
		return new CannonDrillBlockEntity(type, pos, state);
	}

}
