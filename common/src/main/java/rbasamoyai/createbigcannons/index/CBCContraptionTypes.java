package rbasamoyai.createbigcannons.index;

import com.simibubi.create.content.contraptions.ContraptionType;

import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannonloading.CannonLoadingContraption;
import rbasamoyai.createbigcannons.cannonloading.GantryRammerContraption;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillingContraption;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuildingContraption;

public class CBCContraptionTypes {

	public static final ContraptionType
		CANNON_LOADER = ContraptionType.register(CreateBigCannons.resource("cannon_loader").toString(), CannonLoadingContraption::new),
		RAMMER = ContraptionType.register(CreateBigCannons.resource("gantry_rammer").toString(), GantryRammerContraption::new),
		MOUNTED_CANNON = ContraptionType.register(CreateBigCannons.resource("mounted_cannon").toString(), MountedBigCannonContraption::new),
		MOUNTED_AUTOCANNON = ContraptionType.register(CreateBigCannons.resource("mounted_autocannon").toString(), MountedAutocannonContraption::new),
		CANNON_DRILL = ContraptionType.register(CreateBigCannons.resource("cannon_drill").toString(), CannonDrillingContraption::new),
		CANNON_BUILDER = ContraptionType.register(CreateBigCannons.resource("cannon_builder").toString(), CannonBuildingContraption::new);

	public static void prepare() {
	}

}
