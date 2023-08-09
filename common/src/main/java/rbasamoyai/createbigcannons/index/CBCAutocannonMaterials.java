package rbasamoyai.createbigcannons.index;

import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterialProperties;

public class CBCAutocannonMaterials {

	public static final AutocannonMaterial
		CAST_IRON = AutocannonMaterial.register(CreateBigCannons.resource("cast_iron"),
			new AutocannonMaterialProperties(3, 1.5f, 2f, 0.5f, 5f, 2f, 2, 11, 1)),
		BRONZE = AutocannonMaterial.register(CreateBigCannons.resource("bronze"),
			new AutocannonMaterialProperties(5, 1f, 5.0f, 0.75f, 3f, 1.5f, 3, 25, 2)),
		STEEL = AutocannonMaterial.register(CreateBigCannons.resource("steel"),
			new AutocannonMaterialProperties(7, 2.5f, 8.0f, 1.0f, 3f, 1.5f, 4, 60, 3));
}
