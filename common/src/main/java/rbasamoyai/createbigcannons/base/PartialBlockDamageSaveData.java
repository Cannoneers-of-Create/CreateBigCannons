package rbasamoyai.createbigcannons.base;

import java.util.HashMap;
import java.util.Map;

import com.simibubi.create.foundation.utility.NBTHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class PartialBlockDamageSaveData extends SavedData {

	private static final int MAX_DAMAGES_PER_DIMENSION = 32768;

	private Map<ResourceKey<Level>, Map<BlockPos, Integer>> blockDamage = new HashMap<>();

	private PartialBlockDamageSaveData() {}

	@Override
	public CompoundTag save(CompoundTag tag) {
		CompoundTag blockDamage = new CompoundTag();
		for (Map.Entry<ResourceKey<Level>, Map<BlockPos, Integer>> entry : this.blockDamage.entrySet()) {
			blockDamage.put(entry.getKey().location().toString(), NBTHelper.writeCompoundList(entry.getValue().entrySet(), e -> {
				CompoundTag tag1 = new CompoundTag();
				tag1.put("Pos", NbtUtils.writeBlockPos(e.getKey()));
				tag1.putInt("Damage", e.getValue());
				return tag1;
			}));
		}
		tag.put("BlockDamage", blockDamage);
		return tag;
	}

	public Map<ResourceKey<Level>, Map<BlockPos, Integer>> getBlockDamage() { return this.blockDamage; }

	private static PartialBlockDamageSaveData load(CompoundTag tag) {
		PartialBlockDamageSaveData savedata = new PartialBlockDamageSaveData();
		CompoundTag values = tag.getCompound("BlockDamage");

		for (String key : values.getAllKeys()) {
			ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(key));
			ListTag damagesTag = values.getList(key, Tag.TAG_COMPOUND);
			Map<BlockPos, Integer> damages = new HashMap<>(damagesTag.size());

			int len = Math.min(damagesTag.size(), MAX_DAMAGES_PER_DIMENSION);
			for (int i = 0; i < len; ++i) {
				CompoundTag entry = damagesTag.getCompound(i);
				damages.put(NbtUtils.readBlockPos(entry.getCompound("Pos")), entry.getInt("Damage"));
			}
			savedata.blockDamage.put(dimensionKey, damages);
		}
		return savedata;
	}

	public static PartialBlockDamageSaveData load(MinecraftServer server) {
		return server.overworld()
				.getDataStorage()
				.computeIfAbsent(PartialBlockDamageSaveData::load, PartialBlockDamageSaveData::new, "createbigcannons_block_damage");
	}

}
