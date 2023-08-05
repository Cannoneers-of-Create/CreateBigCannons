package rbasamoyai.createbigcannons.cannon_control.contraption;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.collision.Matrix3d;
import com.simibubi.create.foundation.utility.AngleHelper;

import net.minecraft.core.Direction;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class CBCContraptionRotationState extends AbstractContraptionEntity.ContraptionRotationState {

	private final PitchOrientedContraptionEntity entity;
	private Matrix3d matrix;
	private float yaw;
	private float yawOffset;

	public CBCContraptionRotationState(PitchOrientedContraptionEntity entity) {
		this.entity = entity;
		if (entity.pitch != 0 & entity.yaw != 0) {
			this.yawOffset = this.entity.yaw;
			this.yaw = -this.entity.getYawOffset();
		} else {
			this.yaw = this.entity.yaw + this.entity.getYawOffset();
		}
	}

	@Override
	public Matrix3d asMatrix() {
		if (this.matrix != null) return this.matrix;

		// TODO: remove stuff once Fabric rotation catches up with Forge
		this.matrix = new Matrix3d().asIdentity();
		boolean flag = ((AbstractMountedCannonContraption) this.entity.getContraption()).initialOrientation().getAxis() == Direction.Axis.X;
		float yawAdjust = IndexPlatform.modifyRotationStateYaw(flag, this.hasVerticalRotation(), this.yaw); //this.yaw;
		if (this.hasVerticalRotation()) {
			if (flag) {
				this.matrix.multiply(new Matrix3d().asZRotation(AngleHelper.rad(-this.entity.pitch)));
			} else {
				this.matrix.multiply(new Matrix3d().asXRotation(AngleHelper.rad(-this.entity.pitch)));
			}
		}/* else {
			yawAdjust += flag ? 180 : 0;
		}*/
		this.matrix.multiply(new Matrix3d().asYRotation(AngleHelper.rad(yawAdjust)));
		return this.matrix;
	}

	@Override
	public boolean hasVerticalRotation() {
		return this.entity.pitch != 0;
	}

	@Override
	public float getYawOffset() {
		return -this.yawOffset;
	}

}
