package rbasamoyai.createbigcannons.crafting.munition_assembly;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class MunitionFuzingDeployerRecipe implements Recipe<Container> {

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
	public boolean matches(Container container, Level level) {
		if (!(this.fuze.getItem() instanceof FuzeItem)) return false;
		if (this.munition.getItem() instanceof FuzedItemMunition) {
			return !this.munition.getOrCreateTag().contains("Fuze", Tag.TAG_COMPOUND);
		}
		if (this.munition.getItem() instanceof AutocannonCartridgeItem) {
			ItemStack cartridgeRound = AutocannonCartridgeItem.getProjectileStack(this.munition);
			return !cartridgeRound.isEmpty() && cartridgeRound.getItem() instanceof FuzedItemMunition
				&& !cartridgeRound.getOrCreateTag().contains("Fuze", Tag.TAG_COMPOUND);
		}
		return false;
	}

	@Override public ItemStack assemble(Container inv) { return this.getResultItem(); }

	@Override
	public ItemStack getResultItem() {
		ItemStack result = this.munition.copy();
		result.setCount(1);
		ItemStack fuzeCopy = this.fuze.copy();
		fuzeCopy.setCount(1);
		CompoundTag tag = result.getOrCreateTag();
		if (result.getItem() instanceof FuzedItemMunition) {
			tag.put("Fuze", fuzeCopy.save(new CompoundTag()));
		} else if (result.getItem() instanceof AutocannonCartridgeItem) {
			CompoundTag projectileTag = tag.getCompound("Projectile").getCompound("tag");
			projectileTag.put("Fuze", fuzeCopy.save(new CompoundTag()));
			tag.getCompound("Projectile").put("tag", projectileTag);
		}
		return result;
	}

	@Override public boolean canCraftInDimensions(int width, int height) { return true; }

	@Override public ResourceLocation getId() { return CBCRecipeTypes.MUNITION_FUZING_DEPLOYER.getId(); }
	@Override public RecipeSerializer<?> getSerializer() { return CBCRecipeTypes.MUNITION_FUZING_DEPLOYER.getSerializer(); }
	@Override public RecipeType<?> getType() { return CBCRecipeTypes.MUNITION_FUZING_DEPLOYER.getType(); }

}
