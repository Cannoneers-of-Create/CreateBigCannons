package rbasamoyai.createbigcannons.index;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCDamageTypes {

	public static final ResourceKey<DamageType>
		MOLTEN_METAL = register("molten_metal"),
		CANNON_PROJECTILE = register("cannon_projectile"),
		BIG_CANNON_PROJECTILE = register("big_cannon_projectile"),
		MACHINE_GUN_FIRE = register("machine_gun_fire"),
		MACHINE_GUN_FIRE_IN_WATER = register("machine_gun_fire_in_water"),
		TRAFFIC_CONE = register("traffic_cone"),
		SHRAPNEL = register("shrapnel"),
		GRAPESHOT = register("grapeshot");

	public static void init() {}

	private static ResourceKey<DamageType> register(String id) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, CreateBigCannons.resource(id));
	}

}
