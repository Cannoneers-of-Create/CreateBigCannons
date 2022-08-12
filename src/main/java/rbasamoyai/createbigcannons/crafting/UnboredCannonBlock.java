package rbasamoyai.createbigcannons.crafting;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.SolidCannonBlock;

public class UnboredCannonBlock extends SolidCannonBlock implements TransformableByBoring {

	private final NonNullSupplier<? extends Block> boredBlockSup;
	private Block boredBlock;
	private final VoxelShaper shapes;
	
	public UnboredCannonBlock(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup, VoxelShape baseShape) {
		super(properties, material);
		this.boredBlockSup = boredBlockSup;
		this.shapes = new AllShapes.Builder(baseShape).forDirectional();
	}
	
	public static UnboredCannonBlock verySmall(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup) {
		return new UnboredCannonBlock(properties, material, boredBlockSup, Block.box(2, 0, 2, 14, 16, 14));
	}
	
	public static UnboredCannonBlock medium(Properties properties, CannonMaterial material, NonNullSupplier<? extends Block> boredBlockSup) {
		return new UnboredCannonBlock(properties, material, boredBlockSup, Shapes.block());
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}

	@Override
	public BlockState getBoredBlockState(BlockState state, Level level, BlockPos pos) {
		if (this.boredBlock == null) {
			this.boredBlock = this.boredBlockSup.get();
		}
		BlockState bored = this.boredBlock.delegate.get().defaultBlockState();
		if (bored.hasProperty(FACING)) {
			bored = bored.setValue(FACING, state.getValue(FACING));
		}
		return bored;
	}

}
