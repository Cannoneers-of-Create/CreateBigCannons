package rbasamoyai.createbigcannons.cannons.big_cannons;

import net.minecraft.util.StringRepresentable;

public enum BigCannonEnd implements StringRepresentable {
	CLOSED("closed"),
	OPEN("open"),
	PARTIAL("partial");
	
	private final String serializedName;
	
	private BigCannonEnd(String name) {
		this.serializedName = name;
	}
	
	@Override public String getSerializedName() { return this.serializedName; }
	
	public static BigCannonEnd getOpeningType(float openProgress) {
		return openProgress <= 0 ? CLOSED : openProgress >= 1 ? OPEN : PARTIAL;
	}
}