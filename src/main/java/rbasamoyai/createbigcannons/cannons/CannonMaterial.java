package rbasamoyai.createbigcannons.cannons;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public record CannonMaterial(ResourceLocation name, double squibRatio, float weight, int maxSafeCharges, FailureMode failureMode) {

	public static final Map<ResourceLocation, CannonMaterial> CANNON_MATERIALS = new HashMap<>();
	
	public static CannonMaterial register(ResourceLocation loc, double squibRatio, float weight, int maxCharges, FailureMode failureMode) {
		CannonMaterial material = new CannonMaterial(loc, squibRatio, weight, maxCharges, failureMode);
		CANNON_MATERIALS.put(material.name(), material);
		return material;
	}
	
	public static CannonMaterial fromName(ResourceLocation loc) {
		return CANNON_MATERIALS.getOrDefault(loc, CAST_IRON);
	}
	
	public static final CannonMaterial
		WROUGHT_IRON = register(CreateBigCannons.resource("cast_iron"), 1d / 1d, 2.0f, 1, FailureMode.RUPTURE),
		CAST_IRON = register(CreateBigCannons.resource("cast_iron"), 1d / 1d, 3.0f, 2, FailureMode.FRAGMENT),
		BRONZE = register(CreateBigCannons.resource("bronze"), 3d / 2d, 2.0f, 4, FailureMode.RUPTURE),
		STEEL = register(CreateBigCannons.resource("steel"), 2d / 1d, 5.0f, 6, FailureMode.FRAGMENT);
	
	/**
	 * The squib ratio describes the maximum ratio of <b>cannon barrel</b> to
	 * <b>ignited powder charges</b> that cannons of this material can safely
	 * operate at. If the squib ratio exceeds this value, the cannon squib
	 * chance is increased.
	 * 
	 * @return A double representing the squib ratio of the material
	 */
	@Override // overriden for note
	public double squibRatio() {
		return this.squibRatio;
	}
	
	public enum FailureMode {
		RUPTURE,
		FRAGMENT
	}
	
}
