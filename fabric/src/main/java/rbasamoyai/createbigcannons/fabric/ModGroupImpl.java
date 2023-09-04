package rbasamoyai.createbigcannons.fabric;

import java.util.function.Supplier;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.ModGroup;

public class ModGroupImpl {

	public static Supplier<CreativeModeTab> wrapGroup(String id, Supplier<CreativeModeTab> sup) {
		CreativeModeTab tab = sup.get();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModGroup.makeKey(id), tab);
		return sup;
	}

	public static CreativeModeTab.Builder createBuilder() {
		return FabricItemGroup.builder();
	}

	public static void useModTab(ResourceKey<CreativeModeTab> key) { CreateBigCannons.REGISTRATE.useCreativeTab(key); }

}
