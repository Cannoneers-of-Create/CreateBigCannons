package rbasamoyai.createbigcannons.cannon_control.contraption;

import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.jozufozu.flywheel.util.box.ImmutableBox;
import com.simibubi.create.content.contraptions.render.NonStationaryLighter;

import rbasamoyai.createbigcannons.remix.HasCannonLightingVolume;

public class CannonContraptionLighter<C extends AbstractMountedCannonContraption> extends NonStationaryLighter<C> {

	public CannonContraptionLighter(C contraption) {
		super(contraption);
		if (this.lightVolume instanceof HasCannonLightingVolume cannonLighting)
			cannonLighting.createbigcannons$setCannonContraption(contraption);
	}

	@Override
	public GridAlignedBB getContraptionBounds() {
		GridAlignedBB bb = GridAlignedBB.from(this.contraption.createInitialLightingBounds());
		bb.translate(this.contraption.entity.blockPosition());
		return bb;
	}

	@Override
	public ImmutableBox getVolume() {
		GridAlignedBB bb = GridAlignedBB.from(this.contraption.createBoundsFromExtensionLengths());
		bb.translate(this.contraption.entity.blockPosition());
		return bb;
	}

}
