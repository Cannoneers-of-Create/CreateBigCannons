package rbasamoyai.createbigcannons.crafting.casting;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class CastingWandItem extends Item {

	public CastingWandItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (!level.isClientSide) {
			if (level.getBlockEntity(context.getClickedPos()) instanceof CannonCastBlockEntity cast) {
				cast.getControllerTE().castingTime = 0;
				cast.notifyUpdate();
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

}
