package rbasamoyai.createbigcannons.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity.ContraptionRotationState;

@Mixin(ContraptionRotationState.class)
public interface MixinContraptionRotationState {
	
	@Accessor("xRotation") public float getXRotation();
	@Accessor("xRotation") public void setXRotation(float xRotation);
	
	@Accessor("yRotation") public float getYRotation();
	@Accessor("yRotation") public void setYRotation(float yRotation);
	
	@Accessor("zRotation") public float getZRotation();
	@Accessor("zRotation") public void setZRotation(float zRotation);	
	
}
