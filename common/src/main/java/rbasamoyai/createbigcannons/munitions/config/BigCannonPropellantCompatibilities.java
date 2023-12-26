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

public record BigCannonPropellantCompatibilities(Map<Block, Integer> validPropellantCounts) {

	public static BigCannonPropellantCompatibilities DEFAULT = new BigCannonPropellantCompatibilities(ImmutableMap.of());

	public static BigCannonPropellantCompatibilities fromJson(JsonObject obj, @Nullable BigCannonPropellantCompatibilities previous,
															  boolean replaceValues) {
		if (previous == null) previous = DEFAULT;
		ImmutableMap.Builder<Block, Integer> builder = ImmutableMap.builder();
		if (!replaceValues) builder.putAll(previous.validPropellantCounts);

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
		return new BigCannonPropellantCompatibilities(builder.build());
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
		return obj;
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(this.validPropellantCounts.size());
		for (Map.Entry<Block, Integer> e : this.validPropellantCounts.entrySet()) {
			ResourceLocation loc = Registry.BLOCK.getKey(e.getKey());
			buf.writeUtf(loc.toString()).writeVarInt(e.getValue());
		}
	}

	public static BigCannonPropellantCompatibilities readBuf(FriendlyByteBuf buf) {
		int sz = buf.readVarInt();
		ImmutableMap.Builder<Block, Integer> builder = ImmutableMap.builder();
		for (int i = 0; i < sz; ++i) {
			ResourceLocation loc = new ResourceLocation(buf.readUtf());
			Optional<Block> block = Registry.BLOCK.getOptional(loc);
			if (block.isEmpty()) continue;
			int count = buf.readVarInt();
			builder.put(block.get(), count);
		}
		return new BigCannonPropellantCompatibilities(builder.build());
	}

}
