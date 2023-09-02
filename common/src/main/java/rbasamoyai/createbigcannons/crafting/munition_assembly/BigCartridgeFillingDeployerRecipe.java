package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

public class BigCartridgeFillingDeployerRecipe implements Recipe<Container> {

	private final int startPower;
	private final int resultPower;

	public BigCartridgeFillingDeployerRecipe() {
		this.startPower = 0;
		this.resultPower = 0;
	}

	public BigCartridgeFillingDeployerRecipe(int startPower, int resultPower) {
		this.startPower = startPower;
		this.resultPower = resultPower;
	}

	@Override
	public boolean matches(Container container, Level level) {
		ItemStack cartridge = container.getItem(0);
		return CBCBlocks.BIG_CARTRIDGE.isIn(cartridge) && BigCartridgeBlockItem.getPower(cartridge) == this.startPower
			&& container.getItem(1).is(CBCTags.ItemCBC.NITROPOWDER);
	}

	@Override public ItemStack assemble(Container inv, RegistryAccess access) { return this.getResultItem(access); }
	@Override public ItemStack getResultItem(RegistryAccess access) { return BigCartridgeBlockItem.getWithPower(this.resultPower); }

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override public ResourceLocation getId() { return CBCRecipeTypes.BIG_CARTRIDGE_FILLING_DEPLOYER.getId(); }
	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.BIG_CARTRIDGE_FILLING_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.BIG_CARTRIDGE_FILLING_DEPLOYER.getType(); }

}
