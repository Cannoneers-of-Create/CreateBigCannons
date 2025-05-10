package rbasamoyai.createbigcannons.crafting.munition_assembly;

import com.simibubi.create.foundation.item.SmartInventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class CartridgeAssemblyDeployerRecipe implements Recipe<SmartInventory> { // todo: check this works. was Recipe<Container>

	private final ItemStack round;

	public CartridgeAssemblyDeployerRecipe() {
		this.round = ItemStack.EMPTY;
	}

	public CartridgeAssemblyDeployerRecipe(ItemStack round) {
		this.round = round.copy();
	}

	@Override
	public boolean matches(SmartInventory container, Level level) {
		return CBCItems.FILLED_AUTOCANNON_CARTRIDGE.isIn(container.getItem(0)) && this.round.getItem() instanceof AutocannonRoundItem;
	}

	@Override public ItemStack assemble(SmartInventory inv, HolderLookup.Provider registries) { return this.getResultItem(registries); }

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		ItemStack result = CBCItems.AUTOCANNON_CARTRIDGE.asStack();
		CompoundTag tag = (CompoundTag) result.saveOptional(registries); // todo: not entirely sure that's correct, but it's how create does it. there are probably multiple occurrences of this in our codebase
		ItemStack roundCopy = this.round.copy();
		roundCopy.setCount(1);
		tag.put("Projectile", roundCopy.save(registries));
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getType(); }

}
