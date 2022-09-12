package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCCompactingRecipeProvider extends ProcessingRecipeGen {

	public CBCCompactingRecipeProvider(DataGenerator generator) {
		super(generator);
	}
	
	@Override protected IRecipeTypeInfo getRecipeType() { return AllRecipeTypes.COMPACTING; }
	
	GeneratedRecipe
	
	PACKED_GUNPOWDER = create(CreateBigCannons.resource("packed_gunpowder"), b -> b.require(Ingredient.of(new ItemStack(Items.GUNPOWDER, 6)))
			.output(CBCItems.PACKED_GUNPOWDER.get()));
	
}
