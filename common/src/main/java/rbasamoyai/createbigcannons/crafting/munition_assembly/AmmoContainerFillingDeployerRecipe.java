package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AmmoContainerItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;

public class AmmoContainerFillingDeployerRecipe implements Recipe<Container> {

	private final ItemStack ammoContainer;
	private final ItemStack insertedAmmo;

	public AmmoContainerFillingDeployerRecipe(ItemStack ammoContainer, ItemStack insertedAmmo) {
		this.ammoContainer = ammoContainer;
		this.insertedAmmo = insertedAmmo;
	}

	public AmmoContainerFillingDeployerRecipe() {
		this(ItemStack.EMPTY, ItemStack.EMPTY);
	}

	@Override
	public boolean matches(Container container, Level level) {
		if (!(this.ammoContainer.getItem() instanceof AmmoContainerItem)
			|| !(this.insertedAmmo.getItem() instanceof AutocannonAmmoItem ammoItem)) return false;
		AutocannonAmmoType ammoType = AutocannonAmmoType.of(this.insertedAmmo);
		AutocannonAmmoType ctType = AmmoContainerItem.getTypeOfContainer(this.ammoContainer);
		ItemStack existing = ammoItem.isTracer(this.insertedAmmo) ? AmmoContainerItem.getTracerAmmoStack(this.ammoContainer)
			: AmmoContainerItem.getMainAmmoStack(this.ammoContainer);
		if (existing.isEmpty() && (ammoType == AutocannonAmmoType.NONE || ammoType != ctType && ctType != AutocannonAmmoType.NONE)
			|| !existing.isEmpty() && !ItemStack.isSameItemSameTags(existing, this.insertedAmmo)) return false;
		return (ctType == AutocannonAmmoType.NONE || AmmoContainerItem.getTotalAmmoCount(this.ammoContainer) < ctType.getCapacity())
			&& existing.getCount() < existing.getMaxStackSize();
	}

	@Override public ItemStack assemble(Container container) { return this.getResultItem(); }

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override
	public ItemStack getResultItem() {
		ItemStack result = this.ammoContainer.copy();
		boolean tracer = ((AutocannonAmmoItem) this.insertedAmmo.getItem()).isTracer(this.insertedAmmo);
		ItemStack existing = tracer ? AmmoContainerItem.getTracerAmmoStack(result) : AmmoContainerItem.getMainAmmoStack(result);
		ItemStack insert = existing.isEmpty() ? this.insertedAmmo.copy() : existing;
		if (existing.isEmpty()) {
			insert.setCount(1);
		} else {
			insert.grow(1);
		}
		result.getOrCreateTag().put(tracer ? "Tracers" : "Ammo", insert.save(new CompoundTag()));
		return result;
	}

	@Override public ResourceLocation getId() { return CBCRecipeTypes.AMMO_CONTAINER_FILLING_DEPLOYER.getId(); }
	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.AMMO_CONTAINER_FILLING_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.AMMO_CONTAINER_FILLING_DEPLOYER.getType(); }

}
