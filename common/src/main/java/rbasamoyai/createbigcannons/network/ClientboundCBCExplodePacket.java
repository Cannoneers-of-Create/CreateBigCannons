package rbasamoyai.createbigcannons.network;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public record ClientboundCBCExplodePacket(double x, double y, double z, float power, List<BlockPos> toBlow, float knockbackX,
										  float knockbackY, float knockbackZ, ExplosionType explosionType) implements RootPacket {

	public ClientboundCBCExplodePacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), readToBlow(buf), buf.readFloat(),
			buf.readFloat(), buf.readFloat(), buf.readEnum(ExplosionType.class));
	}

	private static List<BlockPos> readToBlow(FriendlyByteBuf buf) {
		int sz = buf.readVarInt();
		List<BlockPos> toBlow = new LinkedList<>();
		for (int i = 0; i < sz; ++i)
			toBlow.add(buf.readBlockPos());
		return toBlow;
	}

	@Override
	public void rootEncode(FriendlyByteBuf buf) {
		buf.writeDouble(this.x)
			.writeDouble(this.y)
			.writeDouble(this.z)
			.writeFloat(this.power);
		buf.writeVarInt(this.toBlow.size());
		for (BlockPos pos : this.toBlow)
			buf.writeBlockPos(pos);
		buf.writeFloat(this.knockbackX)
			.writeFloat(this.knockbackY)
			.writeFloat(this.knockbackZ);
		buf.writeEnum(this.explosionType);
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.addExplosionFromServer(this));
	}


	public enum ExplosionType {
		SHRAPNEL,
		FLAK,
		SMOKE,
		MORTAR_STONE,
		IMPACT
	}

}
