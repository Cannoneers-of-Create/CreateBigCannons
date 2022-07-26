package rbasamoyai.createbigcannons;

import java.util.HashMap;
import java.util.Map;

import com.jozufozu.flywheel.core.PartialModel;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;

public class CBCBlockPartials {

	private static final Map<CannonMaterial, PartialModel> BREECHBLOCK_BY_MATERIAL = new HashMap<>();
	
	public static final PartialModel
		CAST_IRON_SLIDING_BREECHBLOCK = breechblockPartial(CannonMaterial.CAST_IRON, "sliding_breechblock/cast_iron_sliding_breechblock"),
		BRONZE_SLIDING_BREECHBLOCK = breechblockPartial(CannonMaterial.BRONZE, "sliding_breechblock/bronze_sliding_breechblock"),
		ROTATING_MOUNT = block("cannon_mount/rotating_mount"),
		YAW_SHAFT = block("cannon_mount/yaw_axis"),
		FUZE = block("fuze");
	
	private static PartialModel block(String path) {
		return new PartialModel(CreateBigCannons.resource("block/" + path));
	}
	
	private static PartialModel breechblockPartial(CannonMaterial material, String path) {
		return breechblockPartial(material, CreateBigCannons.resource("block/" + path));
	}
	
	public static PartialModel breechblockPartial(CannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		BREECHBLOCK_BY_MATERIAL.put(material, model);
		return model;
	}
	
	public static PartialModel breechblockFor(CannonMaterial material) {
		return BREECHBLOCK_BY_MATERIAL.getOrDefault(material, CAST_IRON_SLIDING_BREECHBLOCK);
	}
	
	public static void init() {}
	
}