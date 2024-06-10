package rbasamoyai.createbigcannons.fabric.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import rbasamoyai.createbigcannons.CBCTags.CBCItemTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class CBCCuttingRecipeProvider extends ProcessingRecipeGen {

	public CBCCuttingRecipeProvider(DataGenerator gen) {
		super(IndexPlatform.castGen(gen));
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.CUTTING;
	}

	GeneratedRecipe

		SPRING_WIRE_IRON = springWire("iron", CBCItemTags.SHEET_IRON, 1),
		SPRING_WIRE_STEEL = springWire("steel", CBCItemTags.SHEET_STEEL, 3),

	AUTOCANNON_CARTRIDGE_SHEET_IRON = autocannonCartridgeSheet("iron", CBCItemTags.SHEET_IRON, 1),
		AUTOCANNON_CARTRIDGE_SHEET_COPPER = autocannonCartridgeSheet("copper", CBCItemTags.SHEET_COPPER, 1),
		AUTOCANNON_CARTRIDGE_SHEET_BRASS = autocannonCartridgeSheet("brass", CBCItemTags.SHEET_BRASS, 4),
		AUTOCANNON_CARTRIDGE_SHEET_GOLD = create(CreateBigCannons.resource("autocannon_cartridge_sheet_gold"), b -> b
			.require(CBCItemTags.SHEET_GOLD)
			.output(CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get())
			.output(0.125f, CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get(), 1)
			.output(0.125f, CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get(), 1)),

	EMPTY_MACHINE_GUN_ROUND = create(CreateBigCannons.resource("empty_machine_gun_round"), b -> b.require(CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get())
		.output(CBCItems.EMPTY_MACHINE_GUN_ROUND.get(), 4)),

	VERY_SMALL_CAST_MOULD = castMould(CBCBlocks.VERY_SMALL_CAST_MOULD.get()),
		SMALL_CAST_MOULD = castMould(CBCBlocks.SMALL_CAST_MOULD.get()),
		MEDIUM_CAST_MOULD = castMould(CBCBlocks.MEDIUM_CAST_MOULD.get()),
		LARGE_CAST_MOULD = castMould(CBCBlocks.LARGE_CAST_MOULD.get()),
		VERY_LARGE_CAST_MOULD = castMould(CBCBlocks.VERY_LARGE_CAST_MOULD.get()),
		CANNON_END_CAST_MOULD = castMould(CBCBlocks.CANNON_END_CAST_MOULD.get()),
		SLIDING_BREECH_CAST_MOULD = castMould(CBCBlocks.SLIDING_BREECH_CAST_MOULD.get()),
		SCREW_BREECH_CAST_MOULD = castMould(CBCBlocks.SCREW_BREECH_CAST_MOULD.get()),
		AUTOCANNON_BARREL_CAST_MOULD = castMould(CBCBlocks.AUTOCANNON_BARREL_CAST_MOULD.get()),
		AUTOCANNON_RECOIL_SPRING_CAST_MOULD = castMould(CBCBlocks.AUTOCANNON_RECOIL_SPRING_CAST_MOULD.get()),
		AUTOCANNON_BREECH_CAST_MOULD = castMould(CBCBlocks.AUTOCANNON_BREECH_CAST_MOULD.get());

	private GeneratedRecipe springWire(String metal, TagKey<Item> tag, int count) {
		return create(CreateBigCannons.resource("spring_wire_" + metal), b -> b.require(tag)
			.output(CBCItems.SPRING_WIRE.get(), count));
	}

	private GeneratedRecipe autocannonCartridgeSheet(String metal, TagKey<Item> tag, int count) {
		return create(CreateBigCannons.resource("autocannon_cartridge_sheet_" + metal), b -> b.require(tag)
			.output(CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get(), count));
	}

	private GeneratedRecipe castMould(ItemLike item) {
		return create(CBCRegistryUtils.getItemLocation(item.asItem()), b -> b.require(ItemTags.LOGS)
			.output(item));
	}

}
