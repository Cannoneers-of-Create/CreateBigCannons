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

	SPRING_WIRE_IRON = create(CreateBigCannons.resource("spring_wire_iron"), b -> b.require(AllTags.forgeItemTag("plates/iron"))
			.output(CBCItems.SPRING_WIRE.asStack())),

	SPRING_WIRE_STEEL = create(CreateBigCannons.resource("spring_wire_steel"), b -> b.require(AllTags.forgeItemTag("plates/steel"))
			.output(CBCItems.SPRING_WIRE.asStack(3)));

}
