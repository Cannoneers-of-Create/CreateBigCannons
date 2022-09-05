package rbasamoyai.createbigcannons.cannons;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public record CannonMaterial(ResourceLocation name, int squibRatioNum, int squibRatioDem, float weight, int maxSafeCharges, FailureMode failureMode) {

	public static final Map<ResourceLocation, CannonMaterial> CANNON_MATERIALS = new HashMap<>();
	
	public static CannonMaterial register(ResourceLocation loc, int num, int dem, float weight, int maxCharges, FailureMode failureMode) {
		CannonMaterial material = new CannonMaterial(loc, num, dem, weight, maxCharges, failureMode);
		CANNON_MATERIALS.put(material.name(), material);
		return material;
	}
	
	public static CannonMaterial fromName(ResourceLocation loc) {
		return CANNON_MATERIALS.getOrDefault(loc, CAST_IRON);
	}
	
	public static final CannonMaterial
		INCOMPLETE_LAYERED = register(CreateBigCannons.resource("incomplete_layered"), 0, 1, 1.0f, 0, FailureMode.FRAGMENT),
		LOG = register(CreateBigCannons.resource("log"), 0, 1, 1.0f, 0, FailureMode.FRAGMENT),
		WROUGHT_IRON = register(CreateBigCannons.resource("cast_iron"), 1, 1, 2.0f, 1, FailureMode.RUPTURE),
		CAST_IRON = register(CreateBigCannons.resource("cast_iron"), 1, 1, 3.0f, 2, FailureMode.FRAGMENT),
		BRONZE = register(CreateBigCannons.resource("bronze"), 3, 2, 2.0f, 4, FailureMode.RUPTURE),
		STEEL = register(CreateBigCannons.resource("steel"), 2, 1, 5.0f, 6, FailureMode.FRAGMENT),
		NETHERSTEEL = register(CreateBigCannons.resource("nethersteel"), 3, 1, 6.0f, 8, FailureMode.FRAGMENT);
	
	/**
	 * The squib ratio describes the maximum ratio of <b>cannon barrel</b> to
	 * <b>ignited powder charges</b> that cannons of this material can safely
	 * operate at. If the squib ratio exceeds this value, the cannon squib
	 * chance is increased.
	 * 
	 * @return A double representing the squib ratio of the material
	 */
	public double squibRatio() {
		return this.squibRatioDem == 0 ? 0 : (double) this.squibRatioNum / (double) this.squibRatioDem;
	}
	
	public enum FailureMode {
		RUPTURE,
		FRAGMENT
	}
	
}
