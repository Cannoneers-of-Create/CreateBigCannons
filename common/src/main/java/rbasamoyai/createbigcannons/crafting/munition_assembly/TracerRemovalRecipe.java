package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class TracerRemovalRecipe extends CustomRecipe {


	public TracerRemovalRecipe(ResourceLocation id) {
		super(id, CraftingBookCategory.MISC);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		ItemStack target = ItemStack.EMPTY;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack = container.getItem(i);
			if (stack.isEmpty()) continue;
			if (!target.isEmpty()) return false;

			if (stack.getItem() instanceof AutocannonCartridgeItem)
				stack = AutocannonCartridgeItem.getProjectileStack(stack);
			if (stack.getItem() instanceof AutocannonRoundItem) {
				if (!stack.getOrCreateTag().getBoolean("Tracer")) return false;
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
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
		return new ItemStack(CBCItems.TRACER_TIP.get());
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> result = super.getRemainingItems(container);
		int sz = container.getContainerSize();

		for (int i = 0; i < sz; ++i) {
			ItemStack stack = container.getItem(i);
			ItemStack originalStack = stack.copy();

			boolean isCartridge = stack.getItem() instanceof AutocannonCartridgeItem;
			if (isCartridge)
				stack = AutocannonCartridgeItem.getProjectileStack(stack);
			if (stack.getItem() instanceof AutocannonRoundItem) {
				if (stack.getOrCreateTag().getBoolean("Tracer")) {
					ItemStack copy = stack.copy();
					copy.setCount(1);
					copy.getOrCreateTag().remove("Tracer");
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
