package rbasamoyai.createbigcannons.cannons;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CannonBarrelBlock extends CannonTubeBlock {

	private static final EnumProperty<Axis> AXIS = RotatedPillarBlock.AXIS;
	
	private final VoxelShape[] shapes;
	
	public CannonBarrelBlock(BlockBehaviour.Properties properties, CannonMaterial material) {
		super(properties, material);
		this.shapes = this.makeShapes();
	}
	
	protected VoxelShape[] makeShapes() {
		VoxelShape[] ashapes = new VoxelShape[Axis.VALUES.length];
		ashapes[Axis.X.ordinal()] = Block.box(0, 2, 2, 16, 14, 14);
		ashapes[Axis.Y.ordinal()] = Block.box(2, 0, 2, 14, 16, 14);
		ashapes[Axis.Z.ordinal()] = Block.box(2, 2, 0, 14, 14, 16);
		return ashapes;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes[state.getValue(AXIS).ordinal()];
	}
	
}
