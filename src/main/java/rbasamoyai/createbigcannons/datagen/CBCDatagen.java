package rbasamoyai.createbigcannons.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;
import rbasamoyai.createbigcannons.ponder.CBCPonderTags;

@Mod.EventBusSubscriber(modid = CreateBigCannons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CBCDatagen {
	
	@SubscribeEvent
	public static void onDatagen(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		if (event.includeServer()) {
			BlockRecipeProvider.registerAll(gen);
			gen.addProvider(new CBCCraftingRecipeProvider(gen));
			gen.addProvider(new CBCCompactingRecipeProvider(gen));
			gen.addProvider(new MeltingRecipeProvider(gen));
		}
		if (event.includeClient()) {
			CBCLangGen.prepare();
			
			CBCPonderTags.register();
			CBCPonderIndex.register();
			CBCPonderIndex.registerLang();
		}
	}
	
}
