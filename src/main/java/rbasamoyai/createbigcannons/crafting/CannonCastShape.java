package rbasamoyai.createbigcannons.crafting;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import net.minecraft.util.StringRepresentable;

public enum CannonCastShape implements StringRepresentable {
	VERY_SMALL(1008, "very_small"),
	SMALL(1296, "small"),
	MEDIUM(1728, "medium"),
	LARGE(2016, "large"),
	VERY_LARGE(2880, "very_large");
	
	private static final Map<String, CannonCastShape> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(CannonCastShape::getSerializedName, Function.identity()));
	
	private final int fluidSize;
	private final String name;
	
	private CannonCastShape(int fluidSize, String name) {
		this.fluidSize = fluidSize;
		this.name = name;
	}
	
	public int fluidSize() { return this.fluidSize; }
	@Override public String getSerializedName() { return this.name; }
	
	public static CannonCastShape byId(String name) { return BY_ID.getOrDefault(name, MEDIUM); }	
	
}
