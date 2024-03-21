package rbasamoyai.createbigcannons.block_terminal_properties;

import com.google.gson.JsonObject;

import net.minecraft.util.GsonHelper;

public record MimickingBlockTerminalBallisticsProperties(double emptyHardness, double materialHardnessMultiplier) {

	public static MimickingBlockTerminalBallisticsProperties fromJson(JsonObject obj) {
		double emptyHardness = Math.max(GsonHelper.getAsDouble(obj, "default_block_hardness", 1d), 0d);
		double materialHardnessMultiplier = Math.max(GsonHelper.getAsDouble(obj, "material_hardness_multiplier", 1d), 0d);
		return new MimickingBlockTerminalBallisticsProperties(emptyHardness, materialHardnessMultiplier);
	}

}
