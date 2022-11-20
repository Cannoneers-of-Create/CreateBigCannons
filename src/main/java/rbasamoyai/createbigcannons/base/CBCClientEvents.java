package rbasamoyai.createbigcannons.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import rbasamoyai.createbigcannons.CreateBigCannonsClient;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.network.CBCNetwork;
import rbasamoyai.createbigcannons.network.ServerboundFiringActionPacket;

public class CBCClientEvents {

    public static void onClientGameTick(TickEvent.ClientTickEvent evt) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.player.getVehicle() instanceof CannonCarriageEntity carriage) {
            Input input = mc.player.input;
            boolean isPitching = CreateBigCannonsClient.PITCH_MODE.isDown();
            boolean isFiring = CreateBigCannonsClient.FIRE_CONTROLLED_CANNON.isDown();
            carriage.setInput(input.left, input.right, input.up, input.down, isPitching);
            mc.player.handsBusy |= input.left | input.right | input.up | input.down | isFiring;

            if (isFiring) {
                CBCNetwork.INSTANCE.sendToServer(new ServerboundFiringActionPacket());
            }
        }
    }

    public static void register(IEventBus forgeBus) {
        forgeBus.addListener(CBCClientEvents::onClientGameTick);
    }

}
