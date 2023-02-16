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
				.consumerMainThread(ServerboundTimedFuzePacket::handle)
				.add();
		
		channel.messageBuilder(ClientboundRecipesPacket.class, id++)
				.encoder(ClientboundRecipesPacket::encode)
				.decoder(ClientboundRecipesPacket::new)
				.consumerMainThread(ClientboundRecipesPacket::handle)
				.add();
		
		channel.messageBuilder(ClientboundUpdateContraptionPacket.class, id++)
				.encoder(ClientboundUpdateContraptionPacket::encode)
				.decoder(ClientboundUpdateContraptionPacket::new)
				.consumerMainThread(ClientboundUpdateContraptionPacket::handle)
				.add();
		
		channel.messageBuilder(ServerboundProximityFuzePacket.class, id++)
				.encoder(ServerboundProximityFuzePacket::encode)
				.decoder(ServerboundProximityFuzePacket::new)
				.consumerMainThread(ServerboundProximityFuzePacket::handle)
				.add();

		channel.messageBuilder(ServerboundFiringActionPacket.class, id++)
				.encoder(ServerboundFiringActionPacket::encode)
				.decoder(ServerboundFiringActionPacket::new)
				.consumerMainThread(ServerboundFiringActionPacket::handle)
				.add();

		channel.messageBuilder(ServerboundCarriageWheelPacket.class, id++)
				.encoder(ServerboundCarriageWheelPacket::encode)
				.decoder(ServerboundCarriageWheelPacket::new)
				.consumerMainThread(ServerboundCarriageWheelPacket::handle)
				.add();

		channel.messageBuilder(ClientboundAnimateCannonContraptionPacket.class, id++)
				.encoder(ClientboundAnimateCannonContraptionPacket::encode)
				.decoder(ClientboundAnimateCannonContraptionPacket::new)
				.consumerMainThread(ClientboundAnimateCannonContraptionPacket::handle)
				.add();

		channel.messageBuilder(ServerboundSetFireRatePacket.class, id++)
				.encoder(ServerboundSetFireRatePacket::encode)
				.decoder(ServerboundSetFireRatePacket::new)
				.consumerMainThread(ServerboundSetFireRatePacket::handle)
				.add();
		
		return channel;
	}
	
	public static void init() {}
	
}
