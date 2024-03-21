package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.google.gson.JsonObject;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.block_hardness.HasSpecialTerminalBallisticsBlockProperties;
import rbasamoyai.createbigcannons.block_hardness.TerminalBallisticsBlockPropertiesHandler;

@Mixin(CopycatBlock.class)
public abstract class CopycatBlockMixin implements HasSpecialTerminalBallisticsBlockProperties {

	@Unique private double createbigcannons$emptyHardness = 1;
	@Unique private double createbigcannons$materialHardnessMultiplier = 1;

	@Override
	public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		BlockState copiedState = CopycatBlock.getMaterial(level, pos);
		if (!recurse || copiedState.isAir() || AllBlocks.COPYCAT_BASE.has(copiedState))
			return this.createbigcannons$emptyHardness;
		return TerminalBallisticsBlockPropertiesHandler.getProperties(copiedState).hardness(level, state, pos, false)
			* this.createbigcannons$materialHardnessMultiplier;
	}

	@Override
	public void loadTerminalBallisticsBlockPropertiesFromJson(String id, JsonObject obj) {
		this.createbigcannons$emptyHardness = Math.max(GsonHelper.getAsDouble(obj, "default_block_hardness", 1d), 0d);
		this.createbigcannons$materialHardnessMultiplier = Math.max(GsonHelper.getAsDouble(obj, "material_hardness_multiplier", 1d), 0d);
	}

}
