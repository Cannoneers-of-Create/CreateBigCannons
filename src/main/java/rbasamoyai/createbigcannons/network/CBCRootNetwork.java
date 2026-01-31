package rbasamoyai.createbigcannons.network;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler.ClientboundSyncBlockArmorPropertiesPacket;
import rbasamoyai.createbigcannons.cannon_control.config.CannonMountPropertiesHandler.ClientboundSyncCannonMountPropertiesPacket;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterialPropertiesHandler.ClientboundAutocannonMaterialPropertiesPacket;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.BigCannonBreechStrengthHandler.ClientboundBigCannonBreechStrengthPacket;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialPropertiesHandler.ClientboundBigCannonMaterialPropertiesPacket;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.casting.FluidCastingTimeHandler.ClientboundFluidCastingTimePacket;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilityHandler.ClientboundBigCannonPropellantCompatibilitiesPacket;
import rbasamoyai.createbigcannons.munitions.config.DimensionMunitionPropertiesHandler.ClientboundSyncDimensionMunitionPropertiesPacket;
import rbasamoyai.createbigcannons.munitions.config.FluidDragHandler.ClientboundFluidDragPacket;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler.ClientboundMunitionPropertiesPacket;

public class CBCRootNetwork {

	private static final Int2ObjectMap<StreamCodec<RegistryFriendlyByteBuf, ? extends RootPacket>> ID_TO_STREAM_CODEC = new Int2ObjectOpenHashMap<>();
	private static final Object2IntMap<Class<? extends RootPacket>> TYPE_TO_ID = new Object2IntOpenHashMap<>();

	public static final String VERSION = "15.0.0";

    public static final StreamCodec<RegistryFriendlyByteBuf, RootPacket> PACKET_STREAM_CODEC = ByteBufCodecs.VAR_INT.<RegistryFriendlyByteBuf>cast()
        .dispatch(CBCRootNetwork::getPacketId, CBCRootNetwork::getStreamCodec);

	public static void init() {
		int id = 0;
		addMsg(id++, ClientboundCheckChannelVersionPacket.class, ClientboundCheckChannelVersionPacket.STREAM_CODEC);

		addMsg(id++, BlockRecipesManager.ClientboundBlockRecipesPacket.class, BlockRecipesManager.ClientboundBlockRecipesPacket.STREAM_CODEC);
		addMsg(id++, ClientboundAnimateCannonContraptionPacket.class, ClientboundAnimateCannonContraptionPacket.STREAM_CODEC);
		addMsg(id++, ClientboundUpdateContraptionPacket.class, ClientboundUpdateContraptionPacket.STREAM_CODEC);
		addMsg(id++, ServerboundCarriageWheelPacket.class, ServerboundCarriageWheelPacket.STREAM_CODEC);
		addMsg(id++, ServerboundFiringActionPacket.class, ServerboundFiringActionPacket.STREAM_CODEC);
		addMsg(id++, ServerboundSetFireRatePacket.class, ServerboundSetFireRatePacket.STREAM_CODEC);
		addMsg(id++, ServerboundSetContainerValuePacket.class, ServerboundSetContainerValuePacket.STREAM_CODEC);
		addMsg(id++, ClientboundMunitionPropertiesPacket.class, ClientboundMunitionPropertiesPacket.STREAM_CODEC);
		addMsg(id++, ClientboundAutocannonMaterialPropertiesPacket.class, ClientboundAutocannonMaterialPropertiesPacket.STREAM_CODEC);
		addMsg(id++, ClientboundBigCannonMaterialPropertiesPacket.class, ClientboundBigCannonMaterialPropertiesPacket.STREAM_CODEC);
		addMsg(id++, ClientboundBigCannonBreechStrengthPacket.class, ClientboundBigCannonBreechStrengthPacket.STREAM_CODEC);
		addMsg(id++, ClientboundPreciseRotationSyncPacket.class, ClientboundPreciseRotationSyncPacket.STREAM_CODEC);
		addMsg(id++, ClientboundFluidCastingTimePacket.class, ClientboundFluidCastingTimePacket.STREAM_CODEC);
		addMsg(id++, ServerboundUseWelderPacket.class, ServerboundUseWelderPacket.STREAM_CODEC);
		addMsg(id++, ClientboundBigCannonPropellantCompatibilitiesPacket.class, ClientboundBigCannonPropellantCompatibilitiesPacket.STREAM_CODEC);
		addMsg(id++, ClientboundFluidBlobStackSyncPacket.class, ClientboundFluidBlobStackSyncPacket.STREAM_CODEC);
		addMsg(id++, ClientboundSyncCannonMountPropertiesPacket.class, ClientboundSyncCannonMountPropertiesPacket.STREAM_CODEC);
		addMsg(id++, ClientboundSyncBlockArmorPropertiesPacket.class, ClientboundSyncBlockArmorPropertiesPacket.STREAM_CODEC);
		addMsg(id++, ClientboundSyncDimensionMunitionPropertiesPacket.class, ClientboundSyncDimensionMunitionPropertiesPacket.STREAM_CODEC);
		addMsg(id++, ClientboundCBCExplodePacket.class, ClientboundCBCExplodePacket.STREAM_CODEC);
		addMsg(id++, ClientboundFluidExplodePacket.class, ClientboundFluidExplodePacket.STREAM_CODEC);
		addMsg(id++, ClientboundNotifyTagReloadPacket.class, ClientboundNotifyTagReloadPacket.STREAM_CODEC);
		addMsg(id++, ClientboundPlayBlockHitEffectPacket.class, ClientboundPlayBlockHitEffectPacket.STREAM_CODEC);
		addMsg(id++, ClientboundSendCustomBreakProgressPacket.class, ClientboundSendCustomBreakProgressPacket.STREAM_CODEC);
		addMsg(id++, ClientboundFluidDragPacket.class, ClientboundFluidDragPacket.STREAM_CODEC);
		addMsg(id++, ClientboundBlastSoundPacket.class, ClientboundBlastSoundPacket.STREAM_CODEC);
		addMsg(id++, ServerboundSetFixedCannonMountValuePacket.class, ServerboundSetFixedCannonMountValuePacket.STREAM_CODEC);
	}

	private static <T extends RootPacket> void addMsg(int id, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
		TYPE_TO_ID.put(clazz, id);
		ID_TO_STREAM_CODEC.put(id, streamCodec);
	}

    public static int getPacketId(RootPacket pkt) {
        int id = TYPE_TO_ID.getOrDefault(pkt.getClass(), -1);
        if (id == -1)
            throw new IllegalStateException("Attempted to serialize packet with illegal id: " + id);
        return id;
    }

    public static StreamCodec<RegistryFriendlyByteBuf, ? extends RootPacket> getStreamCodec(int id) {
        if (!ID_TO_STREAM_CODEC.containsKey(id))
            throw new IllegalStateException("Attempted to deserialize packet with illegal id: " + id);
        return ID_TO_STREAM_CODEC.get(id);
    }

	public static void onPlayerJoin(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundCheckChannelVersionPacket(VERSION), player);
	}

}
