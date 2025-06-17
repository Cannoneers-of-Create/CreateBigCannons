package rbasamoyai.createbigcannons.crafting.munition_assembly;

import com.google.common.collect.Lists;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;

public class AutocannonAmmoContainerFillingDeployerRecipe implements Recipe<RecipeInput> { // TODO c6 playtest

	private final ItemStack ammoContainer;
	private final ItemStack insertedAmmo;

	public AutocannonAmmoContainerFillingDeployerRecipe(ItemStack ammoContainer, ItemStack insertedAmmo) {
		this.ammoContainer = ammoContainer;
		this.insertedAmmo = insertedAmmo;
	}

	public AutocannonAmmoContainerFillingDeployerRecipe() {
		this(ItemStack.EMPTY, ItemStack.EMPTY);
	}

    public AutocannonAmmoContainerFillingDeployerRecipe(CraftingBookCategory cat) {
        this();
    }

	@Override
	public boolean matches(RecipeInput input, Level level) {
		if (!(this.ammoContainer.getItem() instanceof AutocannonAmmoContainerItem containerItem)
			|| containerItem.isCreative()
			|| !(this.insertedAmmo.getItem() instanceof AutocannonAmmoItem ammoItem)) return false;
		AutocannonAmmoType ammoType = AutocannonAmmoType.of(this.insertedAmmo);
		AutocannonAmmoType ctType = AutocannonAmmoContainerItem.getTypeOfContainer(this.ammoContainer);
		ItemStack existing = ammoItem.isTracer(this.insertedAmmo) ? AutocannonAmmoContainerItem.getTracerAmmoStack(this.ammoContainer)
			: AutocannonAmmoContainerItem.getMainAmmoStack(this.ammoContainer);
		if (existing.isEmpty() && (ammoType == AutocannonAmmoType.NONE || ammoType != ctType && ctType != AutocannonAmmoType.NONE)
			|| !existing.isEmpty() && !ItemStack.isSameItemSameComponents(existing, this.insertedAmmo)) return false;
		return (ctType == AutocannonAmmoType.NONE || AutocannonAmmoContainerItem.getTotalAmmoCount(this.ammoContainer) < ctType.getCapacity())
			&& existing.getCount() < existing.getMaxStackSize();
	}

	@Override
	public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
		return this.getResultItem(registries);
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		if (!(this.insertedAmmo.getItem() instanceof AutocannonAmmoItem ammoItem)) return ItemStack.EMPTY;
		ItemStack result = this.ammoContainer.copy();
		boolean tracer = ammoItem.isTracer(this.insertedAmmo);
		ItemStack existing = tracer ? AutocannonAmmoContainerItem.getTracerAmmoStack(result) : AutocannonAmmoContainerItem.getMainAmmoStack(result);
		ItemStack insert = existing.isEmpty() ? this.insertedAmmo.copy() : existing;
		if (existing.isEmpty()) {
			insert.setCount(1);
		} else {
			insert.grow(1);
		}
        ItemContainerContents container = ItemContainerContents.fromItems(Lists.newArrayList(insert));
        if (tracer) {
            result.set(CBCDataComponents.TRACER, container);
        } else {
            result.set(CBCDataComponents.AMMO, container);
        }
		return result;
	}

	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.AUTOCANNON_AMMO_CONTAINER_FILLING_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.AUTOCANNON_AMMO_CONTAINER_FILLING_DEPLOYER.getType(); }

}
