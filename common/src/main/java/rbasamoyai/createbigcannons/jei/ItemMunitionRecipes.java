package rbasamoyai.createbigcannons.jei;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.ArrayList;
import java.util.List;

public class ItemMunitionRecipes {

	public static List<CraftingRecipe> getFuzingRecipes() {
		List<Item> fuzes = new ArrayList<>();
		List<Item> munitions = new ArrayList<>();

		Registry.ITEM.stream()
		.forEach(i -> {
			if (i instanceof FuzeItem) fuzes.add(i);
			else if (i instanceof FuzedItemMunition) munitions.add(i);
		});

		String group = "createbigcannons.fuzing";
		return munitions.stream()
				.filter(FuzedItemMunition.class::isInstance)
				.map(munition -> fuzes.stream()
						.<CraftingRecipe>map(fuze -> {
							NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(munition), Ingredient.of(fuze));
							ResourceLocation id = CreateBigCannons.resource(group + "." + fuze.getDescriptionId() + "." + munition.getDescriptionId());
							return new ShapelessRecipe(id, group, new ItemStack(munition), inputs);
						})
						.toList())
				.flatMap(List::stream)
				.toList();
	}

	public static List<CraftingRecipe> getAutocannonRoundRecipes() {
		String group = "createbigcannons.autocannon_round";
		Ingredient cartridge = Ingredient.of(CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get());

		return Registry.ITEM.stream()
				.filter(AutocannonRoundItem.class::isInstance)
				.map(AutocannonRoundItem.class::cast)
				.<CraftingRecipe>map(round -> {
					NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(round), cartridge);
					ResourceLocation id = CreateBigCannons.resource(group + "." + round.getDescriptionId());
					return new ShapedRecipe(id, group, 1, 2, inputs, round.getCreativeTabCartridgeItem());
				})
				.toList();
	}

	private ItemMunitionRecipes() {}

}
