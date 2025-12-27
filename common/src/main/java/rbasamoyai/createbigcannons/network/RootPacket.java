package rbasamoyai.createbigcannons.network;

import java.util.concurrent.Executor;

import net.minecraft.network.PacketListener;
import net.minecraft.world.entity.player.Player;

public interface RootPacket {

	void handle(Executor exec, PacketListener listener, Player player);

}
