package rbasamoyai.createbigcannons.network;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class CBCRootNetwork {

	private static final Int2ObjectMap<Function<FriendlyByteBuf, ? extends RootPacket>> ID_TO_CONSTRUCTOR = new Int2ObjectOpenHashMap<>();
	private static final Object2IntMap<Class<? extends RootPacket>> TYPE_TO_ID = new Object2IntOpenHashMap<>();

	public static void init() {
		int id = 0;

		addMsg(id++, ClientboundAnimateCannonContraptionPacket.class, ClientboundAnimateCannonContraptionPacket::new);
		addMsg(id++, ClientboundUpdateContraptionPacket.class, ClientboundUpdateContraptionPacket::new);
		addMsg(id++, ServerboundCarriageWheelPacket.class, ServerboundCarriageWheelPacket::new);
		addMsg(id++, ServerboundFiringActionPacket.class, ServerboundFiringActionPacket::new);
		addMsg(id++, ServerboundProximityFuzePacket.class, ServerboundProximityFuzePacket::new);
		addMsg(id++, ServerboundSetFireRatePacket.class, ServerboundSetFireRatePacket::new);
		addMsg(id++, ServerboundTimedFuzePacket.class, ServerboundTimedFuzePacket::new);
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

}
