package rbasamoyai.createbigcannons;

import com.simibubi.create.Create;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroup {
	
	public static final CreativeModeTab GROUP = new CreativeModeTab(Create.PALETTES_CREATIVE_TAB.getId() + 1, CreateBigCannons.MOD_ID) {
		@Override
		public ItemStack makeIcon() {
			return CBCBlocks.CAST_IRON_CANNON_BARREL.asStack();
		}
	};
	
	public static void register() {
		CreateBigCannons.registrate().creativeModeTab(() -> GROUP, "Create Big Cannons");
	}
	
}
