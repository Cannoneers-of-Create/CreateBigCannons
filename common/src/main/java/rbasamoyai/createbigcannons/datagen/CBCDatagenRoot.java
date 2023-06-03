package rbasamoyai.createbigcannons.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.assets.CBCBlockPartialsGen;
import rbasamoyai.createbigcannons.datagen.assets.CBCLangGen;
import rbasamoyai.createbigcannons.datagen.recipes.BlockRecipeProvider;
import rbasamoyai.createbigcannons.datagen.recipes.CBCCraftingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.values.BlockHardnessProvider;
import rbasamoyai.createbigcannons.datagen.values.MunitionPropertiesProvider;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;
import rbasamoyai.createbigcannons.ponder.CBCPonderTags;

public class CBCDatagenRoot {
	public static void register(DataGenerator gen, ExistingFileHelper helper, boolean client, boolean server) {
		if (server) {
			BlockRecipeProvider.registerAll(gen);
			CBCCraftingRecipeProvider.register();
			gen.addProvider(true, new BlockHardnessProvider(CreateBigCannons.MOD_ID, gen));
			gen.addProvider(true, new MunitionPropertiesProvider(CreateBigCannons.MOD_ID, gen));
			IndexPlatform.addSidedDataGenerators(gen);
		}
		if (client) {
			CBCLangGen.prepare();
			gen.addProvider(true, new CBCBlockPartialsGen(gen, helper));
			gen.addProvider(true, CBCSoundEvents.provider(gen));
			CBCSoundEvents.registerLangEntries();
			CBCPonderTags.register();
			CBCPonderIndex.register();
			CBCPonderIndex.registerLang();
		}
	}
}
