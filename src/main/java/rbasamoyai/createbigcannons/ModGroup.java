package rbasamoyai.createbigcannons;

import java.util.Arrays;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeItem;

public class ModGroup {
	
	public static final CreativeModeTab GROUP = new CreativeModeTab(CreateBigCannons.MOD_ID) {
		@Override
		public ItemStack makeIcon() {
			return CBCBlocks.SOLID_SHOT.asStack();
		}
		
		@Override
		public void fillItemList(NonNullList<ItemStack> list) {
			list.addAll(Arrays.asList(
				CBCBlocks.CANNON_MOUNT.asStack(),
				CBCBlocks.YAW_CONTROLLER.asStack(),
				
				CBCBlocks.LOG_CANNON_END.asStack(),
				CBCBlocks.LOG_CANNON_CHAMBER.asStack(),
				
				CBCBlocks.WROUGHT_IRON_CANNON_END.asStack(),
				CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER.asStack(),
				
				CBCBlocks.CAST_IRON_CANNON_END.asStack(),
				CBCBlocks.CAST_IRON_SLIDING_BREECH.asStack(),
				CBCBlocks.CAST_IRON_CANNON_CHAMBER.asStack(),
				CBCBlocks.CAST_IRON_CANNON_BARREL.asStack(),
				
				CBCBlocks.BRONZE_CANNON_END.asStack(),
				CBCBlocks.BRONZE_SLIDING_BREECH.asStack(),
				CBCBlocks.BRONZE_CANNON_CHAMBER.asStack(),
				CBCBlocks.BRONZE_CANNON_BARREL.asStack(),
				
				CBCBlocks.STEEL_SLIDING_BREECH.asStack(),
				CBCBlocks.STEEL_SCREW_BREECH.asStack(),
				CBCBlocks.THICK_STEEL_CANNON_CHAMBER.asStack(),
				CBCBlocks.BUILT_UP_STEEL_CANNON_CHAMBER.asStack(),
				CBCBlocks.STEEL_CANNON_CHAMBER.asStack(),
				CBCBlocks.BUILT_UP_STEEL_CANNON_BARREL.asStack(),
				CBCBlocks.STEEL_CANNON_BARREL.asStack(),
				
				CBCBlocks.NETHERSTEEL_SCREW_BREECH.asStack(),
				CBCBlocks.THICK_NETHERSTEEL_CANNON_CHAMBER.asStack(),
				CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_CHAMBER.asStack(),
				CBCBlocks.NETHERSTEEL_CANNON_CHAMBER.asStack(),
				CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_BARREL.asStack(),
				CBCBlocks.NETHERSTEEL_CANNON_BARREL.asStack(),
				
				CBCBlocks.CANNON_LOADER.asStack(),
				CBCBlocks.RAM_HEAD.asStack(),
				CBCBlocks.WORM_HEAD.asStack(),
				CBCBlocks.POWDER_CHARGE.asStack(),
				CBCBlocks.SOLID_SHOT.asStack(),
				CBCBlocks.MORTAR_STONE.asStack(),
				CBCBlocks.BAG_OF_GRAPESHOT.asStack(),
				CBCBlocks.HE_SHELL.asStack(),
				CBCBlocks.AP_SHELL.asStack(),
				CBCBlocks.SHRAPNEL_SHELL.asStack(),
				CBCBlocks.FLUID_SHELL.asStack(),
				CBCItems.IMPACT_FUZE.asStack(),
				TimedFuzeItem.getCreativeTabItem(20),
				ProximityFuzeItem.getCreativeTabItem(1),
				
				CBCItems.CANNON_CRAFTING_WAND.asStack(),
				CBCBlocks.CASTING_SAND.asStack(),
				CBCBlocks.VERY_SMALL_CAST_MOULD.asStack(),
				CBCBlocks.SMALL_CAST_MOULD.asStack(),
				CBCBlocks.MEDIUM_CAST_MOULD.asStack(),
				CBCBlocks.LARGE_CAST_MOULD.asStack(),
				CBCBlocks.VERY_LARGE_CAST_MOULD.asStack(),
				CBCBlocks.CANNON_END_CAST_MOULD.asStack(),
				CBCBlocks.SLIDING_BREECH_CAST_MOULD.asStack(),
				CBCBlocks.SCREW_BREECH_CAST_MOULD.asStack(),
				
				CBCBlocks.CANNON_DRILL.asStack(),
				CBCBlocks.CANNON_BUILDER.asStack(),				
				
				new ItemStack(CBCFluids.MOLTEN_CAST_IRON.get().getBucket()),
				CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.asStack(),
				CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.asStack(),
				CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.asStack(),
				CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH.asStack(),
				CBCItems.CAST_IRON_SLIDING_BREECHBLOCK.asStack(),
				
				new ItemStack(CBCFluids.MOLTEN_BRONZE.get().getBucket()),
				CBCBlocks.UNBORED_BRONZE_CANNON_BARREL.asStack(),
				CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER.asStack(),
				CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH.asStack(),
				CBCBlocks.INCOMPLETE_BRONZE_SLIDING_BREECH.asStack(),
				CBCItems.BRONZE_SLIDING_BREECHBLOCK.asStack(),
				
				new ItemStack(CBCFluids.MOLTEN_STEEL.get().getBucket()),
				CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER.asStack(),
				CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER.asStack(),
				CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER.asStack(),
				CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER.asStack(),
				CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER.asStack(),
				
				CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER.asStack(),
				CBCBlocks.SMALL_STEEL_CANNON_LAYER.asStack(),
				CBCBlocks.MEDIUM_STEEL_CANNON_LAYER.asStack(),
				CBCBlocks.LARGE_STEEL_CANNON_LAYER.asStack(),
				CBCBlocks.VERY_LARGE_STEEL_CANNON_LAYER.asStack(),
				
				CBCBlocks.UNBORED_STEEL_SLIDING_BREECH.asStack(),
				CBCBlocks.INCOMPLETE_STEEL_SLIDING_BREECH.asStack(),	
				CBCItems.STEEL_SLIDING_BREECHBLOCK.asStack(),
				
				CBCBlocks.UNBORED_STEEL_SCREW_BREECH.asStack(),
				CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH.asStack(),
				CBCItems.STEEL_SCREW_LOCK.asStack(),
				
				new ItemStack(CBCFluids.MOLTEN_NETHERSTEEL.get().getBucket()),
				CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER.asStack(),
				CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER.asStack(),
				CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER.asStack(),
				CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER.asStack(),
				CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER.asStack(),
				
				CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER.asStack(),
				CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER.asStack(),
				CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER.asStack(),
				CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER.asStack(),
				CBCBlocks.VERY_LARGE_NETHERSTEEL_CANNON_LAYER.asStack(),

				CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH.asStack(),
				CBCBlocks.INCOMPLETE_NETHERSTEEL_SCREW_BREECH.asStack(),
				CBCItems.NETHERSTEEL_SCREW_LOCK.asStack(),
				
				CBCBlocks.BASIN_FOUNDRY_LID.asStack(),
				
				CBCItems.PACKED_GUNPOWDER.asStack(),
				CBCItems.EMPTY_POWDER_CHARGE.asStack(),
				CBCItems.CAST_IRON_NUGGET.asStack(),
				CBCItems.CAST_IRON_INGOT.asStack(),
				CBCBlocks.CAST_IRON_BLOCK.asStack(),
				CBCItems.NETHERSTEEL_NUGGET.asStack(),
				CBCItems.NETHERSTEEL_INGOT.asStack(),
				CBCBlocks.NETHERSTEEL_BLOCK.asStack(),
				
				CBCItems.RAM_ROD.asStack(),
				CBCItems.WORM.asStack(),
				CBCBlocks.CANNON_CARRIAGE.asStack()));
		}
	};
	
	public static void register() {
		CreateBigCannons.registrate().creativeModeTab(() -> GROUP, "Create Big Cannons");
	}
	
}
