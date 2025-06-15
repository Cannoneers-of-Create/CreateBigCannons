package rbasamoyai.createbigcannons.forge;

import java.util.function.BiConsumer;

import com.tterrag.registrate.providers.ProviderType;

import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.CBCDatagenCommon;
import rbasamoyai.createbigcannons.datagen.assets.CBCLangGen;
import rbasamoyai.createbigcannons.datagen.assets.forge.CBCBlockPartialsGen;
import rbasamoyai.createbigcannons.datagen.forge.CBCCompactingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.forge.CBCCuttingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.forge.CBCMillingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.forge.CBCMixingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.forge.CBCSequencedAssemblyRecipeProvider;
import rbasamoyai.createbigcannons.datagen.forge.MeltingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.loot.BoringScrapLoot;
import rbasamoyai.createbigcannons.datagen.recipes.BlockRecipeProvider;
import rbasamoyai.createbigcannons.datagen.recipes.CBCCraftingRecipeProvider;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.ponder.CBCPonderPlugin;

@EventBusSubscriber(modid = CreateBigCannons.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CBCDataForge {

    @SubscribeEvent
    public static void onGatherDataEvent(GatherDataEvent evt) {
        ExistingFileHelper helper = evt.getExistingFileHelper();
        CreateBigCannons.REGISTRATE.addDataGenerator(ProviderType.LOOT, prov -> {
            prov.addLootAction(LootContextParamSets.BLOCK, new BoringScrapLoot()::generate);
        });
        CBCDatagenCommon.init();

        DataGenerator generator = evt.getGenerator();
        PackOutput output = generator.getPackOutput();

        boolean includeServer = evt.includeServer();
        BlockRecipeProvider.registerAll(provider -> generator.addProvider(includeServer, provider.create(output)));
        CBCCraftingRecipeProvider.register();

        CBCLangGen.prepare();
        generator.addProvider(evt.includeClient(), CBCSoundEvents.provider(output));
        CBCSoundEvents.registerLangEntries();
        CreateBigCannons.REGISTRATE.addDataGenerator(ProviderType.LANG, prov -> {
            providePonderLang(prov::add);
        });

        generator.addProvider(evt.includeClient(), new CBCBlockPartialsGen(output, helper));

        generator.addProvider(includeServer, new CBCCompactingRecipeProvider(output));
        generator.addProvider(includeServer, new MeltingRecipeProvider(output));
        generator.addProvider(includeServer, new CBCMixingRecipeProvider(output));
        generator.addProvider(includeServer, new CBCMillingRecipeProvider(output));
        generator.addProvider(includeServer, new CBCSequencedAssemblyRecipeProvider(output));
        generator.addProvider(includeServer, new CBCCuttingRecipeProvider(output));
    }

    private static void providePonderLang(BiConsumer<String, String> cons) {
        PonderIndex.addPlugin(new CBCPonderPlugin());
        PonderIndex.getLangAccess().provideLang(CreateBigCannons.MOD_ID, cons);
    }

}
