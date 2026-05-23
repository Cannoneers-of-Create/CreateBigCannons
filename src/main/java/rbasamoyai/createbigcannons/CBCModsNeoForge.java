package rbasamoyai.createbigcannons;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.loading.LoadingModList;
import rbasamoyai.createbigcannons.utils.CBCUtils;

// Copied from Create's Mods class --ritchie
public enum CBCModsNeoForge {
	COPYCATS,
	CURIOS,
	FRAMEDBLOCKS,
    SABLE,
    SIMULATED;

	private final String id;
    private final boolean isLoaded;

	CBCModsNeoForge() {
		this.id = name().toLowerCase(Locale.ROOT);
        this.isLoaded = LoadingModList.get().getModFileById(this.id) != null;
	}

	public String id() {
		return this.id;
	}

	public ResourceLocation resource(String path) {
		return CBCUtils.location(id, path);
	}

	public Block getBlock(String id) {
		return BuiltInRegistries.BLOCK.get(this.resource(id));
	}

	public boolean isLoaded() { return this.isLoaded; }

	public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
		return this.isLoaded() ? Optional.of(toRun.get().get()) : Optional.empty();
	}

	public void executeIfInstalled(Supplier<Runnable> toExecute) {
		if (isLoaded()) toExecute.get().run();
	}

}
