package rbasamoyai.createbigcannons;

import java.util.Arrays;
import java.util.function.Supplier;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
import rbasamoyai.createbigcannons.munitions.fuzes.DelayedImpactFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeItem;

public class ModGroup {

	public static final ResourceKey<CreativeModeTab> MAIN_TAB_KEY = makeKey("base");

	public static final Supplier<CreativeModeTab> GROUP = wrapGroup("base", () -> createBuilder()
		.title(Component.translatable("itemGroup." + CreateBigCannons.MOD_ID))
		.icon(CBCBlocks.SOLID_SHOT::asStack)
		.displayItems((param, output) -> {
			output.acceptAll(Arrays.asList(
				CBCBlocks.CANNON_MOUNT.asStack(),
				CBCBlocks.CANNON_MOUNT_EXTENSION.asStack(),
				CBCBlocks.FIXED_CANNON_MOUNT.asStack(),

				CBCBlocks.LOG_CANNON_END.asStack(),
				CBCBlocks.LOG_CANNON_CHAMBER.asStack(),

				CBCBlocks.WROUGHT_IRON_CANNON_END.asStack(),
				CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER.asStack(),
				CBCBlocks.WROUGHT_IRON_DROP_MORTAR_END.asStack(),

				CBCBlocks.CAST_IRON_CANNON_END.asStack(),
				CBCBlocks.CAST_IRON_SLIDING_BREECH.asStack(),
				CBCBlocks.CAST_IRON_QUICKFIRING_BREECH.asStack(),
				CBCBlocks.CAST_IRON_CANNON_CHAMBER.asStack(),
				CBCBlocks.CAST_IRON_CANNON_BARREL.asStack(),

				CBCBlocks.BRONZE_CANNON_END.asStack(),
				CBCBlocks.BRONZE_SLIDING_BREECH.asStack(),
				CBCBlocks.BRONZE_QUICKFIRING_BREECH.asStack(),
				CBCBlocks.BRONZE_CANNON_CHAMBER.asStack(),
				CBCBlocks.BRONZE_CANNON_BARREL.asStack(),

				CBCBlocks.STEEL_SLIDING_BREECH.asStack(),
				CBCBlocks.STEEL_QUICKFIRING_BREECH.asStack(),
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
				BigCartridgeBlockItem.getWithPower(0),
				BigCartridgeBlockItem.getWithPower(4),
				CBCItems.BIG_CARTRIDGE_SHEET.asStack(),
				CBCItems.CONGEALED_NITRO.asStack(),
				CBCItems.HARDENED_NITRO.asStack(),
				CBCItems.NITROPOWDER.asStack(),
				CBCItems.PACKED_GUNPOWDER.asStack(),
				CBCItems.GUNPOWDER_PINCH.asStack(),
				CBCItems.GUNCOTTON.asStack(),
				CBCItems.PACKED_GUNCOTTON.asStack(),
				CBCItems.EMPTY_POWDER_CHARGE.asStack(),

				CBCBlocks.SOLID_SHOT.asStack(),
				CBCBlocks.AP_SHOT.asStack(),
				CBCBlocks.MORTAR_STONE.asStack(),
				CBCBlocks.BAG_OF_GRAPESHOT.asStack(),
				CBCBlocks.HE_SHELL.asStack(),
				CBCBlocks.AP_SHELL.asStack(),
				CBCBlocks.SHRAPNEL_SHELL.asStack(),
				CBCBlocks.FLUID_SHELL.asStack(),
				CBCBlocks.SMOKE_SHELL.asStack(),
				CBCBlocks.DROP_MORTAR_SHELL.asStack(),
				CBCItems.IMPACT_FUZE.asStack(),
				TimedFuzeItem.getCreativeTabItem(20),
				ProximityFuzeItem.getCreativeTabItem(1),
				DelayedImpactFuzeItem.getCreativeTabItem(20),
				CBCItems.WIRED_FUZE.asStack(),
				CBCItems.TRACER_TIP.asStack(),

				CBCItems.CANNON_WELDER.asStack(),
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

				CBCItems.CAST_IRON_NUGGET.asStack(),
				CBCItems.CAST_IRON_INGOT.asStack(),
				CBCBlocks.CAST_IRON_BLOCK.asStack(),
				CBCItems.NETHERSTEEL_NUGGET.asStack(),
				CBCItems.NETHERSTEEL_INGOT.asStack(),
				CBCBlocks.NETHERSTEEL_BLOCK.asStack(),

				CBCItems.GAS_MASK.asStack(),

				CBCItems.RAM_ROD.asStack(),
				CBCItems.WORM.asStack(),
				CBCBlocks.CANNON_CARRIAGE.asStack(),
				CBCItems.PAIR_OF_CANNON_WHEELS.asStack(),
				CBCItems.QUICKFIRING_MECHANISM.asStack(),

				CBCBlocks.CAST_IRON_AUTOCANNON_BREECH.asStack(),
				CBCBlocks.CAST_IRON_AUTOCANNON_RECOIL_SPRING.asStack(),
				CBCBlocks.CAST_IRON_AUTOCANNON_BARREL.asStack(),

				CBCBlocks.BRONZE_AUTOCANNON_BREECH.asStack(),
				CBCBlocks.BRONZE_AUTOCANNON_RECOIL_SPRING.asStack(),
				CBCBlocks.BRONZE_AUTOCANNON_BARREL.asStack(),

				CBCBlocks.STEEL_AUTOCANNON_BREECH.asStack(),
				CBCBlocks.STEEL_AUTOCANNON_RECOIL_SPRING.asStack(),
				CBCBlocks.STEEL_AUTOCANNON_BARREL.asStack(),

				CBCItems.AUTOCANNON_CARTRIDGE_SHEET.asStack(),
				CBCItems.EMPTY_AUTOCANNON_CARTRIDGE.asStack(),
				CBCItems.FILLED_AUTOCANNON_CARTRIDGE.asStack(),
				CBCItems.AP_AUTOCANNON_ROUND.get().getCreativeTabCartridgeItem(),
				CBCItems.AP_AUTOCANNON_ROUND.asStack(),
				CBCItems.FLAK_AUTOCANNON_ROUND.get().getCreativeTabCartridgeItem(),
				CBCItems.FLAK_AUTOCANNON_ROUND.asStack(),
				CBCItems.MACHINE_GUN_ROUND.asStack(),
				CBCItems.EMPTY_MACHINE_GUN_ROUND.asStack(),
				CBCBlocks.AUTOCANNON_AMMO_CONTAINER.asStack(),
				CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER.asStack(),

				CBCItems.RECOIL_SPRING.asStack(),

				CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BREECH.asStack(),
				CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING.asStack(),
				CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BARREL.asStack(),
				CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH.asStack(),
				CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING.asStack(),
				CBCItems.CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR.asStack(),

				CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BREECH.asStack(),
				CBCBlocks.UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING.asStack(),
				CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BARREL.asStack(),
				CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_BREECH.asStack(),
				CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING.asStack(),
				CBCItems.BRONZE_AUTOCANNON_BREECH_EXTRACTOR.asStack(),

				CBCBlocks.UNBORED_STEEL_AUTOCANNON_BREECH.asStack(),
				CBCBlocks.UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING.asStack(),
				CBCBlocks.UNBORED_STEEL_AUTOCANNON_BARREL.asStack(),
				CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_BREECH.asStack(),
				CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING.asStack(),
				CBCItems.STEEL_AUTOCANNON_BREECH_EXTRACTOR.asStack(),

				CBCBlocks.AUTOCANNON_BREECH_CAST_MOULD.asStack(),
				CBCBlocks.AUTOCANNON_RECOIL_SPRING_CAST_MOULD.asStack(),
				CBCBlocks.AUTOCANNON_BARREL_CAST_MOULD.asStack(),

				CBCItems.SPRING_WIRE.asStack(),

				CBCItems.BLOCK_ARMOR_INSPECTION_TOOL.asStack()));
		})
		.build());

	@ExpectPlatform public static Supplier<CreativeModeTab> wrapGroup(String id, Supplier<CreativeModeTab> sup) { throw new AssertionError(); }
	@ExpectPlatform public static CreativeModeTab.Builder createBuilder() { throw new AssertionError(); }

	@ExpectPlatform public static void useModTab(ResourceKey<CreativeModeTab> key) { throw new AssertionError(); }

	public static ResourceKey<CreativeModeTab> makeKey(String id) {
		return ResourceKey.create(Registries.CREATIVE_MODE_TAB, CreateBigCannons.resource(id));
	}

	public static void register() {
		CreateBigCannons.REGISTRATE.addRawLang("itemGroup." + CreateBigCannons.MOD_ID, "Create Big Cannons");
	}

}
