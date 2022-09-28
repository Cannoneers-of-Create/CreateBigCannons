package rbasamoyai.createbigcannons.crafting.casting;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonCastShape {
	
	private static final Map<ResourceLocation, CannonCastShape> SHAPES = new HashMap<>();
	public static final CannonCastShape	
		VERY_SMALL = register(CreateBigCannons.resource("very_small"), 1008, 12, CBCBlocks.VERY_SMALL_CAST_MOULD),
		SMALL = register(CreateBigCannons.resource("small"), 1296, 14, CBCBlocks.SMALL_CAST_MOULD),
		MEDIUM = register(CreateBigCannons.resource("medium"), 1728, 16, CBCBlocks.MEDIUM_CAST_MOULD),
		LARGE = register(CreateBigCannons.resource("large"), 2016, 18, CBCBlocks.LARGE_CAST_MOULD),
		VERY_LARGE = register(CreateBigCannons.resource("very_large"), 2880, 20, CBCBlocks.VERY_LARGE_CAST_MOULD),
		CANNON_END = register(CreateBigCannons.resource("cannon_end"), 1296, 16, CBCBlocks.CANNON_END_CAST_MOULD),
		SLIDING_BREECH = register(CreateBigCannons.resource("sliding_breech"), 1296, 16, CBCBlocks.SLIDING_BREECH_CAST_MOULD, PropertySetter.of(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE, false)),
		SCREW_BREECH = register(CreateBigCannons.resource("screw_breech"), 1296, 16, CBCBlocks.SCREW_BREECH_CAST_MOULD);
	
	private final int fluidSize;
	private final int diameter;
	private final ResourceLocation name;
	private final Supplier<? extends Block> castMould;
	private final PropertySetter<?>[] properties;
	
	private Block resolvedCastMould;
	
	private CannonCastShape(ResourceLocation name, int fluidSize, int diameter, Supplier<? extends Block> castMould, PropertySetter<?>... properties) {
		this.fluidSize = fluidSize;
		this.diameter = diameter;
		this.name = name;
		this.castMould = castMould;
		this.properties = properties;
	}
	
	public static CannonCastShape register(ResourceLocation name, int fluidSize, int diameter, Supplier<? extends Block> castMould, PropertySetter<?>... properties) {
		if (SHAPES.containsKey(name)) {
			throw new IllegalStateException("Duplicate cannon cast shape " + name.toString());
		}
		CannonCastShape shape = new CannonCastShape(name, fluidSize, diameter, castMould, properties);
		SHAPES.put(name, shape);
		return shape;
	}
	
	public int fluidSize() { return this.fluidSize; }
	public int diameter() { return this.diameter; }
	public ResourceLocation name() { return this.name; }
	
	public Block castMould() {
		if (this.resolvedCastMould == null) {
			this.resolvedCastMould = this.castMould.get();
			if (this.resolvedCastMould == null) this.resolvedCastMould = Blocks.AIR;
		}
		return this.resolvedCastMould;
	}
	
	public BlockState applyTo(BlockState state) {
		for (PropertySetter<?> setter : this.properties) {
			state = setter.applyTo(state);
		}
		return state;
	}
	
	public static CannonCastShape byId(ResourceLocation name) { return SHAPES.get(name); }
	
	public static void register() {}
	
	@Override
	public String toString() {
		return "CannonCastShape[" + this.name + ",fluidSize=" + this.fluidSize + ",diameter=" + this.diameter + "]";
	}
	
	public static class PropertySetter<T extends Comparable<T>> {
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
	
}
