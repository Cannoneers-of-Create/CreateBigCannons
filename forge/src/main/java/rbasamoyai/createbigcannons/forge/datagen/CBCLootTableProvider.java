package rbasamoyai.createbigcannons.forge.datagen;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import rbasamoyai.createbigcannons.datagen.loot.BoringScrapLoot;

public class CBCLootTableProvider extends RegistrateLootTableProvider {
	public CBCLootTableProvider(AbstractRegistrate<?> parent, DataGenerator dataGeneratorIn) {
		super(parent, dataGeneratorIn);
	}

	@Override
	public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
		return List.of(Pair.of(BoringScrapLoot::new, LootContextParamSets.BLOCK));
	}

	@Override
	public void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
		for (Map.Entry<ResourceLocation, LootTable> entry : map.entrySet()) {
			LootTables.validate(validationtracker, entry.getKey(), entry.getValue());
		}
	}
}
