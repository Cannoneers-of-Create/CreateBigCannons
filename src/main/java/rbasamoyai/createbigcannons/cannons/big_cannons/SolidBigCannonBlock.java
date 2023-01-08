package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.CannonEnd;

public abstract class SolidBigCannonBlock<TE extends BigCannonEndBlockEntity> extends BigCannonBaseBlock implements ITE<TE> {
	
	public SolidBigCannonBlock(Properties properties, BigCannonMaterial material) {
		super(properties, material);
	}
	
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.CLOSED; }

}
