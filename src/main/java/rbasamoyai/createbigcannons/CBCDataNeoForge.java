package rbasamoyai.createbigcannons;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import com.tterrag.registrate.providers.ProviderType;

import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import rbasamoyai.createbigcannons.datagen.CBCCompactingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.CBCCraftingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.CBCCuttingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.CBCMillingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.CBCMixingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.CBCSequencedAssemblyRecipeProvider;
import rbasamoyai.createbigcannons.datagen.MeltingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.assets.CBCBlockPartialsGen;
import rbasamoyai.createbigcannons.datagen.assets.CBCLangGen;
import rbasamoyai.createbigcannons.datagen.loot.BoringScrapLoot;
import rbasamoyai.createbigcannons.datagen.recipes.BlockRecipeProvider;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.ponder.CBCPonderPlugin;

@EventBusSubscriber(modid = CreateBigCannons.MOD_ID)
public class CBCDataNeoForge {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGatherRegistrateData(GatherDataEvent evt) {
        if (!evt.getMods().contains(CreateBigCannons.MOD_ID))
            return;

        CreateBigCannons.REGISTRATE.addDataGenerator(ProviderType.LOOT, prov -> {
            prov.addLootAction(LootContextParamSets.BLOCK, new BoringScrapLoot()::generate);
        });
        CreateBigCannons.REGISTRATE.addDataGenerator(ProviderType.LANG, prov -> {
            providePonderLang(prov::add);
        });
        CBCLangGen.prepare();
        CBCSoundEvents.registerLangEntries();
        CBCCraftingRecipeProvider.register();
        CBCTags.register();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGatherDataEvent(GatherDataEvent evt) {
        if (!evt.getMods().contains(CreateBigCannons.MOD_ID))
            return;

        ExistingFileHelper helper = evt.getExistingFileHelper();

        DataGenerator generator = evt.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = evt.getLookupProvider();

        boolean includeServer = evt.includeServer();
        BlockRecipeProvider.registerAll(provider -> generator.addProvider(includeServer, provider.create(output)), registries);

        generator.addProvider(evt.includeClient(), CBCSoundEvents.provider(output));

        generator.addProvider(evt.includeClient(), new CBCBlockPartialsGen(output, helper));

        generator.addProvider(includeServer, new CBCCompactingRecipeProvider(output, registries));
        generator.addProvider(includeServer, new MeltingRecipeProvider(output, registries));
        generator.addProvider(includeServer, new CBCMixingRecipeProvider(output, registries));
        generator.addProvider(includeServer, new CBCMillingRecipeProvider(output, registries));
        generator.addProvider(includeServer, new CBCSequencedAssemblyRecipeProvider(output, registries));
        generator.addProvider(includeServer, new CBCCuttingRecipeProvider(output, registries));
    }

    private static void providePonderLang(BiConsumer<String, String> cons) {
        PonderIndex.addPlugin(new CBCPonderPlugin());
        PonderIndex.getLangAccess().provideLang(CreateBigCannons.MOD_ID, cons);
    }

}
