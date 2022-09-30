package rbasamoyai.createbigcannons.crafting.boring;

import java.util.function.Supplier;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.SolidCannonBlock;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEndBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class UnboredCannonBlock extends SolidCannonBlock<CannonEndBlockEntity> implements TransformableByBoring {

	private final NonNullSupplier<? extends Block> boredBlockSup;
	private Block boredBlock;
	private final VoxelShaper shapes;
	private final Supplier<CannonCastShape> cannonShape;
	
	public UnboredCannonBlock(Properties properties, CannonMaterial material, Supplier<CannonCastShape> cannonShape, NonNullSupplier<? extends Block> boredBlockSup, VoxelShape baseShape) {
		super(properties, material);
		this.boredBlockSup = boredBlockSup;
		this.shapes = new AllShapes.Builder(baseShape).forDirectional();
		this.cannonShape = cannonShape;
	}
	
	public static UnboredCannonBlock verySmall(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup) {
		return new UnboredCannonBlock(properties, material, CannonCastShape.VERY_SMALL, boredBlockSup, box(2, 0, 2, 14, 16, 14));
	}

	public static UnboredCannonBlock small(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup) {
		return new UnboredCannonBlock(properties, material, CannonCastShape.SMALL, boredBlockSup, box(1, 0, 1, 15, 16, 15));
	}
	
	public static UnboredCannonBlock medium(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup) {
		return new UnboredCannonBlock(properties, material, CannonCastShape.MEDIUM, boredBlockSup, Shapes.block());
	}

	public static UnboredCannonBlock large(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup) {
		return new UnboredCannonBlock(properties, material, CannonCastShape.LARGE, boredBlockSup, box(-1, 0, -1, 17, 16, 17));
	}

	public static UnboredCannonBlock veryLarge(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup) {
		return new UnboredCannonBlock(properties, material, CannonCastShape.VERY_LARGE, boredBlockSup, box(-2, 0, -2, 18, 16, 18));
	}
		
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}
	
	@Override public CannonCastShape getCannonShape() { return this.cannonShape.get(); }

	@Override
	public BlockState getBoredBlockState(BlockState state) {
		if (this.boredBlock == null) {
			this.boredBlock = this.boredBlockSup.get();
		}
		BlockState bored = this.boredBlock.delegate.get().defaultBlockState();
		return bored.hasProperty(FACING) ? bored.setValue(FACING, state.getValue(FACING)) : bored;
	}
	
	@Override public boolean isComplete(BlockState state) { return false; }

	@Override public Class<CannonEndBlockEntity> getTileEntityClass() { return CannonEndBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonEndBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_END.get(); }

}
