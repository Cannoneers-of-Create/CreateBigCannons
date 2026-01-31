package rbasamoyai.createbigcannons.crafting;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public interface WandActionable {

	InteractionResult onWandUsed(UseOnContext context);
	
}
