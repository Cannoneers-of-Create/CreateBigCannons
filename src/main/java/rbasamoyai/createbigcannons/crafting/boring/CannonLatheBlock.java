package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.content.contraptions.components.structureMovement.bearing.BearingBlock;
import com.simibubi.create.foundation.block.ITE;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class CannonLatheBlock extends BearingBlock implements ITE<CannonLatheBlockEntity> {

	public CannonLatheBlock(Properties properties) {
		super(properties);
	}

	@Override public Class<CannonLatheBlockEntity> getTileEntityClass() { return CannonLatheBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonLatheBlockEntity> getTileEntityType() { return null; }

}
