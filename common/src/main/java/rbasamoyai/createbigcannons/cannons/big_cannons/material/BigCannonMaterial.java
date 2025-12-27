package rbasamoyai.createbigcannons.cannons.big_cannons.material;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public record BigCannonMaterial(ResourceLocation name, BigCannonMaterialProperties defaultProperties) {

	public BigCannonMaterialProperties properties() {
		BigCannonMaterialProperties custom = BigCannonMaterialPropertiesHandler.getMaterial(this);
		return custom == null ? this.defaultProperties : custom;
	}

	private static final Map<ResourceLocation, BigCannonMaterial> CANNON_MATERIALS = new HashMap<>();

	public static BigCannonMaterial register(ResourceLocation loc, BigCannonMaterialProperties defaultProperties) {
		BigCannonMaterial material = new BigCannonMaterial(loc, defaultProperties);
		CANNON_MATERIALS.put(material.name(), material);
		return material;
	}

	public static BigCannonMaterial fromName(ResourceLocation loc) {
		if (!CANNON_MATERIALS.containsKey(loc)) throw new IllegalArgumentException("No big cannon material '" + loc + "' registered");
		return CANNON_MATERIALS.get(loc);
	}

	public static BigCannonMaterial fromNameOrNull(ResourceLocation loc) { return CANNON_MATERIALS.get(loc); }

}
