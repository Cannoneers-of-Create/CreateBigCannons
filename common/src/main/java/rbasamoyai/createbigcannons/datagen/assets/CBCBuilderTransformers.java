package rbasamoyai.createbigcannons.datagen.assets;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageBlock;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageBlockItem;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderGen;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBarrelBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlockItem;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlockItem;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechBlockGen;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillGen;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderGen;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderHeadBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastMouldBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteScrewBreechBlockGen;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteSlidingBreechBlockGen;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

public class CBCBuilderTransformers {

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBarrel(String material, boolean bored) {
		NonNullUnaryOperator<BlockBuilder<T, P>> b1 = cannonPart(CreateBigCannons.resource("block/cannon_barrel"),
			"cannon_barrel/" + material + "_cannon_barrel_side",
			"cannon_barrel/" + (bored ? "" : "unbored_") + material + "_cannon_barrel_end");
		return bored ? b1.andThen(b -> b.tag(CBCTags.CBCBlockTags.REDUCES_SPREAD)) : b1;
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> builtUpCannonBarrel(String material, boolean bored) {
		NonNullUnaryOperator<BlockBuilder<T, P>> b1 = cannonPart(CreateBigCannons.resource("block/built_up_cannon_barrel"),
			"cannon_barrel/built_up_" + material + "_cannon_barrel_side",
			"cannon_barrel/" + (bored ? "" : "unbored_") + "built_up_" + material + "_cannon_barrel_end");
		return bored ? b1.andThen(b -> b.tag(CBCTags.CBCBlockTags.REDUCES_SPREAD)) : b1;
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonChamber(String material, boolean bored) {
		NonNullUnaryOperator<BlockBuilder<T, P>> b1 = cannonPart(new ResourceLocation("block/cube_column"),
			"cannon_chamber/" + material + "_cannon_chamber_side",
			"cannon_chamber/" + (bored ? "" : "unbored_") + material + "_cannon_chamber_end");
		return bored ? b1.andThen(b -> b.tag(CBCTags.CBCBlockTags.THICK_TUBING).tag(CBCTags.CBCBlockTags.REDUCES_SPREAD)) : b1;
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> builtUpCannonChamber(String material, boolean bored) {
		NonNullUnaryOperator<BlockBuilder<T, P>> b1 = cannonPart(CreateBigCannons.resource("block/built_up_cannon_chamber"),
			"cannon_chamber/built_up_" + material + "_cannon_chamber_side",
			"cannon_chamber/" + (bored ? "" : "unbored_") + "built_up_" + material + "_cannon_chamber_end");
		return bored ? b1.andThen(b -> b.tag(CBCTags.CBCBlockTags.THICK_TUBING).tag(CBCTags.CBCBlockTags.REDUCES_SPREAD)) : b1;
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> thickCannonChamber(String material, boolean bored) {
		NonNullUnaryOperator<BlockBuilder<T, P>> b1 = cannonPart(CreateBigCannons.resource("block/thick_cannon_chamber"),
			"cannon_chamber/thick_" + material + "_cannon_chamber_side",
			"cannon_chamber/" + (bored ? "" : "unbored_") + "thick_" + material + "_cannon_chamber_end");
		return bored ? b1.andThen(b -> b.tag(CBCTags.CBCBlockTags.THICK_TUBING).tag(CBCTags.CBCBlockTags.REDUCES_SPREAD)) : b1;
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonPart(ResourceLocation model, String side, String end) {
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + side);
		ResourceLocation endLoc = CreateBigCannons.resource("block/" + end);
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().withExistingParent(c.getName(), model)
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

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> dropMortarEnd(String pathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/drop_mortar_end");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_end_side");
		ResourceLocation topLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_end_top");
		ResourceLocation bottomLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_end_bottom");
		ResourceLocation knobLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_end_knob");
		ResourceLocation spikeLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_drop_mortar_end_spike");
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().withExistingParent(c.getName(), baseLoc)
				.texture("spike", spikeLoc)
				.texture("side", sideLoc)
				.texture("top", topLoc)
				.texture("bottom", bottomLoc)
				.texture("knob", knobLoc)
				.texture("particle", topLoc)));
	}

	public static <T extends Block & BigCannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> slidingBreech(String pathAndMaterial) {
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
			.blockstate(new SlidingBreechBlockGen(pathAndMaterial)::generate)
			.item(BigCannonBlockItem::new)
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

	public static <T extends Block & BigCannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> screwBreech(String pathAndMaterial) {
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
			.item(BigCannonBlockItem::new)
			.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(itemBaseLoc))
				.texture("side", sideLoc)
				.texture("top", topLoc)
				.texture("bottom", bottomLoc)
				.texture("lock", lockLoc))
			.build();
	}

	public static <T extends Block & BigCannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> screwBreechUnbored(String pathAndMaterial, String typePathAndMaterial) {
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
			.item(BigCannonBlockItem::new)
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

	public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> autocannonBarrel(String pathAndMaterial) {
		ResourceLocation texLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon");
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, s -> {
				String name = c.getName() + "_" + (s.getValue(AutocannonBarrelBlock.ASSEMBLED) ? "assembled" : s.getValue(AutocannonBarrelBlock.BARREL_END).getSerializedName());
				if (s.getValue(AutocannonBarrelBlock.ASSEMBLED))
					return p.models().getBuilder(name).texture("particle", texLoc);
				ResourceLocation loc = switch (s.getValue(AutocannonBarrelBlock.BARREL_END)) {
					case FLANGED -> CreateBigCannons.resource("block/autocannon/barrel_flanged");
					default -> CreateBigCannons.resource("block/autocannon/barrel");
				};
				return p.models().withExistingParent(name, loc).texture("material", texLoc);
			}))
			.item(AutocannonBlockItem::new)
			.model((c, p) -> p.withExistingParent(c.getName(), CreateBigCannons.resource("block/autocannon/barrel")).texture("material", texLoc))
			.build();
	}

	public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> autocannonBreech(String pathAndMaterial, boolean complete) {
		ResourceLocation texLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon");
		ResourceLocation tex1Loc = CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon_1");
		ResourceLocation baseLoc = CreateBigCannons.resource("block/autocannon/breech");
		ResourceLocation handleLoc = CreateBigCannons.resource("block/autocannon/breech_handle");
		NonNullUnaryOperator<BlockBuilder<T, P>> result = b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p,
				s -> {
					boolean handle = s.hasProperty(AutocannonBreechBlock.HANDLE) && s.getValue(AutocannonBreechBlock.HANDLE);
					return p.models().withExistingParent(handle ? c.getName() + "_handle" : c.getName(), handle ? handleLoc : baseLoc)
						.texture("material", texLoc)
						.texture("handle", tex1Loc);
				}));
		if (complete) {
			result = result.andThen(b -> b.item(AutocannonBlockItem::new)
				.model((c, p) -> p.withExistingParent(c.getName(), CreateBigCannons.resource("block/autocannon/breech_item")).texture("material", texLoc))
				.build());
		} else {
			result = result.andThen(b -> b.item(AutocannonBlockItem::new)
				.model((c, p) -> p.blockItem(c))
				.build());
		}
		return result;
	}

	public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> autocannonRecoilSpring(String pathAndMaterial, boolean complete) {
		ResourceLocation texLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon");
		NonNullUnaryOperator<BlockBuilder<T, P>> result = b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p,
				$ -> p.models().withExistingParent(c.getName(), CreateBigCannons.resource("block/autocannon/recoil_spring")).texture("material", texLoc)));
		if (complete) {
			result = result.andThen(b -> b.item(AutocannonBlockItem::new)
				.model((c, p) -> p.withExistingParent(c.getName(), CreateBigCannons.resource("block/autocannon/recoil_spring_item")).texture("material", texLoc))
				.build());
		} else {
			result = result.andThen(b -> b.item(AutocannonBlockItem::new)
				.model((c, p) -> p.blockItem(c))
				.build());
		}
		return result;
	}

	public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> unboredAutocannonBarrel(String pathAndMaterial) {
		return unboredAutocannonBlock(CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon"),
			CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon_1"),
			CreateBigCannons.resource("block/autocannon/unbored_barrel"));
	}

	public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> unboredAutocannonRecoilSpring(String pathAndMaterial) {
		return unboredAutocannonBlock(CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon"),
			CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon_1"),
			CreateBigCannons.resource("block/autocannon/unbored_recoil_spring"));
	}

	public static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> unboredAutocannonBreech(String pathAndMaterial) {
		return unboredAutocannonBlock(CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon"),
			CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon_1"),
			CreateBigCannons.resource("block/autocannon/unbored_breech"));
	}

	private static <T extends Block & AutocannonBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> unboredAutocannonBlock(ResourceLocation tex, ResourceLocation tex1, ResourceLocation model) {
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p,
				$ -> p.models().withExistingParent(c.getName(), model)
					.texture("material", tex)
					.texture("material1", tex1)))
			.item(AutocannonBlockItem::new)
			.model((c, p) -> p.blockItem(c))
			.build();
	}

	public static <T extends Item, P> NonNullUnaryOperator<ItemBuilder<T, P>> autocannonBreechExtractor(String pathAndMaterial) {
		return b -> b.model((c, p) -> p.getBuilder(c.getName())
			.parent(p.getExistingFile(CreateBigCannons.resource("block/autocannon/extractor")))
			.texture("material", CreateBigCannons.resource("block/" + pathAndMaterial + "_autocannon")));
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
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate(new CannonLoaderGen()::generate)
			.item()
			.model((c, p) -> {})
			.build();
	}

	public static <T extends DirectionalAxisKineticBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonDrill() {
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate(new CannonDrillGen()::generate)
			.item()
			.model((c, p) -> {})
			.build();
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonDrillBit() {
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.loot((t, p) -> t.dropOther(p, AllBlocks.PISTON_EXTENSION_POLE.get()))
			.blockstate(BlockStateGen.directionalBlockProvider(false));
	}

	public static <T extends DirectionalAxisKineticBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBuilder() {
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate(new CannonBuilderGen()::generate)
			.item()
			.model((c, p) -> {})
			.build();
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBuilderHead() {
		ResourceLocation notAttachedLoc = CreateBigCannons.resource("block/cannon_builder/cannon_builder_head");
		ResourceLocation attachedLoc = CreateBigCannons.resource("block/cannon_builder/cannon_builder_head_attached");
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.loot((t, p) -> t.dropOther(p, AllBlocks.PISTON_EXTENSION_POLE.get()))
			.blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, s ->
				p.models().getExistingFile(s.getValue(CannonBuilderHeadBlock.ATTACHED) ? attachedLoc : notAttachedLoc)));
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> projectileLegacy(String pathAndMaterial) {
		return projectile(pathAndMaterial, true);
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> projectile(String pathAndMaterial) {
		return projectile(pathAndMaterial, false);
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

	public static <T extends Block & BigCannonPropellantBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> bigCartridge() {
		ResourceLocation filledLoc = CreateBigCannons.resource("block/big_cartridge_filled");
		ResourceLocation emptyLoc = CreateBigCannons.resource("block/big_cartridge_empty");
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::solid)
			.blockstate((c, p) -> BlockStateGen.directionalBlockIgnoresWaterlogged(c, p, s -> {
				return p.models().getExistingFile(s.getValue(BigCartridgeBlock.FILLED) ? filledLoc : emptyLoc);
			}))
			.loot((t, c) -> {
				((BlockLoot) t).add(c, LootTable.lootTable()
					.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(c))
						.apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
						.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Power", "Power"))));
			})
			.item(BigCartridgeBlockItem::new)
			.model((c, p) -> {
				p.withExistingParent(c.getName(), emptyLoc)
					.override().model(p.getExistingFile(filledLoc)).predicate(CreateBigCannons.resource("big_cartridge_filled"), 1).end();
			})
			.build();
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonMount() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/cannon_mount/cannon_mount");
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p) -> p.horizontalBlock(c.get(), p.models().getExistingFile(baseLoc), 0))
			.item()
			.model((c, p) -> {})
			.build();
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> yawController() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/cannon_mount/yaw_controller");
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p) -> p.simpleBlock(c.get(), p.models().getExistingFile(baseLoc)))
			.item()
			.model((c, p) -> {})
			.build();
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonCarriage() {
		ResourceLocation blockLoc = CreateBigCannons.resource("block/cannon_carriage/block");
		ResourceLocation saddleLoc = CreateBigCannons.resource("block/cannon_carriage/block_saddle");
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p) -> p.horizontalBlock(c.get(), s -> p.models().getExistingFile(s.getValue(CannonCarriageBlock.SADDLED) ? saddleLoc : blockLoc)))
			.loot((t, u) -> t.add(u, LootTable.lootTable()
				.withPool(LootPool.lootPool()
					.add(BlockLoot.applyExplosionDecay(u, LootItem.lootTableItem(u))))
				.withPool(LootPool.lootPool()
					.add(BlockLoot.applyExplosionDecay(u, LootItem.lootTableItem(Items.SADDLE)))
					.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(u)
						.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CannonCarriageBlock.SADDLED, true))))))
			.item(CannonCarriageBlockItem::new)
			.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(blockLoc)))
			.build();
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> invisibleWithParticle(String path) {
		return b -> b.properties(p -> p.noOcclusion())
			.blockstate((c, p) -> p.simpleBlock(c.get(), p.models().getBuilder(c.getName())
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
		return (t, u) -> t.add(u, BlockLoot.createSingleItemTableWithSilkTouch(u, CBCItems.CAST_IRON_NUGGET.get(), ConstantValue.exactly(count)));
	}

	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> bronzeScrapLoot(int count) {
		return (t, u) -> t.add(u, BlockLoot.createSingleItemTableWithSilkTouch(u, CBCItems.BRONZE_SCRAP.get(), ConstantValue.exactly(count)));
	}

	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> steelScrapLoot(int count) {
		return (t, u) -> t.add(u, BlockLoot.createSingleItemTableWithSilkTouch(u, CBCItems.STEEL_SCRAP.get(), ConstantValue.exactly(count)));
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
						.copy("Fuze", "BlockEntityTag.Fuze")
						.copy("id", "BlockEntityTag.id"))))));
	}

	public static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> shellLoot() {
		return shellLoot(t -> t);
	}

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> dropMortarShell() {
		return b -> b.properties(p -> p.noOcclusion())
			.addLayer(() -> RenderType::cutout)
			.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().getExistingFile(CreateBigCannons.resource("block/drop_mortar_shell"))));
	}

}
