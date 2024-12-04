package rbasamoyai.createbigcannons.network;

import java.util.function.Function;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler.ClientboundSyncBlockArmorPropertiesPacket;
import rbasamoyai.createbigcannons.cannon_control.config.CannonMountPropertiesHandler.ClientboundSyncCannonMountPropertiesPacket;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterialPropertiesHandler.ClientboundAutocannonMaterialPropertiesPacket;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.BigCannonBreechStrengthHandler.ClientboundBigCannonBreechStrengthPacket;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialPropertiesHandler.ClientboundBigCannonMaterialPropertiesPacket;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.casting.FluidCastingTimeHandler.ClientboundFluidCastingTimePacket;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilityHandler.ClientboundBigCannonPropellantPropertiesPacket;
import rbasamoyai.createbigcannons.munitions.config.DimensionMunitionPropertiesHandler.ClientboundSyncDimensionMunitionPropertiesPacket;
import rbasamoyai.createbigcannons.munitions.config.FluidDragHandler.ClientboundFluidDragPacket;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler.ClientboundMunitionPropertiesPacket;

public class CBCRootNetwork {

	private static final Int2ObjectMap<Function<FriendlyByteBuf, ? extends RootPacket>> ID_TO_CONSTRUCTOR = new Int2ObjectOpenHashMap<>();
	private static final Object2IntMap<Class<? extends RootPacket>> TYPE_TO_ID = new Object2IntOpenHashMap<>();

	public static final String VERSION = "12.2.0";

	public static void init() {
		int id = 0;
		addMsg(id++, ClientboundCheckChannelVersionPacket.class, ClientboundCheckChannelVersionPacket::new);

		addMsg(id++, BlockRecipesManager.ClientboundRecipesPacket.class, BlockRecipesManager.ClientboundRecipesPacket::new);
		addMsg(id++, ClientboundAnimateCannonContraptionPacket.class, ClientboundAnimateCannonContraptionPacket::new);
		addMsg(id++, ClientboundUpdateContraptionPacket.class, ClientboundUpdateContraptionPacket::new);
		addMsg(id++, ServerboundCarriageWheelPacket.class, ServerboundCarriageWheelPacket::new);
		addMsg(id++, ServerboundFiringActionPacket.class, ServerboundFiringActionPacket::new);
		addMsg(id++, ServerboundSetFireRatePacket.class, ServerboundSetFireRatePacket::new);
		addMsg(id++, ServerboundSetContainerValuePacket.class, ServerboundSetContainerValuePacket::new);
		addMsg(id++, ServerBoundSetProximityAnglePacket.class, ServerBoundSetProximityAnglePacket::new);
		addMsg(id++, ClientboundMunitionPropertiesPacket.class, ClientboundMunitionPropertiesPacket::copyOf);
		addMsg(id++, ClientboundAutocannonMaterialPropertiesPacket.class, ClientboundAutocannonMaterialPropertiesPacket::copyOf);
		addMsg(id++, ClientboundBigCannonMaterialPropertiesPacket.class, ClientboundBigCannonMaterialPropertiesPacket::copyOf);
		addMsg(id++, ClientboundBigCannonBreechStrengthPacket.class, ClientboundBigCannonBreechStrengthPacket::copyOf);
		addMsg(id++, ClientboundPreciseRotationSyncPacket.class, ClientboundPreciseRotationSyncPacket::new);
		addMsg(id++, ClientboundFluidCastingTimePacket.class, ClientboundFluidCastingTimePacket::copyOf);
		addMsg(id++, ServerboundUseWelderPacket.class, ServerboundUseWelderPacket::new);
		addMsg(id++, ClientboundBigCannonPropellantPropertiesPacket.class, ClientboundBigCannonPropellantPropertiesPacket::copyOf);
		addMsg(id++, ClientboundFluidBlobStackSyncPacket.class, ClientboundFluidBlobStackSyncPacket::new);
		addMsg(id++, ClientboundSyncCannonMountPropertiesPacket.class, ClientboundSyncCannonMountPropertiesPacket::copyOf);
		addMsg(id++, ClientboundSyncBlockArmorPropertiesPacket.class, ClientboundSyncBlockArmorPropertiesPacket::copyOf);
		addMsg(id++, ClientboundSyncDimensionMunitionPropertiesPacket.class, ClientboundSyncDimensionMunitionPropertiesPacket::copyOf);
		addMsg(id++, ClientboundCBCExplodePacket.class, ClientboundCBCExplodePacket::new);
		addMsg(id++, ClientboundFluidExplodePacket.class, ClientboundFluidExplodePacket::new);
		addMsg(id++, ClientboundNotifyTagReloadPacket.class, ClientboundNotifyTagReloadPacket::new);
		addMsg(id++, ClientboundPlayBlockHitEffectPacket.class, ClientboundPlayBlockHitEffectPacket::new);
		addMsg(id++, ClientboundSyncExtraEntityDataPacket.class, ClientboundSyncExtraEntityDataPacket::new);
		addMsg(id++, ClientboundSendCustomBreakProgressPacket.class, ClientboundSendCustomBreakProgressPacket::new);
		addMsg(id++, ClientboundFluidDragPacket.class, ClientboundFluidDragPacket::copyOf);
		addMsg(id++, ClientboundBlastSoundPacket.class, ClientboundBlastSoundPacket::new);
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
