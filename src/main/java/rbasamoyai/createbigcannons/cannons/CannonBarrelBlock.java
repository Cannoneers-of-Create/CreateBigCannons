package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CannonBarrelBlock extends CannonTubeBlock {
	
	private final VoxelShaper shapes;
	
	public CannonBarrelBlock(Properties properties, CannonMaterial material) {
		super(properties, material);
		this.shapes = this.makeShapes();
	}
	
	protected VoxelShaper makeShapes() {
		VoxelShape base = Block.box(2, 0, 2, 14, 16, 14);
		return new AllShapes.Builder(base).forDirectional();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(this.getFacing(state));
	}
	
}
