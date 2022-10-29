package rbasamoyai.createbigcannons.cannons.cannonend;

import net.minecraft.util.StringRepresentable;

public enum CannonEnd implements StringRepresentable {
	CLOSED("closed"),
	OPEN("open"),
	PARTIAL("partial");
	
	private final String serializedName;
	
	private CannonEnd(String name) {
		this.serializedName = name;
	}
	
	@Override public String getSerializedName() { return this.serializedName; }
	
	public static CannonEnd getOpeningType(float openProgress) {
		return openProgress <= 0 ? CLOSED : openProgress >= 1 ? OPEN : PARTIAL;
	}
}