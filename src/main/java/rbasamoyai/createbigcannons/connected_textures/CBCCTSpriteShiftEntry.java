package rbasamoyai.createbigcannons.connected_textures;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTType;

public class CBCCTSpriteShiftEntry extends CTSpriteShiftEntry {

	private final int scale;

	public CBCCTSpriteShiftEntry(CTType type, int scale) {
		super(type);
		this.scale = scale;
	}

	@Override
	public float getTargetU(float localU, int index) {
		float uOffset = index % this.type.getSheetSize();
		return getTarget().getU(
			(getUnInterpolatedU(getOriginal(), localU) * this.scale + (uOffset * 16)) / ((float) type.getSheetSize()));
	}

	@Override
	public float getTargetV(float localV, int index) {
		float vOffset = index / this.type.getSheetSize();
		return getTarget().getV(
			(getUnInterpolatedV(getOriginal(), localV) * this.scale + (vOffset * 16)) / ((float) type.getSheetSize()));
	}

}
