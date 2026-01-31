package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class TracerApplicationDeployerRecipe implements Recipe<RecipeInput> { // TODO c6 playtest

	private final ItemStack munition;
	private final ItemStack fuze;

	public TracerApplicationDeployerRecipe() {
		this.munition = ItemStack.EMPTY;
		this.fuze = ItemStack.EMPTY;
	}

    public TracerApplicationDeployerRecipe(CraftingBookCategory cat) {
        this();
    }

	public TracerApplicationDeployerRecipe(ItemStack munition, ItemStack fuze) {
		this.munition = munition.copy();
		this.fuze = fuze.copy();
	}

	@Override
	public boolean matches(RecipeInput input, Level level) {
		if (!CBCItems.TRACER_TIP.isIn(this.fuze)) return false;
		if (this.munition.getItem() instanceof AutocannonRoundItem) return !this.munition.getOrDefault(CBCDataComponents.AUTOCANNON_TRACER, false);
		if (this.munition.getItem() instanceof AutocannonAmmoItem item) return !item.isTracer(this.munition);
		return false;
	}

	@Override public ItemStack assemble(RecipeInput inv, HolderLookup.Provider registries) { return this.getResultItem(registries); }

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		ItemStack result = this.munition.copy();
		result.setCount(1);
		if (result.getItem() instanceof AutocannonRoundItem) {
			result.set(CBCDataComponents.AUTOCANNON_TRACER, true);
		} else if (result.getItem() instanceof AutocannonAmmoItem item) {
			item.setTracer(result, true);
		}
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.TRACER_APPLICATION_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.TRACER_APPLICATION_DEPLOYER.getType(); }

}
