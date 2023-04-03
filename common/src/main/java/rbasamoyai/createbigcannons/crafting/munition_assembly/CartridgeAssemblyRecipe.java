package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class CartridgeAssemblyRecipe extends CustomRecipe {

	public CartridgeAssemblyRecipe(ResourceLocation id) { super(id); }

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		int roundPosition = -1;
		int cartridgePosition = -1;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof AutocannonRoundItem) {
				if (roundPosition != -1) return false;
				roundPosition = i;
			} else if (stack.getItem() == CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get()) {
				if (cartridgePosition != -1) return false;
				cartridgePosition = i;
			} else {
				return false;
			}
		}
		return roundPosition != -1 && cartridgePosition != -1 && cartridgePosition - roundPosition == container.getWidth();
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		int roundPosition = -1;
		int cartridgePosition = -1;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof AutocannonRoundItem) {
				if (roundPosition != -1) return ItemStack.EMPTY;
				roundPosition = i;
			} else if (stack.getItem() == CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get()) {
				if (cartridgePosition != -1) return ItemStack.EMPTY;
				cartridgePosition = i;
			} else {
				return ItemStack.EMPTY;
			}
		}

		if (roundPosition == -1 || cartridgePosition == -1 || cartridgePosition - roundPosition != container.getWidth()) return ItemStack.EMPTY;
		ItemStack result = CBCItems.AUTOCANNON_CARTRIDGE.asStack();
		CompoundTag tag = result.getOrCreateTag();
		ItemStack roundCopy = container.getItem(roundPosition).copy();
		roundCopy.setCount(1);
		tag.put("Projectile", roundCopy.save(new CompoundTag()));
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return width >= 1 && height >= 2; }

	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.CARTRIDGE_ASSEMBLY.getSerializer(); }
}
