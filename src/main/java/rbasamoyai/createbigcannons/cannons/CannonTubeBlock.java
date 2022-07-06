package rbasamoyai.createbigcannons.cannons;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CBCBlockEntities;

public class CannonTubeBlock extends RotatedPillarBlock implements EntityBlock {
	
	private final CannonMaterial material;
	
	public CannonTubeBlock(BlockBehaviour.Properties properties, CannonMaterial material) {
		super(properties);
		this.material = material;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CannonBlockEntity(CBCBlockEntities.CANNON.get(), pos, state);
	}
	
	public CannonMaterial material() { return this.material; }
	
}
