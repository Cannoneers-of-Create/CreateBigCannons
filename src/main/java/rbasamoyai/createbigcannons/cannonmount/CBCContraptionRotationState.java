package rbasamoyai.createbigcannons.cannonmount;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity.ContraptionRotationState;
import com.simibubi.create.foundation.collision.Matrix3d;
import com.simibubi.create.foundation.utility.AngleHelper;

import net.minecraft.core.Direction;

public class CBCContraptionRotationState extends ContraptionRotationState {

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
		
		this.matrix = new Matrix3d().asIdentity();
		boolean flag = ((MountedCannonContraption) this.entity.getContraption()).initialOrientation().getAxis() == Direction.Axis.X;
		float yawAdjust = this.yaw + (flag ? 180.0f : 0.0f);
		if (this.hasVerticalRotation()) {
			if (flag) {
				this.matrix.multiply(new Matrix3d().asZRotation(AngleHelper.rad(-this.entity.pitch)));
			} else {
				this.matrix.multiply(new Matrix3d().asXRotation(AngleHelper.rad(-this.entity.pitch)));
			}
			this.matrix.multiply(new Matrix3d().asYRotation(AngleHelper.rad(yawAdjust)));
		} else {
			this.matrix.multiply(new Matrix3d().asYRotation(AngleHelper.rad(-yawAdjust)));
		}
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
