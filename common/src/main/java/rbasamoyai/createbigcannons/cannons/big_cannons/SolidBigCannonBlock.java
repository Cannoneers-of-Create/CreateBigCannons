package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.simibubi.create.foundation.block.IBE;

import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEndBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;

public abstract class SolidBigCannonBlock<TE extends BigCannonEndBlockEntity> extends BigCannonBaseBlock implements IBE<TE> {

	public SolidBigCannonBlock(Properties properties, BigCannonMaterial material) {
		super(properties, material);
	}

	@Override public BigCannonEnd getDefaultOpeningType() { return BigCannonEnd.CLOSED; }

}
