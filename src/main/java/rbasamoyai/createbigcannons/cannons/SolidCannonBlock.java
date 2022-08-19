package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEndBlockEntity;

public abstract class SolidCannonBlock<TE extends CannonEndBlockEntity> extends CannonBaseBlock implements ITE<TE> {
	
	public SolidCannonBlock(Properties properties, CannonMaterial material) {
		super(properties, material);
	}
	
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.CLOSED; }

}
