package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class YawControllerBlockEntity extends KineticBlockEntity implements ExtendsCannonMount {

	public YawControllerBlockEntity(BlockEntityType<? extends YawControllerBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		super.tick();
		CannonMountBlockEntity cmbe = this.getCannonMount();
		if (cmbe != null) {
			if (cmbe.isRunning()) cmbe.yawSpeed = this.getSpeed();
		}
	}

	@Override
	public float calculateStressApplied() {
		BlockEntity be = this.level.getBlockEntity(this.worldPosition.above());
		return be instanceof CannonMountBlockEntity cmbe ? cmbe.calculateStressApplied() : 0.0f;
	}

	@Nullable
	@Override
	public CannonMountBlockEntity getCannonMount() {
		return this.level.getBlockEntity(this.worldPosition.above()) instanceof CannonMountBlockEntity mount ? mount : null;
	}

}
