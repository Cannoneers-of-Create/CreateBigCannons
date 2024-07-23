package rbasamoyai.createbigcannons.fabric;

import java.util.Optional;
import java.util.function.Supplier;

import com.simibubi.create.foundation.utility.Lang;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

// Copied from Create's Mods class --ritchie
public enum CBCModsFabric {
	COPYCATS,
	TRINKETS,
	COMPUTERCRAFT;

	private final String id;

	CBCModsFabric() {
		this.id = Lang.asId(name());
	}

	public String id() { return this.id; }

	public ResourceLocation resource(String path) { return CBCUtils.location(this.id, path); }

	public Block getBlock(String id) { return CBCRegistryUtils.getBlock(resource(id)); }

	public boolean isLoaded() { return FabricLoader.getInstance().isModLoaded(this.id); }

	public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
		return this.isLoaded() ? Optional.of(toRun.get().get()) : Optional.empty();
	}

	public void executeIfInstalled(Supplier<Runnable> toExecute) {
		if (this.isLoaded()) toExecute.get().run();
	}

}
