package rbasamoyai.createbigcannons;

import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonRenderer;
import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountInstance;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.YawControllerBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.YawControllerBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.YawControllerInstance;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.*;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonEndBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.ScrewBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.ScrewBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.big_cannons.ScrewBreechInstance;
import rbasamoyai.createbigcannons.cannons.big_cannons.SlidingBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.SlidingBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.big_cannons.SlidingBreechInstance;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlockEntity;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredBigCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredCannonBlockEntityRenderer;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastBlockEntityRenderer;
import rbasamoyai.createbigcannons.crafting.casting.FinishedCannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.FinishedCannonCastBlockEntityRenderer;
import rbasamoyai.createbigcannons.crafting.foundry.BasinFoundryBlockEntity;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteAutocannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteBigCannonBlockEntity;
import rbasamoyai.createbigcannons.munitions.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.FuzedBlockEntityRenderer;
import rbasamoyai.createbigcannons.munitions.FuzedBlockInstance;
import rbasamoyai.createbigcannons.munitions.fluidshell.FluidShellBlockEntity;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

public class CBCBlockEntities {

	public static final BlockEntityEntry<BigCannonBlockEntity> CANNON = REGISTRATE
			.tileEntity("cannon", BigCannonBlockEntity::new)
			.validBlocks(CBCBlocks.LOG_CANNON_CHAMBER,
						CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER,
						CBCBlocks.CAST_IRON_CANNON_BARREL, CBCBlocks.CAST_IRON_CANNON_CHAMBER,
						CBCBlocks.BRONZE_CANNON_BARREL, CBCBlocks.BRONZE_CANNON_CHAMBER,
						CBCBlocks.STEEL_CANNON_BARREL, CBCBlocks.BUILT_UP_STEEL_CANNON_BARREL, CBCBlocks.STEEL_CANNON_CHAMBER, CBCBlocks.BUILT_UP_STEEL_CANNON_CHAMBER, CBCBlocks.THICK_STEEL_CANNON_CHAMBER,
						CBCBlocks.NETHERSTEEL_CANNON_BARREL, CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_BARREL, CBCBlocks.NETHERSTEEL_CANNON_CHAMBER, CBCBlocks.BUILT_UP_NETHERSTEEL_CANNON_CHAMBER, CBCBlocks.THICK_NETHERSTEEL_CANNON_CHAMBER)
			.register();
	
	public static final BlockEntityEntry<SlidingBreechBlockEntity> SLIDING_BREECH = REGISTRATE
			.tileEntity("sliding_breech", SlidingBreechBlockEntity::new)
			.instance(() -> SlidingBreechInstance::new, false)
			.renderer(() -> SlidingBreechBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.CAST_IRON_SLIDING_BREECH, CBCBlocks.BRONZE_SLIDING_BREECH, CBCBlocks.STEEL_SLIDING_BREECH)
			.register();
	
	public static final BlockEntityEntry<ScrewBreechBlockEntity> SCREW_BREECH = REGISTRATE
			.tileEntity("screw_breech", ScrewBreechBlockEntity::new)
			.instance(() -> ScrewBreechInstance::new, false)
			.renderer(() -> ScrewBreechBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.STEEL_SCREW_BREECH, CBCBlocks.NETHERSTEEL_SCREW_BREECH)
			.register();
	
	public static final BlockEntityEntry<BigCannonEndBlockEntity> CANNON_END = REGISTRATE
			.tileEntity("cannon_end", BigCannonEndBlockEntity::new)
			.validBlocks(CBCBlocks.LOG_CANNON_END, CBCBlocks.WROUGHT_IRON_CANNON_END, CBCBlocks.CAST_IRON_CANNON_END, CBCBlocks.BRONZE_CANNON_END,
						CBCBlocks.UNBORED_CAST_IRON_CANNON_BARREL, CBCBlocks.UNBORED_CAST_IRON_CANNON_CHAMBER, CBCBlocks.UNBORED_CAST_IRON_SLIDING_BREECH,
						CBCBlocks.UNBORED_BRONZE_CANNON_BARREL, CBCBlocks.UNBORED_BRONZE_CANNON_CHAMBER, CBCBlocks.UNBORED_BRONZE_SLIDING_BREECH,
						CBCBlocks.UNBORED_VERY_SMALL_STEEL_CANNON_LAYER, CBCBlocks.UNBORED_SMALL_STEEL_CANNON_LAYER, CBCBlocks.UNBORED_MEDIUM_STEEL_CANNON_LAYER,
						CBCBlocks.UNBORED_LARGE_STEEL_CANNON_LAYER, CBCBlocks.UNBORED_VERY_LARGE_STEEL_CANNON_LAYER, CBCBlocks.UNBORED_STEEL_SLIDING_BREECH, CBCBlocks.UNBORED_STEEL_SCREW_BREECH,
						CBCBlocks.UNBORED_VERY_SMALL_NETHERSTEEL_CANNON_LAYER, CBCBlocks.UNBORED_SMALL_NETHERSTEEL_CANNON_LAYER, CBCBlocks.UNBORED_MEDIUM_NETHERSTEEL_CANNON_LAYER,
						CBCBlocks.UNBORED_LARGE_NETHERSTEEL_CANNON_LAYER, CBCBlocks.UNBORED_VERY_LARGE_NETHERSTEEL_CANNON_LAYER, CBCBlocks.UNBORED_NETHERSTEEL_SCREW_BREECH)
			.register();
	
	public static final BlockEntityEntry<CannonLoaderBlockEntity> CANNON_LOADER = REGISTRATE
			.tileEntity("cannon_loader", CannonLoaderBlockEntity::new)
			.instance(() -> ShaftInstance::new, false)
			.renderer(() -> MechanicalPistonRenderer::new)
			.validBlock(CBCBlocks.CANNON_LOADER)
			.register();
	
	public static final BlockEntityEntry<CannonMountBlockEntity> CANNON_MOUNT = REGISTRATE
			.tileEntity("cannon_mount", CannonMountBlockEntity::new)
			.instance(() -> CannonMountInstance::new)
			.renderer(() -> CannonMountBlockEntityRenderer::new)
			.validBlock(CBCBlocks.CANNON_MOUNT)
			.register();
	
	public static final BlockEntityEntry<YawControllerBlockEntity> YAW_CONTROLLER = REGISTRATE
			.tileEntity("yaw_controller", YawControllerBlockEntity::new)
			.instance(() -> YawControllerInstance::new)
			.renderer(() -> YawControllerBlockEntityRenderer::new)
			.validBlock(CBCBlocks.YAW_CONTROLLER)
			.register();

	public static final BlockEntityEntry<CannonCarriageBlockEntity> CANNON_CARRIAGE = REGISTRATE
			.tileEntity("cannon_carriage", CannonCarriageBlockEntity::new)
			.validBlock(CBCBlocks.CANNON_CARRIAGE)
			.register();

	public static final BlockEntityEntry<FuzedBlockEntity> FUZED_BLOCK = REGISTRATE
			.tileEntity("fuzed_block", FuzedBlockEntity::new)
			.instance(() -> FuzedBlockInstance::new)
			.renderer(() -> FuzedBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.HE_SHELL, CBCBlocks.SHRAPNEL_SHELL, CBCBlocks.AP_SHELL)
			.register();
	
	public static final BlockEntityEntry<FluidShellBlockEntity> FLUID_SHELL = REGISTRATE
			.tileEntity("fluid_shell", FluidShellBlockEntity::new)
			.instance(() -> FuzedBlockInstance::new)
			.renderer(() -> FuzedBlockEntityRenderer::new)
			.validBlock(CBCBlocks.FLUID_SHELL)
			.register();
	
	public static final BlockEntityEntry<CannonCastBlockEntity> CANNON_CAST = REGISTRATE
			.tileEntity("cannon_cast", CannonCastBlockEntity::new)
			.renderer(() -> CannonCastBlockEntityRenderer::new)
			.validBlock(CBCBlocks.CANNON_CAST)
			.register();
	
	public static final BlockEntityEntry<FinishedCannonCastBlockEntity> FINISHED_CANNON_CAST = REGISTRATE
			.tileEntity("finished_cannon_cast", FinishedCannonCastBlockEntity::new)
			.renderer(() -> FinishedCannonCastBlockEntityRenderer::new)
			.validBlock(CBCBlocks.FINISHED_CANNON_CAST)
			.register();
	
	public static final BlockEntityEntry<CannonDrillBlockEntity> CANNON_DRILL = REGISTRATE
			.tileEntity("cannon_drill", CannonDrillBlockEntity::new)
			.instance(() -> ShaftInstance::new)
			.renderer(() -> MechanicalPistonRenderer::new)
			.validBlock(CBCBlocks.CANNON_DRILL)
			.register();
	
	public static final BlockEntityEntry<IncompleteBigCannonBlockEntity> INCOMPLETE_CANNON = REGISTRATE
			.tileEntity("incomplete_cannon", IncompleteBigCannonBlockEntity::new)
			.validBlocks(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH, CBCBlocks.INCOMPLETE_BRONZE_SLIDING_BREECH, CBCBlocks.INCOMPLETE_STEEL_SLIDING_BREECH,
					CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH, CBCBlocks.INCOMPLETE_NETHERSTEEL_SCREW_BREECH)
			.register();
	
	public static final BlockEntityEntry<CannonBuilderBlockEntity> CANNON_BUILDER = REGISTRATE
			.tileEntity("cannon_builder", CannonBuilderBlockEntity::new)
			.instance(() -> ShaftInstance::new)
			.renderer(() -> MechanicalPistonRenderer::new)
			.validBlock(CBCBlocks.CANNON_BUILDER)
			.register();
	
	public static final BlockEntityEntry<LayeredBigCannonBlockEntity> LAYERED_CANNON = REGISTRATE
			.tileEntity("layered_cannon", LayeredBigCannonBlockEntity::new)
			.renderer(() -> LayeredCannonBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.BUILT_UP_CANNON,
					CBCBlocks.VERY_SMALL_STEEL_CANNON_LAYER, CBCBlocks.SMALL_STEEL_CANNON_LAYER, CBCBlocks.MEDIUM_STEEL_CANNON_LAYER, CBCBlocks.LARGE_STEEL_CANNON_LAYER, CBCBlocks.VERY_LARGE_STEEL_CANNON_LAYER,
					CBCBlocks.VERY_SMALL_NETHERSTEEL_CANNON_LAYER, CBCBlocks.SMALL_NETHERSTEEL_CANNON_LAYER, CBCBlocks.MEDIUM_NETHERSTEEL_CANNON_LAYER, CBCBlocks.LARGE_NETHERSTEEL_CANNON_LAYER, CBCBlocks.VERY_LARGE_NETHERSTEEL_CANNON_LAYER)
			.register();
	
	public static final BlockEntityEntry<BasinFoundryBlockEntity> BASIN_FOUNDRY = REGISTRATE
			.tileEntity("basin_foundry", BasinFoundryBlockEntity::new)
			.validBlock(CBCBlocks.BASIN_FOUNDRY_LID)
			.register();

	public static final BlockEntityEntry<AutocannonBlockEntity> AUTOCANNON = REGISTRATE
			.tileEntity("autocannon", AutocannonBlockEntity::new)
			.validBlocks(CBCBlocks.CAST_IRON_AUTOCANNON_BARREL, CBCBlocks.BRONZE_AUTOCANNON_BARREL, CBCBlocks.STEEL_AUTOCANNON_BARREL)
			.register();

	public static final BlockEntityEntry<AutocannonBreechBlockEntity> AUTOCANNON_BREECH = REGISTRATE
			.tileEntity("autocannon_breech", AutocannonBreechBlockEntity::new)
			.instance(() -> AutocannonBreechInstance::new)
			.renderer(() -> AutocannonBreechRenderer::new)
			.validBlocks(CBCBlocks.CAST_IRON_AUTOCANNON_BREECH, CBCBlocks.BRONZE_AUTOCANNON_BREECH, CBCBlocks.STEEL_AUTOCANNON_BREECH)
			.register();

	public static final BlockEntityEntry<AutocannonRecoilSpringBlockEntity> AUTOCANNON_RECOIL_SPRING = REGISTRATE
			.tileEntity("autocannon_recoil_spring", AutocannonRecoilSpringBlockEntity::new)
			.instance(() -> AutocannonRecoilSpringInstance::new)
			.renderer(() -> AutocannonRecoilSpringRenderer::new)
			.validBlocks(CBCBlocks.CAST_IRON_AUTOCANNON_RECOIL_SPRING, CBCBlocks.BRONZE_AUTOCANNON_RECOIL_SPRING, CBCBlocks.STEEL_AUTOCANNON_RECOIL_SPRING)
			.register();

	public static final BlockEntityEntry<IncompleteAutocannonBlockEntity> INCOMPLETE_AUTOCANNON = REGISTRATE
			.tileEntity("incomplete_autocannon", IncompleteAutocannonBlockEntity::new)
			.validBlocks(CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BARREL, CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_RECOIL_SPRING, CBCBlocks.UNBORED_CAST_IRON_AUTOCANNON_BREECH, CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING, CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH,
					CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BARREL, CBCBlocks.UNBORED_BRONZE_AUTOCANNON_RECOIL_SPRING, CBCBlocks.UNBORED_BRONZE_AUTOCANNON_BREECH, CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING, CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_BREECH,
					CBCBlocks.UNBORED_STEEL_AUTOCANNON_BARREL, CBCBlocks.UNBORED_STEEL_AUTOCANNON_RECOIL_SPRING, CBCBlocks.UNBORED_STEEL_AUTOCANNON_BREECH, CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING, CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_BREECH)
			.register();

	public static void register() {}
	
}
