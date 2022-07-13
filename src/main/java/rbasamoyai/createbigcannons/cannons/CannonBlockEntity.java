package rbasamoyai.createbigcannons.cannons;

import java.util.List;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CannonBlockEntity extends SmartTileEntity implements ICannonBlockEntity {

	private CannonBehavior cannonBehavior;
	
	public CannonBlockEntity(BlockEntityType<? extends CannonBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviors) {
		super.addBehavioursDeferred(behaviors);
		behaviors.add(this.cannonBehavior = new CannonBehavior(this, this::canLoadBlock));
	}

	@Override public CannonBehavior cannonBehavior() { return this.cannonBehavior; }
	
}
