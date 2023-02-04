package rbasamoyai.createbigcannons.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.data.event.GatherDataEvent;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.assets.CBCBlockPartialsGen;
import rbasamoyai.createbigcannons.datagen.assets.CBCLangGen;
import rbasamoyai.createbigcannons.datagen.loot.CBCLootTableProvider;
import rbasamoyai.createbigcannons.datagen.recipes.*;
import rbasamoyai.createbigcannons.datagen.values.BlockHardnessProvider;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;
import rbasamoyai.createbigcannons.ponder.CBCPonderTags;

@Mod.EventBusSubscriber(modid = CreateBigCannons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CBCDatagen {
	
	@SubscribeEvent
	public static void onDatagen(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();
		if (event.includeServer()) {
			BlockRecipeProvider.registerAll(gen);
			gen.addProvider(true, new CBCCraftingRecipeProvider(gen));
			gen.addProvider(true, new CBCCompactingRecipeProvider(gen));
			gen.addProvider(true, new MeltingRecipeProvider(gen));
			gen.addProvider(true, new CBCMixingRecipeProvider(gen));
			gen.addProvider(true, new CBCLootTableProvider(gen));
			gen.addProvider(true, new CBCSequencedAssemblyRecipeProvider(gen));
			gen.addProvider(true, new CBCCuttingRecipeProvider(gen));
			gen.addProvider(true, new BlockHardnessProvider(CreateBigCannons.MOD_ID, gen));
		}
		if (event.includeClient()) {
			CBCLangGen.prepare();

			gen.addProvider(true, new CBCBlockPartialsGen(gen, helper));
			
			CBCPonderTags.register();
			CBCPonderIndex.register();
			CBCPonderIndex.registerLang();
		}
	}
	
}
