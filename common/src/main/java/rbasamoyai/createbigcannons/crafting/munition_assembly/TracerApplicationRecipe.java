package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class TracerApplicationRecipe extends CustomRecipe {

	public TracerApplicationRecipe(ResourceLocation location) { super(location); }

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		ItemStack round = ItemStack.EMPTY;
		ItemStack tracer = ItemStack.EMPTY;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof AutocannonRoundItem) {
				if (!round.isEmpty() || stack.getOrCreateTag().getBoolean("Tracer")) return false;
				round = stack;
			} else if (stack.getItem() instanceof AutocannonAmmoItem item) {
				if (!round.isEmpty() || item.isTracer(stack)) return false;
				round = stack;
			} else if (CBCItems.TRACER_TIP.isIn(stack)) {
				if (!tracer.isEmpty()) return false;
				tracer = stack;
			} else {
				return false;
			}
		}

		return !round.isEmpty() && !tracer.isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack round = ItemStack.EMPTY;
		ItemStack tracer = ItemStack.EMPTY;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof AutocannonRoundItem || stack.getItem() instanceof AutocannonAmmoItem) {
				if (!round.isEmpty()) return ItemStack.EMPTY;
				round = stack;
			} else if (CBCItems.TRACER_TIP.isIn(stack)) {
				if (!tracer.isEmpty()) return ItemStack.EMPTY;
				tracer = stack;
			} else {
				return ItemStack.EMPTY;
			}
		}

		if (round.isEmpty() || tracer.isEmpty()) return ItemStack.EMPTY;
		ItemStack result = round.copy();
		result.setCount(1);
		if (result.getItem() instanceof AutocannonRoundItem) {
			result.getOrCreateTag().putBoolean("Tracer", true);
		} else if (result.getItem() instanceof AutocannonAmmoItem item) {
			item.setTracer(result, true);
		}
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 2; }
	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.TRACER_APPLICATION.getSerializer(); }
}
