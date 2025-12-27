package rbasamoyai.createbigcannons.base;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class PropertySetter<T extends Comparable<T>> {
	private final Property<T> property;
	private final T value;

	public PropertySetter(Property<T> property, T value) {
		this.property = property;
		this.value = value;
	}

	public BlockState applyTo(BlockState state) {
		return state.hasProperty(this.property) ? state.setValue(this.property, this.value) : state;
	}

	public static <T extends Comparable<T>> PropertySetter<T> of(Property<T> property, T value) {
		return new PropertySetter<>(property, value);
	}
}
