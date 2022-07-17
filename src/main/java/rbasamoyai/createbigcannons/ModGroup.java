package rbasamoyai.createbigcannons;

import com.simibubi.create.Create;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroup {
	
	public static final CreativeModeTab GROUP = new CreativeModeTab(Create.PALETTES_CREATIVE_TAB.getId() + 1, CreateBigCannons.MOD_ID) {
		@Override
		public ItemStack makeIcon() {
			return CBCBlocks.CAST_IRON_CANNON_BARREL.asStack();
		}
		
		public void fillItemList(NonNullList<ItemStack> list) {
			list.add(CBCBlocks.CANNON_MOUNT.asStack());
			list.add(CBCBlocks.YAW_CONTROLLER.asStack());
			list.add(CBCBlocks.CAST_IRON_CANNON_END.asStack());
			list.add(CBCBlocks.CAST_IRON_SLIDING_BREECH.asStack());
			list.add(CBCBlocks.CAST_IRON_CANNON_CHAMBER.asStack());
			list.add(CBCBlocks.CAST_IRON_CANNON_BARREL.asStack());
			list.add(CBCBlocks.CANNON_LOADER.asStack());
			list.add(CBCBlocks.RAM_HEAD.asStack());
			list.add(CBCBlocks.WORM_HEAD.asStack());
			list.add(CBCBlocks.SOLID_SHOT.asStack());
			list.add(CBCBlocks.POWDER_CHARGE.asStack());
		}
	};
	
	public static void register() {
		CreateBigCannons.registrate().creativeModeTab(() -> GROUP, "Create Big Cannons");
	}
	
}
