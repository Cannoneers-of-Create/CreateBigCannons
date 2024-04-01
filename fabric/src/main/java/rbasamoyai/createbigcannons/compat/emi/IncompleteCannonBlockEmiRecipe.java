package rbasamoyai.createbigcannons.compat.emi;

import static com.simibubi.create.compat.emi.recipes.CreateEmiRecipe.addSlot;

import java.util.ArrayList;

import com.simibubi.create.foundation.gui.AllGuiTextures;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.compat.common_jei.IncompleteCannonBlockRecipe;

public class IncompleteCannonBlockEmiRecipe extends CBCEmiBlockRecipe<IncompleteCannonBlockRecipe> {

	final String[] romans = { "I", "II", "III", "IV", "V", "VI", "-" };

	public IncompleteCannonBlockEmiRecipe(EmiRecipeCategory category, IncompleteCannonBlockRecipe recipe, int width, int height) {
		super(category, recipe, width, height);
		this.inputs = new ArrayList<>();
		for (ItemStack stack : recipe.ingredients()) {
			this.inputs.add(EmiStack.of(stack));
		}
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		int sz = this.inputs.size();
		int base = this.width / 2 - 12 * sz + 16;
		for (int i = 0; i < sz; ++i) {
			int x = i == 0 ? 21 : base + 24 * i - 24;
			int y = i == 0 ? 34 : 15;
			addSlot(widgets, this.inputs.get(i), x, y);
		}
		addSlot(widgets, EmiStack.of(this.recipe.getResultBlock()), 141, 34).recipeContext(this);

		widgets.addDrawable(0, 0, 0, 0, ((graphics, mouseX, mouseY, delta) -> {
			Minecraft mc = Minecraft.getInstance();
			AllGuiTextures.JEI_LONG_ARROW.render(graphics, 54, 38);

			int base1 = this.width / 2 - 12 * sz + 24;
			for (int i = 0; i < sz; ++i) {
				if (i == 0) continue;
				int j = i - 1;
				int posX = base1 + 24 * j;
				Component num = Component.literal(this.romans[Math.min(j, 6)]);
				graphics.drawString(mc.font, num, mc.font.width(num) / -2 + posX, 2, 0x888888);
			}
		}));
	}

}
