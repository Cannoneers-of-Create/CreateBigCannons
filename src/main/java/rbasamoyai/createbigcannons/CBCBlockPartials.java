package rbasamoyai.createbigcannons;

import java.util.HashMap;
import java.util.Map;

import com.jozufozu.flywheel.core.PartialModel;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;

public class CBCBlockPartials {

	private static final Map<CannonMaterial, PartialModel> BREECHBLOCK_BY_MATERIAL = new HashMap<>();
	private static final Map<CannonMaterial, PartialModel> SCREW_LOCK_BY_MATERIAL = new HashMap<>();
	
	public static final PartialModel
		CAST_IRON_SLIDING_BREECHBLOCK = breechblockPartial(CannonMaterial.CAST_IRON, "sliding_breechblock/cast_iron_sliding_breechblock"),
		BRONZE_SLIDING_BREECHBLOCK = breechblockPartial(CannonMaterial.BRONZE, "sliding_breechblock/bronze_sliding_breechblock"),
		STEEL_SLIDING_BREECHBLOCK = breechblockPartial(CannonMaterial.STEEL, "sliding_breechblock/steel_sliding_breechblock"),
		
		STEEL_SCREW_LOCK = screwLockPartial(CannonMaterial.STEEL, "screw_lock/steel_screw_lock"),
		NETHER_GUNMETAL_SCREW_LOCK = screwLockPartial(CannonMaterial.NETHER_GUNMETAL, "screw_lock/nether_gunmetal_screw_lock"),
		
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
	
	private static PartialModel screwLockPartial(CannonMaterial material, String path) {
		return screwLockPartial(material, CreateBigCannons.resource("block/" + path));
	}
	
	public static PartialModel screwLockPartial(CannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		SCREW_LOCK_BY_MATERIAL.put(material, model);
		return model;
	}
	
	public static PartialModel screwLockFor(CannonMaterial material) {
		return SCREW_LOCK_BY_MATERIAL.getOrDefault(material, STEEL_SCREW_LOCK);
	}
	
	public static void init() {}
	
}