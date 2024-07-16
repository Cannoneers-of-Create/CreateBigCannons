package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencerInstructions;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class YawControllerBlockEntity extends KineticBlockEntity implements ExtendsCannonMount {

	public YawControllerBlockEntity(BlockEntityType<? extends YawControllerBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void onSpeedChanged(float prevSpeed) {
		super.onSpeedChanged(prevSpeed);

		if (this.getCannonMount() == null) return;

		if (this.sequenceContext != null &&
			this.sequenceContext.instruction() == SequencerInstructions.TURN_ANGLE) {
			this.getCannonMount().setSequencedYawAngleLimit(
				(float) (this.sequenceContext.getEffectiveValue(getTheoreticalSpeed()) * 0.125f));
		} else {
			this.getCannonMount().setSequencedYawAngleLimit(-1);
		}
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

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (!super.addToGoggleTooltip(tooltip, isPlayerSneaking)) return false;
		CannonMountBlockEntity mount = this.getCannonMount();
		if (mount != null) ExtendsCannonMount.addCannonInfoToTooltip(tooltip, mount.mountedContraption);
		return true;
	}

	@Override
	public void remove() {
		super.remove();
		CannonMountBlockEntity mount = this.getCannonMount();
		if (mount != null)
			mount.yawSpeed = 0;
	}

}
