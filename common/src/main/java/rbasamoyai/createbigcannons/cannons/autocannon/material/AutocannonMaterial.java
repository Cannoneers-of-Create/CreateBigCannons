package rbasamoyai.createbigcannons.cannons.autocannon.material;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public record AutocannonMaterial(ResourceLocation name, AutocannonMaterialProperties defaultProperties) {

	public AutocannonMaterialProperties properties() {
		AutocannonMaterialProperties handlerProperties = AutocannonMaterialPropertiesHandler.getMaterial(this);
		return handlerProperties == null ? this.defaultProperties : handlerProperties;
	}

    private static final Map<ResourceLocation, AutocannonMaterial> CANNON_MATERIALS = new HashMap<>();

    public static AutocannonMaterial register(ResourceLocation loc, AutocannonMaterialProperties defaultProperties) {
        AutocannonMaterial material = new AutocannonMaterial(loc, defaultProperties);
        CANNON_MATERIALS.put(material.name(), material);
        return material;
    }

    public static AutocannonMaterial fromName(ResourceLocation loc) {
		if (!CANNON_MATERIALS.containsKey(loc)) throw new IllegalArgumentException("No autocannon material '" + loc + "' registered");
        return CANNON_MATERIALS.get(loc);
    }

	public static AutocannonMaterial fromNameOrNull(ResourceLocation loc) {
		return CANNON_MATERIALS.getOrDefault(loc, null);
	}

}
