package rbasamoyai.createbigcannons.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCNetwork {

	public static final String VERSION = "1.0.0";
	
	public static final SimpleChannel INSTANCE = construct();
	
	public static SimpleChannel construct() {
		SimpleChannel channel = NetworkRegistry.ChannelBuilder
				.named(CreateBigCannons.resource("network"))
				.clientAcceptedVersions(VERSION::equals)
				.serverAcceptedVersions(VERSION::equals)
				.networkProtocolVersion(() -> VERSION)
				.simpleChannel();
		
		int id = 0;
		
		channel.messageBuilder(ServerboundTimedFuzePacket.class, id++)
				.encoder(ServerboundTimedFuzePacket::encode)
				.decoder(ServerboundTimedFuzePacket::new)
				.consumer(ServerboundTimedFuzePacket::handle)
				.add();
		
		return channel;
	}
	
	public static void init() {}
	
}
