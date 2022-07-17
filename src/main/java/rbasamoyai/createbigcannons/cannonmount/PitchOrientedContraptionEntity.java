package rbasamoyai.createbigcannons.cannonmount;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.OrientedContraptionEntity;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.mixin.MixinContraptionRotationState;

public class PitchOrientedContraptionEntity extends OrientedContraptionEntity {

	public PitchOrientedContraptionEntity(EntityType<? extends PitchOrientedContraptionEntity> type, Level level) {
		super(type, level);
	}
	
	public static PitchOrientedContraptionEntity create(Level level, Contraption contraption, Direction initialOrientation) {
		PitchOrientedContraptionEntity entity = new PitchOrientedContraptionEntity(CBCEntityTypes.PITCH_ORIENTED_CONTRAPTION.get(), level);
		
		entity.setContraption(contraption);
		entity.setInitialOrientation(initialOrientation);
		entity.startAtInitialYaw();
		return entity;
	}
	
	@Override
	public ContraptionRotationState getRotationState() {
		ContraptionRotationState crs = super.getRotationState();
		MixinContraptionRotationState crsmixin = (MixinContraptionRotationState) crs;
		
		float zRot = crsmixin.getZRotation();
		crsmixin.setXRotation(zRot);
		crsmixin.setZRotation(0.0f);
		
		return crs;
	}
	
	@Override
	public void doLocalTransforms(float partialTicks, PoseStack[] poseStacks) {
		float initialYaw = this.getInitialYaw();
		float pitch = this.getViewXRot(partialTicks);
		float yaw = this.getViewYRot(partialTicks) + initialYaw;
		
		for (PoseStack stack : poseStacks) {
			stack.translate(-0.5f, 0.0f, -0.5f);
			
			TransformStack tstack = TransformStack.cast(stack)
					.nudge(this.getId())
					.centre()
					.rotateY(yaw);
			
			if (this.getInitialOrientation().getAxis() == Direction.Axis.X) {
				tstack.rotateZ(pitch);
			} else {
				tstack.rotateX(pitch);
			}
			tstack.unCentre();
		}
	}

}
