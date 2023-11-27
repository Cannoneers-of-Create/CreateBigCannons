package rbasamoyai.createbigcannons.munitions.config;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;

public record BigCannonPropellantProperties(Map<Block, Integer> validPropellantCounts, float strength, float addedStress,
											float addedRecoil, float addedSpread) {

	public static BigCannonPropellantProperties DEFAULT = new BigCannonPropellantProperties(ImmutableMap.of(), 2, 1, 2, 2);

	public static BigCannonPropellantProperties fromJson(JsonObject obj, @Nullable BigCannonPropellantProperties previous,
														 boolean replaceStats, boolean replacePropellant) {
		if (previous == null) previous = DEFAULT;
		ImmutableMap.Builder<Block, Integer> builder = ImmutableMap.builder();
		if (!replacePropellant) builder.putAll(previous.validPropellantCounts);

		JsonArray validPropellants = GsonHelper.getAsJsonArray(obj, "propellant", new JsonArray());
		for (JsonElement el : validPropellants) {
			if (!el.isJsonObject()) continue;
			JsonObject obj1 = el.getAsJsonObject();
			if (!obj1.has("type") && !obj1.getAsJsonPrimitive("type").isString()) continue;
			ResourceLocation loc = new ResourceLocation(obj1.getAsJsonPrimitive("type").getAsString());
			Block block = Registry.BLOCK.getOptional(loc).orElseThrow(() -> {
				return new JsonSyntaxException("Unknown block '" + loc + "'");
			});
			int maxCount = Math.max(-1, GsonHelper.getAsInt(obj1, "maximum_amount", -1));
			builder.put(block, maxCount);
		}
		float strength = replaceStats ? Math.max(0, GsonHelper.getAsFloat(obj, "strength", previous.strength)) : previous.strength;
		float addedStress = replaceStats ? Math.max(0, GsonHelper.getAsFloat(obj, "added_stress", previous.addedStress)) : previous.addedStress;
		float addedRecoil = replaceStats ? Math.max(0, GsonHelper.getAsFloat(obj, "added_recoil", previous.addedRecoil)) : previous.addedRecoil;
		float addedSpread = replaceStats ? Math.max(0, GsonHelper.getAsFloat(obj, "added_spread", previous.addedSpread)) : previous.addedSpread;

		return new BigCannonPropellantProperties(builder.build(), strength, addedStress, addedRecoil, addedSpread);
	}

	public JsonElement serialize() {
		JsonObject obj = new JsonObject();
		JsonArray propellants = new JsonArray();
		for (Map.Entry<Block, Integer> e : this.validPropellantCounts.entrySet()) {
			JsonObject obj1 = new JsonObject();
			obj1.addProperty("type", Registry.BLOCK.getKey(e.getKey()).toString());
			obj1.addProperty("maximum_amount", e.getValue());
		}
		obj.add("propellant", propellants);
		obj.addProperty("strength", this.strength);
		obj.addProperty("added_stress", this.addedStress);
		obj.addProperty("added_recoil", this.addedRecoil);
		obj.addProperty("added_spread", this.addedSpread);
		return obj;
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(this.validPropellantCounts.size());
		for (Map.Entry<Block, Integer> e : this.validPropellantCounts.entrySet()) {
			ResourceLocation loc = Registry.BLOCK.getKey(e.getKey());
			buf.writeUtf(loc.toString()).writeVarInt(e.getValue());
		}
		buf.writeFloat(this.strength).writeFloat(this.addedStress).writeFloat(this.addedRecoil).writeFloat(this.addedSpread);
	}

	public static BigCannonPropellantProperties readBuf(FriendlyByteBuf buf) {
		int sz = buf.readVarInt();
		ImmutableMap.Builder<Block, Integer> builder = ImmutableMap.builder();
		for (int i = 0; i < sz; ++i) {
			ResourceLocation loc = new ResourceLocation(buf.readUtf());
			Optional<Block> block = Registry.BLOCK.getOptional(loc);
			if (block.isEmpty()) continue;
			int count = buf.readVarInt();
			builder.put(block.get(), count);
		}
		float strength = buf.readFloat();
		float addedStress = buf.readFloat();
		float addedRecoil = buf.readFloat();
		float addedSpread = buf.readFloat();
		return new BigCannonPropellantProperties(builder.build(), strength, addedStress, addedRecoil, addedSpread);
	}

}
