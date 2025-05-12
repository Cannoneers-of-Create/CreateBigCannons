package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class MunitionFuzingDeployerRecipe implements Recipe<CraftingInput> {

	private final ItemStack munition;
	private final ItemStack fuze;

	public MunitionFuzingDeployerRecipe() {
		this.munition = ItemStack.EMPTY;
		this.fuze = ItemStack.EMPTY;
	}

	public MunitionFuzingDeployerRecipe(ItemStack munition, ItemStack fuze) {
		this.munition = munition.copy();
		this.fuze = fuze.copy();
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (!(this.fuze.getItem() instanceof FuzeItem)) return false;
		if (this.munition.getItem() instanceof FuzedItemMunition) {
			return !this.munition.has(CBCDataComponents.FUZE);
		}
		if (this.munition.getItem() instanceof AutocannonCartridgeItem) {
			ItemStack cartridgeRound = AutocannonCartridgeItem.getProjectileStack(this.munition);
			return !cartridgeRound.isEmpty() && cartridgeRound.getItem() instanceof FuzedItemMunition
				&& !cartridgeRound.has(CBCDataComponents.FUZE);
		}
		return false;
	}

	@Override public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) { return this.getResultItem(registries); }

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		ItemStack result = this.munition.copy();
		result.setCount(1);
		ItemStack fuzeCopy = this.fuze.copy();
		fuzeCopy.setCount(1);
		if (result.getItem() instanceof FuzedItemMunition) {
            result.set(CBCDataComponents.FUZE, fuzeCopy);
		} else if (result.getItem() instanceof AutocannonCartridgeItem) {
            ItemStack projectile = result.get(CBCDataComponents.PROJECTILE);
            projectile.set(CBCDataComponents.FUZE, fuzeCopy);
		}
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.MUNITION_FUZING_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.MUNITION_FUZING_DEPLOYER.getType(); }

}
