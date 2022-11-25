package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderGen;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageBlock;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageBlockItem;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonBlockItem;
import rbasamoyai.createbigcannons.cannons.cannonend.SlidingBreechBlockGen;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillGen;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderGen;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderHeadBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastMouldBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteScrewBreechBlockGen;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteSlidingBreechBlockGen;

public class CBCBuilderTransformers {

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBarrel(String pathAndMaterial) {
		return cannonBarrel(pathAndMaterial, pathAndMaterial);
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBarrel(String sidePathAndMaterial, String endPathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/cannon_barrel");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + sidePathAndMaterial + "_cannon_barrel_side");
		ResourceLocation endLoc = CreateBigCannons.resource("block/" + endPathAndMaterial + "_cannon_barrel_end");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.tag(CBCTags.BlockCBC.REDUCES_SPREAD)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().withExistingParent(c.getName(), baseLoc)
					.texture("side", sideLoc)
					.texture("end", endLoc)
					.texture("particle", sideLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonChamber(String pathAndMaterial) {
		return cannonChamber(pathAndMaterial, pathAndMaterial);
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonChamber(String sidePathAndMaterial, String endPathAndMaterial) {
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + sidePathAndMaterial + "_cannon_chamber_side");
		ResourceLocation endLoc = CreateBigCannons.resource("block/" + endPathAndMaterial + "_cannon_chamber_end");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.tag(CBCTags.BlockCBC.THICK_TUBING)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().withExistingParent(c.getName(), "block/cube_column")
						.texture("side", sideLoc)
						.texture("end", endLoc)
						.texture("particle", sideLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> sizedCannon(String model, String pathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/" + model);
		ResourceLocation tubeLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_tube");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().withExistingParent(c.getName(), baseLoc)
						.texture("tube", tubeLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> sizedHollowCannon(String sizePath, String pathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/" + sizePath + "_cannon_tube");
		ResourceLocation tubeLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_tube");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().withExistingParent(c.getName(), baseLoc)
						.texture("tube", tubeLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonEnd(String pathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/cannon_end");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_end_side");
		ResourceLocation topLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_end_top");
		ResourceLocation bottomLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_end_bottom");
		ResourceLocation knobLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_end_knob");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().withExistingParent(c.getName(), baseLoc)
						.texture("side", sideLoc)
						.texture("top", topLoc)
						.texture("bottom", bottomLoc)
						.texture("knob", knobLoc)
						.texture("particle", topLoc)));
	}
	
	public static <T extends Block & CannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> slidingBreech(String pathAndMaterial) {
		ResourceLocation itemBaseLoc = CreateBigCannons.resource("block/sliding_breech_item");
		ResourceLocation holeLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_hole");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_side");
		ResourceLocation sideHoleLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_side_hole");
		ResourceLocation insideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_inside");
		ResourceLocation breechblockTopLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_breechblock_top");
		ResourceLocation breechblockEndLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_breechblock_end");
		ResourceLocation breechblockSideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_breechblock_side");
		ResourceLocation breechblockBottomLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_breechblock_bottom");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.tag(CBCTags.BlockCBC.WEAK_CANNON_END)
				.blockstate(new SlidingBreechBlockGen(pathAndMaterial)::generate)
				.item(CannonBlockItem::new)
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(itemBaseLoc))
					.texture("hole", holeLoc)
					.texture("side", sideLoc)
					.texture("side_hole", sideHoleLoc)
					.texture("inside", insideLoc)
					.texture("breechblock_top", breechblockTopLoc)
					.texture("breechblock_end", breechblockEndLoc)
					.texture("breechblock_side", breechblockSideLoc)
					.texture("breechblock_bottom", breechblockBottomLoc))
				.build();
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> slidingBreechUnbored(String pathAndMaterial) {
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate(new SlidingBreechBlockGen(pathAndMaterial)::generate);
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> slidingBreechIncomplete(String pathAndMaterial) {
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate(new IncompleteSlidingBreechBlockGen(pathAndMaterial)::generate);
	}
	
	public static <T extends Block & CannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> screwBreech(String pathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/screw_breech");
		ResourceLocation itemBaseLoc = CreateBigCannons.resource("block/screw_breech_item");
		ResourceLocation topLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_screw_breech_top");
		ResourceLocation bottomLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_screw_breech_bottom");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_screw_breech_side");
		ResourceLocation lockLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_screw_lock");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().withExistingParent(c.getName(), baseLoc)
					.texture("side", sideLoc)
					.texture("top", topLoc)
					.texture("bottom", bottomLoc)
					.texture("particle", topLoc)))
				.item(CannonBlockItem::new)
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(itemBaseLoc))
					.texture("side", sideLoc)
					.texture("top", topLoc)
					.texture("bottom", bottomLoc)
					.texture("lock", lockLoc))
				.build();
	}
	
	public static <T extends Block & CannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> screwBreechUnbored(String pathAndMaterial, String typePathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/screw_breech");
		ResourceLocation itemBaseLoc = CreateBigCannons.resource("block/screw_breech_item");
		ResourceLocation topLoc = CreateBigCannons.resource("block/" + typePathAndMaterial + "_screw_breech_top");
		ResourceLocation bottomLoc = CreateBigCannons.resource("block/" + typePathAndMaterial + "_screw_breech_bottom");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_screw_breech_side");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().withExistingParent(c.getName(), baseLoc)
					.texture("side", sideLoc)
					.texture("top", topLoc)
					.texture("bottom", bottomLoc)
					.texture("particle", topLoc)))
				.item(CannonBlockItem::new)
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(itemBaseLoc))
					.texture("side", sideLoc)
					.texture("top", topLoc)
					.texture("bottom", bottomLoc))
				.build();
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> screwBreechIncomplete(String pathAndMaterial) {
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate(new IncompleteScrewBreechBlockGen(pathAndMaterial)::generate);
	}
	
	public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> screwLock(String pathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("item/screw_lock");
		ResourceLocation lockLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_screw_lock");
		return b -> b.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(baseLoc))
				.texture("lock", lockLoc));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> ramHead() {
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate(BlockStateGen.directionalBlockProvider(false));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> wormHead() {
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate(BlockStateGen.directionalBlockProvider(false));
	}
	
	public static <T extends DirectionalAxisKineticBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonLoader() {
		ResourceLocation itemModelLoc = CreateBigCannons.resource("block/cannon_loader_item");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate(new CannonLoaderGen()::generate)
				.item()
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(itemModelLoc)))
				.build();
	}
	
	public static <T extends DirectionalAxisKineticBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonDrill() {
		ResourceLocation itemModelLoc = CreateBigCannons.resource("block/cannon_drill_item");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate(new CannonDrillGen()::generate)
				.item()
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(itemModelLoc)))
				.build();
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonDrillBit() {
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.loot((t, p) -> t.dropOther(p, AllBlocks.PISTON_EXTENSION_POLE.get()))
				.blockstate(BlockStateGen.directionalBlockProvider(false));
	}
	
	public static <T extends DirectionalAxisKineticBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBuilder() {
		ResourceLocation itemModelLoc = CreateBigCannons.resource("item/cannon_builder");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate(new CannonBuilderGen()::generate)
				.item()
				.model((c, p) -> p.getExistingFile(itemModelLoc))
				.build();
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBuilderHead() {
		ResourceLocation notAttachedLoc = CreateBigCannons.resource("block/cannon_builder/cannon_builder_head");
		ResourceLocation attachedLoc = CreateBigCannons.resource("block/cannon_builder/cannon_builder_head_attached");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.loot((t, p) -> t.dropOther(p, AllBlocks.PISTON_EXTENSION_POLE.get()))
				.blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, s -> p.models().getExistingFile(s.getValue(CannonBuilderHeadBlock.ATTACHED) ? attachedLoc : notAttachedLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> projectile(String pathAndMaterial) {
		return projectile(pathAndMaterial, true);
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> projectile(String pathAndMaterial, boolean useStandardModel) {
		ResourceLocation baseLoc = CreateBigCannons.resource(String.format("block/%sprojectile_block", useStandardModel ? "standard_" : ""));
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + pathAndMaterial);
		ResourceLocation topLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_top");
		ResourceLocation bottomLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_bottom");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::solid)
				.blockstate((c, p) -> {
					BlockModelBuilder builder = p.models().withExistingParent(c.getName(), baseLoc)
						.texture("side", sideLoc)
						.texture("top", topLoc)
						.texture("particle", topLoc);
					if (!useStandardModel) builder.texture("bottom", bottomLoc);
					p.directionalBlock(c.get(), builder);
				});
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> powderCharge() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/powder_charge");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::solid)
				.blockstate((c, p) -> BlockStateGen.axisBlock(c, p, $ -> p.models().getExistingFile(baseLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonMount() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/cannon_mount/cannon_mount");
		ResourceLocation itemModelLoc = CreateBigCannons.resource("block/cannon_mount/cannon_mount_item");		
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.horizontalBlock(c.get(), p.models().getExistingFile(baseLoc), 0))
				.item()
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(itemModelLoc)))
				.build();
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> yawController() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/cannon_mount/yaw_controller");
		ResourceLocation itemModelLoc = CreateBigCannons.resource("block/cannon_mount/yaw_controller_item");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.simpleBlock(c.get(), p.models().getExistingFile(baseLoc)))
				.item()
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(itemModelLoc)))
				.build();
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonCarriage() {
		ResourceLocation blockLoc = CreateBigCannons.resource("block/cannon_carriage/block");
		ResourceLocation saddleLoc = CreateBigCannons.resource("block/cannon_carriage/block_saddle");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.horizontalBlock(c.get(), s -> p.models().getExistingFile(s.getValue(CannonCarriageBlock.SADDLED) ? saddleLoc : blockLoc)))
				.item(CannonCarriageBlockItem::new)
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(blockLoc)))
				.build();
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> invisibleWithParticle(String path) {
		return b -> b.blockstate((c, p) -> p.simpleBlock(c.get(), p.models().getBuilder(c.getName())
				.texture("particle", CreateBigCannons.resource(path))));
	}
	
	public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> slidingBreechblock(String pathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("item/sliding_breechblock");
		ResourceLocation topLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_breechblock_top");
		ResourceLocation endLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_breechblock_end");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_breechblock_side");
		ResourceLocation bottomLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_sliding_breech_breechblock_bottom");
		return b -> b.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(baseLoc))
				.texture("top", topLoc)
				.texture("end", endLoc)
				.texture("side", sideLoc)
				.texture("bottom", bottomLoc));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> castMould(String size) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/cast_mould/" + size + "_cast_mould");
		ResourceLocation sandLoc = CreateBigCannons.resource("block/casting_sand");
		return b -> b.initialProperties(Material.WOOD, MaterialColor.PODZOL)
				.properties(p -> p.strength(2.0f, 3.0f))
				.properties(p -> p.sound(SoundType.WOOD))
				.properties(p -> p.noOcclusion())
				.tag(BlockTags.MINEABLE_WITH_AXE)
				.addLayer(() -> RenderType::solid)
				.blockstate((c, p) -> p.getMultipartBuilder(c.get())
						.part()
							.modelFile(p.models().getExistingFile(baseLoc))
							.addModel()
							.end()
						.part()
							.modelFile(p.models().getExistingFile(sandLoc))
							.addModel()
							.condition(CannonCastMouldBlock.SAND, true)
							.end())
				.item()
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(baseLoc)))
				.build();
	}
	
	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> castIronScrapLoot(int count) {
		return (t, u) -> t.add(u, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.CAST_IRON_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))))));
	}

	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> bronzeScrapLoot(int count) {
		return (t, u) -> t.add(u, LootTable.lootTable().withPool(LootPool.lootPool().add(TagEntry.expandTag(CBCTags.ItemCBC.NUGGET_BRONZE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))))));
	}
	
	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> steelScrapLoot(int count) {
		return (t, u) -> t.add(u, LootTable.lootTable().withPool(LootPool.lootPool().add(TagEntry.expandTag(CBCTags.ItemCBC.NUGGET_STEEL).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))))));
	}
	
	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> nethersteelScrapLoot(int count) {
		return (t, u) -> t.add(u, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(CBCItems.NETHERSTEEL_NUGGET.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))))));
	}
	
	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> shellLoot(NonNullFunction<CopyNbtFunction.Builder, CopyNbtFunction.Builder> additionalCopyData) {
		return (t, u) -> t.add(u, LootTable.lootTable()
			.withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0f))
				.add(LootItem.lootTableItem(u)
					.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
					.apply(additionalCopyData.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
						.copy("Fuze", "BlockEntityTag.Fuze"))))));
	}
	
	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> shellLoot() {
		return shellLoot(t -> t);
	}
	
}
