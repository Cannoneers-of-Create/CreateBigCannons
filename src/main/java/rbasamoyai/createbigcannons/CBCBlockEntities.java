package rbasamoyai.createbigcannons;

import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlockEntity;
import rbasamoyai.createbigcannons.cannonloading.CannonLoaderBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.CannonBlockEntity;

public class CBCBlockEntities {

	public static final BlockEntityEntry<CannonBlockEntity> CANNON = CreateBigCannons.registrate()
			.tileEntity("cannon", CannonBlockEntity::new)
			.validBlocks(CBCBlocks.CAST_IRON_CANNON_BARREL, CBCBlocks.CAST_IRON_CANNON_CHAMBER)
			.register();
	
	public static final BlockEntityEntry<CannonLoaderBlockEntity> CANNON_LOADER = CreateBigCannons.registrate()
			.tileEntity("cannon_loader", CannonLoaderBlockEntity::new)
			.instance(() -> ShaftInstance::new, false)
			.validBlocks(CBCBlocks.CANNON_LOADER)
			.renderer(() -> CannonLoaderBlockEntityRenderer::new)
			.register();
	
	public static void register() {}
	
}
