package rbasamoyai.createbigcannons.compat.rei;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.Components;

import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.compat.common_jei.IncompleteCannonBlockRecipe;

import static com.simibubi.create.compat.rei.category.CreateRecipeCategory.basicSlot;

public class IncompleteCannonBlockCategory extends CBCBlockRecipeCategory<IncompleteCannonBlockRecipe> {

	public IncompleteCannonBlockCategory(Info<IncompleteCannonBlockRecipe> info) {
		super(info);
	}

	final String[] romans = { "I", "II", "III", "IV", "V", "VI", "-" };

	@Override
	public void draw(IncompleteCannonBlockRecipe recipe, PoseStack stack, double mouseX, double mouseY) {
		Minecraft mc = Minecraft.getInstance();
		AllGuiTextures.JEI_LONG_ARROW.render(stack, 54, 38);

		int sz = recipe.ingredients().size();
		int base = this.getDisplayWidth(null) / 2 - 12 * sz + 24;
		for (int i = 0; i < sz; ++i) {
			if (i == 0) continue;
			int j = i - 1;
			int posX = base + 24 * j;
			Component num = Components.literal(this.romans[Math.min(j, 6)]);
			mc.font.draw(stack, num, mc.font.width(num) / -2 + posX, 2, 0x888888);
		}
	}

	@Override
	public void addWidgets(CBCDisplay<IncompleteCannonBlockRecipe> display, List<Widget> ingredients, Point origin) {
		IncompleteCannonBlockRecipe recipe = display.getRecipe();
		List<ItemStack> recipeInds = recipe.ingredients();
		int sz = recipeInds.size();
		int base = this.getDisplayWidth(null) / 2 - 12 * sz + 16;
		for (int i = 0; i < sz; ++i) {
			int x = i == 0 ? 21 : base + 24 * i - 24;
			int y = i == 0 ? 34 : 15;
			ingredients.add(basicSlot(x, y, origin).markInput().entry(EntryStacks.of(recipeInds.get(i))).backgroundEnabled(true));
		}
		ingredients.add(basicSlot(141, 34, origin).markOutput().entry(EntryStacks.of(recipe.getResultBlock())).backgroundEnabled(true));
	}

}
