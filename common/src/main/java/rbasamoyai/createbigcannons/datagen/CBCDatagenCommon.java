package rbasamoyai.createbigcannons.datagen;

import net.minecraft.data.DataGenerator;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.assets.CBCLangGen;
import rbasamoyai.createbigcannons.datagen.recipes.BlockRecipeProvider;
import rbasamoyai.createbigcannons.datagen.recipes.CBCCraftingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.values.BlockHardnessProvider;
import rbasamoyai.createbigcannons.datagen.values.MunitionPropertiesProvider;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;
import rbasamoyai.createbigcannons.ponder.CBCPonderTags;

public class CBCDatagenCommon {

	public static final String PLATFORM = System.getProperty("createbigcannons.datagen.platform", "fabric");
	public static final int FLUID_MULTIPLIER = PLATFORM.equals("fabric") ? 81 : 1;

	public static void register(DataGenerator gen, boolean client, boolean server) {
		DataGenerator.PackGenerator modDatapack = gen.getBuiltinDatapack(client || server, CreateBigCannons.MOD_ID);
		if (server) {
			BlockRecipeProvider.registerAll(modDatapack);
			CBCCraftingRecipeProvider.register();
			modDatapack.addProvider(output -> new BlockHardnessProvider(CreateBigCannons.MOD_ID, output));
			modDatapack.addProvider(output -> new MunitionPropertiesProvider(CreateBigCannons.MOD_ID, output));
		}
		if (client) {
			CBCLangGen.prepare();
			modDatapack.addProvider(CBCSoundEvents::provider);
			CBCSoundEvents.registerLangEntries();
			CBCPonderTags.register();
			CBCPonderIndex.register();
			CBCPonderIndex.registerLang();
		}
	}

}
