package rbasamoyai.createbigcannons.mixin.client;

import java.util.Iterator;
import java.util.SortedSet;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import rbasamoyai.createbigcannons.remix.CustomBlockDamageDisplay;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements CustomBlockDamageDisplay {

	@Shadow protected abstract void removeProgress(BlockDestructionProgress progress);

	@Shadow private int ticks;
	@Shadow @Final private Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;

	@Unique private final Long2ObjectOpenHashMap<BlockDestructionProgress> createbigcannons$trackedProgresses = new Long2ObjectOpenHashMap<>();

	@Override
	public void createbigcannons$trackCustomProgress(BlockPos pos, int damage) {
		long compressed = pos.asLong();
		if (damage >= 0 && damage < 10) {
			BlockDestructionProgress blockDestructionProgress = this.createbigcannons$trackedProgresses.get(compressed);
			if (blockDestructionProgress != null)
				this.removeProgress(blockDestructionProgress);

			if (blockDestructionProgress == null
				|| blockDestructionProgress.getPos().getX() != pos.getX()
				|| blockDestructionProgress.getPos().getY() != pos.getY()
				|| blockDestructionProgress.getPos().getZ() != pos.getZ()) {
				blockDestructionProgress = new BlockDestructionProgress(-1, pos);
				this.createbigcannons$trackedProgresses.put(compressed, blockDestructionProgress);
			}

			blockDestructionProgress.setProgress(damage);
			blockDestructionProgress.updateTick(this.ticks);
			SortedSet<BlockDestructionProgress> set = this.destructionProgress.get(compressed);
			if (set == null)
				this.destructionProgress.put(compressed, set = Sets.newTreeSet());
			set.add(blockDestructionProgress);
		} else {
			BlockDestructionProgress blockDestructionProgress = this.createbigcannons$trackedProgresses.remove(compressed);
			if (blockDestructionProgress != null)
				this.removeProgress(blockDestructionProgress);
		}
	}

	@WrapMethod(method = "tick")
	private void createbigcannons$tick(Operation<Void> original) {
        original.call();
		if (this.ticks % 20 == 0) {
			for (Iterator<BlockDestructionProgress> iter = this.createbigcannons$trackedProgresses.values().iterator(); iter.hasNext(); ) {
				BlockDestructionProgress blockDestructionProgress = iter.next();
				if (this.ticks - blockDestructionProgress.getUpdatedRenderTick() > 400) {
					iter.remove();
					this.removeProgress(blockDestructionProgress);
				}
			}
		}
	}

}
