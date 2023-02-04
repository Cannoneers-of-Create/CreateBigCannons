package rbasamoyai.createbigcannons.base;

import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import rbasamoyai.createbigcannons.CreateBigCannons;

import java.util.HashMap;
import java.util.Map;

public class PartialBlockDamageSaveData extends SavedData {

	private Map<BlockPos, Integer> blockDamage = new HashMap<>();

	private PartialBlockDamageSaveData() {}

	@Override
	public CompoundTag save(CompoundTag tag) {
		CreateBigCannons.LOGGER.info("Saving Create Big Cannons damage data...");
		tag.put("BlockDamage", NBTHelper.writeCompoundList(this.blockDamage.entrySet(), e -> {
			CompoundTag tag1 = new CompoundTag();
			tag1.put("Pos", NbtUtils.writeBlockPos(e.getKey()));
			tag1.putInt("Damage", e.getValue());
			return tag1;
		}));
		return tag;
	}

	public Map<BlockPos, Integer> getBlockDamage() { return this.blockDamage; }

	private static PartialBlockDamageSaveData load(CompoundTag tag) {
		PartialBlockDamageSaveData savedata = new PartialBlockDamageSaveData();
		ListTag values = tag.getList("BlockDamage", Tag.TAG_COMPOUND);
		int len = Math.min(values.size(), 32768);
		for (int i = 0; i < len; ++i) {
			CompoundTag entry = values.getCompound(i);
			savedata.blockDamage.put(NbtUtils.readBlockPos(entry.getCompound("Pos")), entry.getInt("Damage"));
		}
		return savedata;
	}

	public static PartialBlockDamageSaveData load(MinecraftServer server) {
		return server.overworld()
				.getDataStorage()
				.computeIfAbsent(PartialBlockDamageSaveData::load, PartialBlockDamageSaveData::new, "createbigcannons_block_damage");
	}

}
