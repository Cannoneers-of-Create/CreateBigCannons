package rbasamoyai.createbigcannons.datagen.loot;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import rbasamoyai.createbigcannons.base.CBCUtils;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;

public class BoringScrapLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {

	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> cons) {
		ItemLike castIron = CBCItems.CAST_IRON_NUGGET.get();
		ItemLike bronze = CBCItems.BRONZE_SCRAP.get();
		ItemLike steel = CBCItems.STEEL_SCRAP.get();
		ItemLike nethersteel = CBCItems.NETHERSTEEL_NUGGET.get();

		cons.accept(loc(CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL.get()), dropAmount(castIron, 40, 50));
		cons.accept(loc(CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER.get()), dropAmount(castIron, 85, 95));
		cons.accept(loc(CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH.get()), dropAmount(castIron, 58, 68));

		cons.accept(loc(CBCBlocks.UNBORED_BRONZE_CANNON_BARREL.get()), dropAmount(bronze, 40, 50));
		cons.accept(loc(CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER.get()), dropAmount(bronze, 85, 95));
		cons.accept(loc(CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH.get()), dropAmount(bronze, 58, 68));

		cons.accept(loc(CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER.get()), dropAmount(steel, 40, 50));
		cons.accept(loc(CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER.get()), dropAmount(steel, 58, 68));
		cons.accept(loc(CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER.get()), dropAmount(steel, 85, 95));
		cons.accept(loc(CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER.get()), dropAmount(steel, 103, 113));
		cons.accept(loc(CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER.get()), dropAmount(steel, 157, 167));
		cons.accept(loc(CBCBlocks.UNBORED_STEEL_SLIDING_BREECH.get()), dropAmount(steel, 58, 68));
		cons.accept(loc(CBCBlocks.UNBORED_STEEL_SCREW_BREECH.get()), dropAmount(steel, 58, 68));

		cons.accept(loc(CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER.get()), dropAmount(nethersteel, 40, 50));
		cons.accept(loc(CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER.get()), dropAmount(nethersteel, 58, 68));
		cons.accept(loc(CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER.get()), dropAmount(nethersteel, 85, 95));
		cons.accept(loc(CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER.get()), dropAmount(nethersteel, 103, 113));
		cons.accept(loc(CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER.get()), dropAmount(nethersteel, 157, 167));
		cons.accept(loc(CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH.get()), dropAmount(nethersteel, 58, 68));

		cons.accept(loc(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BARREL.get()), dropAmount(castIron, 15, 20));
		cons.accept(loc(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING.get()), dropAmount(castIron, 25, 30));
		cons.accept(loc(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BREECH.get()), dropAmount(castIron, 25, 30));

		cons.accept(loc(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BARREL.get()), dropAmount(bronze, 15, 20));
		cons.accept(loc(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING.get()), dropAmount(bronze, 25, 30));
		cons.accept(loc(CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BREECH.get()), dropAmount(bronze, 25, 30));

		cons.accept(loc(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BARREL.get()), dropAmount(steel, 15, 20));
		cons.accept(loc(CBCBlocks.UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING.get()), dropAmount(steel, 25, 30));
		cons.accept(loc(CBCBlocks.UNBORED_STEEL_AUTOCANNON_BREECH.get()), dropAmount(steel, 25, 30));
	}

	protected static ResourceLocation loc(Block block) {
		ResourceLocation id = Registry.BLOCK.getKey(block);
		return CBCUtils.location(id.getNamespace(), "boring_scrap/" + id.getPath());
	}

	protected static LootTable.Builder dropAmount(ItemLike drop, int min, int max) {
		int maxSz = new ItemStack(drop).getMaxStackSize();
		LootTable.Builder table = LootTable.lootTable();
		for (int i = 0; i < Mth.floor((float) min / maxSz); ++i)
			table.withPool(LootPool.lootPool()
					.add(LootItem.lootTableItem(drop))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(64))));
		int base = min % maxSz;
		int diff = max - min;
		if (base + diff > maxSz) {
			table.withPool(LootPool.lootPool()
					.add(LootItem.lootTableItem(drop))
					.apply(SetItemCountFunction.setCount(ConstantValue.exactly(base))));
			base = 0;
		}
		return table.withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(drop))
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(base, base + diff))));
	}

}
