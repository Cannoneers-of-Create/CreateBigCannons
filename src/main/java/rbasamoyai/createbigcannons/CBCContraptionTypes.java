package rbasamoyai.createbigcannons;

import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionType;

import rbasamoyai.createbigcannons.cannonloading.CannonLoadingContraption;
import rbasamoyai.createbigcannons.cannonmount.MountedCannonContraption;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillingContraption;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuildingContraption;

public class CBCContraptionTypes {

	public static final ContraptionType
			CANNON_LOADER = ContraptionType.register(CreateBigCannons.resource("cannon_loader").toString(), CannonLoadingContraption::new),
			MOUNTED_CANNON = ContraptionType.register(CreateBigCannons.resource("mounted_cannon").toString(), MountedCannonContraption::new),
			CANNON_DRILL = ContraptionType.register(CreateBigCannons.resource("cannon_drill").toString(), CannonDrillingContraption::new),
			CANNON_BUILDER = ContraptionType.register(CreateBigCannons.resource("cannon_builder").toString(), CannonBuildingContraption::new);
	
	public static void prepare() {}
	
}
