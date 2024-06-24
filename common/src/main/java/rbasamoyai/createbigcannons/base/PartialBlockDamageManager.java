package rbasamoyai.createbigcannons.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class PartialBlockDamageManager {

	private Map<ResourceKey<Level>, Map<BlockPos, Integer>> blockDamage;
	private Map<ResourceKey<Level>, Map<BlockPos, BlockState>> blockStates;

	private PartialBlockDamageSaveData savedata;

	public PartialBlockDamageManager() {
		this.cleanUp();
	}

	public void playerLogin(Player player) {
		if (player instanceof ServerPlayer splayer) {
			this.loadDamageData(splayer.getServer());
		}
	}

	public void playerLogout(Player player) {
	}

	public void levelLoaded(LevelAccessor level) {
		MinecraftServer server = level.getServer();
		if (server == null) return;
		this.cleanUp();
		this.savedata = null;
		this.loadDamageData(server);
	}

	private void cleanUp() {
		this.blockDamage = new HashMap<>();
	}

	private void loadDamageData(MinecraftServer server) {
		if (this.savedata != null) return;
		this.savedata = PartialBlockDamageSaveData.load(server);
		this.blockDamage = this.savedata.getBlockDamage();
		this.blockStates = new Object2ObjectLinkedOpenHashMap<>();
	}

	public void tick(Level level) {
		ResourceKey<Level> dimension = level.dimension();
		if (!this.blockDamage.containsKey(dimension))
			return;
		Map<BlockPos, Integer> levelSet = this.blockDamage.get(dimension);
		Map<BlockPos, BlockState> blockStateSet = this.blockStates.get(dimension);
		if (levelSet.isEmpty()) {
			this.blockDamage.remove(dimension);
			this.blockStates.remove(dimension);
			return;
		}
		if (level.getGameTime() % 20 != 0) return;

		Map<BlockPos, Integer> newSet = new Object2ObjectLinkedOpenHashMap<>();
		for (Iterator<Map.Entry<BlockPos, Integer>> iter = levelSet.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry<BlockPos, Integer> entry = iter.next();
			BlockPos pos = entry.getKey();

			BlockState state = level.getChunkAt(pos).getBlockState(pos);
			int oldProgress = entry.getValue();
			Material mat = state.getMaterial();
			boolean blockChanged = blockStateSet != null && blockStateSet.containsKey(pos) && !blockStateSet.get(pos).equals(state);
			if (mat.isReplaceable() || !mat.isSolid() || state.getDestroySpeed(level, pos) == -1 || blockChanged) {
				CBCUtils.sendCustomBlockDamage(level, pos, -1);
				if (blockStateSet != null)
					blockStateSet.remove(pos);
				iter.remove();
			} else {
				if (blockStateSet == null) {
					blockStateSet = new Object2ObjectLinkedOpenHashMap<>();
					this.blockStates.put(dimension, blockStateSet);
				}
				blockStateSet.put(pos, state);
				int newProgress = oldProgress - 3;
				if (newProgress <= 0) {
					CBCUtils.sendCustomBlockDamage(level, pos, -1);
					iter.remove();
				} else {
					newSet.put(entry.getKey(), newProgress);
				}
				double hardnessRec = 1 / BlockArmorPropertiesHandler.getProperties(state).hardness(level, state, pos, true);
				int oldPart = (int) Math.floor(oldProgress * hardnessRec);
				int newPart = (int) Math.floor(newProgress * hardnessRec);
				if (oldPart - newPart > 0)
					CBCUtils.sendCustomBlockDamage(level, pos, newPart);
			}
		}

		levelSet.putAll(newSet);
		if (blockStateSet != null && blockStateSet.isEmpty())
			this.blockStates.remove(dimension);
		this.markDirty();
	}

	public void markDirty() {
		if (this.savedata != null) this.savedata.setDirty();
	}

	public void damageBlock(BlockPos pos, int added, BlockState state, Level level) {
		this.damageBlock(pos, added, state, level, PartialBlockDamageManager::destroyBlockDefault);
	}

	public static void voidBlock(Level level, BlockPos pos) {
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
	}

	public static void destroyBlockDefault(Level level, BlockPos pos) {
		level.destroyBlock(pos, false);
	}

	public void damageBlock(BlockPos pos, int added, BlockState state, Level level, BiConsumer<Level, BlockPos> onDestroy) {
		Map<BlockPos, Integer> levelSet = this.blockDamage.computeIfAbsent(level.dimension(), k -> new Object2ObjectLinkedOpenHashMap<>());


		int oldProgress = levelSet.getOrDefault(pos, 0);
		levelSet.merge(pos, added, Integer::sum);

		double hardnessRec = 1 / BlockArmorPropertiesHandler.getProperties(state).hardness(level, state, pos, true);
		int oldPart = (int) Math.floor(oldProgress * hardnessRec);
		int newPart = (int) Math.floor(levelSet.get(pos) * hardnessRec);

		if (newPart >= 10) {
			if (!level.isClientSide())
				onDestroy.accept(level, pos);
			levelSet.remove(pos);
			Map<BlockPos, BlockState> stateSet = this.blockStates.get(level.dimension());
			if (stateSet != null)
				stateSet.remove(pos);
			CBCUtils.sendCustomBlockDamage(level, pos, -1);
		} else if (newPart - oldPart > 0) {
			CBCUtils.sendCustomBlockDamage(level, pos, newPart);
		}
		this.markDirty();
	}

}
