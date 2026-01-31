package rbasamoyai.createbigcannons.block_hit_effects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.simibubi.create.foundation.utility.BlockHelper;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import rbasamoyai.createbigcannons.base.PropertySetter;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class BlockTransformation {

	private static final Transform NO_TRANSFORM = new Transform() {
		@Override public BlockState apply(BlockState blockState) { return blockState; }
	};

	private final Map<Transform, Float> weightedTransforms;
	private final float weightSum;
	private final float chance;

	public BlockTransformation(Map<Transform, Float> weightedTransforms, float chance) {
		this.weightedTransforms = weightedTransforms;
		float sum = 0;
		for (Float f : this.weightedTransforms.values())
			sum += f;
		this.weightSum = sum;
		this.chance = chance;
	}

	public BlockState transformBlock(BlockState inputBlockState) {
		if (Math.random() > this.chance)
			return inputBlockState;
		float sum = this.weightSum;
		Transform transform = NO_TRANSFORM;
		for (Map.Entry<Transform, Float> entry : this.weightedTransforms.entrySet()) {
			float weight = entry.getValue();
			if (Math.random() * sum <= weight) {
				transform = entry.getKey();
				break;
			}
			sum -= weight;
		}
		return transform.apply(inputBlockState);
	}

	public static BlockTransformation fromJson(JsonObject obj) throws JsonParseException {
		JsonArray array = GsonHelper.getAsJsonArray(obj, "transforms");
		Map<Transform, Float> weightedTransforms = new Object2ObjectLinkedOpenHashMap<>();
		for (JsonElement el : array) {
			JsonObject transformObj = el.getAsJsonObject();
			Transform transform = Transform.optionalFromJson(GsonHelper.getAsJsonObject(transformObj, "transform"));
			float weight = GsonHelper.getAsFloat(transformObj, "weight", 1);
			weightedTransforms.put(transform, weight);
		}
		float chance = Mth.clamp(GsonHelper.getAsFloat(obj, "transform_chance", 1), 0, 1);
		return new BlockTransformation(weightedTransforms, chance);
	}

	public interface Transform extends UnaryOperator<BlockState> {
		record Impl(Block base, List<PropertySetter<?>> propertySetters, boolean copyProperties) implements Transform {
			public BlockState apply(BlockState inputBlockState) {
				BlockState outputState = this.base.defaultBlockState();
				if (this.copyProperties)
					outputState = BlockHelper.copyProperties(inputBlockState, outputState);
				for (PropertySetter<?> setter : this.propertySetters)
					outputState = setter.applyTo(outputState);
				return outputState;
			}
		}

		private static Transform optionalFromJson(JsonObject obj) throws JsonParseException {
			String id = GsonHelper.getAsString(obj, "block");
			ResourceLocation blockLocation = CBCUtils.location(id);
			Optional<Block> optional = CBCRegistryUtils.getOptionalBlock(blockLocation);
			if (optional.isEmpty())
				return NO_TRANSFORM;
			Block block = optional.get();
			boolean copy = GsonHelper.getAsBoolean(obj, "copy_properties", false);
			List<PropertySetter<?>> propertySetters = new ArrayList<>();
			JsonArray arr = GsonHelper.getAsJsonArray(obj, "properties", new JsonArray());
			StateDefinition<Block, BlockState> definition = block.getStateDefinition();
			for (JsonElement el : arr) {
				JsonObject setterObj = el.getAsJsonObject();
				String propertyName = GsonHelper.getAsString(setterObj, "property");
				Property<?> property = definition.getProperty(propertyName);
				if (property == null)
					throw new JsonParseException("Block " + blockLocation + " does not have property \"" + propertyName + "\"");
				String value = GsonHelper.getAsString(setterObj, "value");
				PropertySetter<?> setter = getSetterHelper(property, value);
				propertySetters.add(setter);
			}
			return new Impl(block, propertySetters, copy);
		}

		// Adapted from NbtUtils --ritchie
		private static <T extends Comparable<T>> PropertySetter<T> getSetterHelper(Property<T> property, String value) {
			Optional<T> optional = property.getValue(value);
			if (optional.isEmpty())
				throw new JsonParseException("Unable to read property: %s with value: %s".formatted(property, value));
			return PropertySetter.of(property, optional.get());
		}
	}

}
