package rbasamoyai.createbigcannons.crafting.casting;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;

public class CannonCastShape extends ForgeRegistryEntry<CannonCastShape> {
	
	public static final DeferredRegister<CannonCastShape> CANNON_CAST_SHAPES = DeferredRegister.create(CBCRegistries.Keys.CANNON_CAST_SHAPES, CreateBigCannons.MOD_ID);
	
	private static final int INGOT_SIZE_MB = 90;
	
	public static final RegistryObject<CannonCastShape>	
		VERY_SMALL = CANNON_CAST_SHAPES.register("very_small", () -> new CannonCastShape(7 * INGOT_SIZE_MB, 12, CBCBlocks.VERY_SMALL_CAST_MOULD)),
		SMALL = CANNON_CAST_SHAPES.register("small", () -> new CannonCastShape(9 * INGOT_SIZE_MB, 14, CBCBlocks.SMALL_CAST_MOULD)),
		MEDIUM = CANNON_CAST_SHAPES.register("medium", () -> new CannonCastShape(12 * INGOT_SIZE_MB, 16, CBCBlocks.MEDIUM_CAST_MOULD)),
		LARGE = CANNON_CAST_SHAPES.register("large", () -> new CannonCastShape(14 * INGOT_SIZE_MB, 18, CBCBlocks.LARGE_CAST_MOULD)),
		VERY_LARGE = CANNON_CAST_SHAPES.register("very_large", () -> new CannonCastShape(20 * INGOT_SIZE_MB, 20, CBCBlocks.VERY_LARGE_CAST_MOULD)),
		CANNON_END = CANNON_CAST_SHAPES.register("cannon_end", () -> new CannonCastShape(9 * INGOT_SIZE_MB, 16, CBCBlocks.CANNON_END_CAST_MOULD)),
		SLIDING_BREECH = CANNON_CAST_SHAPES.register("sliding_breech", () -> new CannonCastShape(9 * INGOT_SIZE_MB, 16, CBCBlocks.SLIDING_BREECH_CAST_MOULD, PropertySetter.of(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE, false))),
		SCREW_BREECH = CANNON_CAST_SHAPES.register("screw_breech", () -> new CannonCastShape(9 * INGOT_SIZE_MB, 16, CBCBlocks.SCREW_BREECH_CAST_MOULD));
	
	private final int fluidSize;
	private final int diameter;
	private final NonNullSupplier<? extends Block> castMould;
	private final PropertySetter<?>[] properties;
	
	private Block resolvedCastMould;
	
	public CannonCastShape(int fluidSize, int diameter, NonNullSupplier<? extends Block> castMould, PropertySetter<?>... properties) {
		this.fluidSize = fluidSize;
		this.diameter = diameter;
		this.castMould = castMould;
		this.properties = properties;
	}
	
	public int fluidSize() { return this.fluidSize; }
	public int diameter() { return this.diameter; }
	
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
	
	@Override
	public String toString() {
		return "CannonCastShape[" + CBCRegistries.CANNON_CAST_SHAPES.get().getKey(this) + ",fluidSize=" + this.fluidSize + ",diameter=" + this.diameter + "]";
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
