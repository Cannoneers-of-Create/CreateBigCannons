package rbasamoyai.createbigcannons.fabric;

import io.github.fabricators_of_create.porting_lib.fake_players.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;

public class CBCExpectPlatformImpl {
	public static String platformName() {
		return FabricLoader.getInstance().isModLoaded("quilt_loader") ? "Quilt" : "Fabric";
	}

	public static boolean isFakePlayer(Player player) {
		return player instanceof FakePlayer;
	}

}
