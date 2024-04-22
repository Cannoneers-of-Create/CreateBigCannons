package rbasamoyai.createbigcannons.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;

public class PartialBlockDamageManager {

	private Map<ResourceKey<Level>, Map<BlockPos, Integer>> blockDamage;

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
	}

	public void tick(Level level) {
		ResourceKey<Level> dimension = level.dimension();
		if (!this.blockDamage.containsKey(dimension)) return;
		Map<BlockPos, Integer> levelSet = this.blockDamage.get(dimension);
		if (levelSet.isEmpty()) {
			this.blockDamage.remove(dimension);
			return;
		}
		if (level.getGameTime() % 20 != 0) return;

		Map<BlockPos, Integer> newSet = new HashMap<>();
		for (Iterator<Map.Entry<BlockPos, Integer>> iter = levelSet.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry<BlockPos, Integer> entry = iter.next();
			BlockPos pos = entry.getKey();

			BlockState state = level.getChunkAt(pos).getBlockState(pos);
			int oldProgress = entry.getValue();
			if (state.canBeReplaced() || !state.isSolid() || state.getDestroySpeed(level, pos) == -1) {
				if (oldProgress > 0) level.destroyBlockProgress(-1, pos, -1);
				iter.remove();
			} else {
				int newProgress = oldProgress - 3;
				if (newProgress <= 0) {
					level.destroyBlockProgress(-1, pos, -1);
					iter.remove();
				} else {
					newSet.put(entry.getKey(), newProgress);
				}
				double hardnessRec = 1 / BlockArmorPropertiesHandler.getProperties(state).hardness(level, state, pos, true);
				int oldPart = (int) Math.floor(oldProgress * hardnessRec);
				int newPart = (int) Math.floor(newProgress * hardnessRec);
				if (oldPart - newPart > 0) level.destroyBlockProgress(-1, pos, newPart);
			}
		}

		levelSet.putAll(newSet);
		this.markDirty();
	}

	public void markDirty() {
		if (this.savedata != null) this.savedata.setDirty();
	}

	public void damageBlock(BlockPos pos, int added, BlockState state, Level level) {
		Map<BlockPos, Integer> levelSet = this.blockDamage.computeIfAbsent(level.dimension(), k -> new HashMap<>());

		int oldProgress = levelSet.getOrDefault(pos, 0);
		levelSet.merge(pos, added, Integer::sum);

		double hardnessRec = 1 / BlockArmorPropertiesHandler.getProperties(state).hardness(level, state, pos, true);
		int oldPart = (int) Math.floor(oldProgress * hardnessRec);
		int newPart = (int) Math.floor(levelSet.get(pos) * hardnessRec);

		if (newPart >= 10) {
			if (!level.isClientSide()) level.destroyBlock(pos, false);
			levelSet.remove(pos);
		} else if (newPart - oldPart > 0) {
			level.destroyBlockProgress(-1, pos, newPart);
		}
		this.markDirty();
	}

}
