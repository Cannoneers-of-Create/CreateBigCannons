package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

public class BigCartridgeFillingRecipe extends CustomRecipe {

	public BigCartridgeFillingRecipe() { this(CraftingBookCategory.MISC); }

    public BigCartridgeFillingRecipe(CraftingBookCategory cat) { super(cat); }

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack cartridge = ItemStack.EMPTY;
		int powderCount = 0;

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) continue;
			if (CBCBlocks.BIG_CARTRIDGE.is(stack.getItem())) {
				if (!cartridge.isEmpty()) return false;
				cartridge = stack;
			} else if (stack.is(CBCTags.CBCItemTags.NITROPOWDER)) {
				++powderCount;
			} else {
				return false;
			}
		}
		return !cartridge.isEmpty() && powderCount > 0;
	}

	@Override
	public ItemStack assemble(CraftingInput input , HolderLookup.Provider access) {
		ItemStack cartridge = ItemStack.EMPTY;
		BigCartridgeBlockItem cartridgeItem = null;
		int powderCount = 0;

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof BigCartridgeBlockItem foundCartridge) {
				if (!cartridge.isEmpty()) return ItemStack.EMPTY;
				cartridge = stack;
				cartridgeItem = foundCartridge;
			} else if (stack.is(CBCTags.CBCItemTags.NITROPOWDER)) {
				++powderCount;
			} else {
				return ItemStack.EMPTY;
			}
		}

		if (cartridge.isEmpty() || cartridgeItem == null || powderCount == 0) return ItemStack.EMPTY;

		ItemStack result = cartridge.copy();
		result.setCount(1);
		int oldPower = result.get(CBCDataComponents.POWER);
		int newPower = Math.min(cartridgeItem.getMaximumPowerLevels(), oldPower + powderCount);
		result.set(CBCDataComponents.POWER, newPower);
		return result;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> result = super.getRemainingItems(input);
		BigCartridgeBlockItem cartridgeItem = null;
		int sz = input.size();
		int powderCount = 0;

		ItemStack oldCartridge = ItemStack.EMPTY;
		for (int i = 0; i < sz; ++i) {
			ItemStack stack = input.getItem(i);
			if (stack.getItem() instanceof BigCartridgeBlockItem foundCartridge) {
				oldCartridge = stack;
				cartridgeItem = foundCartridge;
			} else if (stack.is(CBCTags.CBCItemTags.NITROPOWDER)) {
				++powderCount;
			}
		}

		if (oldCartridge.isEmpty() || cartridgeItem == null) return result;

		int oldPower = oldCartridge.get(CBCDataComponents.POWER);
		int newPower = Math.min(cartridgeItem.getMaximumPowerLevels(), oldPower + powderCount);
		int consumed = newPower - oldPower;

		for (int i = 0; i < sz; ++i) {
			ItemStack stack = input.getItem(i);
			if (stack.is(CBCTags.CBCItemTags.NITROPOWDER)) {
				if (consumed > 0) --consumed;
				else stack.grow(1);
			}
		}

		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return width * height > 1; }

	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.BIG_CARTRIDGE_FILLING.getSerializer(); }

}
