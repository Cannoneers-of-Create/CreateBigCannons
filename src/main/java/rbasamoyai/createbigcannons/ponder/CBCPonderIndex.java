package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;

import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;

public class CBCPonderIndex {

	private static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateBigCannons.MOD_ID);

	public static void register() {
		HELPER.forComponents(CBCBlocks.CANNON_MOUNT, CBCBlocks.YAW_CONTROLLER, CBCBlocks.CANNON_MOUNT_EXTENSION)
			.addStoryBoard("cannon_mount/assembly_and_use", CannonMountScenes::assemblyAndUse)
			.addStoryBoard("cannon_mount/firing_big_cannons", CannonMountScenes::firingBigCannons)
			.addStoryBoard("cannon_mount/upside_down_cannon_mounts", CannonMountScenes::upsideDownCannonMounts)
			.addStoryBoard("cannon_mount/using_cannon_mount_extensions", CannonMountScenes::usingExtensions)
			.addStoryBoard("cannon_mount/using_autocannons", CannonMountScenes::usingAutocannons)
			.addStoryBoard("cannon_mount/customizing_autocannons", CannonMountScenes::customizingAutocannons);

		HELPER.forComponents(CBCBlocks.FIXED_CANNON_MOUNT)
			.addStoryBoard("cannon_mount/using_fixed_cannon_mounts", CannonMountScenes::usingFixedCannonMounts);

		HELPER.forComponents(CBCBlocks.CANNON_LOADER, CBCBlocks.RAM_HEAD, CBCBlocks.WORM_HEAD)
			.addStoryBoard("cannon_loader/loading_big_cannons", CannonLoadingScenes::loadingBigCannons);

		HELPER.forComponents(CBCBlocks.CANNON_LOADER, CBCBlocks.POWDER_CHARGE, CBCBlocks.BIG_CARTRIDGE)
			.addStoryBoard("munitions/cannon_loads", CannonLoadingScenes::cannonLoads, CBCPonderTags.MUNITIONS);

		HELPER.forComponents(CBCItems.IMPACT_FUZE, CBCItems.TIMED_FUZE, CBCItems.PROXIMITY_FUZE, CBCItems.DELAYED_IMPACT_FUZE,
				CBCBlocks.HE_SHELL, CBCBlocks.SHRAPNEL_SHELL, CBCBlocks.AP_SHELL, CBCBlocks.FLUID_SHELL, CBCBlocks.SMOKE_SHELL,
				CBCBlocks.DROP_MORTAR_SHELL, CBCItems.FLAK_AUTOCANNON_ROUND)
			.addStoryBoard("munitions/fuzing_munitions", CannonLoadingScenes::fuzingMunitions, CBCPonderTags.MUNITIONS);

		HELPER.forComponents(CBCBlocks.CAST_IRON_SLIDING_BREECH, CBCBlocks.BRONZE_SLIDING_BREECH, CBCBlocks.STEEL_SLIDING_BREECH)
			.addStoryBoard("cannon_kinetics/sliding_breech", CannonKineticsScenes::slidingBreech);

		HELPER.forComponents(CBCBlocks.CAST_IRON_QUICKFIRING_BREECH, CBCBlocks.BRONZE_QUICKFIRING_BREECH, CBCBlocks.STEEL_QUICKFIRING_BREECH,
				CBCItems.QUICKFIRING_MECHANISM)
			.addStoryBoard("cannon_crafting/making_quick_firing_breeches", CannonCraftingScenes::makingQuickFiringBreeches)
			.addStoryBoard("cannon_kinetics/quick_firing_breech", CannonLoadingScenes::quickFiringBreech)
			.addStoryBoard("cannon_kinetics/quick_firing_breech", CannonLoadingScenes::automatingQuickFiringBreeches);

		HELPER.forComponents(AllBlocks.MECHANICAL_ARM)
			.addStoryBoard("cannon_kinetics/quick_firing_breech", CannonLoadingScenes::automatingQuickFiringBreeches);

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

		HELPER.forComponents(CBCItems.WORM, CBCItems.RAM_ROD)
			.addStoryBoard("cannon_loader/handloading_tools", CannonLoadingScenes::handloadingTools);

		HELPER.forComponents(CBCBlocks.AUTOCANNON_AMMO_CONTAINER, CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER)
			.addStoryBoard("munitions/using_autocannon_ammo_container", CannonLoadingScenes::usingAutocannonAmmoContainer)
			.addStoryBoard("munitions/filling_autocannon_ammo_container", CannonLoadingScenes::fillingAutocannonAmmoContainer)
			.addStoryBoard("munitions/automating_autocannon_ammo_container", CannonLoadingScenes::automatingAutocannonAmmoContainer);

		HELPER.forComponents(CBCItems.CANNON_WELDER)
			.addStoryBoard("cannon_crafting/cannon_welder", CannonCraftingScenes::weldingCannons);

		HELPER.forComponents(AllBlocks.MECHANICAL_PISTON, AllBlocks.STICKY_MECHANICAL_PISTON,
				AllBlocks.GANTRY_CARRIAGE, AllBlocks.GANTRY_SHAFT, AllBlocks.ROPE_PULLEY)
			.addStoryBoard("cannon_loader/base_contraption_loading", CannonLoadingScenes::baseContraptionLoadingBigCannons);

		HELPER.forComponents(AllBlocks.PISTON_EXTENSION_POLE)
			.addStoryBoard("cannon_loader/loading_big_cannons", CannonLoadingScenes::loadingBigCannons)
			.addStoryBoard("cannon_loader/base_contraption_loading", CannonLoadingScenes::baseContraptionLoadingBigCannons);

		HELPER.forComponents(CBCItems.TRACER_TIP)
			.addStoryBoard("munitions/adding_tracers", CannonLoadingScenes::addingTracers);
	}

	public static void registerTags() {
		PonderRegistry.TAGS.forTag(CBCPonderTags.OPERATING_CANNONS)
			.add(CBCBlocks.CANNON_MOUNT)
			.add(CBCBlocks.CANNON_MOUNT_EXTENSION)
			.add(CBCBlocks.FIXED_CANNON_MOUNT)
			.add(CBCBlocks.CANNON_LOADER)
			.add(CBCBlocks.RAM_HEAD)
			.add(CBCBlocks.WORM_HEAD)
			.add(AllBlocks.PISTON_EXTENSION_POLE)
			.add(CBCBlocks.CAST_IRON_SLIDING_BREECH)
			.add(CBCBlocks.CAST_IRON_QUICKFIRING_BREECH)
			.add(CBCBlocks.BRONZE_SLIDING_BREECH)
			.add(CBCBlocks.BRONZE_QUICKFIRING_BREECH)
			.add(CBCBlocks.STEEL_SLIDING_BREECH)
			.add(CBCBlocks.STEEL_QUICKFIRING_BREECH)
			.add(CBCBlocks.STEEL_SCREW_BREECH)
			.add(CBCBlocks.NETHERSTEEL_SCREW_BREECH)
			.add(CBCItems.QUICKFIRING_MECHANISM)
			.add(AllBlocks.MECHANICAL_ARM)
			.add(CBCItems.RAM_ROD)
			.add(CBCItems.WORM)
			.add(AllBlocks.MECHANICAL_PISTON)
			.add(AllBlocks.STICKY_MECHANICAL_PISTON)
			.add(AllBlocks.GANTRY_CARRIAGE)
			.add(AllBlocks.GANTRY_SHAFT)
			.add(AllBlocks.ROPE_PULLEY);

		PonderRegistry.TAGS.forTag(CBCPonderTags.MUNITIONS)
			.add(CBCBlocks.POWDER_CHARGE)
			.add(CBCBlocks.BIG_CARTRIDGE)
			.add(CBCBlocks.HE_SHELL)
			.add(CBCBlocks.SHRAPNEL_SHELL)
			.add(CBCBlocks.AP_SHELL)
			.add(CBCBlocks.SMOKE_SHELL)
			.add(CBCBlocks.FLUID_SHELL)
			.add(CBCBlocks.DROP_MORTAR_SHELL)
			.add(CBCItems.IMPACT_FUZE)
			.add(CBCItems.TIMED_FUZE)
			.add(CBCItems.PROXIMITY_FUZE)
			.add(CBCItems.DELAYED_IMPACT_FUZE)
			.add(CBCItems.TRACER_TIP)
			.add(CBCBlocks.AUTOCANNON_AMMO_CONTAINER)
			.add(CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER);

		PonderRegistry.TAGS.forTag(CBCPonderTags.CANNON_CRAFTING)
			.add(CBCBlocks.CASTING_SAND)
			.add(CBCBlocks.CANNON_DRILL)
			.add(CBCBlocks.CANNON_BUILDER)
			.add(AllBlocks.ENCASED_FAN)
			.add(CBCItems.CANNON_WELDER)
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
