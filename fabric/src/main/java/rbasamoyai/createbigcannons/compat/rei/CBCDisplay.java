package rbasamoyai.createbigcannons.compat.rei;

import java.util.Collections;
import java.util.List;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;

public class CBCDisplay<R extends BlockRecipe> implements Display {

	protected final R recipe;
	private final CategoryIdentifier<CBCDisplay<R>> uid;
	private final List<EntryIngredient> input;
	private final List<EntryIngredient> output;

	public CBCDisplay(R recipe, CategoryIdentifier<CBCDisplay<R>> uid, List<EntryIngredient> input, List<EntryIngredient> output) {
		this.recipe = recipe;
		this.uid = uid;
		this.input = input;
		this.output = output;
	}

	public CBCDisplay(R recipe, CategoryIdentifier<CBCDisplay<R>> id) {
		this(recipe, id, Collections.singletonList(EntryIngredients.of(ItemStack.EMPTY)) /* TODO */, Collections.singletonList(EntryIngredients.of(recipe.getResultBlock())));
	}

	public R getRecipe() { return this.recipe; }

	@Override public CategoryIdentifier<?> getCategoryIdentifier() { return this.uid; }
	@Override public List<EntryIngredient> getInputEntries() { return this.input; }
	@Override public List<EntryIngredient> getOutputEntries() { return this.output; }

}
