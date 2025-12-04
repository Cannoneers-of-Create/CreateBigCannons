package rbasamoyai.createbigcannons.fabric.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCItems;

public class CBCMillingRecipeProvider extends ProcessingRecipeGen {

	public CBCMillingRecipeProvider(FabricDataOutput output) {
		super(output, CreateBigCannons.MOD_ID);
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.MILLING;
	}

	GeneratedRecipe

		NITROPOWDER = create(CreateBigCannons.resource("nitropowder"), b -> b.require(CBCItems.HARDENED_NITRO.get())
		.output(CBCItems.NITROPOWDER.get(), 2));

}
