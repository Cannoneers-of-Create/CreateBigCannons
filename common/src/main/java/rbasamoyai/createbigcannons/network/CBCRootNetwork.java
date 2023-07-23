package rbasamoyai.createbigcannons.network;

import java.util.function.Function;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterialPropertiesHandler.ClientboundAutocannonMaterialPropertiesPacket;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler.ClientboundMunitionPropertiesPacket;

public class CBCRootNetwork {

	private static final Int2ObjectMap<Function<FriendlyByteBuf, ? extends RootPacket>> ID_TO_CONSTRUCTOR = new Int2ObjectOpenHashMap<>();
	private static final Object2IntMap<Class<? extends RootPacket>> TYPE_TO_ID = new Object2IntOpenHashMap<>();

	public static final String VERSION = "1.0.2";

	public static void init() {
		int id = 0;
		addMsg(id++, ClientboundCheckChannelVersionPacket.class, ClientboundCheckChannelVersionPacket::new);

		addMsg(id++, BlockRecipesManager.ClientboundRecipesPacket.class, BlockRecipesManager.ClientboundRecipesPacket::new);
		addMsg(id++, ClientboundAnimateCannonContraptionPacket.class, ClientboundAnimateCannonContraptionPacket::new);
		addMsg(id++, ClientboundUpdateContraptionPacket.class, ClientboundUpdateContraptionPacket::new);
		addMsg(id++, ClientboundPreciseMotionSyncPacket.class, ClientboundPreciseMotionSyncPacket::new);
		addMsg(id++, ServerboundCarriageWheelPacket.class, ServerboundCarriageWheelPacket::new);
		addMsg(id++, ServerboundFiringActionPacket.class, ServerboundFiringActionPacket::new);
		addMsg(id++, ServerboundSetFireRatePacket.class, ServerboundSetFireRatePacket::new);
		addMsg(id++, ServerboundSetFuzePacket.class, ServerboundSetFuzePacket::new);
		addMsg(id++, ClientboundMunitionPropertiesPacket.class, ClientboundMunitionPropertiesPacket::copyOf);
		addMsg(id++, ClientboundAutocannonMaterialPropertiesPacket.class, ClientboundAutocannonMaterialPropertiesPacket::new);
	}

	private static <T extends RootPacket> void addMsg(int id, Class<T> clazz, Function<FriendlyByteBuf, T> decoder) {
		TYPE_TO_ID.put(clazz, id);
		ID_TO_CONSTRUCTOR.put(id, decoder);
	}

	public static RootPacket constructPacket(FriendlyByteBuf buf, int id) {
		if (!ID_TO_CONSTRUCTOR.containsKey(id)) throw new IllegalStateException("Attempted to deserialize packet with illegal id: " + id);
		return ID_TO_CONSTRUCTOR.get(id).apply(buf);
	}

	public static void writeToBuf(RootPacket pkt, FriendlyByteBuf buf) {
		int id = TYPE_TO_ID.getOrDefault(pkt.getClass(), -1);
		if (id == -1) throw new IllegalStateException("Attempted to serialize packet with illegal id: " + id);
		buf.writeVarInt(id);
		pkt.rootEncode(buf);
	}

	public static void onPlayerJoin(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundCheckChannelVersionPacket(VERSION), player);
	}

}
