package rbasamoyai.createbigcannons.cannons.big_cannons;

import java.util.List;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BigCannonBlockEntity extends SmartBlockEntity implements IBigCannonBlockEntity {

	private BigCannonBehavior cannonBehavior;

	public BigCannonBlockEntity(BlockEntityType<? extends BigCannonBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviors) {
		super.addBehavioursDeferred(behaviors);
		behaviors.add(this.cannonBehavior = new BigCannonBehavior(this, this::canLoadBlock));
	}

	@Override
	public BigCannonBehavior cannonBehavior() {
		return this.cannonBehavior;
	}

}
