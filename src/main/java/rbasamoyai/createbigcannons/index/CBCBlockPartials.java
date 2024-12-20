package rbasamoyai.createbigcannons.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.jozufozu.flywheel.core.PartialModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CBCBlockPartials {

	private static final Map<BigCannonMaterial, PartialModel> BREECHBLOCK_BY_MATERIAL = new HashMap<>();
	private static final Map<BigCannonMaterial, PartialModel> SCREW_LOCK_BY_MATERIAL = new HashMap<>();
	private static final Map<CannonCastShape, PartialModel> CANNON_CAST_BY_SIZE = new HashMap<>();

	private static final Map<AutocannonMaterial, PartialModel> AUTOCANNON_SPRING_BY_MATERIAL = new HashMap<>();
	private static final Map<AutocannonMaterial, PartialModel> AUTOCANNON_EJECTOR_BY_MATERIAL = new HashMap<>();
	private static final Map<DyeColor, PartialModel> AUTOCANNON_SEAT_BY_COLOR = new EnumMap<>(DyeColor.class);

	private static final Collection<Runnable> DEFERRED_MODEL_CALLBACKS = new ArrayList<>();

	public static final PartialModel
		CAST_IRON_SLIDING_BREECHBLOCK = breechblockPartial(CBCBigCannonMaterials.CAST_IRON, "cast_iron_sliding_breechblock"),
		BRONZE_SLIDING_BREECHBLOCK = breechblockPartial(CBCBigCannonMaterials.BRONZE, "bronze_sliding_breechblock"),
		STEEL_SLIDING_BREECHBLOCK = breechblockPartial(CBCBigCannonMaterials.STEEL, "steel_sliding_breechblock"),

		QUICKFIRING_BREECH_LEVER = block("quickfiring_breech_lever"),

		STEEL_SCREW_LOCK = screwLockPartial(CBCBigCannonMaterials.STEEL, "steel_screw_lock"),
		NETHERSTEEL_SCREW_LOCK = screwLockPartial(CBCBigCannonMaterials.NETHERSTEEL, "nethersteel_screw_lock"),

		VERY_SMALL_CANNON_CAST = cannonCastPartial(() -> CannonCastShape.VERY_SMALL, "cannon_cast/very_small_cannon_cast"),
		SMALL_CANNON_CAST = cannonCastPartial(() -> CannonCastShape.SMALL, "cannon_cast/small_cannon_cast"),
		MEDIUM_CANNON_CAST = cannonCastPartial(() -> CannonCastShape.MEDIUM, "cannon_cast/medium_cannon_cast"),
		LARGECANNON_CAST = cannonCastPartial(() -> CannonCastShape.LARGE, "cannon_cast/large_cannon_cast"),
		VERY_LARGE_CANNON_CAST = cannonCastPartial(() -> CannonCastShape.VERY_LARGE, "cannon_cast/very_large_cannon_cast"),
		CANNON_END_CAST = cannonCastPartial(() -> CannonCastShape.CANNON_END, "cannon_cast/cannon_end_cast"),
		SLIDING_BREECH_CAST = cannonCastPartial(() -> CannonCastShape.SLIDING_BREECH, "cannon_cast/sliding_breech_cast"),
		SCREW_BREECH_CAST = cannonCastPartial(() -> CannonCastShape.SCREW_BREECH, "cannon_cast/screw_breech_cast"),
		AUTOCANNON_BREECH_CAST = cannonCastPartial(() -> CannonCastShape.AUTOCANNON_BREECH, "cannon_cast/autocannon_breech_cast"),
		AUTOCANNON_RECOIL_SPRING_CAST = cannonCastPartial(() -> CannonCastShape.AUTOCANNON_RECOIL_SPRING, "cannon_cast/autocannon_recoil_spring_cast"),
		AUTOCANNON_BARREL_CAST = cannonCastPartial(() -> CannonCastShape.AUTOCANNON_BARREL, "cannon_cast/autocannon_barrel_cast"),

		ROTATING_MOUNT = block("cannon_mount/rotating_mount"),
		FUZE = block("fuze"),
		DROP_MORTAR_SHELL_FLYING = new PartialModel(CreateBigCannons.resource("block/drop_mortar_shell_flying")),

		CANNON_CARRIAGE = block("cannon_carriage/carriage"),
		CANNON_CARRIAGE_AXLE = block("cannon_carriage/carriage_axle"),
		CANNON_CARRIAGE_WHEEL = block("cannon_carriage/carriage_wheel"),
		CANNON_CARRIAGE_SADDLE = block("cannon_carriage/carriage_saddle"),

		CAST_IRON_AUTOCANNON_SPRING = autocannonSpringPartial(CBCAutocannonMaterials.CAST_IRON, "autocannon/cast_iron_autocannon_spring"),
		BRONZE_AUTOCANNON_SPRING = autocannonSpringPartial(CBCAutocannonMaterials.BRONZE, "autocannon/bronze_autocannon_spring"),
		STEEL_IRON_AUTOCANNON_SPRING = autocannonSpringPartial(CBCAutocannonMaterials.STEEL, "autocannon/steel_autocannon_spring"),

		CAST_IRON_AUTOCANNON_EJECTOR = autocannonEjectorPartial(CBCAutocannonMaterials.CAST_IRON, "cast_iron"),
		BRONZE_AUTOCANNON_EJECTOR = autocannonEjectorPartial(CBCAutocannonMaterials.BRONZE, "bronze"),
		STEEL_AUTOCANNON_EJECTOR = autocannonEjectorPartial(CBCAutocannonMaterials.STEEL, "steel"),

		AUTOCANNON_SEAT_WHITE = autocannonSeatPartial(DyeColor.WHITE, "autocannon/seat_white"),
		AUTOCANNON_SEAT_ORANGE = autocannonSeatPartial(DyeColor.ORANGE, "autocannon/seat_orange"),
		AUTOCANNON_SEAT_MAGENTA = autocannonSeatPartial(DyeColor.MAGENTA, "autocannon/seat_magenta"),
		AUTOCANNON_SEAT_LIGHT_BLUE = autocannonSeatPartial(DyeColor.LIGHT_BLUE, "autocannon/seat_light_blue"),
		AUTOCANNON_SEAT_YELLOW = autocannonSeatPartial(DyeColor.YELLOW, "autocannon/seat_yellow"),
		AUTOCANNON_SEAT_LIME = autocannonSeatPartial(DyeColor.LIME, "autocannon/seat_lime"),
		AUTOCANNON_SEAT_PINK = autocannonSeatPartial(DyeColor.PINK, "autocannon/seat_pink"),
		AUTOCANNON_SEAT_GRAY = autocannonSeatPartial(DyeColor.GRAY, "autocannon/seat_gray"),
		AUTOCANNON_SEAT_LIGHT_GRAY = autocannonSeatPartial(DyeColor.LIGHT_GRAY, "autocannon/seat_light_gray"),
		AUTOCANNON_SEAT_CYAN = autocannonSeatPartial(DyeColor.CYAN, "autocannon/seat_cyan"),
		AUTOCANNON_SEAT_PURPLE = autocannonSeatPartial(DyeColor.PURPLE, "autocannon/seat_purple"),
		AUTOCANNON_SEAT_BLUE = autocannonSeatPartial(DyeColor.BLUE, "autocannon/seat_blue"),
		AUTOCANNON_SEAT_BROWN = autocannonSeatPartial(DyeColor.BROWN, "autocannon/seat_brown"),
		AUTOCANNON_SEAT_GREEN = autocannonSeatPartial(DyeColor.GREEN, "autocannon/seat_green"),
		AUTOCANNON_SEAT_RED = autocannonSeatPartial(DyeColor.RED, "autocannon/seat_red"),
		AUTOCANNON_SEAT_BLACK = autocannonSeatPartial(DyeColor.BLACK, "autocannon/seat_black"),

		GAS_MASK = block("gas_mask");


	private static PartialModel block(String path) {
		return new PartialModel(CreateBigCannons.resource("block/" + path));
	}
	private static PartialModel entity(String path) { return new PartialModel(CreateBigCannons.resource("entity/" + path)); }

	private static PartialModel breechblockPartial(BigCannonMaterial material, String path) {
		return breechblockPartial(material, CreateBigCannons.resource("item/" + path));
	}

	public static PartialModel breechblockPartial(BigCannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		BREECHBLOCK_BY_MATERIAL.put(material, model);
		return model;
	}

	public static PartialModel breechblockFor(BigCannonMaterial material) {
		return BREECHBLOCK_BY_MATERIAL.getOrDefault(material, CAST_IRON_SLIDING_BREECHBLOCK);
	}

	private static PartialModel screwLockPartial(BigCannonMaterial material, String path) {
		return screwLockPartial(material, CreateBigCannons.resource("item/" + path));
	}

	public static PartialModel screwLockPartial(BigCannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		SCREW_LOCK_BY_MATERIAL.put(material, model);
		return model;
	}

	public static PartialModel screwLockFor(BigCannonMaterial material) {
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

	private static PartialModel autocannonSpringPartial(AutocannonMaterial material, String path) {
		return autocannonSpringPartial(material, CreateBigCannons.resource("block/" + path));
	}

	public static PartialModel autocannonSpringPartial(AutocannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		AUTOCANNON_SPRING_BY_MATERIAL.put(material, model);
		return model;
	}

	public static PartialModel autocannonSpringFor(AutocannonMaterial material) {
		return AUTOCANNON_SPRING_BY_MATERIAL.getOrDefault(material, CAST_IRON_AUTOCANNON_SPRING);
	}

	private static PartialModel autocannonEjectorPartial(AutocannonMaterial material, String path) {
		return autocannonEjectorPartial(material, CreateBigCannons.resource("item/" + path + "_autocannon_breech_extractor"));
	}

	public static PartialModel autocannonEjectorPartial(AutocannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		AUTOCANNON_EJECTOR_BY_MATERIAL.put(material, model);
		return model;
	}

	public static PartialModel autocannonEjectorFor(AutocannonMaterial material) {
		return AUTOCANNON_EJECTOR_BY_MATERIAL.getOrDefault(material, CAST_IRON_AUTOCANNON_EJECTOR);
	}

	private static PartialModel autocannonSeatPartial(DyeColor color, String path) {
		return autocannonSeatPartial(color, CreateBigCannons.resource("block/" + path));
	}

	public static PartialModel autocannonSeatPartial(DyeColor color, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		AUTOCANNON_SEAT_BY_COLOR.put(color, model);
		return model;
	}

	public static PartialModel autocannonSeatFor(DyeColor color) {
		return AUTOCANNON_SEAT_BY_COLOR.getOrDefault(color, AUTOCANNON_SEAT_WHITE);
	}

	public static void init() {}

	public static void resolveDeferredModels() {
		for (Runnable run : DEFERRED_MODEL_CALLBACKS) run.run();
		DEFERRED_MODEL_CALLBACKS.clear();
	}

}
