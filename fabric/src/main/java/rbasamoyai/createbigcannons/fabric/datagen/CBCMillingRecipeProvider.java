package rbasamoyai.createbigcannons.fabric.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.DataGenerator;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class CBCMillingRecipeProvider extends ProcessingRecipeGen {

	public CBCMillingRecipeProvider(DataGenerator gen) {
		super(IndexPlatform.castGen(gen));
	}

	@Override
	protected IRecipeTypeInfo getRecipeType() {
		return AllRecipeTypes.MILLING;
	}

	GeneratedRecipe

		NITROPOWDER = create(CreateBigCannons.resource("alloy_nethersteel_cast_iron"), b -> b.require(CBCItems.HARDENED_NITRO.get())
		.output(CBCItems.NITROPOWDER.get(), 2));

}
