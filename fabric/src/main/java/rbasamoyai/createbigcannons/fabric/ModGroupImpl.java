package rbasamoyai.createbigcannons.fabric;

import java.util.function.Supplier;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class ModGroupImpl {

	public static Supplier<CreativeModeTab> wrapGroup(Supplier<CreativeModeTab> sup) {
		ResourceLocation id = CreateBigCannons.resource("base");
		ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
		CreativeModeTab tab = sup.get();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
		return sup;
	}

	public static CreativeModeTab.Builder createBuilder() {
		return FabricItemGroup.builder();
	}

}
