package rbasamoyai.createbigcannons.fabric;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import com.tterrag.registrate.providers.ProviderType;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.CBCDatagenCommon;
import rbasamoyai.createbigcannons.datagen.loot.BoringScrapLoot;
import rbasamoyai.createbigcannons.fabric.datagen.CBCCompactingRecipeProvider;
import rbasamoyai.createbigcannons.fabric.datagen.CBCCuttingRecipeProvider;
import rbasamoyai.createbigcannons.fabric.datagen.CBCMillingRecipeProvider;
import rbasamoyai.createbigcannons.fabric.datagen.CBCMixingRecipeProvider;
import rbasamoyai.createbigcannons.fabric.datagen.CBCSequencedAssemblyRecipeProvider;
import rbasamoyai.createbigcannons.fabric.datagen.MeltingRecipeProvider;

public class CBCDataFabric implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		Path cbcResources = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
		ExistingFileHelper helper = new ExistingFileHelper(
			Set.of(cbcResources), Set.of("create"), true, null, null
		);
		CreateBigCannons.REGISTRATE.setupDatagen(generator.createPack(), helper);
		CBCDatagenCommon.register(generator, true, true);

		FabricDataGenerator.Pack modDatapack = generator.createPack();

		modDatapack.addProvider(CBCCompactingRecipeProvider::new);
		modDatapack.addProvider(MeltingRecipeProvider::new);
		modDatapack.addProvider(CBCMixingRecipeProvider::new);
		modDatapack.addProvider(CBCMillingRecipeProvider::new);
		modDatapack.addProvider(CBCSequencedAssemblyRecipeProvider::new);
		modDatapack.addProvider(CBCCuttingRecipeProvider::new);
		CreateBigCannons.REGISTRATE.addDataGenerator(ProviderType.LOOT, prov -> {
			prov.addLootAction(LootContextParamSets.BLOCK, new BoringScrapLoot()::generate);
		});
	}

}
