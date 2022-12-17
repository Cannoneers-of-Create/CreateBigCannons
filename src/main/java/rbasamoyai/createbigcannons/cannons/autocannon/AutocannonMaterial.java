package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

import java.util.HashMap;
import java.util.Map;

public record AutocannonMaterial(ResourceLocation name, int maxLength, float weight) {

    public static final Map<ResourceLocation, AutocannonMaterial> CANNON_MATERIALS = new HashMap<>();

    public static AutocannonMaterial register(ResourceLocation loc, int maxLength, float weight) {
        AutocannonMaterial material = new AutocannonMaterial(loc, maxLength, weight);
        CANNON_MATERIALS.put(material.name(), material);
        return material;
    }

    public static AutocannonMaterial fromName(ResourceLocation loc) {
        return CANNON_MATERIALS.getOrDefault(loc, CAST_IRON);
    }

    public static final AutocannonMaterial
            CAST_IRON = register(CreateBigCannons.resource("cast_iron"), 3, 1.5f),
            BRONZE = register(CreateBigCannons.resource("bronze"), 5, 1.0f),
            STEEL = register(CreateBigCannons.resource("steel"), 7, 2.5f);

}
