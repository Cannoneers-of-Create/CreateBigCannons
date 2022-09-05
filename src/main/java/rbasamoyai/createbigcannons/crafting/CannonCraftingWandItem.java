package rbasamoyai.createbigcannons.crafting;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class CannonCraftingWandItem extends Item {

	public CannonCraftingWandItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		return context.getLevel().getBlockEntity(context.getClickedPos()) instanceof WandActionable wanded ? wanded.onWandUsed(context) : InteractionResult.PASS;
	}

}
