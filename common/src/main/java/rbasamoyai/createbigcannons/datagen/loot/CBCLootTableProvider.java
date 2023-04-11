package rbasamoyai.createbigcannons.datagen.loot;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import rbasamoyai.createbigcannons.CreateBigCannons;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CBCLootTableProvider extends RegistrateLootTableProvider {
	
	public CBCLootTableProvider(AbstractRegistrate<?> reg, FabricDataGenerator gen) {
		super(reg, gen);
	}
	
	@Override
	public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
		return List.of(Pair.of(BoringScrapLoot::new, LootContextParamSets.BLOCK));
	}
	
	@Override
	public void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
		for (Map.Entry<ResourceLocation, LootTable> entry : map.entrySet()) {
			LootTables.validate(validationtracker, entry.getKey(), entry.getValue());
		}
	}

}
