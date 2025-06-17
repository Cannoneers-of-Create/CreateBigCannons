package rbasamoyai.createbigcannons.datagen.forge;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;

import net.minecraft.data.PackOutput;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCItems;

public class CBCMillingRecipeProvider extends ProcessingRecipeGen {

	public CBCMillingRecipeProvider(PackOutput output) {
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
