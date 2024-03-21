package rbasamoyai.createbigcannons.block_hardness;

import com.google.gson.JsonObject;

import net.minecraft.core.BlockPos;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record SimpleTerminalBallisticsBlockProperties(double hardness) implements TerminalBallisticsBlockPropertiesProvider {
	@Override
	public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		return this.hardness;
	}

	public static SimpleTerminalBallisticsBlockProperties fromJson(String id, JsonObject obj) {
		double hardness = Math.max(GsonHelper.getAsDouble(obj, "block_hardness", 1), 0);
		return new SimpleTerminalBallisticsBlockProperties(hardness);
	}

}
