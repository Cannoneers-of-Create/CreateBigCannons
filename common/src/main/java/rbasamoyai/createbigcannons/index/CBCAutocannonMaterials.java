package rbasamoyai.createbigcannons.index;

import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterialProperties;

public class CBCAutocannonMaterials {

	public static final AutocannonMaterial
		CAST_IRON = AutocannonMaterial.register(CreateBigCannons.resource("cast_iron"),
			AutocannonMaterialProperties.builder()
				.maxBarrelLength(3)
				.weight(1.5f)
				.baseSpread(2f)
				.spreadReductionPerBarrel(1.5f)
				.baseSpeed(5f)
				.speedIncreasePerBarrel(2f)
				.maxSpeedIncreases(2)
				.projectileLifetime(11)
				.baseRecoil(1f)
				.connectsInSurvival(false)
				.isWeldable(true)
				.weldDamage(1)
				.weldStressPenalty(1)
				.build()),

		BRONZE = AutocannonMaterial.register(CreateBigCannons.resource("bronze"),
			AutocannonMaterialProperties.builder()
				.maxBarrelLength(5)
				.weight(1f)
				.baseSpread(2.5f)
				.spreadReductionPerBarrel(2.0f)
				.baseSpeed(3f)
				.speedIncreasePerBarrel(1.5f)
				.maxSpeedIncreases(3)
				.projectileLifetime(25)
				.baseRecoil(2f)
				.connectsInSurvival(false)
				.isWeldable(true)
				.weldDamage(1)
				.weldStressPenalty(0)
				.build()),

		STEEL = AutocannonMaterial.register(CreateBigCannons.resource("steel"),
			AutocannonMaterialProperties.builder()
				.maxBarrelLength(7)
				.weight(2.5f)
				.baseSpread(3.0f)
				.spreadReductionPerBarrel(1.5f)
				.baseSpeed(3f)
				.speedIncreasePerBarrel(1.5f)
				.maxSpeedIncreases(4)
				.projectileLifetime(60)
				.baseRecoil(3f)
				.connectsInSurvival(false)
				.isWeldable(true)
				.weldDamage(2)
				.weldStressPenalty(2)
				.build());

}
