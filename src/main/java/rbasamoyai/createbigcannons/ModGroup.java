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
		
		@Override
		public void fillItemList(NonNullList<ItemStack> list) {
			list.add(CBCBlocks.CANNON_MOUNT.asStack());
			list.add(CBCBlocks.YAW_CONTROLLER.asStack());
			list.add(CBCBlocks.LOG_CANNON_END.asStack());
			list.add(CBCBlocks.LOG_CANNON_CHAMBER.asStack());
			list.add(CBCBlocks.WROUGHT_IRON_CANNON_END.asStack());
			list.add(CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER.asStack());
			list.add(CBCBlocks.CAST_IRON_CANNON_END.asStack());
			list.add(CBCBlocks.CAST_IRON_SLIDING_BREECH.asStack());
			list.add(CBCBlocks.CAST_IRON_CANNON_CHAMBER.asStack());
			list.add(CBCBlocks.CAST_IRON_CANNON_BARREL.asStack());
			list.add(CBCBlocks.BRONZE_CANNON_END.asStack());
			list.add(CBCBlocks.BRONZE_SLIDING_BREECH.asStack());
			list.add(CBCBlocks.BRONZE_CANNON_CHAMBER.asStack());
			list.add(CBCBlocks.BRONZE_CANNON_BARREL.asStack());
			list.add(CBCBlocks.STEEL_SLIDING_BREECH.asStack());
			list.add(CBCBlocks.STEEL_SCREW_BREECH.asStack());
			list.add(CBCBlocks.STEEL_CANNON_CHAMBER.asStack());
			list.add(CBCBlocks.STEEL_CANNON_BARREL.asStack());
			list.add(CBCBlocks.NETHER_GUNMETAL_SCREW_BREECH.asStack());
			list.add(CBCBlocks.NETHER_GUNMETAL_CANNON_CHAMBER.asStack());
			list.add(CBCBlocks.NETHER_GUNMETAL_CANNON_BARREL.asStack());
			list.add(CBCBlocks.CANNON_LOADER.asStack());
			list.add(CBCBlocks.RAM_HEAD.asStack());
			list.add(CBCBlocks.WORM_HEAD.asStack());
			list.add(CBCBlocks.POWDER_CHARGE.asStack());
			list.add(CBCBlocks.SOLID_SHOT.asStack());
			list.add(CBCBlocks.BAG_OF_GRAPESHOT.asStack());
			list.add(CBCBlocks.HE_SHELL.asStack());
			list.add(CBCBlocks.SHRAPNEL_SHELL.asStack());
			list.add(CBCItems.IMPACT_FUZE.asStack());
			list.add(TimedFuzeItem.getCreativeTabItem(20));
			list.add(CBCBlocks.CASTING_SAND.asStack());
			list.add(CBCBlocks.VERY_SMALL_CAST_MOULD.asStack());
			list.add(CBCBlocks.SMALL_CAST_MOULD.asStack());
			list.add(CBCBlocks.MEDIUM_CAST_MOULD.asStack());
			list.add(CBCBlocks.LARGE_CAST_MOULD.asStack());
			list.add(CBCBlocks.VERY_LARGE_CAST_MOULD.asStack());
			list.add(CBCBlocks.CANNON_END_CAST_MOULD.asStack());
			list.add(CBCBlocks.UNBORED_SLIDING_BREECH_CAST_MOULD.asStack());
			list.add(new ItemStack(CBCFluids.MOLTEN_CAST_IRON.get().getBucket()));
			list.add(CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.asStack());
			list.add(CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.asStack());
			list.add(CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.asStack());
			list.add(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH.asStack());
			list.add(CBCItems.CAST_IRON_SLIDING_BREECHBLOCK.asStack());
			list.add(CBCItems.BRONZE_SLIDING_BREECHBLOCK.asStack());
			list.add(CBCItems.STEEL_SLIDING_BREECHBLOCK.asStack());
			list.add(CBCBlocks.CANNON_DRILL.asStack());
		}
	};
	
	public static void register() {
		CreateBigCannons.registrate().creativeModeTab(() -> GROUP, "Create Big Cannons");
	}
	
}
