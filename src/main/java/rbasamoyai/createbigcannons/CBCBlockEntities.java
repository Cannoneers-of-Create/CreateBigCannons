package rbasamoyai.createbigcannons;

import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlockEntity;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.CannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.SlidingBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.SlidingBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.cannonend.SlidingBreechInstance;

public class CBCBlockEntities {

	public static final BlockEntityEntry<CannonBlockEntity> CANNON = CreateBigCannons.registrate()
			.tileEntity("cannon", CannonBlockEntity::new)
			.validBlocks(CBCBlocks.CAST_IRON_CANNON_BARREL, CBCBlocks.CAST_IRON_CANNON_CHAMBER)
			.register();
	
	public static final BlockEntityEntry<SlidingBreechBlockEntity> SLIDING_BREECH = CreateBigCannons.registrate()
			.tileEntity("sliding_breech", SlidingBreechBlockEntity::new)
			.instance(() -> SlidingBreechInstance::new, false)
			.validBlocks(CBCBlocks.CAST_IRON_SLIDING_BREECH)
			.renderer(() -> SlidingBreechBlockEntityRenderer::new)
			.register();
	
	public static final BlockEntityEntry<CannonLoaderBlockEntity> CANNON_LOADER = CreateBigCannons.registrate()
			.tileEntity("cannon_loader", CannonLoaderBlockEntity::new)
			.instance(() -> ShaftInstance::new, false)
			.validBlocks(CBCBlocks.CANNON_LOADER)
			.renderer(() -> CannonLoaderBlockEntityRenderer::new)
			.register();
	
	public static void register() {}
	
}
