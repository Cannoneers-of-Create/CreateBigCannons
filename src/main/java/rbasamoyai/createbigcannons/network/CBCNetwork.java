package rbasamoyai.createbigcannons.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager.ClientboundRecipesPacket;

public class CBCNetwork {

	public static final String VERSION = "1.3.0";
	
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
		
		channel.messageBuilder(ClientboundRecipesPacket.class, id++)
				.encoder(ClientboundRecipesPacket::encode)
				.decoder(ClientboundRecipesPacket::new)
				.consumer(ClientboundRecipesPacket::handle)
				.add();
		
		channel.messageBuilder(ClientboundUpdateContraptionPacket.class, id++)
				.encoder(ClientboundUpdateContraptionPacket::encode)
				.decoder(ClientboundUpdateContraptionPacket::new)
				.consumer(ClientboundUpdateContraptionPacket::handle)
				.add();
		
		channel.messageBuilder(ServerboundProximityFuzePacket.class, id++)
				.encoder(ServerboundProximityFuzePacket::encode)
				.decoder(ServerboundProximityFuzePacket::new)
				.consumer(ServerboundProximityFuzePacket::handle)
				.add();

		channel.messageBuilder(ServerboundFiringActionPacket.class, id++)
				.encoder(ServerboundFiringActionPacket::encode)
				.decoder(ServerboundFiringActionPacket::new)
				.consumer(ServerboundFiringActionPacket::handle)
				.add();

		channel.messageBuilder(ServerboundCarriageWheelPacket.class, id++)
				.encoder(ServerboundCarriageWheelPacket::encode)
				.decoder(ServerboundCarriageWheelPacket::new)
				.consumer(ServerboundCarriageWheelPacket::handle)
				.add();
		
		return channel;
	}
	
	public static void init() {}
	
}
