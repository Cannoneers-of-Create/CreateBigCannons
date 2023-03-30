package rbasamoyai.createbigcannons.fabric.network;

import me.pepperbell.simplenetworking.SimpleChannel;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCNetworkFabric {

	public static final SimpleChannel INSTANCE = construct();

	private static SimpleChannel construct() {

		SimpleChannel channel = new SimpleChannel(CreateBigCannons.resource("network"));
		channel.registerC2SPacket(FabricPacket.class, 0, FabricPacket::new);
		channel.registerS2CPacket(FabricPacket.class, 0, FabricPacket::new);
		return channel;
	}

}
