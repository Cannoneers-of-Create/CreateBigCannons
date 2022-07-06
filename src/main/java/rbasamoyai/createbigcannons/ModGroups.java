package rbasamoyai.createbigcannons;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroups {
	
	public static final CreativeModeTab MOD_GROUP = new CreativeModeTab(CreateBigCannons.MOD_ID) {
		@Override
		public ItemStack makeIcon() {
			return CBCBlocks.CAST_IRON_CANNON_BARREL.asStack();
		}
	};
	
}
