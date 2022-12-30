package rbasamoyai.createbigcannons.datagen.recipes;

import com.simibubi.create.content.contraptions.components.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;

import java.util.function.UnaryOperator;

public class CBCSequencedAssemblyRecipeProvider extends CreateRecipeProvider {

	public CBCSequencedAssemblyRecipeProvider(DataGenerator gen) { super(gen); }

	@Override public String getName() { return "Create Big Cannons - sequenced assembly recipes"; }

	GeneratedRecipe

	ASSEMBLING_AUTOCANNON_CARTRIDGE = create("assembling_autocannon_cartridge", b -> b.require(CBCItems.EMPTY_AUTOCANNON_CARTRIDGE.get())
			.transitionTo(CBCItems.PARTIALLY_FILLED_AUTOCANNON_CARTRIDGE.get())
			.loops(3)
			.addStep(DeployerApplicationRecipe::new, rb -> rb.require(Tags.Items.GUNPOWDER))
			.addOutput(CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get(), 1));

	protected GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
		GeneratedRecipe generatedRecipe =
				c -> transform.apply(new SequencedAssemblyRecipeBuilder(CreateBigCannons.resource(name)))
						.build(c);
		this.all.add(generatedRecipe);
		return generatedRecipe;
	}

}
