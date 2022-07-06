package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.repack.registrate.builders.BlockBuilder;
import com.simibubi.create.repack.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;

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
	
}
