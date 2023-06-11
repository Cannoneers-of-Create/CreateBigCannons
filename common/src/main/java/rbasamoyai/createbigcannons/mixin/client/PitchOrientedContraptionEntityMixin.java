package rbasamoyai.createbigcannons.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

@Mixin(PitchOrientedContraptionEntity.class)
public abstract class PitchOrientedContraptionEntityMixin extends OrientedContraptionEntity {

	PitchOrientedContraptionEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public void applyLocalTransforms(PoseStack stack, float partialTicks) {
		float initialYaw = this.getInitialYaw();
		float pitch = this.getViewXRot(partialTicks);
		float yaw = this.getViewYRot(partialTicks) + initialYaw;

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
