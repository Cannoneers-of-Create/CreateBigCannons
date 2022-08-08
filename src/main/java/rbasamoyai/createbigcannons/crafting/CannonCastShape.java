package rbasamoyai.createbigcannons.crafting;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CannonCastShape {
	
	public static final Map<ResourceLocation, CannonCastShape> SHAPES = new HashMap<>();
	public static final CannonCastShape	
		VERY_SMALL = register(CreateBigCannons.resource("very_small"), 1008),
		SMALL = register(CreateBigCannons.resource("small"), 1296),
		MEDIUM = register(CreateBigCannons.resource("medium"), 1728),
		LARGE = register(CreateBigCannons.resource("large"), 2016),
		VERY_LARGE = register(CreateBigCannons.resource("very_large"), 2880);
	
	private final int fluidSize;
	private final ResourceLocation name;
	
	private CannonCastShape(ResourceLocation name, int fluidSize) {
		this.fluidSize = fluidSize;
		this.name = name;
	}
	
	public static CannonCastShape register(ResourceLocation name, int fluidSize) {
		CannonCastShape shape = new CannonCastShape(name, fluidSize);
		
		return shape;
	}
	
	public int fluidSize() { return this.fluidSize; }
	public ResourceLocation name() { return this.name; }
	
	public static CannonCastShape byId(String name) {
		return SHAPES.getOrDefault(new ResourceLocation(name), VERY_SMALL);
	}
	
	public static void register() {}
	
}
