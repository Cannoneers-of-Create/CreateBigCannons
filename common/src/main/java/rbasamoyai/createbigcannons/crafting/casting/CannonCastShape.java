package rbasamoyai.createbigcannons.crafting.casting;

import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.base.PropertySetter;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class CannonCastShape {

	private static final int INGOT_SIZE_MB = 90;

	public static final CannonCastShape
		VERY_SMALL = register("very_small", new CannonCastShape(7 * INGOT_SIZE_MB, 12, CBCBlocks.VERY_SMALL_CAST_MOULD)),
		SMALL = register("small", new CannonCastShape(9 * INGOT_SIZE_MB, 14, CBCBlocks.SMALL_CAST_MOULD)),
		MEDIUM = register("medium", new CannonCastShape(12 * INGOT_SIZE_MB, 16, CBCBlocks.MEDIUM_CAST_MOULD)),
		LARGE = register("large", new CannonCastShape(14 * INGOT_SIZE_MB, 18, CBCBlocks.LARGE_CAST_MOULD)),
		VERY_LARGE = register("very_large", new CannonCastShape(20 * INGOT_SIZE_MB, 20, CBCBlocks.VERY_LARGE_CAST_MOULD)),
		CANNON_END = register("cannon_end", new CannonCastShape(9 * INGOT_SIZE_MB, 16, CBCBlocks.CANNON_END_CAST_MOULD)),
		SLIDING_BREECH = register("sliding_breech", new CannonCastShape(9 * INGOT_SIZE_MB, 16, CBCBlocks.SLIDING_BREECH_CAST_MOULD, PropertySetter.of(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE, false))),
		SCREW_BREECH = register("screw_breech", new CannonCastShape(9 * INGOT_SIZE_MB, 16, CBCBlocks.SCREW_BREECH_CAST_MOULD, true, false)),
		DROP_MORTAR_END = register("drop_mortar_end", new CannonCastShape(9 * INGOT_SIZE_MB, 16, () -> Blocks.AIR)),

	AUTOCANNON_BARREL = register("autocannon_barrel", new CannonCastShape(3 * INGOT_SIZE_MB, 4, CBCBlocks.AUTOCANNON_BARREL_CAST_MOULD, false, false, PropertySetter.of(BlockStateProperties.FACING, Direction.UP))),
		AUTOCANNON_BARREL_FLANGED = register("autocannon_barrel_flanged", new CannonCastShape(3 * INGOT_SIZE_MB, 4, () -> Blocks.AIR, false, false)),
		AUTOCANNON_BREECH = register("autocannon_breech", new CannonCastShape(4 * INGOT_SIZE_MB, 8, CBCBlocks.AUTOCANNON_BREECH_CAST_MOULD, false, false, PropertySetter.of(BlockStateProperties.FACING, Direction.UP))),
		AUTOCANNON_RECOIL_SPRING = register("autocannon_recoil_spring", new CannonCastShape(4 * INGOT_SIZE_MB, 6, CBCBlocks.AUTOCANNON_RECOIL_SPRING_CAST_MOULD, false, false, PropertySetter.of(BlockStateProperties.FACING, Direction.UP)));

	private final int fluidSize;
	private final int diameter;
	private final NonNullSupplier<? extends Block> castMould;
	private final boolean isLarge;
	private final PropertySetter<?>[] properties;
	private final boolean texturesCanConnect;

	private Block resolvedCastMould;


	/**
	 * For old, big cannons. Is large (3x3) by default.
	 *
	 * @param fluidSizeForge
	 * @param diameter
	 * @param castMould
	 * @param properties
	 */

	public CannonCastShape(int fluidSizeForge, int diameter, NonNullSupplier<? extends Block> castMould, PropertySetter<?>... properties) {
		this(fluidSizeForge, diameter, castMould, true, true, properties);
	}

	public CannonCastShape(int fluidSizeForge, int diameter, NonNullSupplier<? extends Block> castMould, boolean large, boolean texturesCanConnect,
						   PropertySetter<?>... properties) {
		this.fluidSize = IndexPlatform.convertFluid(fluidSizeForge);
		this.diameter = diameter;
		this.castMould = castMould;
		this.isLarge = large;
		this.properties = properties;
		this.texturesCanConnect = texturesCanConnect;
	}

	public int fluidSize() {
		return this.fluidSize;
	}

	public int diameter() {
		return this.diameter;
	}

	public boolean isLarge() {
		return this.isLarge;
	}
	public boolean texturesCanConnect() { return this.texturesCanConnect; }

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
		return "CannonCastShape[" + CBCRegistries.CANNON_CAST_SHAPES.getKey(this) + ",fluidSize=" + this.fluidSize + ",diameter=" + this.diameter + "]";
	}

	public static void register() {
	}

	private static CannonCastShape register(String id, CannonCastShape shape) {
		return Registry.register(CBCRegistries.CANNON_CAST_SHAPES, CreateBigCannons.resource(id), shape);
	}

}
