package rbasamoyai.createbigcannons.index;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountExtensionBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountInstance;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.YawControllerBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageBlockEntity;
import rbasamoyai.createbigcannons.cannon_loading.CannonLoaderBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechInstance;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AutocannonBreechRenderer;
import rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring.AutocannonRecoilSpringBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring.AutocannonRecoilSpringInstance;
import rbasamoyai.createbigcannons.cannons.autocannon.recoil_spring.AutocannonRecoilSpringRenderer;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechInstance;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech.ScrewBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech.ScrewBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech.ScrewBreechInstance;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechInstance;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEndBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlockEntity;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredBigCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredCannonBlockEntityRenderer;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.FinishedCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.FinishedCannonCastBlockEntityRenderer;
import rbasamoyai.createbigcannons.crafting.foundry.BasinFoundryBlockEntity;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteAutocannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteBigCannonBlockEntity;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntityRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockInstance;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockEntity;

public class CBCBlockEntities {

	public static final BlockEntityEntry<BigCannonBlockEntity> CANNON = REGISTRATE
		.blockEntity("cannon", BigCannonBlockEntity::new)
		.validBlocks(CBCBlocks.LOG_CANNON_CHAMBER,
			CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER,
			CBCBlocks.CAST_IRON_CANNON_BARREL, CBCBlocks.CAST_IRON_CANNON_CHAMBER,
			CBCBlocks.BRONZE_CANNON_BARREL, CBCBlocks.BRONZE_CANNON_CHAMBER,
			CBCBlocks.STEEL_CANNON_BARREL, CBCBlocks.BUILT_UP_STEEL_CANNON_BARREL, CBCBlocks.STEEL_CANNON_CHAMBER, CBCBlocks.BUILT_UP_STEEL_CANNON_CHAMBER, CBCBlocks.THICK_STEEL_CANNON_CHAMBER,
			CBCBlocks.NETHERSTEEL_CANNON_BARREL, CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_BARREL, CBCBlocks.NETHERSTEEL_CANNON_CHAMBER, CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_CHAMBER, CBCBlocks.THICK_NETHERSTEEL_CANNON_CHAMBER)
		.register();

	public static final BlockEntityEntry<SlidingBreechBlockEntity> SLIDING_BREECH = REGISTRATE
		.blockEntity("sliding_breech", SlidingBreechBlockEntity::new)
		.instance(() -> SlidingBreechInstance::new, false)
		.renderer(() -> SlidingBreechBlockEntityRenderer::new)
		.validBlocks(CBCBlocks.CAST_IRON_SLIDING_BREECH, CBCBlocks.BRONZE_SLIDING_BREECH, CBCBlocks.STEEL_SLIDING_BREECH)
		.register();

	public static final BlockEntityEntry<ScrewBreechBlockEntity> SCREW_BREECH = REGISTRATE
		.blockEntity("screw_breech", ScrewBreechBlockEntity::new)
		.instance(() -> ScrewBreechInstance::new, false)
		.renderer(() -> ScrewBreechBlockEntityRenderer::new)
		.validBlocks(CBCBlocks.STEEL_SCREW_BREECH, CBCBlocks.NETHERSTEEL_SCREW_BREECH)
		.register();

	public static final BlockEntityEntry<BigCannonEndBlockEntity> CANNON_END = REGISTRATE
		.blockEntity("cannon_end", BigCannonEndBlockEntity::new)
		.validBlocks(CBCBlocks.LOG_CANNON_END, CBCBlocks.WROUGHT_IRON_CANNON_END, CBCBlocks.WROUGHT_IRON_DROP_MORTAR_END,
			CBCBlocks.CAST_IRON_CANNON_END, CBCBlocks.BRONZE_CANNON_END,
			CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL, CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER, CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH,
			CBCBlocks.UNBORED_BRONZE_CANNON_BARREL, CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER, CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH,
			CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER, CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER, CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER,
			CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER, CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER, CBCBlocks.UNBORED_STEEL_SLIDING_BREECH, CBCBlocks.UNBORED_STEEL_SCREW_BREECH,
			CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER, CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER, CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER,
			CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER, CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER, CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH)
		.register();

	public static final BlockEntityEntry<QuickfiringBreechBlockEntity> QUICKFIRING_BREECH = REGISTRATE
		.blockEntity("quickfiring_breech", QuickfiringBreechBlockEntity::new)
		.instance(() -> QuickfiringBreechInstance::new)
		.renderer(() -> QuickfiringBreechBlockEntityRenderer::new)
		.validBlocks(CBCBlocks.CAST_IRON_QUICKFIRING_BREECH, CBCBlocks.BRONZE_QUICKFIRING_BREECH, CBCBlocks.STEEL_QUICKFIRING_BREECH)
		.register();

	public static final BlockEntityEntry<CannonLoaderBlockEntity> CANNON_LOADER = REGISTRATE
		.blockEntity("cannon_loader", CannonLoaderBlockEntity::new)
		.instance(() -> ShaftInstance::new, false)
		.renderer(() -> ShaftRenderer::new)
		.validBlock(CBCBlocks.CANNON_LOADER)
		.register();

	public static final BlockEntityEntry<CannonMountBlockEntity> CANNON_MOUNT = REGISTRATE
		.blockEntity("cannon_mount", CannonMountBlockEntity::new)
		.instance(() -> CannonMountInstance::new)
		.renderer(() -> CannonMountBlockEntityRenderer::new)
		.validBlock(CBCBlocks.CANNON_MOUNT)
		.register();

	public static final BlockEntityEntry<YawControllerBlockEntity> YAW_CONTROLLER = REGISTRATE
		.blockEntity("yaw_controller", YawControllerBlockEntity::new)
		.instance(() -> ShaftInstance::new)
		.renderer(() -> ShaftRenderer::new)
		.validBlock(CBCBlocks.YAW_CONTROLLER)
		.register();

	public static final BlockEntityEntry<CannonMountExtensionBlockEntity> CANNON_MOUNT_EXTENSION = REGISTRATE
		.blockEntity("cannon_mount_extension", CannonMountExtensionBlockEntity::new)
		.instance(() -> ShaftInstance::new)
		.renderer(() -> ShaftRenderer::new)
		.validBlock(CBCBlocks.CANNON_MOUNT_EXTENSION)
		.register();

	public static final BlockEntityEntry<CannonCarriageBlockEntity> CANNON_CARRIAGE = REGISTRATE
		.blockEntity("cannon_carriage", CannonCarriageBlockEntity::new)
		.validBlock(CBCBlocks.CANNON_CARRIAGE)
		.register();

	public static final BlockEntityEntry<BigCannonProjectileBlockEntity> PROJECTILE_BLOCK = REGISTRATE
		.blockEntity("projectile_block", BigCannonProjectileBlockEntity::new)
		.validBlocks(CBCBlocks.SOLID_SHOT, CBCBlocks.AP_SHOT, CBCBlocks.TRAFFIC_CONE)
		.register();

	public static final BlockEntityEntry<FuzedBlockEntity> FUZED_BLOCK = REGISTRATE
		.blockEntity("fuzed_block", FuzedBlockEntity::new)
		.instance(() -> FuzedBlockInstance::new)
		.renderer(() -> FuzedBlockEntityRenderer::new)
		.validBlocks(CBCBlocks.HE_SHELL, CBCBlocks.SHRAPNEL_SHELL, CBCBlocks.AP_SHELL, CBCBlocks.DROP_MORTAR_SHELL, CBCBlocks.SMOKE_SHELL)
		.register();

	public static final BlockEntityEntry<AbstractFluidShellBlockEntity> FLUID_SHELL = REGISTRATE
		.blockEntity("fluid_shell", IndexPlatform::makeFluidShellBlockEntity)
		.instance(() -> FuzedBlockInstance::new)
		.renderer(() -> FuzedBlockEntityRenderer::new)
		.validBlock(CBCBlocks.FLUID_SHELL)
		.register();

	public static final BlockEntityEntry<AbstractCannonCastBlockEntity> CANNON_CAST = REGISTRATE
		.blockEntity("cannon_cast", IndexPlatform::makeCast)
		.renderer(IndexPlatform.getCastRenderer())
		.validBlock(CBCBlocks.CANNON_CAST)
		.register();

	public static final BlockEntityEntry<FinishedCannonCastBlockEntity> FINISHED_CANNON_CAST = REGISTRATE
		.blockEntity("finished_cannon_cast", FinishedCannonCastBlockEntity::new)
		.renderer(() -> FinishedCannonCastBlockEntityRenderer::new)
		.validBlock(CBCBlocks.FINISHED_CANNON_CAST)
		.register();

	public static final BlockEntityEntry<AbstractCannonDrillBlockEntity> CANNON_DRILL = REGISTRATE
		.blockEntity("cannon_drill", IndexPlatform::makeDrill)
		.instance(() -> ShaftInstance::new)
		.renderer(() -> ShaftRenderer::new)
		.validBlock(CBCBlocks.CANNON_DRILL)
		.register();

	public static final BlockEntityEntry<IncompleteBigCannonBlockEntity> INCOMPLETE_CANNON = REGISTRATE
		.blockEntity("incomplete_cannon", IncompleteBigCannonBlockEntity::new)
		.validBlocks(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH, CBCBlocks.INCOMPLETE_BRONZE_SLIDING_BREECH, CBCBlocks.INCOMPLETE_STEEL_SLIDING_BREECH,
			CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH, CBCBlocks.INCOMPLETE_NETHERSTEEL_SCREW_BREECH)
		.register();

	public static final BlockEntityEntry<CannonBuilderBlockEntity> CANNON_BUILDER = REGISTRATE
		.blockEntity("cannon_builder", CannonBuilderBlockEntity::new)
		.instance(() -> ShaftInstance::new)
		.renderer(() -> ShaftRenderer::new)
		.validBlock(CBCBlocks.CANNON_BUILDER)
		.register();

	public static final BlockEntityEntry<LayeredBigCannonBlockEntity> LAYERED_CANNON = REGISTRATE
		.blockEntity("layered_cannon", LayeredBigCannonBlockEntity::new)
		.renderer(() -> LayeredCannonBlockEntityRenderer::new)
		.validBlocks(CBCBlocks.BUILT_UP_CANNON,
			CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER, CBCBlocks.SMALL_STEEL_CANNON_LAYER, CBCBlocks.MEDIUM_STEEL_CANNON_LAYER, CBCBlocks.LARGE_STEEL_CANNON_LAYER, CBCBlocks.VERY_LARGE_STEEL_CANNON_LAYER,
			CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER, CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER, CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER, CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER, CBCBlocks.VERY_LARGE_NETHERSTEEL_CANNON_LAYER)
		.register();

	public static final BlockEntityEntry<BasinFoundryBlockEntity> BASIN_FOUNDRY = REGISTRATE
		.blockEntity("basin_foundry", BasinFoundryBlockEntity::new)
		.validBlock(CBCBlocks.BASIN_FOUNDRY_LID)
		.register();

	public static final BlockEntityEntry<AutocannonBlockEntity> AUTOCANNON = REGISTRATE
		.blockEntity("autocannon", AutocannonBlockEntity::new)
		.validBlocks(CBCBlocks.CAST_IRON_AUTOCANNON_BARREL, CBCBlocks.BRONZE_AUTOCANNON_BARREL, CBCBlocks.STEEL_AUTOCANNON_BARREL)
		.register();

	public static final BlockEntityEntry<AbstractAutocannonBreechBlockEntity> AUTOCANNON_BREECH = REGISTRATE
		.blockEntity("autocannon_breech", IndexPlatform::makeAutocannonBreech)
		.instance(() -> AutocannonBreechInstance::new)
		.renderer(() -> AutocannonBreechRenderer::new)
		.validBlocks(CBCBlocks.CAST_IRON_AUTOCANNON_BREECH, CBCBlocks.BRONZE_AUTOCANNON_BREECH, CBCBlocks.STEEL_AUTOCANNON_BREECH)
		.register();

	public static final BlockEntityEntry<AutocannonRecoilSpringBlockEntity> AUTOCANNON_RECOIL_SPRING = REGISTRATE
		.blockEntity("autocannon_recoil_spring", AutocannonRecoilSpringBlockEntity::new)
		.instance(() -> AutocannonRecoilSpringInstance::new)
		.renderer(() -> AutocannonRecoilSpringRenderer::new)
		.validBlocks(CBCBlocks.CAST_IRON_AUTOCANNON_RECOIL_SPRING, CBCBlocks.BRONZE_AUTOCANNON_RECOIL_SPRING, CBCBlocks.STEEL_AUTOCANNON_RECOIL_SPRING)
		.register();

	public static final BlockEntityEntry<IncompleteAutocannonBlockEntity> INCOMPLETE_AUTOCANNON = REGISTRATE
		.blockEntity("incomplete_autocannon", IncompleteAutocannonBlockEntity::new)
		.validBlocks(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BARREL, CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING, CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BREECH, CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING, CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH,
			CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BARREL, CBCBlocks.UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING, CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BREECH, CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING, CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_BREECH,
			CBCBlocks.UNBORED_STEEL_AUTOCANNON_BARREL, CBCBlocks.UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING, CBCBlocks.UNBORED_STEEL_AUTOCANNON_BREECH, CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING, CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_BREECH)
		.register();

	public static final BlockEntityEntry<BigCartridgeBlockEntity> BIG_CARTRIDGE = REGISTRATE
		.blockEntity("big_cartridge", BigCartridgeBlockEntity::new)
		.validBlock(CBCBlocks.BIG_CARTRIDGE)
		.register();

	public static final BlockEntityEntry<AutocannonAmmoContainerBlockEntity> AUTOCANNON_AMMO_CONTAINER = REGISTRATE
		.blockEntity("autocannon_ammo_container", AutocannonAmmoContainerBlockEntity::new)
		.validBlocks(CBCBlocks.AUTOCANNON_AMMO_CONTAINER, CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER)
		.register();

	public static void register() {
	}

}
