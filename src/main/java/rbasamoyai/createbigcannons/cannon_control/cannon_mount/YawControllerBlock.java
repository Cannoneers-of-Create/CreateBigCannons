package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class YawControllerBlock extends KineticBlock implements IBE<YawControllerBlockEntity> {

	public YawControllerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Direction.Axis getRotationAxis(BlockState arg0) {
		return Direction.Axis.Y;
	}

	@Override
	public boolean hasShaftTowards(LevelReader level, BlockPos pos, BlockState state, Direction face) {
		return face.getAxis() == Direction.Axis.Y;
	}

	@Override
	public Class<YawControllerBlockEntity> getBlockEntityClass() {
		return YawControllerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends YawControllerBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.YAW_CONTROLLER.get();
	}

}
