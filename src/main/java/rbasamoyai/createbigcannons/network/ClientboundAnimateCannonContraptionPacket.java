package rbasamoyai.createbigcannons.network;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundAnimateCannonContraptionPacket {

	private final int id;

	public ClientboundAnimateCannonContraptionPacket(AbstractContraptionEntity entity) {
		this.id = entity.getId();
	}

	public ClientboundAnimateCannonContraptionPacket(FriendlyByteBuf buf) {
		this.id = buf.readVarInt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(this.id);
	}

	public void handle(Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> CBCClientHandlers.animateCannon(this));
		});
		ctx.setPacketHandled(true);
	}

	public int id() { return this.id; }

}
