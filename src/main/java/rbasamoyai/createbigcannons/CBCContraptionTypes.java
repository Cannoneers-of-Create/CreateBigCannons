package rbasamoyai.createbigcannons;

import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionType;

import rbasamoyai.createbigcannons.cannonloading.CannonLoadingContraption;
import rbasamoyai.createbigcannons.cannonmount.MountedCannonContraption;

public class CBCContraptionTypes {

	public static final ContraptionType
			CANNON_LOADER = ContraptionType.register(CreateBigCannons.resource("cannon_loader").toString(), CannonLoadingContraption::new),
			MOUNTED_CANNON = ContraptionType.register(CreateBigCannons.resource("mounted_cannon").toString(), MountedCannonContraption::new);
	
	public static void prepare() {}
	
}
