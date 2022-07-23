package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;

import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCPonderIndex {

	private static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateBigCannons.MOD_ID);
	
	public static void register() {
		HELPER.forComponents(CBCBlocks.CANNON_MOUNT, CBCBlocks.YAW_CONTROLLER)
			.addStoryBoard("cannon_mount/assembly_and_use", CannonMountScenes::assemblyAndUse)
			.addStoryBoard("cannon_mount/firing_big_cannons", CannonMountScenes::firingBigCannons);
	}
	
}
