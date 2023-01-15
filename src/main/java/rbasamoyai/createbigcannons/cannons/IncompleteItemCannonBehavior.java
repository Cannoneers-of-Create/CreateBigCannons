package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.world.item.ItemStack;

public class IncompleteItemCannonBehavior extends ItemCannonBehavior {

	public IncompleteItemCannonBehavior(SmartTileEntity te) { super(te); }

	@Override public boolean canLoadItem(ItemStack stack) { return false; }

}
