package rbasamoyai.createbigcannons.compat.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CBCBlockRecipeCategory<T> implements IRecipeCategory<T> {

	@Override public Component getTitle() { return null; }

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDrawable getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceLocation getUid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends T> getRecipeClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
