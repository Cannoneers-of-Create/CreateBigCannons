package rbasamoyai.createbigcannons.base;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PartialBlockDamageManager {

	private Map<BlockPos, Integer> blockDamage;

	private PartialBlockDamageSaveData savedata;

	public PartialBlockDamageManager() { cleanUp(); }

	public void playerLogin(Player player) {
		if (player instanceof ServerPlayer splayer) {
			this.loadDamageData(splayer.getServer());
		}
	}

	public void playerLogout(Player player) {}

	public void levelLoaded(LevelAccessor level) {
		MinecraftServer server = level.getServer();
		if (server == null || server.overworld() != level) return;
		cleanUp();
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
		if (level.dimension() != Level.OVERWORLD) return;

		if (this.blockDamage != null && !this.blockDamage.isEmpty() && level.getGameTime() % 20 == 0) {
			Map<BlockPos, Integer> newSet = new HashMap<>();
			for (Iterator<Map.Entry<BlockPos, Integer>> iter = this.blockDamage.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry<BlockPos, Integer> entry = iter.next();
				BlockPos pos = entry.getKey();

				BlockState state = level.getChunkAt(pos).getBlockState(pos);
				Material mat = state.getMaterial();
				int oldProgress = entry.getValue();
				if (mat.isReplaceable() || !mat.isSolid() || state.getDestroySpeed(level, pos) == -1) {
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
					double hardnessRec = 1 / BlockHardnessHandler.getHardness(state);
					int oldPart = (int) Math.floor(oldProgress * hardnessRec);
					int newPart = (int) Math.floor(newProgress * hardnessRec);
					if (oldPart - newPart > 0) level.destroyBlockProgress(-1, pos, newPart);
				}
			}
			this.blockDamage.putAll(newSet);
			this.markDirty();
		}
	}

	public void markDirty() {
		if (this.savedata != null) this.savedata.setDirty();
	}

	public void damageBlock(BlockPos pos, int added, BlockState state, Level level) {
		if (this.blockDamage != null) {
			int oldProgress = this.blockDamage.getOrDefault(pos, 0);
			this.blockDamage.merge(pos, added, Integer::sum);

			double hardnessRec = 1 / BlockHardnessHandler.getHardness(state);
			int oldPart = (int) Math.floor(oldProgress * hardnessRec);
			int newPart = (int) Math.floor(this.blockDamage.get(pos) * hardnessRec);

			if (newPart >= 10) {
				CBCCommonEvents.onCannonBreakBlock(level, pos);
				this.blockDamage.remove(pos);
			} else if (newPart - oldPart > 0) {
				level.destroyBlockProgress(-1, pos, newPart);
			}
			this.markDirty();
		}
	}

}
