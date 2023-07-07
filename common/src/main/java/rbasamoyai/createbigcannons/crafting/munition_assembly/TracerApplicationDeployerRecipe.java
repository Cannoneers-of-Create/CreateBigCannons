package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

public class TracerApplicationDeployerRecipe implements Recipe<Container> {

	private final ItemStack munition;
	private final ItemStack fuze;

	public TracerApplicationDeployerRecipe() {
		this.munition = ItemStack.EMPTY;
		this.fuze = ItemStack.EMPTY;
	}

	public TracerApplicationDeployerRecipe(ItemStack munition, ItemStack fuze) {
		this.munition = munition.copy();
		this.fuze = fuze.copy();
	}

	@Override
	public boolean matches(Container container, Level level) {
		if (!CBCItems.TRACER_TIP.isIn(this.fuze)) return false;
		if (this.munition.getItem() instanceof AutocannonRoundItem) return !this.munition.getOrCreateTag().getBoolean("Tracer");
		if (this.munition.getItem() instanceof AutocannonAmmoItem item) return !item.isTracer(this.munition);
		return false;
	}

	@Override public ItemStack assemble(Container inv) { return this.getResultItem(); }

	@Override
	public ItemStack getResultItem() {
		ItemStack result = this.munition.copy();
		result.setCount(1);
		if (result.getItem() instanceof AutocannonRoundItem) {
			result.getOrCreateTag().putBoolean("Tracer", true);
		} else if (result.getItem() instanceof AutocannonAmmoItem item) {
			item.setTracer(result, true);
		}
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override public ResourceLocation getId() { return CBCRecipeTypes.TRACER_APPLICATION_DEPLOYER.getId(); }
	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.TRACER_APPLICATION_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.TRACER_APPLICATION_DEPLOYER.getType(); }

}
