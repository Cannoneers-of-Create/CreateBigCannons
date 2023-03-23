package rbasamoyai.createbigcannons.datagen.recipes;

import com.simibubi.create.content.contraptions.components.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.contraptions.components.press.PressingRecipe;
import com.simibubi.create.content.contraptions.components.saw.CuttingRecipe;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import net.minecraft.data.DataGenerator;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

import java.util.function.UnaryOperator;

public class CBCSequencedAssemblyRecipeProvider extends CreateRecipeProvider {

	public CBCSequencedAssemblyRecipeProvider(DataGenerator gen) { super(IndexPlatform.castGen(gen)); }

	@Override public String getName() { return "Create Big Cannons - sequenced assembly recipes"; }

	GeneratedRecipe

	FILLING_AUTOCANNON_CARTRIDGE = create("filling_autocannon_cartridge", b -> b.require(CBCItems.EMPTY_AUTOCANNON_CARTRIDGE.get())
			.transitionTo(CBCItems.PARTIALLY_FILLED_AUTOCANNON_CARTRIDGE.get())
			.loops(3)
			.addStep(DeployerApplicationRecipe::new, rb -> rb.require(CBCTags.ItemCBC.GUNPOWDER))
			.addOutput(CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get(), 1)),

	PRESSING_AUTOCANNON_CARTRIDGE = create("pressing_autocannon_cartridge", b -> b.require(CBCItems.AUTOCANNON_CARTRIDGE_SHEET.get())
			.transitionTo(CBCItems.PARTIALLY_FORMED_AUTOCANNON_CARTRIDGE.get())
			.loops(6)
			.addStep(PressingRecipe::new, rb -> rb)
			.addOutput(CBCItems.EMPTY_AUTOCANNON_CARTRIDGE.get(), 1)),

	RECOIL_SPRING = create("recoil_spring", b -> b.require(CBCItems.SPRING_WIRE.get())
			.transitionTo(CBCItems.PARTIAL_RECOIL_SPRING.get())
			.loops(3)
			.addStep(PressingRecipe::new, rb -> rb)
			.addOutput(CBCItems.RECOIL_SPRING.get(), 1)),

	CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR = create("cast_iron_autocannon_breech_extractor", b -> b.require(CBCTags.ItemCBC.INGOT_CAST_IRON)
			.transitionTo(CBCItems.PARTIAL_CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR.get())
			.loops(3)
			.addStep(CuttingRecipe::new, rb -> rb)
			.addOutput(CBCItems.CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR.get(), 1)),

	BRONZE_AUTOCANNON_BREECH_EXTRACTOR = create("bronze_autocannon_breech_extractor", b -> b.require(CBCTags.ItemCBC.INGOT_BRONZE)
			.transitionTo(CBCItems.PARTIAL_BRONZE_AUTOCANNON_BREECH_EXTRACTOR.get())
			.loops(3)
			.addStep(CuttingRecipe::new, rb -> rb)
			.addOutput(CBCItems.BRONZE_AUTOCANNON_BREECH_EXTRACTOR.get(), 1)),

	STEEL_AUTOCANNON_BREECH_EXTRACTOR = create("steel_autocannon_breech_extractor", b -> b.require(CBCTags.ItemCBC.INGOT_STEEL)
			.transitionTo(CBCItems.PARTIAL_STEEL_AUTOCANNON_BREECH_EXTRACTOR.get())
			.loops(3)
			.addStep(CuttingRecipe::new, rb -> rb)
			.addOutput(CBCItems.STEEL_AUTOCANNON_BREECH_EXTRACTOR.get(), 1));

	protected GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
		GeneratedRecipe generatedRecipe =
				c -> transform.apply(new SequencedAssemblyRecipeBuilder(CreateBigCannons.resource(name)))
						.build(c);
		this.all.add(generatedRecipe);
		return generatedRecipe;
	}

}
