package rbasamoyai.createbigcannons.crafting.munition_assembly;

import com.simibubi.create.foundation.item.SmartInventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class CartridgeAssemblyDeployerRecipe implements Recipe<CraftingInput> { // todo: check this works. was Recipe<Container>

	private final ItemStack round;

	public CartridgeAssemblyDeployerRecipe() {
		this.round = ItemStack.EMPTY;
	}

	public CartridgeAssemblyDeployerRecipe(ItemStack round) {
		this.round = round.copy();
	}

	@Override
	public boolean matches(CraftingInput container, Level level) {
		return CBCItems.FILLED_AUTOCANNON_CARTRIDGE.isIn(container.getItem(0)) && this.round.getItem() instanceof AutocannonRoundItem;
	}

	@Override public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) { return this.getResultItem(registries); }

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		ItemStack result = CBCItems.AUTOCANNON_CARTRIDGE.asStack();
		ItemStack roundCopy = this.round.copy();
		roundCopy.setCount(1);
		result.set(CBCDataComponents.PROJECTILE, roundCopy);
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getType(); }

}
