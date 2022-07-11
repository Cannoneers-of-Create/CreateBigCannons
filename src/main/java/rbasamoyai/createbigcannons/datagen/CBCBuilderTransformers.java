package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.repack.registrate.builders.BlockBuilder;
import com.simibubi.create.repack.registrate.providers.DataGenContext;
import com.simibubi.create.repack.registrate.providers.RegistrateItemModelProvider;
import com.simibubi.create.repack.registrate.util.nullness.NonNullBiConsumer;
import com.simibubi.create.repack.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderGen;

public class CBCBuilderTransformers {

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBarrel(String material) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/cannon_barrel");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + material + "_cannon_barrel_side");
		ResourceLocation endLoc = CreateBigCannons.resource("block/" + material + "_cannon_barrel_end");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> BlockStateGen.axisBlock(c, p, $ -> p.models().withExistingParent(c.getName(), baseLoc)
					.texture("side", sideLoc)
					.texture("end", endLoc)
					.texture("particle", sideLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonChamber(String material) {
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + material + "_cannon_chamber_side");
		ResourceLocation endLoc = CreateBigCannons.resource("block/" + material + "_cannon_chamber_end");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> BlockStateGen.axisBlock(c, p, $ -> p.models().withExistingParent(c.getName(), "block/cube_column")
						.texture("side", sideLoc)
						.texture("end", endLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> ramHead() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/ram_head");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().getExistingFile(baseLoc)));
	}
	
	public static <T extends DirectionalAxisKineticBlock, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonLoader() {
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate(new CannonLoaderGen()::generate);
	}
	
	public static NonNullBiConsumer<DataGenContext<Item, BlockItem>, RegistrateItemModelProvider> cannonLoaderItemModel() {
		return (c, p) -> {
			ResourceLocation itemLoc = CreateBigCannons.resource("cannon_loader");
			ResourceLocation parentLoc = CreateBigCannons.resource("block/cannon_loader_item");
			p.getBuilder(itemLoc.toString()).parent(p.getExistingFile(parentLoc));
		};
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> powderCharge() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/powder_charge");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> BlockStateGen.axisBlock(c, p, $ -> p.models().getExistingFile(baseLoc)));
	}
	
}
