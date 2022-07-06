package rbasamoyai.createbigcannons;

import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

import rbasamoyai.createbigcannons.cannons.CannonBlockEntity;

public class CBCBlockEntities {

	public static final BlockEntityEntry<CannonBlockEntity> CANNON = CreateBigCannons.registrate()
			.tileEntity("cannon", CannonBlockEntity::new)
			.validBlocks(CBCBlocks.CAST_IRON_CANNON_BARREL)
			.register();
	
	public static void register() {}
	
}
