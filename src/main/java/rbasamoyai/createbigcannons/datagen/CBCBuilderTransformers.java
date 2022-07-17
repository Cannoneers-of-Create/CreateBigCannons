package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.repack.registrate.builders.BlockBuilder;
import com.simibubi.create.repack.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderGen;
import rbasamoyai.createbigcannons.cannons.cannonend.SlidingBreechBlockGen;

public class CBCBuilderTransformers {

	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBarrel(String pathAndMaterial) {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/cannon_barrel");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_barrel_side");
		ResourceLocation endLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_barrel_end");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> BlockStateGen.axisBlock(c, p, $ -> p.models().withExistingParent(c.getName(), baseLoc)
					.texture("side", sideLoc)
					.texture("end", endLoc)
					.texture("particle", sideLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonChamber(String pathAndMaterial) {
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_chamber_side");
		ResourceLocation endLoc = CreateBigCannons.resource("block/" + pathAndMaterial + "_cannon_chamber_end");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> BlockStateGen.axisBlock(c, p, $ -> p.models().withExistingParent(c.getName(), "block/cube_column")
						.texture("side", sideLoc)
						.texture("end", endLoc)
						.texture("particle", sideLoc)));
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
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> slidingBreech(String pathAndMaterial) {
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
				.item()
				.model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(itemBaseLoc))
					.texture("hole", holeLoc)
					.texture("side", sideLoc)
					.texture("side_hole", sideHoleLoc)
					.texture("inside", insideLoc)
					.texture("breechblock_top", breechblockTopLoc)
					.texture("breechblock_end", breechblockEndLoc)
					.texture("breechblock_side", breechblockSideLoc)
					.texture("breechblock_bottom", breechblockBottomLoc)
					.texture("particle", sideLoc))
				.build();
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> ramHead() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/ram_head");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().getExistingFile(baseLoc)));
	}
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> wormHead() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/worm_head");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::cutoutMipped)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().getExistingFile(baseLoc)));
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
	
	public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> solidShot() {
		ResourceLocation baseLoc = CreateBigCannons.resource("block/solid_shot");
		return b -> b.properties(p -> p.noOcclusion())
				.addLayer(() -> RenderType::solid)
				.blockstate((c, p) -> p.directionalBlock(c.get(), p.models().getExistingFile(baseLoc)));
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
	
}
