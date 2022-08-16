package rbasamoyai.createbigcannons.base;

import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionLighter;

import net.minecraft.core.Vec3i;

public class PoleContraptionLighter extends ContraptionLighter<PoleContraption> {

	protected PoleContraptionLighter(PoleContraption contraption) {
		super(contraption);
	}

	@Override
	public GridAlignedBB getContraptionBounds() {
		GridAlignedBB bounds = GridAlignedBB.from(this.contraption.bounds);
		bounds.translate(contraption.anchor);
		
		int length = this.contraption.extensionLength;
		Vec3i direction = this.contraption.orientation.getNormal();
		
		int shift = length / 2;
		int shiftX = direction.getX() * shift;
		int shiftY = direction.getY() * shift;
		int shiftZ = direction.getZ() * shift;
		bounds.translate(shiftX, shiftY, shiftZ);
		
		int grow = (length + 1) / 2;
		int extendX = Math.abs(direction.getX() * grow);
		int extendY = Math.abs(direction.getY() * grow);
		int extendZ = Math.abs(direction.getZ() * grow);
		bounds.grow(extendX, extendY, extendZ);
		
		return bounds;
	}

}
