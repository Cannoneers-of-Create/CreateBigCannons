package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCPonderIndex {

	private static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateBigCannons.MOD_ID);
	
	public static void register() {
		HELPER.forComponents(CBCBlocks.CANNON_MOUNT, CBCBlocks.YAW_CONTROLLER)
			.addStoryBoard("cannon_mount/assembly_and_use", CannonMountScenes::assemblyAndUse)
			.addStoryBoard("cannon_mount/firing_big_cannons", CannonMountScenes::firingBigCannons)
			.addStoryBoard("cannon_mount/using_autocannons", CannonMountScenes::usingAutocannons)
			.addStoryBoard("cannon_mount/customizing_autocannons", CannonMountScenes::customizingAutocannons);
		
		HELPER.forComponents(CBCBlocks.CANNON_LOADER, CBCBlocks.RAM_HEAD, CBCBlocks.WORM_HEAD, AllBlocks.PISTON_EXTENSION_POLE)
			.addStoryBoard("cannon_loader/loading_big_cannons", CannonLoadingScenes::loadingBigCannons);
		
		HELPER.forComponents(CBCBlocks.CANNON_LOADER, CBCBlocks.POWDER_CHARGE)
			.addStoryBoard("munitions/cannon_loads", CannonLoadingScenes::cannonLoads, CBCPonderTags.MUNITIONS);
		
		HELPER.forComponents(CBCItems.IMPACT_FUZE, CBCItems.TIMED_FUZE, CBCItems.PROXIMITY_FUZE, CBCItems.DELAYED_IMPACT_FUZE,
						CBCBlocks.HE_SHELL, CBCBlocks.SHRAPNEL_SHELL, CBCBlocks.AP_SHELL, CBCBlocks.FLUID_SHELL, CBCItems.FLAK_AUTOCANNON_ROUND)
			.addStoryBoard("munitions/fuzing_munitions", CannonLoadingScenes::fuzingMunitions, CBCPonderTags.MUNITIONS);
		
		HELPER.forComponents(CBCBlocks.CAST_IRON_SLIDING_BREECH, CBCBlocks.BRONZE_SLIDING_BREECH, CBCBlocks.STEEL_SLIDING_BREECH)
			.addStoryBoard("cannon_kinetics/sliding_breech", CannonKineticsScenes::slidingBreech);
		
		HELPER.forComponents(CBCBlocks.STEEL_SCREW_BREECH, CBCBlocks.NETHERSTEEL_SCREW_BREECH)
			.addStoryBoard("cannon_kinetics/screw_breech", CannonKineticsScenes::screwBreech);
		
		HELPER.forComponents(CBCBlocks.CASTING_SAND)
			.addStoryBoard("cannon_crafting/cannon_casting", CannonCraftingScenes::cannonCasting)
			.addStoryBoard("cannon_crafting/moving_cannons", CannonCraftingScenes::cannonMovement);
		
		HELPER.forComponents(CBCBlocks.CANNON_DRILL)
			.addStoryBoard("cannon_crafting/cannon_boring", CannonCraftingScenes::cannonBoring)
			.addStoryBoard("cannon_crafting/moving_cannons", CannonCraftingScenes::cannonMovement);
		
		HELPER.forComponents(CBCBlocks.CANNON_BUILDER)
			.addStoryBoard("cannon_crafting/cannon_building", CannonCraftingScenes::cannonBuilding)
			.addStoryBoard("cannon_crafting/finishing_built_up_cannons", CannonCraftingScenes::finishingBuiltUpCannons)
			.addStoryBoard("cannon_crafting/moving_cannons", CannonCraftingScenes::cannonMovement);

		HELPER.forComponents(AllBlocks.ENCASED_FAN)
			.addStoryBoard("cannon_crafting/finishing_built_up_cannons", CannonCraftingScenes::finishingBuiltUpCannons)
			.addStoryBoard("cannon_crafting/moving_cannons", CannonCraftingScenes::cannonMovement);
		
		HELPER.forComponents(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH, CBCBlocks.INCOMPLETE_BRONZE_SLIDING_BREECH, CBCBlocks.INCOMPLETE_STEEL_SLIDING_BREECH,
				CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH, CBCBlocks.INCOMPLETE_NETHERSTEEL_SCREW_BREECH,
				CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH, CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_BREECH, CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_BREECH,
				CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING, CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING, CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING)
			.addStoryBoard("cannon_crafting/incomplete_cannon_blocks", CannonCraftingScenes::incompleteCannonBlocks);
		
		HELPER.forComponents(CBCBlocks.BASIN_FOUNDRY_LID)
			.addStoryBoard("cannon_crafting/basin_foundry", CannonCraftingScenes::basinFoundry);
	}
	
	public static void registerTags() {
		PonderRegistry.TAGS.forTag(CBCPonderTags.OPERATING_CANNONS)
			.add(CBCBlocks.CANNON_MOUNT)
			.add(CBCBlocks.YAW_CONTROLLER)
			.add(CBCBlocks.CANNON_LOADER)
			.add(CBCBlocks.RAM_HEAD)
			.add(CBCBlocks.WORM_HEAD)
			.add(AllBlocks.PISTON_EXTENSION_POLE)
			.add(CBCBlocks.CAST_IRON_SLIDING_BREECH)
			.add(CBCBlocks.BRONZE_SLIDING_BREECH)
			.add(CBCBlocks.STEEL_SLIDING_BREECH)
			.add(CBCBlocks.STEEL_SCREW_BREECH)
			.add(CBCBlocks.NETHERSTEEL_SCREW_BREECH);
		
		PonderRegistry.TAGS.forTag(CBCPonderTags.MUNITIONS)
			.add(CBCBlocks.POWDER_CHARGE)
			.add(CBCBlocks.HE_SHELL)
			.add(CBCBlocks.SHRAPNEL_SHELL)
			.add(CBCBlocks.AP_SHELL)
			.add(CBCItems.IMPACT_FUZE)
			.add(CBCItems.TIMED_FUZE)
			.add(CBCItems.PROXIMITY_FUZE)
			.add(CBCItems.DELAYED_IMPACT_FUZE);
		
		PonderRegistry.TAGS.forTag(CBCPonderTags.CANNON_CRAFTING)
			.add(CBCBlocks.CASTING_SAND)
			.add(CBCBlocks.CANNON_DRILL)
			.add(CBCBlocks.CANNON_BUILDER)
			.add(AllBlocks.ENCASED_FAN)
			.add(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH)
			.add(CBCBlocks.INCOMPLETE_BRONZE_SLIDING_BREECH)
			.add(CBCBlocks.INCOMPLETE_STEEL_SLIDING_BREECH)
			.add(CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH)
			.add(CBCBlocks.INCOMPLETE_NETHERSTEEL_SCREW_BREECH)
			.add(CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH)
			.add(CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_BREECH)
			.add(CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_BREECH)
			.add(CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING)
			.add(CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING)
			.add(CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING);
	}
	
	public static void registerLang() {
		PonderLocalization.provideRegistrateLang(CreateBigCannons.REGISTRATE);
	}
	
}
