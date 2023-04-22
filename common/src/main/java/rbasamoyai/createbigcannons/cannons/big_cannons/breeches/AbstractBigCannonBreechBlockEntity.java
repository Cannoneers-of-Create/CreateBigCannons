package rbasamoyai.createbigcannons.cannons.big_cannons.breeches;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;

import java.util.List;

public abstract class AbstractBigCannonBreechBlockEntity extends KineticTileEntity implements IBigCannonBlockEntity {

	protected BigCannonBehavior cannonBehavior;

	public AbstractBigCannonBreechBlockEntity(BlockEntityType<? extends AbstractBigCannonBreechBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviors) {
		super.addBehaviours(behaviors);
		behaviors.add(this.cannonBehavior = new BigCannonBehavior(this, this::canLoadBlock));
	}
	
	public abstract boolean isOpen();
	
	@Override
	public boolean canLoadBlock(StructureBlockInfo blockInfo) {
		return this.isOpen() && IBigCannonBlockEntity.super.canLoadBlock(blockInfo);
	}

	@Override public BigCannonBehavior cannonBehavior() { return this.cannonBehavior; }

}
