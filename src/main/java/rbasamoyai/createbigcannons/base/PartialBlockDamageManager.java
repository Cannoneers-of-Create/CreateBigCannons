package rbasamoyai.createbigcannons.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

import it.unimi.dsi.fastutil.objects.Object2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class PartialBlockDamageManager {

	private Map<ResourceKey<Level>, Object2FloatLinkedOpenHashMap<BlockPos>> blockDamage;
	private Map<ResourceKey<Level>, Object2ObjectMap<BlockPos, BlockState>> blockStates;

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
		Object2FloatLinkedOpenHashMap<BlockPos> levelSet = this.blockDamage.get(dimension);
		Object2ObjectMap<BlockPos, BlockState> blockStateSet = this.blockStates.get(dimension);
		if (levelSet.isEmpty()) {
			this.blockDamage.remove(dimension);
			this.blockStates.remove(dimension);
			return;
		}
		if (level.getGameTime() % 20 != 0) return;

		Object2FloatLinkedOpenHashMap<BlockPos> newSet = new Object2FloatLinkedOpenHashMap<>();
		for (Iterator<Object2FloatMap.Entry<BlockPos>> iter = levelSet.object2FloatEntrySet().fastIterator(); iter.hasNext(); ) {
			Object2FloatMap.Entry<BlockPos> entry = iter.next();
			BlockPos pos = entry.getKey();

			BlockState state = level.getChunkAt(pos).getBlockState(pos);
			float oldProgress = entry.getFloatValue();
			boolean blockChanged = blockStateSet != null && blockStateSet.containsKey(pos) && !blockStateSet.get(pos).equals(state);
			if (state.canBeReplaced() || !state.isSolid() || state.getDestroySpeed(level, pos) == -1 || blockChanged) {
				if (oldProgress > 0) level.destroyBlockProgress(-1, pos, -1);
				iter.remove();
			} else {
				if (blockStateSet == null) {
					blockStateSet = new Object2ObjectLinkedOpenHashMap<>();
					this.blockStates.put(dimension, blockStateSet);
				}
				blockStateSet.put(pos, state);
				float newProgress = oldProgress - 3;
				if (newProgress <= 0) {
					CBCUtils.sendCustomBlockDamage(level, pos, -1);
					iter.remove();
				} else {
					newSet.put(entry.getKey(), newProgress);
				}
				double toughnessRec = 1 / BlockArmorPropertiesHandler.getProperties(state).toughness(level, state, pos, true);
				int oldPart = (int) Math.floor(oldProgress * toughnessRec);
				int newPart = (int) Math.floor(newProgress * toughnessRec);
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

	public boolean damageBlock(BlockPos pos, float added, BlockState state, Level level) {
		return this.damageBlock(pos, added, state, level, PartialBlockDamageManager::destroyBlockDefault);
	}

	public static void voidBlock(Level level, BlockPos pos) {
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
	}

	public static void destroyBlockDefault(Level level, BlockPos pos) {
		level.destroyBlock(pos, false);
	}

	public boolean damageBlock(BlockPos pos, float added, BlockState state, Level level, BiConsumer<Level, BlockPos> onDestroy) {
		Object2FloatLinkedOpenHashMap<BlockPos> levelSet = this.blockDamage.computeIfAbsent(level.dimension(), k -> new Object2FloatLinkedOpenHashMap<>());

		float oldProgress = levelSet.getOrDefault(pos, 0f);
		levelSet.mergeFloat(pos, added, Float::sum);

		double toughnessRec = 1 / BlockArmorPropertiesHandler.getProperties(state).toughness(level, state, pos, true);
		int oldPart = (int) Math.floor(oldProgress * toughnessRec);
		int newPart = (int) Math.floor(levelSet.getFloat(pos) * toughnessRec);

		boolean destroyed = false;
		if (newPart >= 10) {
			if (!level.isClientSide())
				onDestroy.accept(level, pos);
			levelSet.removeFloat(pos);
			Map<BlockPos, BlockState> stateSet = this.blockStates.get(level.dimension());
			if (stateSet != null)
				stateSet.remove(pos);
			CBCUtils.sendCustomBlockDamage(level, pos, -1);
			destroyed = true;
		} else if (newPart - oldPart > 0) {
			CBCUtils.sendCustomBlockDamage(level, pos, newPart);
		}
		this.markDirty();
		return destroyed;
	}

}
