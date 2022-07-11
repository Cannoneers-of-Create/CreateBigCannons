package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import rbasamoyai.createbigcannons.CBCBlockEntities;

public class CannonTubeBlock extends RotatedPillarBlock implements ITE<CannonBlockEntity>, IWrenchable {
	
	private final CannonMaterial material;
	
	public CannonTubeBlock(BlockBehaviour.Properties properties, CannonMaterial material) {
		super(properties);
		this.material = material;
	}
	
	public CannonMaterial material() { return this.material; }

	@Override
	public Class<CannonBlockEntity> getTileEntityClass() {
		return CannonBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends CannonBlockEntity> getTileEntityType() {
		return CBCBlockEntities.CANNON.get();
	}
	
}
