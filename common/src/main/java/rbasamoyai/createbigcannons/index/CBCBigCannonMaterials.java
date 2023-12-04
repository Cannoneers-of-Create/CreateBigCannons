package rbasamoyai.createbigcannons.index;

import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialProperties;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialProperties.FailureMode;

public class CBCBigCannonMaterials {

	public static final BigCannonMaterial
		INCOMPLETE_LAYERED = BigCannonMaterial.register(CreateBigCannons.resource("incomplete_layered"),
			new BigCannonMaterialProperties(-1d, 1.0f, 0, FailureMode.FRAGMENT, false, false, 0, 0)),
		LOG = BigCannonMaterial.register(CreateBigCannons.resource("log"),
			new BigCannonMaterialProperties(-1d, 1.0f, 0, FailureMode.FRAGMENT, true, false, 0, 0)),
		WROUGHT_IRON = BigCannonMaterial.register(CreateBigCannons.resource("wrought_iron"),
			new BigCannonMaterialProperties(2d, 2.0f, 1, FailureMode.RUPTURE, true, false, 0, 0)),
		CAST_IRON = BigCannonMaterial.register(CreateBigCannons.resource("cast_iron"),
			new BigCannonMaterialProperties(2d, 3.0f, 2, FailureMode.FRAGMENT, false, true, 1, 1)),
		BRONZE = BigCannonMaterial.register(CreateBigCannons.resource("bronze"),
			new BigCannonMaterialProperties(4d / 3d, 2.0f, 4, FailureMode.RUPTURE, false, true, 1, 0)),
		STEEL = BigCannonMaterial.register(CreateBigCannons.resource("steel"),
			new BigCannonMaterialProperties(1d, 5.0f, 6, FailureMode.FRAGMENT, false, true, 2, 2)),
		NETHERSTEEL = BigCannonMaterial.register(CreateBigCannons.resource("nethersteel"),
			new BigCannonMaterialProperties(2d / 3d, 6.0f, 8, FailureMode.FRAGMENT, false, false, 0, 0));

}
