package rbasamoyai.createbigcannons.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import rbasamoyai.createbigcannons.CreateBigCannons;

@EventBusSubscriber(modid = CreateBigCannons.MOD_ID)
public class CBCNetworkNeoForge {

	public static final String VERSION = "3.0.0";

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent evt) {
        final PayloadRegistrar registrar = evt.registrar(VERSION)
            .executesOn(HandlerThread.MAIN);

        registrar.playBidirectional(CBCNeoForgePacket.TYPE, CBCNeoForgePacket.STREAM_CODEC, new MainThreadPayloadHandler<>(CBCNeoForgePacket::handlePacket));
    }

}
