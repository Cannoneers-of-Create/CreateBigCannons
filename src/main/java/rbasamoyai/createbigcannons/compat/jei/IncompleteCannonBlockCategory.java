package rbasamoyai.createbigcannons.compat.jei;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.Components;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class IncompleteCannonBlockCategory extends CBCBlockRecipeCategory<IncompleteCannonBlockRecipe> {
	
	public IncompleteCannonBlockCategory(Info<IncompleteCannonBlockRecipe> info) {
		super(info);
	}
	
	final String[] romans = { "I", "II", "III", "IV", "V", "VI", "-" };
	
	@Override
	public void draw(IncompleteCannonBlockRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		Minecraft mc = Minecraft.getInstance();
		AllGuiTextures.JEI_LONG_ARROW.render(stack, 54, 44);
		
		int sz = recipe.ingredients().size();
		int base = this.getBackground().getWidth() / 2 - 12 * sz + 24;
		for (int i = 0; i < sz; ++i) {
			if (i == 0) continue;
			int j = i - 1;
			int posX = base + 24 * j;
			Component num = Components.literal(this.romans[Math.min(j, 6)]);
			mc.font.draw(stack, num, mc.font.width(num) / -2 + posX, 2, 0x888888);
		}
	}
	
	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, IncompleteCannonBlockRecipe recipe, IFocusGroup focuses) {
		List<ItemStack> ingredients = recipe.ingredients();
		int base = this.getBackground().getWidth() / 2 - 12 * ingredients.size() + 16;
		for (int i = 0; i < ingredients.size(); ++i) {
			builder.addSlot(RecipeIngredientRole.INPUT, i == 0 ? 21 : base + 24 * i - 24, i == 0 ? 40 : 15)
			.addItemStack(ingredients.get(i))
			.setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1);
		}
		
		builder.addSlot(RecipeIngredientRole.OUTPUT, 141, 40)
		.addItemStack(new ItemStack(recipe.getResultBlock()))
		.setBackground(CreateRecipeCategory.getRenderedSlot(), -1, -1);
	}

}
