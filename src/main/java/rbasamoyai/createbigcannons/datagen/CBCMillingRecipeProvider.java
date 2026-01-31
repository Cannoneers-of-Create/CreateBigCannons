package rbasamoyai.createbigcannons.datagen;

import java.util.concurrent.CompletableFuture;

import com.simibubi.create.api.data.recipe.MillingRecipeGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCItems;

public class CBCMillingRecipeProvider extends MillingRecipeGen {

	public CBCMillingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, CreateBigCannons.MOD_ID);
	}

	GeneratedRecipe

		NITROPOWDER = create(CreateBigCannons.resource("nitropowder"), b -> b.require(CBCItems.HARDENED_NITRO.get())
		.output(CBCItems.NITROPOWDER.get(), 2));

}
