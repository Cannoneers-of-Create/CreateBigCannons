package rbasamoyai.createbigcannons.cannons;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CannonBlockEntity extends BlockEntity {

	public CannonBlockEntity(BlockEntityType<? extends CannonBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
}
