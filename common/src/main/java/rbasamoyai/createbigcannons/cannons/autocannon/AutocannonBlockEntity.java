package rbasamoyai.createbigcannons.cannons.autocannon;

import java.util.List;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;

public class AutocannonBlockEntity extends SmartBlockEntity implements IAutocannonBlockEntity {

	private ItemCannonBehavior behavior;

	public AutocannonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(this.behavior = this.makeBehavior());
	}

	protected ItemCannonBehavior makeBehavior() {
		return new ItemCannonBehavior(this);
	}

	@Override
	public ItemCannonBehavior cannonBehavior() {
		return this.behavior;
	}

}
