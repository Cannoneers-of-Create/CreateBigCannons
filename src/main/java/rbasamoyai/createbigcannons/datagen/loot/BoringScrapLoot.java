package rbasamoyai.createbigcannons.datagen.loot;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CBCTags;

public class BoringScrapLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
		cons.accept(loc(CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.get()), LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(CBCItems.CAST_IRON_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(40, 50))))
				));
		cons.accept(loc(CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.CAST_IRON_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.CAST_IRON_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(21, 31)))))
				);
		cons.accept(loc(CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.CAST_IRON_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(58)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.CAST_IRON_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 10)))))
				);
		
		cons.accept(loc(CBCBlocks.UNBORED_BRONZE_CANNON_BARREL.get()), LootTable.lootTable().withPool(LootPool.lootPool()
				.add(TagEntry.expandTag(CBCTags.ItemCBC.NUGGET_BRONZE).apply(SetItemCountFunction.setCount(UniformGenerator.between(40, 50))))
				));
		cons.accept(loc(CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.BRONZE_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.BRONZE_SCRAP.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(21, 31)))))
				);
		cons.accept(loc(CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.BRONZE_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(58)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.BRONZE_SCRAP.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 10)))))
				);
		
		cons.accept(loc(CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER.get()), LootTable.lootTable().withPool(LootPool.lootPool()
				.add(TagEntry.expandTag(CBCTags.ItemCBC.NUGGET_STEEL).apply(SetItemCountFunction.setCount(UniformGenerator.between(40, 50))))
				));
		cons.accept(loc(CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(58)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 10)))))
				);
		cons.accept(loc(CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(21, 31))))
				));
		cons.accept(loc(CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(39, 49)))))
				);
		cons.accept(loc(CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(29, 39)))))
				);
		cons.accept(loc(CBCBlocks.UNBORED_STEEL_SLIDING_BREECH.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(58)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 10)))))
				);
		cons.accept(loc(CBCBlocks.UNBORED_STEEL_SCREW_BREECH.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(58)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.STEEL_SCRAP.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 10)))))
				);
		
		cons.accept(loc(CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get()), LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(40, 50))))
				));
		cons.accept(loc(CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(58)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 10)))))
				);
		cons.accept(loc(CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(21, 31))))
				));
		cons.accept(loc(CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(39, 49)))))
				);
		cons.accept(loc(CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(64)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(29, 39)))))
				);
		cons.accept(loc(CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH.get()), LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(58)))))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 10)))))
				);
	}
	
	protected static ResourceLocation loc(Block block) {
		ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
		return new ResourceLocation(id.getNamespace(), "boring_scrap/" + id.getPath());
	}

}
