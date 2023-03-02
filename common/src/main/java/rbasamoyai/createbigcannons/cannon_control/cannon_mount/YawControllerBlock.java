package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import com.simibubi.create.content.contraptions.base.KineticBlock;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class YawControllerBlock extends KineticBlock implements ITE<YawControllerBlockEntity> {

	public YawControllerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Direction.Axis getRotationAxis(BlockState arg0) {
		return Direction.Axis.Y;
	}

	@Override
	public boolean hasShaftTowards(LevelReader level, BlockPos pos, BlockState state, Direction face) {
		return face == Direction.DOWN;
	}

	@Override public Class<YawControllerBlockEntity> getTileEntityClass() { return YawControllerBlockEntity.class; }
	@Override public BlockEntityType<? extends YawControllerBlockEntity> getTileEntityType() { return CBCBlockEntities.YAW_CONTROLLER.get(); }

}
