package rbasamoyai.createbigcannons.index;

import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionType;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannonloading.CannonLoadingContraption;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillingContraption;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuildingContraption;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class CBCContraptionTypes {

	public static final ContraptionType
			CANNON_LOADER = ContraptionType.register(CreateBigCannons.resource("cannon_loader").toString(), CannonLoadingContraption::new),
			MOUNTED_CANNON = ContraptionType.register(CreateBigCannons.resource("mounted_cannon").toString(), MountedBigCannonContraption::new),
			MOUNTED_AUTOCANNON = ContraptionType.register(CreateBigCannons.resource("mounted_autocannon").toString(), IndexPlatform::makeAutocannon),
			CANNON_DRILL = ContraptionType.register(CreateBigCannons.resource("cannon_drill").toString(), CannonDrillingContraption::new),
			CANNON_BUILDER = ContraptionType.register(CreateBigCannons.resource("cannon_builder").toString(), CannonBuildingContraption::new);
	
	public static void prepare() {}
	
}
