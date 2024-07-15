package rbasamoyai.createbigcannons.index;

import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialProperties;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialProperties.FailureMode;

public class CBCBigCannonMaterials {

	public static final BigCannonMaterial
		INCOMPLETE_LAYERED = BigCannonMaterial.register(CreateBigCannons.resource("incomplete_layered"),
			BigCannonMaterialProperties.builder()
				.minimumVelocityPerBarrel(-1d)
				.weight(1.0f)
				.maxSafePropellantStress(0)
				.failureMode(FailureMode.FRAGMENT)
				.connectsInSurvival(true)
				.isWeldable(false)
				.weldDamage(0)
				.weldStressPenalty(0)
				.minimumSpread(0)
				.spreadReductionPerBarrel(0)
				.build()),

		LOG = BigCannonMaterial.register(CreateBigCannons.resource("log"),
			BigCannonMaterialProperties.builder()
				.minimumVelocityPerBarrel(-1d)
				.weight(1.0f)
				.maxSafePropellantStress(0)
				.failureMode(FailureMode.FRAGMENT)
				.connectsInSurvival(true)
				.isWeldable(false)
				.weldDamage(0)
				.weldStressPenalty(0)
				.minimumSpread(1.5f)
				.spreadReductionPerBarrel(1f)
				.build()),

		WROUGHT_IRON = BigCannonMaterial.register(CreateBigCannons.resource("wrought_iron"),
			BigCannonMaterialProperties.builder()
				.minimumVelocityPerBarrel(2d)
				.weight(2.0f)
				.maxSafePropellantStress(1)
				.failureMode(FailureMode.RUPTURE)
				.connectsInSurvival(true)
				.isWeldable(false)
				.weldDamage(0)
				.weldStressPenalty(0)
				.minimumSpread(0.1f)
				.spreadReductionPerBarrel(1f)
				.build()),

		CAST_IRON = BigCannonMaterial.register(CreateBigCannons.resource("cast_iron"),
			BigCannonMaterialProperties.builder()
				.minimumVelocityPerBarrel(2d)
				.weight(3.0f)
				.maxSafePropellantStress(2)
				.failureMode(FailureMode.FRAGMENT)
				.connectsInSurvival(false)
				.isWeldable(true)
				.weldDamage(1)
				.weldStressPenalty(1)
				.minimumSpread(0.05f)
				.spreadReductionPerBarrel(2f)
				.build()),

		BRONZE = BigCannonMaterial.register(CreateBigCannons.resource("bronze"),
			BigCannonMaterialProperties.builder()
				.minimumVelocityPerBarrel(4d / 3d)
				.weight(2.0f)
				.maxSafePropellantStress(4)
				.failureMode(FailureMode.RUPTURE)
				.connectsInSurvival(false)
				.isWeldable(true)
				.weldDamage(1)
				.weldStressPenalty(0)
				.minimumSpread(0.03f)
				.spreadReductionPerBarrel(1.4f)
				.build()),

		STEEL = BigCannonMaterial.register(CreateBigCannons.resource("steel"),
			BigCannonMaterialProperties.builder()
				.minimumVelocityPerBarrel(1d)
				.weight(5.0f)
				.maxSafePropellantStress(6)
				.failureMode(FailureMode.FRAGMENT)
				.connectsInSurvival(false)
				.isWeldable(true)
				.weldDamage(2)
				.weldStressPenalty(2)
				.minimumSpread(0.025f)
				.spreadReductionPerBarrel(1.4f)
				.build()),

		NETHERSTEEL = BigCannonMaterial.register(CreateBigCannons.resource("nethersteel"),
			BigCannonMaterialProperties.builder()
				.minimumVelocityPerBarrel(2d / 3d)
				.weight(6.0f)
				.maxSafePropellantStress(8)
				.failureMode(FailureMode.FRAGMENT)
				.connectsInSurvival(false)
				.isWeldable(false)
				.weldDamage(0)
				.weldStressPenalty(0)
				.minimumSpread(0.02f)
				.spreadReductionPerBarrel(1.15f)
				.build());

}
