package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEndBlockEntity;

import javax.annotation.Nullable;

import javax.annotation.Nullable;

public abstract class SolidBigCannonBlock<TE extends BigCannonEndBlockEntity> extends BigCannonBaseBlock implements ITE<TE> {
	
	public SolidBigCannonBlock(Properties properties, BigCannonMaterial material) {
		super(properties, material);
	}
	
	@Override public BigCannonEnd getOpeningType(@Nullable Level level, BlockState state, BlockPos pos) { return BigCannonEnd.CLOSED; }

}
