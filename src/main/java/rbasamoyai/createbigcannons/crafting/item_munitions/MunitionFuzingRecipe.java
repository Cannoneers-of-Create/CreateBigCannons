package rbasamoyai.createbigcannons.crafting.item_munitions;

import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class MunitionFuzingRecipe extends CustomRecipe {

	public MunitionFuzingRecipe(ResourceLocation location) { super(location); }

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		ItemStack round = ItemStack.EMPTY;
		ItemStack fuze = ItemStack.EMPTY;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof FuzedItemMunition) {
				if (!round.isEmpty() || stack.getOrCreateTag().contains("Fuze", Tag.TAG_COMPOUND)) return false;
				round = stack;
			} else if (stack.getItem() instanceof FuzeItem) {
				if (!fuze.isEmpty()) return false;
				fuze = stack;
			} else {
				return false;
			}
		}

		return !round.isEmpty() && !fuze.isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack round = ItemStack.EMPTY;
		ItemStack fuze = ItemStack.EMPTY;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof FuzedItemMunition) {
				if (!round.isEmpty()) return ItemStack.EMPTY;
				round = stack;
			} else if (stack.getItem() instanceof FuzeItem) {
				if (!fuze.isEmpty()) return ItemStack.EMPTY;
				fuze = stack;
			} else {
				return ItemStack.EMPTY;
			}
		}

		if (round.isEmpty() || fuze.isEmpty()) return ItemStack.EMPTY;
		ItemStack result = round.copy();
		result.setCount(1);
		ItemStack fuzeCopy = fuze.copy();
		fuzeCopy.setCount(1);
		result.getOrCreateTag().put("Fuze", fuzeCopy.serializeNBT());
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 2; }
	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.MUNITION_FUZING.getSerializer(); }
}
