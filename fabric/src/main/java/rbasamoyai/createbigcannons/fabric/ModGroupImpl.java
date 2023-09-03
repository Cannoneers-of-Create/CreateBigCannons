package rbasamoyai.createbigcannons.fabric;

import java.util.function.Supplier;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import rbasamoyai.createbigcannons.ModGroup;

public class ModGroupImpl {

	public static Supplier<CreativeModeTab> wrapGroup(Supplier<CreativeModeTab> sup) {
		CreativeModeTab tab = sup.get();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ModGroup.MAIN_TAB_KEY, tab);
		return sup;
	}

	public static CreativeModeTab.Builder createBuilder() {
		return FabricItemGroup.builder();
	}

}
