package rbasamoyai.createbigcannons;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;

public class CBCExpectPlatform {

    @ExpectPlatform public static String platformName() { throw new AssertionError(); }

    @ExpectPlatform public static boolean isFakePlayer(Player player) { throw new AssertionError(); }

}
