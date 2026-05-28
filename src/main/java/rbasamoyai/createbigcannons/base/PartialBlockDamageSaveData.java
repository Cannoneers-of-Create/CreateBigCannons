package rbasamoyai.createbigcannons.base;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class PartialBlockDamageSaveData extends SavedData {

	private static final int MAX_DAMAGES_PER_DIMENSION = 32768;

	private final Object2ObjectLinkedOpenHashMap<ResourceKey<Level>, Object2FloatLinkedOpenHashMap<BlockPos>> blockDamage = new Object2ObjectLinkedOpenHashMap<>();

	private PartialBlockDamageSaveData() {
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
		CompoundTag blockDamage = new CompoundTag();
		for (Map.Entry<ResourceKey<Level>, Object2FloatLinkedOpenHashMap<BlockPos>> entry : this.blockDamage.object2ObjectEntrySet()) {
			blockDamage.put(entry.getKey().location().toString(), NBTHelper.writeCompoundList(entry.getValue().object2FloatEntrySet(), e -> {
				CompoundTag tag1 = new CompoundTag();
				tag1.put("Pos", NbtUtils.writeBlockPos(e.getKey()));
				tag1.putFloat("Damage", e.getFloatValue());
				return tag1;
			}));
		}
		tag.put("BlockDamage", blockDamage);
		return tag;
	}

	public Map<ResourceKey<Level>, Object2FloatLinkedOpenHashMap<BlockPos>> getBlockDamage() {
		return this.blockDamage;
	}

	private static PartialBlockDamageSaveData load(CompoundTag tag, HolderLookup.Provider registries) {
		PartialBlockDamageSaveData savedata = new PartialBlockDamageSaveData();
		CompoundTag values = tag.getCompound("BlockDamage");

		for (String key : values.getAllKeys()) {
			ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, CBCUtils.location(key));
			ListTag damagesTag = values.getList(key, Tag.TAG_COMPOUND);
			Object2FloatLinkedOpenHashMap<BlockPos> damages = new Object2FloatLinkedOpenHashMap<>(damagesTag.size());

			int len = Math.min(damagesTag.size(), MAX_DAMAGES_PER_DIMENSION);
			for (int i = 0; i < len; ++i) {
				CompoundTag entry = damagesTag.getCompound(i);
				damages.put(NbtUtils.readBlockPos(entry, "Pos").get(), entry.getFloat("Damage"));
			}
			savedata.blockDamage.put(dimensionKey, damages);
		}
		return savedata;
	}

	public static PartialBlockDamageSaveData load(MinecraftServer server) {
		return server.overworld()
			.getDataStorage()
			.computeIfAbsent(new Factory<>(PartialBlockDamageSaveData::new, PartialBlockDamageSaveData::load, null), "createbigcannons_block_damage");
	}

}
