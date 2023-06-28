package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class CartridgeAssemblyDeployerRecipe implements Recipe<Container> {

	private final ItemStack round;

	public CartridgeAssemblyDeployerRecipe() {
		this.round = ItemStack.EMPTY;
	}

	public CartridgeAssemblyDeployerRecipe(ItemStack round) {
		this.round = round.copy();
	}

	@Override
	public boolean matches(Container container, Level level) {
		return CBCItems.FILLED_AUTOCANNON_CARTRIDGE.isIn(container.getItem(0)) && this.round.getItem() instanceof AutocannonRoundItem;
	}

	@Override public ItemStack assemble(Container inv) { return this.getResultItem(); }

	@Override
	public ItemStack getResultItem() {
		ItemStack result = CBCItems.AUTOCANNON_CARTRIDGE.asStack();
		CompoundTag tag = result.getOrCreateTag();
		ItemStack roundCopy = this.round.copy();
		roundCopy.setCount(1);
		tag.put("Projectile", roundCopy.save(new CompoundTag()));
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override public ResourceLocation getId() { return CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getId(); }
	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.CARTRIDGE_ASSEMBLY_DEPLOYER.getType(); }

}
