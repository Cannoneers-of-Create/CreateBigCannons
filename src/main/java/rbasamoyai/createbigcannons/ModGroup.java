package rbasamoyai.createbigcannons;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeItem;

public class ModGroup {
	
	public static final CreativeModeTab GROUP = new CreativeModeTab(CreateBigCannons.MOD_ID) {
		@Override
		public ItemStack makeIcon() {
			return CBCBlocks.SOLID_SHOT.asStack();
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
			list.add(CBCBlocks.POWDER_CHARGE.asStack());
			list.add(CBCBlocks.SOLID_SHOT.asStack());
			list.add(CBCBlocks.HE_SHELL.asStack());
			list.add(CBCBlocks.SHRAPNEL_SHELL.asStack());
			list.add(CBCItems.IMPACT_FUZE.asStack());
			list.add(TimedFuzeItem.getCreativeTabItem(20));
		}
	};
	
	public static void register() {
		CreateBigCannons.registrate().creativeModeTab(() -> GROUP, "Create Big Cannons");
	}
	
}
