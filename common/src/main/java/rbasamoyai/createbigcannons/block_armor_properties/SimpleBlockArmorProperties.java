package rbasamoyai.createbigcannons.block_armor_properties;

import com.google.gson.JsonObject;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record SimpleBlockArmorProperties(double hardness, double toughness) implements BlockArmorPropertiesProvider {

	@Override
	public double hardness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		return this.hardness;
	}

	@Override
	public double toughness(Level level, BlockState state, BlockPos pos, boolean recurse) {
		return this.toughness;
	}

	public static SimpleBlockArmorProperties fromJson(JsonObject obj) {
		double hardness = Math.max(GsonHelper.getAsDouble(obj, "hardness", 1), 0);
		double toughness = Math.max(GsonHelper.getAsDouble(obj, "toughness", 1), 0);
		return new SimpleBlockArmorProperties(hardness, toughness);
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeDouble(this.hardness)
			.writeDouble(this.toughness);
	}

	public static SimpleBlockArmorProperties fromNetwork(FriendlyByteBuf buf) {
		double hardness = buf.readDouble();
		double toughness = buf.readDouble();
		return new SimpleBlockArmorProperties(hardness, toughness);
	}

}
