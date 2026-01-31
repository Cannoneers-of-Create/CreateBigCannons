package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;

public class FuzeRemovalRecipe extends CustomRecipe {


	public FuzeRemovalRecipe() {
		this(CraftingBookCategory.MISC);
	}

    public FuzeRemovalRecipe(CraftingBookCategory cat) {
        super(cat);
    }

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack target = ItemStack.EMPTY;

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) continue;
			if (!target.isEmpty()) return false;

			if (stack.getItem() instanceof AutocannonCartridgeItem)
				stack = AutocannonCartridgeItem.getProjectileStack(stack);
			if (stack.getItem() instanceof FuzedItemMunition) {
				if (!stack.has(CBCDataComponents.FUZE)) return false;
				target = stack;
			} else {
				return false;
			}
		}
		return !target.isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registryAccess) {
		ItemStack target = ItemStack.EMPTY;

		for (int i = 0; i < input.size(); ++i) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) continue;
			if (!target.isEmpty()) return ItemStack.EMPTY;

			if (stack.getItem() instanceof AutocannonCartridgeItem)
				stack = AutocannonCartridgeItem.getProjectileStack(stack);
			if (stack.getItem() instanceof FuzedItemMunition) {
				if (!stack.has(CBCDataComponents.FUZE)) return ItemStack.EMPTY;
				target = stack;
			} else {
				return ItemStack.EMPTY;
			}
		}
        return FuzedProjectileBlock.getFuzeFromItemStack(target);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
		NonNullList<ItemStack> result = super.getRemainingItems(container);
		int sz = container.size();

		for (int i = 0; i < sz; ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.getItem() instanceof FuzedItemMunition) {
				if (stack.has(CBCDataComponents.FUZE)) {
					ItemStack copy = stack.copy();
					copy.remove(CBCDataComponents.FUZE);
					copy.setCount(1);
					result.set(i, copy);
				}
				break;
			} else if (stack.getItem() instanceof AutocannonCartridgeItem) {
				ItemStack cartridgeRound = AutocannonCartridgeItem.getProjectileStack(stack);
				if (cartridgeRound.getItem() instanceof FuzedItemMunition && cartridgeRound.has(CBCDataComponents.FUZE)) {
					ItemStack copyRound = cartridgeRound.copy();
					copyRound.remove(CBCDataComponents.FUZE);
					copyRound.setCount(1);
					ItemStack newStack = new ItemStack(stack.getItem());
					AutocannonCartridgeItem.writeProjectile(copyRound, newStack);
					result.set(i, newStack);
				}
				break;
			}
		}
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 1; }
	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.FUZE_REMOVAL.getSerializer(); }

}
