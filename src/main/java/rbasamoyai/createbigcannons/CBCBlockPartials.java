package rbasamoyai.createbigcannons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.jozufozu.flywheel.core.PartialModel;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CBCBlockPartials {

	private static final Map<CannonMaterial, PartialModel> BREECHBLOCK_BY_MATERIAL = new HashMap<>();
	private static final Map<CannonMaterial, PartialModel> SCREW_LOCK_BY_MATERIAL = new HashMap<>();
	private static final Map<CannonCastShape, PartialModel> CANNON_CAST_BY_SIZE = new HashMap<>();
	
	private static final Collection<Runnable> DEFERRED_MODEL_CALLBACKS = new ArrayList<>(); 
	
	public static final PartialModel
		CAST_IRON_SLIDING_BREECHBLOCK = breechblockPartial(CannonMaterial.CAST_IRON, "cast_iron_sliding_breechblock"),
		BRONZE_SLIDING_BREECHBLOCK = breechblockPartial(CannonMaterial.BRONZE, "bronze_sliding_breechblock"),
		STEEL_SLIDING_BREECHBLOCK = breechblockPartial(CannonMaterial.STEEL, "steel_sliding_breechblock"),
		
		STEEL_SCREW_LOCK = screwLockPartial(CannonMaterial.STEEL, "steel_screw_lock"),
		NETHERSTEEL_SCREW_LOCK = screwLockPartial(CannonMaterial.NETHERSTEEL, "nethersteel_screw_lock"),
		
		VERY_SMALL_CANNON_CAST = cannonCastPartial(CannonCastShape.VERY_SMALL, "cannon_cast/very_small_cannon_cast"),
		SMALL_CANNON_CAST = cannonCastPartial(CannonCastShape.SMALL, "cannon_cast/small_cannon_cast"),
		MEDIUM_CANNON_CAST = cannonCastPartial(CannonCastShape.MEDIUM, "cannon_cast/medium_cannon_cast"),
		LARGECANNON_CAST = cannonCastPartial(CannonCastShape.LARGE, "cannon_cast/large_cannon_cast"),
		VERY_LARGE_CANNON_CAST = cannonCastPartial(CannonCastShape.VERY_LARGE, "cannon_cast/very_large_cannon_cast"),
		CANNON_END_CAST = cannonCastPartial(CannonCastShape.CANNON_END, "cannon_cast/cannon_end_cast"),
		SLIDING_BREECH_CAST = cannonCastPartial(CannonCastShape.SLIDING_BREECH, "cannon_cast/sliding_breech_cast"),
		SCREW_BREECH_CAST = cannonCastPartial(CannonCastShape.SCREW_BREECH, "cannon_cast/screw_breech_cast"),
		
		ROTATING_MOUNT = block("cannon_mount/rotating_mount"),
		YAW_SHAFT = block("cannon_mount/yaw_axis"),
		FUZE = block("fuze"),

		CANNON_CARRIAGE = block("cannon_carriage/carriage"),
		CANNON_CARRIAGE_AXLE = block("cannon_carriage/carriage_axle"),
		CANNON_CARRIAGE_WHEEL = block("cannon_carriage/carriage_wheel"),
		CANNON_CARRIAGE_SADDLE = block("cannon_carriage/carriage_saddle");
	
	private static PartialModel block(String path) {
		return new PartialModel(CreateBigCannons.resource("block/" + path));
	}
	
	private static PartialModel breechblockPartial(CannonMaterial material, String path) {
		return breechblockPartial(material, CreateBigCannons.resource("item/" + path));
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
		return screwLockPartial(material, CreateBigCannons.resource("item/" + path));
	}
	
	public static PartialModel screwLockPartial(CannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		SCREW_LOCK_BY_MATERIAL.put(material, model);
		return model;
	}
	
	public static PartialModel screwLockFor(CannonMaterial material) {
		return SCREW_LOCK_BY_MATERIAL.getOrDefault(material, STEEL_SCREW_LOCK);
	}
	
	private static PartialModel cannonCastPartial(Supplier<CannonCastShape> size, String path) {
		PartialModel model = new PartialModel(CreateBigCannons.resource("block/" + path));
		DEFERRED_MODEL_CALLBACKS.add(() -> {
			CANNON_CAST_BY_SIZE.put(size.get(), model);
		});
		return model;
	}
	
	public static PartialModel cannonCastPartial(CannonCastShape size, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		CANNON_CAST_BY_SIZE.put(size, model);
		return model;
	}
	
	public static PartialModel cannonCastFor(CannonCastShape size) {
		return CANNON_CAST_BY_SIZE.getOrDefault(size, VERY_SMALL_CANNON_CAST);
	}
	
	public static void init() {}
	
	public static void resolveDeferredModels() {
		for (Runnable run : DEFERRED_MODEL_CALLBACKS) run.run();
		DEFERRED_MODEL_CALLBACKS.clear();
	}
	
}