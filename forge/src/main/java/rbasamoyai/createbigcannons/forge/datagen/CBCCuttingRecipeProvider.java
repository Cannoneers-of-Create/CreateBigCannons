package rbasamoyai.createbigcannons.forge.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class CBCCuttingRecipeProvider extends ProcessingRecipeGen {

	public CBCCuttingRecipeProvider(DataGenerator gen) { super(IndexPlatform.castGen(gen)); }

	@Override protected IRecipeTypeInfo getRecipeType() { return AllRecipeTypes.CUTTING; }

	GeneratedRecipe

	SPRING_WIRE_IRON = springWire("iron", 1),
	SPRING_WIRE_STEEL = springWire("steel", 3),

	AUTOCANNON_CARTRIDGE_SHEET_IRON = autocannonCartridgeSheet("iron", 1),
	AUTOCANNON_CARTRIDGE_SHEET_COPPER = autocannonCartridgeSheet("copper", 1),
	AUTOCANNON_CARTRIDGE_SHEET_BRASS = autocannonCartridgeSheet("brass", 4),
	AUTOCANNON_CARTRIDGE_SHEET_GOLD = create(CreateBigCannons.resource("autocannon_cartridge_sheet_gold"), b -> b.require(AllTags.forgeItemTag("plates/gold"))
			.output(CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get())
			.output(0.125f, CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get(), 1)
			.output(0.125f, CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get(), 1)),

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

	private GeneratedRecipe springWire(String metal, int count) {
		return create(CreateBigCannons.resource("spring_wire_" + metal), b -> b.require(AllTags.forgeItemTag("plates/" + metal))
				.output(CBCItems.SPRING_WIRE.get(), count));
	}

	private GeneratedRecipe autocannonCartridgeSheet(String metal, int count) {
		return create(CreateBigCannons.resource("autocannon_cartridge_sheet_" + metal), b -> b.require(AllTags.forgeItemTag("plates/" + metal))
				.output(CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get(), count));
	}

	private GeneratedRecipe castMould(ItemLike item) {
		return create(Registry.ITEM.getKey(item.asItem()), b -> b.require(ItemTags.LOGS)
				.output(item));
	}

}
