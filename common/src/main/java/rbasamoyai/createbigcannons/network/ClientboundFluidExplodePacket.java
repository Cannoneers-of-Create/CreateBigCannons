package rbasamoyai.createbigcannons.network;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.Fluid;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public record ClientboundFluidExplodePacket(double x, double y, double z, float power, List<BlockPos> toBlow, float knockbackX,
											float knockbackY, float knockbackZ, Fluid fluid) implements RootPacket {

	public ClientboundFluidExplodePacket(FriendlyByteBuf buf) {
		this(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), readToBlow(buf), buf.readFloat(),
			buf.readFloat(), buf.readFloat(), CBCRegistryUtils.getFluid(buf.readResourceLocation()));
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
		buf.writeResourceLocation(CBCRegistryUtils.getFluidLocation(this.fluid));
	}

	@Override
	public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
		EnvExecute.executeOnClient(() -> () -> CBCClientHandlers.addFluidExplosionFromServer(this));
	}

	public ClientboundCBCExplodePacket asCBCExplodePacket() {
		return new ClientboundCBCExplodePacket(this.x, this.y, this.z, this.power, this.toBlow, this.knockbackX, this.knockbackY,
			this.knockbackZ, ClientboundCBCExplodePacket.ExplosionType.SHRAPNEL);
	}

}
