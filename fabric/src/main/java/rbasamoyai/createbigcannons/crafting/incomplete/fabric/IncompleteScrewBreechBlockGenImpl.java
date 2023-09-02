package rbasamoyai.createbigcannons.crafting.incomplete.fabric;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteScrewBreechBlock;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteScrewBreechBlockGen;

public class IncompleteScrewBreechBlockGenImpl extends IncompleteScrewBreechBlockGen {

	public static IncompleteScrewBreechBlockGen create(String pathAndMaterial) { return new IncompleteScrewBreechBlockGenImpl(pathAndMaterial); }

	public IncompleteScrewBreechBlockGenImpl(String pathAndMaterial) { super(pathAndMaterial); }

	@Override
	public <T extends Block> ModelFile getModel(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider, BlockState state) {
		int stage = state.getValue(IncompleteScrewBreechBlock.STAGE_2);
		String suf = stage == 0 ? "" : "_axis";
		ResourceLocation baseLoc = CreateBigCannons.resource("block/screw_breech" + suf);

		ResourceLocation topLoc = CreateBigCannons.resource("block/" + this.pathAndMaterial + "_screw_breech_top");
		ResourceLocation bottomLoc = CreateBigCannons.resource("block/" + this.pathAndMaterial + "_screw_breech_bottom");
		ResourceLocation sideLoc = CreateBigCannons.resource("block/" + this.pathAndMaterial + "_screw_breech_side");
		return provider.models().withExistingParent(context.getName() + suf, baseLoc)
			.texture("side", sideLoc)
			.texture("top", topLoc)
			.texture("bottom", bottomLoc)
			.texture("particle", topLoc);
	}

}
