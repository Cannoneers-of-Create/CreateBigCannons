package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class CannonMountExtensionBlock extends DirectionalKineticBlock implements IBE<CannonMountExtensionBlockEntity> {

	public CannonMountExtensionBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Direction.Axis getRotationAxis(BlockState state) {
		return state.getValue(FACING).getAxis();
	}

	@Override
	public boolean hasShaftTowards(LevelReader level, BlockPos pos, BlockState state, Direction face) {
		return face.getAxis() == this.getRotationAxis(state);
	}

	@Override
	public Class<CannonMountExtensionBlockEntity> getBlockEntityClass() {
		return CannonMountExtensionBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends CannonMountExtensionBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.CANNON_MOUNT_EXTENSION.get();
	}

}
