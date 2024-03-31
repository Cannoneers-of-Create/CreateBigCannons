package rbasamoyai.createbigcannons.fabric;

import java.util.Optional;
import java.util.function.Supplier;

import com.simibubi.create.foundation.utility.Lang;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

// Copied from Create's Mods class --ritchie
public enum CBCModsFabric {
	COPYCATS;

	private final String id;

	CBCModsFabric() {
		this.id = Lang.asId(name());
	}

	public String id() { return this.id; }

	public ResourceLocation resource(String path) { return new ResourceLocation(this.id, path); }

	public Block getBlock(String id) { return Registry.BLOCK.get(resource(id)); }

	public boolean isLoaded() { return FabricLoader.getInstance().isModLoaded(this.id); }

	public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
		return this.isLoaded() ? Optional.of(toRun.get().get()) : Optional.empty();
	}

	public void executeIfInstalled(Supplier<Runnable> toExecute) {
		if (this.isLoaded()) toExecute.get().run();
	}

}
