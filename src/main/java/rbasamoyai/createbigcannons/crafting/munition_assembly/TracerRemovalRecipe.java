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
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class TracerRemovalRecipe extends CustomRecipe {


	public TracerRemovalRecipe() {
		this(CraftingBookCategory.MISC);
	}

    public TracerRemovalRecipe(CraftingBookCategory cat) {
        super(cat);
    }

	@Override
	public boolean matches(CraftingInput container, Level level) {
		ItemStack target = ItemStack.EMPTY;

		for (int i = 0; i < container.size(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (!target.isEmpty()) return false;

			if (stack.getItem() instanceof AutocannonCartridgeItem)
				stack = AutocannonCartridgeItem.getProjectileStack(stack);
			if (stack.getItem() instanceof AutocannonRoundItem) {
				if (!stack.getOrDefault(CBCDataComponents.AUTOCANNON_TRACER, false)) return false;
				target = stack;
			} else if (stack.getItem() instanceof AutocannonAmmoItem item) {
				if (!item.isTracer(stack)) return false;
				target = stack;
			} else {
				return false;
			}
		}
		return !target.isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registryAccess) {
		return new ItemStack(CBCItems.TRACER_TIP.get());
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> result = super.getRemainingItems(input);
		int sz = input.size();

		for (int i = 0; i < sz; ++i) {
			ItemStack stack = input.getItem(i);
			ItemStack originalStack = stack.copy();

			boolean isCartridge = stack.getItem() instanceof AutocannonCartridgeItem;
			if (isCartridge)
				stack = AutocannonCartridgeItem.getProjectileStack(stack);
			if (stack.getItem() instanceof AutocannonRoundItem) {
				if (stack.getOrDefault(CBCDataComponents.AUTOCANNON_TRACER, false)) {
					ItemStack copy = stack.copy();
					copy.setCount(1);
					copy.remove(CBCDataComponents.AUTOCANNON_TRACER);
					if (isCartridge) {
						ItemStack cartridge = new ItemStack(originalStack.getItem());
						AutocannonCartridgeItem.writeProjectile(copy, cartridge);
						result.set(i, cartridge);
					} else {
						result.set(i, copy);
					}
				}
				break;
			} else if (stack.getItem() instanceof AutocannonAmmoItem item) {
				if (item.isTracer(stack)) {
					ItemStack copy = stack.copy();
					copy.setCount(1);
					item.setTracer(copy, false);
					if (isCartridge) {
						ItemStack cartridge = new ItemStack(originalStack.getItem());
						AutocannonCartridgeItem.writeProjectile(copy, cartridge);
						result.set(i, cartridge);
					} else {
						result.set(i, copy);
					}
				}
				break;
			}
		}
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 1; }
	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.TRACER_REMOVAL.getSerializer(); }

}
