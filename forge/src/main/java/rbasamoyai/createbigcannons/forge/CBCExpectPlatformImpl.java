package rbasamoyai.createbigcannons.forge;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;

public class CBCExpectPlatformImpl {
	public static String platformName() {
		return "Forge";
	}

	public static boolean isFakePlayer(Player player) { return player instanceof FakePlayer; }

}
