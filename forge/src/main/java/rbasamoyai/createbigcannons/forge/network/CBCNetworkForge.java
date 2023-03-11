package rbasamoyai.createbigcannons.forge.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCNetworkForge {

	public static final String VERSION = "2.0.0";
	
	public static final SimpleChannel INSTANCE = construct();
	
	public static SimpleChannel construct() {
		SimpleChannel channel = NetworkRegistry.ChannelBuilder
				.named(CreateBigCannons.resource("network"))
				.clientAcceptedVersions(VERSION::equals)
				.serverAcceptedVersions(VERSION::equals)
				.networkProtocolVersion(() -> VERSION)
				.simpleChannel();
		
		int id = 0;

		channel.messageBuilder(ForgeServerPacket.class, id++)
				.encoder(ForgeServerPacket::encode)
				.decoder(ForgeServerPacket::new)
				.consumer(ForgeServerPacket::handle)
				.add();

		channel.messageBuilder(ForgeClientPacket.class, id++)
				.encoder(ForgeClientPacket::encode)
				.decoder(ForgeClientPacket::new)
				.consumer(ForgeClientPacket::handle)
				.add();
		
		return channel;
	}
	
	public static void init() {}
	
}
