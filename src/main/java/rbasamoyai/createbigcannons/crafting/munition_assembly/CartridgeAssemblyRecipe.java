package rbasamoyai.createbigcannons.crafting.munition_assembly;

import com.google.common.collect.Lists;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class CartridgeAssemblyRecipe extends CustomRecipe {

	public CartridgeAssemblyRecipe() { this(CraftingBookCategory.MISC); }

	public CartridgeAssemblyRecipe(CraftingBookCategory cat) { super(cat); }

	@Override
	public boolean matches(CraftingInput container, Level level) {
		int roundPosition = -1;
		int cartridgePosition = -1;
		boolean searchFuze = false;
		int fuzePosition = -1;

		for (int i = 0; i < container.size(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof AutocannonRoundItem) {
				if (roundPosition != -1) return false;
				roundPosition = i;
				if (stack.getItem() instanceof FuzedItemMunition) searchFuze = true;
			} else if (stack.getItem() == CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get()) {
				if (cartridgePosition != -1) return false;
				cartridgePosition = i;
			} else if (stack.getItem() instanceof FuzeItem) {
				if (fuzePosition != -1) return false;
				fuzePosition = i;
			} else {
				return false;
			}
		}
		if (roundPosition == -1 || cartridgePosition == -1 || !searchFuze && fuzePosition != -1) return false;
		int w = container.width();
		int i = roundPosition % w;
		return cartridgePosition % w == i && (fuzePosition == -1 || fuzePosition % w == i);
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider access) {
		int roundPosition = -1;
		int cartridgePosition = -1;
		boolean searchFuze = false;
		int fuzePosition = -1;

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof AutocannonRoundItem) {
				if (roundPosition != -1) return ItemStack.EMPTY;
				roundPosition = i;
				if (stack.getItem() instanceof FuzedItemMunition) searchFuze = true;
			} else if (stack.getItem() == CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get()) {
				if (cartridgePosition != -1) return ItemStack.EMPTY;
				cartridgePosition = i;
			} else if (stack.getItem() instanceof FuzeItem) {
				if (fuzePosition != -1) return ItemStack.EMPTY;
				fuzePosition = i;
			} else {
				return ItemStack.EMPTY;
			}
		}

		if (roundPosition == -1 || cartridgePosition == -1 || !searchFuze && fuzePosition != -1) return ItemStack.EMPTY;
		int w = input.width();
		int i = roundPosition % w;
		if (cartridgePosition % w != i || fuzePosition != -1 && fuzePosition % w != i) return ItemStack.EMPTY;

		ItemStack result = CBCItems.AUTOCANNON_CARTRIDGE.asStack();
		ItemStack roundCopy = input.getItem(roundPosition).copy();
		roundCopy.setCount(1);
		if (fuzePosition != -1) {
			ItemStack fuzeCopy = input.getItem(fuzePosition).copy();
			fuzeCopy.setCount(1);
			roundCopy.set(CBCDataComponents.FUZE, ItemContainerContents.fromItems(Lists.newArrayList(fuzeCopy)));
		}
		result.set(CBCDataComponents.PROJECTILE, ItemContainerContents.fromItems(Lists.newArrayList(roundCopy)));

		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return width >= 1 && height >= 2; }

	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.CARTRIDGE_ASSEMBLY.getSerializer(); }
}
