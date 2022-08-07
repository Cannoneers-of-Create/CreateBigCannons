package rbasamoyai.createbigcannons;

import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlockEntity;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannonmount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannonmount.CannonMountBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannonmount.CannonMountInstance;
import rbasamoyai.createbigcannons.cannonmount.YawControllerBlockEntity;
import rbasamoyai.createbigcannons.cannonmount.YawControllerBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannonmount.YawControllerInstance;
import rbasamoyai.createbigcannons.cannons.CannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEndBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.ScrewBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.ScrewBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.cannonend.ScrewBreechInstance;
import rbasamoyai.createbigcannons.cannons.cannonend.SlidingBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.SlidingBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.cannonend.SlidingBreechInstance;
import rbasamoyai.createbigcannons.crafting.CannonCastBlockEntity;
import rbasamoyai.createbigcannons.crafting.CannonCastBlockEntityRenderer;
import rbasamoyai.createbigcannons.munitions.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.FuzedBlockEntityRenderer;
import rbasamoyai.createbigcannons.munitions.FuzedBlockInstance;

public class CBCBlockEntities {

	public static final BlockEntityEntry<CannonBlockEntity> CANNON = CreateBigCannons.registrate()
			.tileEntity("cannon", CannonBlockEntity::new)
			.validBlocks(CBCBlocks.LOG_CANNON_CHAMBER,
						CBCBlocks.WROUGHT_IRON_CANNON_CHAMBER,
						CBCBlocks.CAST_IRON_CANNON_BARREL, CBCBlocks.CAST_IRON_CANNON_CHAMBER,
						CBCBlocks.BRONZE_CANNON_BARREL, CBCBlocks.BRONZE_CANNON_CHAMBER,
						CBCBlocks.STEEL_CANNON_BARREL, CBCBlocks.STEEL_CANNON_CHAMBER,
						CBCBlocks.NETHER_GUNMETAL_CANNON_BARREL, CBCBlocks.NETHER_GUNMETAL_CANNON_CHAMBER)
			.register();
	
	public static final BlockEntityEntry<SlidingBreechBlockEntity> SLIDING_BREECH = CreateBigCannons.registrate()
			.tileEntity("sliding_breech", SlidingBreechBlockEntity::new)
			.instance(() -> SlidingBreechInstance::new, false)
			.renderer(() -> SlidingBreechBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.CAST_IRON_SLIDING_BREECH, CBCBlocks.BRONZE_SLIDING_BREECH, CBCBlocks.STEEL_SLIDING_BREECH)
			.register();
	
	public static final BlockEntityEntry<ScrewBreechBlockEntity> SCREW_BREECH = CreateBigCannons.registrate()
			.tileEntity("screw_breech", ScrewBreechBlockEntity::new)
			.instance(() -> ScrewBreechInstance::new, false)
			.renderer(() -> ScrewBreechBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.STEEL_SCREW_BREECH, CBCBlocks.NETHER_GUNMETAL_SCREW_BREECH)
			.register();
	
	public static final BlockEntityEntry<CannonEndBlockEntity> CANNON_END = CreateBigCannons.registrate()
			.tileEntity("cannon_end", CannonEndBlockEntity::new)
			.validBlocks(CBCBlocks.LOG_CANNON_END, CBCBlocks.WROUGHT_IRON_CANNON_END, CBCBlocks.CAST_IRON_CANNON_END, CBCBlocks.BRONZE_CANNON_END)
			.register();
	
	public static final BlockEntityEntry<CannonLoaderBlockEntity> CANNON_LOADER = CreateBigCannons.registrate()
			.tileEntity("cannon_loader", CannonLoaderBlockEntity::new)
			.instance(() -> ShaftInstance::new, false)
			.renderer(() -> CannonLoaderBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.CANNON_LOADER)
			.register();
	
	public static final BlockEntityEntry<CannonMountBlockEntity> CANNON_MOUNT = CreateBigCannons.registrate()
			.tileEntity("cannon_mount", CannonMountBlockEntity::new)
			.instance(() -> CannonMountInstance::new)
			.renderer(() -> CannonMountBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.CANNON_MOUNT)
			.register();
	
	public static final BlockEntityEntry<YawControllerBlockEntity> YAW_CONTROLLER = CreateBigCannons.registrate()
			.tileEntity("yaw_controller", YawControllerBlockEntity::new)
			.instance(() -> YawControllerInstance::new)
			.renderer(() -> YawControllerBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.YAW_CONTROLLER)
			.register();
	
	public static final BlockEntityEntry<FuzedBlockEntity> FUZED_BLOCK = CreateBigCannons.registrate()
			.tileEntity("fuzed_block", FuzedBlockEntity::new)
			.instance(() -> FuzedBlockInstance::new)
			.renderer(() -> FuzedBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.HE_SHELL, CBCBlocks.SHRAPNEL_SHELL)
			.register();
	
	public static final BlockEntityEntry<CannonCastBlockEntity> CANNON_CAST = CreateBigCannons.registrate()
			.tileEntity("cannon_cast", CannonCastBlockEntity::new)
			.renderer(() -> CannonCastBlockEntityRenderer::new)
			.validBlocks(CBCBlocks.CANNON_CAST)
			.register();
	
	public static void register() {}
	
}
