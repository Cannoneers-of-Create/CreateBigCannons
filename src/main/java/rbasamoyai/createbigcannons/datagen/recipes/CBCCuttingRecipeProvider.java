package rbasamoyai.createbigcannons.datagen.recipes;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;
import net.minecraft.data.DataGenerator;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCCuttingRecipeProvider extends ProcessingRecipeGen {

	public CBCCuttingRecipeProvider(DataGenerator gen) { super(gen); }

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
			.output(0.125f, CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get(), 1));

	private GeneratedRecipe springWire(String metal, int count) {
		return create(CreateBigCannons.resource("spring_wire_" + metal), b -> b.require(AllTags.forgeItemTag("plates/" + metal))
				.output(CBCItems.SPRING_WIRE.get(), count));
	}

	private GeneratedRecipe autocannonCartridgeSheet(String metal, int count) {
		return create(CreateBigCannons.resource("autocannon_cartridge_sheet_" + metal), b -> b.require(AllTags.forgeItemTag("plates/" + metal))
				.output(CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get(), count));
	}

}
