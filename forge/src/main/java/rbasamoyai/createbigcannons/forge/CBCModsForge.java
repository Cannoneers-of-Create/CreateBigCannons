package rbasamoyai.createbigcannons.forge;

import java.util.Optional;
import java.util.function.Supplier;

import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import rbasamoyai.createbigcannons.utils.CBCUtils;

// Copied from Create's Mods class --ritchie
public enum CBCModsForge {
	COPYCATS,
	CURIOS,
	FRAMEDBLOCKS;

	private final String id;

	CBCModsForge() {
		this.id = Lang.asId(name());
	}

	public String id() { return this.id; }

	public ResourceLocation resource(String path) { return CBCUtils.location(id, path); }
	public Block getBlock(String id) { return ForgeRegistries.BLOCKS.getValue(this.resource(id)); }
	public boolean isLoaded() { return ModList.get().isLoaded(this.id); }

	public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
		return this.isLoaded() ? Optional.of(toRun.get().get()) : Optional.empty();
	}

	public void executeIfInstalled(Supplier<Runnable> toExecute) {
		if (isLoaded()) toExecute.get().run();
	}

}
