package rbasamoyai.createbigcannons.base;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockStatePredicateHelper {

	private static final Splitter COMMA_SPLITTER = Splitter.on(',');
	private static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);

	// Taken from ModelBakery
	public static Predicate<BlockState> variantPredicate(StateDefinition<Block, BlockState> container, String variant) {
		Map<Property<?>, Comparable<?>> map = Maps.newHashMap();

		for(String s : COMMA_SPLITTER.split(variant)) {
			Iterator<String> iterator = EQUAL_SPLITTER.split(s).iterator();
			if (!iterator.hasNext()) continue;
			String s1 = iterator.next();
			Property<?> property = container.getProperty(s1);
			if (property != null && iterator.hasNext()) {
				String s2 = iterator.next();
				Comparable<?> comparable = getValueHelper(property, s2);
				if (comparable == null) {
					throw new RuntimeException("Unknown value: '" + s2 + "' for blockstate property: '" + s1 + "' " + property.getPossibleValues());
				}
				map.put(property, comparable);
			} else if (!s1.isEmpty()) {
				throw new RuntimeException("Unknown blockstate property: '" + s1 + "'");
			}
		}

		Block block = container.getOwner();
		return arg2 -> {
			if (arg2 == null || !arg2.is(block)) return false;
			for(Map.Entry<Property<?>, Comparable<?>> entry : map.entrySet()) {
				if (!Objects.equals(arg2.getValue((Property) entry.getKey()), entry.getValue())) {
					return false;
				}
			}
			return true;
		};
	}

	@Nullable
	private static <T extends Comparable<T>> T getValueHelper(Property<T> property, String value) {
		return property.getValue(value).orElse(null);
	}

}
