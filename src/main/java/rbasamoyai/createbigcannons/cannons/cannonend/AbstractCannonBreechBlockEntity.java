package rbasamoyai.createbigcannons.cannons.cannonend;

import java.util.List;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;

public abstract class AbstractCannonBreechBlockEntity extends KineticTileEntity implements ICannonBlockEntity {

	public AbstractCannonBreechBlockEntity(BlockEntityType<? extends AbstractCannonBreechBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviors) {
		super.addBehaviours(behaviors);
		behaviors.add(this.cannonBehavior());
	}
	
	public abstract boolean isOpen();
	
	@Override
	public boolean canLoadBlock(StructureBlockInfo blockInfo) {
		return this.isOpen() && ICannonBlockEntity.super.canLoadBlock(blockInfo);
	}

}
