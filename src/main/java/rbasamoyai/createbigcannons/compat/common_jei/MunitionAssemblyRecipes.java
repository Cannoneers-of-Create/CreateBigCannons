package rbasamoyai.createbigcannons.compat.common_jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class MunitionAssemblyRecipes {

	public static List<RecipeHolder<CraftingRecipe>> getFuzingRecipes() {
		List<Item> fuzes = new ArrayList<>();
		List<Item> munitions = new ArrayList<>();

		CBCRegistryUtils.streamAllItems()
		.forEach(i -> {
			if (i instanceof FuzeItem) fuzes.add(i);
			else if (i instanceof FuzedItemMunition) munitions.add(i);
		});

		Ingredient fuzeIngredient = Ingredient.of(fuzes.toArray(new Item[]{}));

		String group = CreateBigCannons.MOD_ID + ".fuzing";

		List<Component> loreList = new ArrayList<>(1);
		loreList.add(Component.translatable("tooltip." + CreateBigCannons.MOD_ID + ".jei_info.added_fuze"));

		List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(munition), fuzeIngredient);
            ResourceLocation id = CreateBigCannons.resource(group + "." + munition.getDescriptionId());
			ItemStack fuzedMunition = new ItemStack(munition);
			fuzedMunition.set(DataComponents.LORE, new ItemLore(loreList));
			recipes.add(new RecipeHolder<>(id, new ShapelessRecipe(group, CraftingBookCategory.MISC, fuzedMunition, inputs)));

			if (munition instanceof AutocannonRoundItem round) {
				NonNullList<Ingredient> inputs1 = NonNullList.of(Ingredient.EMPTY, Ingredient.of(round.getCreativeTabCartridgeItem()), fuzeIngredient);
                ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + munition.getDescriptionId());
				ItemStack fuzedCartridge = round.getCreativeTabCartridgeItem();
                fuzedCartridge.set(DataComponents.LORE, new ItemLore(loreList));
				recipes.add(new RecipeHolder<>(id1, new ShapelessRecipe(group + ".autocannon_round", CraftingBookCategory.MISC, fuzedCartridge, inputs1)));
			}
		}
		return recipes;
	}

	public static List<RecipeHolder<CraftingRecipe>> getAutocannonRoundRecipes() {
		String group = CreateBigCannons.MOD_ID + ".autocannon_round";
		Ingredient cartridge = Ingredient.of(CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get());

		List<Item> fuzes = new ArrayList<>();
		List<AutocannonRoundItem> munitions = new ArrayList<>();

		CBCRegistryUtils.streamAllItems()
		.forEach(i -> {
			if (i instanceof FuzeItem) fuzes.add(i);
			else if (i instanceof AutocannonRoundItem acr) munitions.add(acr);
		});

		Ingredient fuzeIngredient = Ingredient.of(fuzes.toArray(new Item[]{}));

        List<Component> loreList = new ArrayList<>(1);
        loreList.add(Component.translatable("tooltip." + CreateBigCannons.MOD_ID + ".jei_info.added_fuze"));

		List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
		for (AutocannonRoundItem round : munitions) {
			NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(round), cartridge);
            ResourceLocation id = CreateBigCannons.resource(group + "." + round.getDescriptionId());
			recipes.add(new RecipeHolder<>(id, new ShapedRecipe(group, CraftingBookCategory.MISC, new ShapedRecipePattern(1, 2, inputs, Optional.empty()), round.getCreativeTabCartridgeItem())));

			if (round instanceof FuzedItemMunition) {
				NonNullList<Ingredient> inputs1 = NonNullList.of(Ingredient.EMPTY, fuzeIngredient, Ingredient.of(round), cartridge);
                ResourceLocation id1 = CreateBigCannons.resource(group + ".fuzed." + round.getDescriptionId());
				ItemStack fuzedRound = round.getCreativeTabCartridgeItem();
                fuzedRound.set(DataComponents.LORE, new ItemLore(loreList));
				recipes.add(new RecipeHolder<>(id1, new ShapedRecipe(group, CraftingBookCategory.MISC, new ShapedRecipePattern(1, 3, inputs1, Optional.empty()), fuzedRound)));
			}
		}
		return recipes;
	}

	public static List<RecipeHolder<CraftingRecipe>> getBigCartridgeFillingRecipe() {
		String group = CreateBigCannons.MOD_ID + ".big_cartridge_filling";
        ResourceLocation id = CreateBigCannons.resource(group + "." + CBCBlocks.BIG_CARTRIDGE.get().getDescriptionId());
		NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(BigCartridgeBlockItem.getWithPower(0)),
				Ingredient.of(CBCTags.CBCItemTags.NITROPOWDER));
		ItemStack result = BigCartridgeBlockItem.getWithPower(1);
        List<Component> loreList = new ArrayList<>(1);
        loreList.add(Component.translatable("tooltip." + CreateBigCannons.MOD_ID + ".jei_info.added_power"));
		result.set(DataComponents.LORE, new ItemLore(loreList));
		return List.of(new RecipeHolder<>(id, new ShapelessRecipe(group, CraftingBookCategory.MISC, result, inputs)));
	}

	public static List<RecipeHolder<CraftingRecipe>> getTracerRecipes() {
		List<Item> munitions = new ArrayList<>();

		CBCRegistryUtils.streamAllItems()
		.forEach(i -> {
			if (i instanceof AutocannonRoundItem || CBCItems.MACHINE_GUN_ROUND.is(i)) munitions.add(i);
		});

		Ingredient tracerIngredient = Ingredient.of(CBCItems.TRACER_TIP.get());

		String group = CreateBigCannons.MOD_ID + ".tracer";

		List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(munition), tracerIngredient);
            ResourceLocation id = CreateBigCannons.resource(group + "." + munition.getDescriptionId());
			ItemStack tracerMunition = new ItemStack(munition);
			tracerMunition.set(CBCDataComponents.AUTOCANNON_TRACER, true);
			recipes.add(new RecipeHolder<>(id, new ShapelessRecipe(group, CraftingBookCategory.MISC, tracerMunition, inputs)));

			if (munition instanceof AutocannonRoundItem round) {
				NonNullList<Ingredient> inputs1 = NonNullList.of(Ingredient.EMPTY, Ingredient.of(round.getCreativeTabCartridgeItem()), tracerIngredient);
                ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + munition.getDescriptionId());
				ItemStack tracerCartridge = round.getCreativeTabCartridgeItem();
				CBCItems.AUTOCANNON_CARTRIDGE.get().setTracer(tracerCartridge, true);
				recipes.add(new RecipeHolder<>(id1, new ShapelessRecipe(group + ".autocannon_round", CraftingBookCategory.MISC, tracerCartridge, inputs1)));
			}
		}
		return recipes;
	}

	public static List<RecipeHolder<DeployerApplicationRecipe>> getFuzingDeployerRecipes() {
		List<Item> fuzes = new ArrayList<>();
		List<Item> munitions = new ArrayList<>();

		CBCRegistryUtils.streamAllItems()
			.forEach(i -> {
				if (i instanceof FuzeItem) fuzes.add(i);
				else if (i instanceof FuzedItemMunition) munitions.add(i);
			});

		Ingredient fuzeIngredient = Ingredient.of(fuzes.toArray(new Item[]{}));

		String group = CreateBigCannons.MOD_ID + ".fuzing_deployer";

        List<Component> loreList = new ArrayList<>(1);
        loreList.add(Component.translatable("tooltip." + CreateBigCannons.MOD_ID + ".jei_info.added_fuze"));

		List<RecipeHolder<DeployerApplicationRecipe>> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			ResourceLocation id = CreateBigCannons.resource(group + "." + munition.getDescriptionId());
			ItemStack fuzedMunition = new ItemStack(munition);
            fuzedMunition.set(DataComponents.LORE, new ItemLore(loreList));

			recipes.add(new RecipeHolder<>(id, new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, id)
				.require(Ingredient.of(munition))
				.require(fuzeIngredient)
				.output(fuzedMunition)
				.build()));

			if (munition instanceof AutocannonRoundItem round) {
				ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + munition.getDescriptionId());
				ItemStack fuzedCartridge = round.getCreativeTabCartridgeItem();
                fuzedCartridge.set(DataComponents.LORE, new ItemLore(loreList));

				recipes.add(new RecipeHolder<>(id1, new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, id1)
					.require(Ingredient.of(round.getCreativeTabCartridgeItem()))
					.require(fuzeIngredient)
					.output(fuzedCartridge)
					.build()));
			}
		}
		return recipes;
	}

	public static List<RecipeHolder<DeployerApplicationRecipe>> getAutocannonRoundDeployerRecipes() {
		String group = CreateBigCannons.MOD_ID + ".autocannon_round_deployer";

		List<AutocannonRoundItem> munitions = new ArrayList<>();

		CBCRegistryUtils.streamAllItems()
		.forEach(i -> {
			if (i instanceof AutocannonRoundItem acr) munitions.add(acr);
		});

		List<RecipeHolder<DeployerApplicationRecipe>> recipes = new ArrayList<>();
		for (AutocannonRoundItem round : munitions) {
			ResourceLocation id = CreateBigCannons.resource(group + "." + round.getDescriptionId());

			recipes.add(new RecipeHolder<>(id, new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, id)
				.require(CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get())
				.require(round)
				.output(round.getCreativeTabCartridgeItem())
				.build()));
		}
		return recipes;
	}

	public static List<RecipeHolder<DeployerApplicationRecipe>> getBigCartridgeDeployerRecipe() {
		String group = CreateBigCannons.MOD_ID + ".big_cartridge_filling_deployer";
		ResourceLocation id = CreateBigCannons.resource(group + "." + CBCBlocks.BIG_CARTRIDGE.get().getDescriptionId());

		ItemStack result = BigCartridgeBlockItem.getWithPower(1);
        List<Component> loreList = new ArrayList<>(1);
        loreList.add(Component.translatable("tooltip." + CreateBigCannons.MOD_ID + ".jei_info.added_power"));
        result.set(DataComponents.LORE, new ItemLore(loreList));

		return List.of(new RecipeHolder<>(id, new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, id)
			.require(Ingredient.of(BigCartridgeBlockItem.getWithPower(0)))
			.require(CBCTags.CBCItemTags.NITROPOWDER)
			.output(result)
			.build()));
	}

	public static List<RecipeHolder<DeployerApplicationRecipe>> getTracerDeployerRecipes() {
		List<Item> munitions = new ArrayList<>();

		CBCRegistryUtils.streamAllItems()
		.forEach(i -> {
			if (i instanceof AutocannonRoundItem || CBCItems.MACHINE_GUN_ROUND.is(i)) munitions.add(i);
		});

		Ingredient tracerIngredient = Ingredient.of(CBCItems.TRACER_TIP.get());

		String group = CreateBigCannons.MOD_ID + ".tracer_deployer";

		List<RecipeHolder<DeployerApplicationRecipe>> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			ResourceLocation id = CreateBigCannons.resource(group + "." + munition.getDescriptionId());
			ItemStack tracerMunition = new ItemStack(munition);
			tracerMunition.set(CBCDataComponents.AUTOCANNON_TRACER, true);

			recipes.add(new RecipeHolder<>(id, new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, id)
				.require(Ingredient.of(munition))
				.require(tracerIngredient)
				.output(tracerMunition)
				.build()));

			if (munition instanceof AutocannonRoundItem round) {
				ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + munition.getDescriptionId());
				ItemStack tracerCartridge = round.getCreativeTabCartridgeItem();
				CBCItems.AUTOCANNON_CARTRIDGE.get().setTracer(tracerCartridge, true);

				recipes.add(new RecipeHolder<>(id1, new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, id1)
					.require(Ingredient.of(round.getCreativeTabCartridgeItem()))
					.require(tracerIngredient)
					.output(tracerCartridge)
					.build()));
			}
		}
		return recipes;
	}

	public static List<RecipeHolder<CraftingRecipe>> getFuzeRemovalRecipes() {
		List<Item> fuzes = new ArrayList<>();
		List<Item> munitions = new ArrayList<>();

		CBCRegistryUtils.streamAllItems()
		.forEach(i -> {
			if (i instanceof FuzeItem) fuzes.add(i);
			else if (i instanceof FuzedItemMunition) munitions.add(i);
		});

		String group = CreateBigCannons.MOD_ID + ".fuze_removal";

		List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			for (Item fuze : fuzes) {
				ItemStack fuzeStack = new ItemStack(fuze);
				ItemStack fuzedMunitionStack = new ItemStack(munition);

				fuzedMunitionStack.set(CBCDataComponents.FUZE, ItemContainerContents.fromItems(Lists.newArrayList(fuzeStack)));

				String subid = munition.getDescriptionId() + "." + fuze.getDescriptionId();
				NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(fuzedMunitionStack));
                ResourceLocation id = CreateBigCannons.resource(group + "." + subid);
				recipes.add(new RecipeHolder<>(id, new ShapelessRecipe(group, CraftingBookCategory.MISC, fuzeStack, inputs)));

				if (munition instanceof AutocannonRoundItem) {
					ItemStack fuzedCartridge = CBCItems.AUTOCANNON_CARTRIDGE.asStack();
					AutocannonCartridgeItem.writeProjectile(fuzedMunitionStack, fuzedCartridge);

					NonNullList<Ingredient> inputs1 = NonNullList.of(Ingredient.EMPTY, Ingredient.of(fuzedCartridge));
                    ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + subid);
					recipes.add(new RecipeHolder<>(id1, new ShapelessRecipe(group + ".autocannon_round", CraftingBookCategory.MISC, fuzeStack, inputs1)));
				}
			}
		}
		return recipes;
	}

	public static List<RecipeHolder<CraftingRecipe>> getTracerRemovalRecipes() {
		List<Item> munitions = new ArrayList<>();
		CBCRegistryUtils.streamAllItems()
		.forEach(i -> {
			if (i instanceof AutocannonRoundItem || CBCItems.MACHINE_GUN_ROUND.is(i)) munitions.add(i);
		});

		String group = CreateBigCannons.MOD_ID + ".tracer_removal";

		List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			ItemStack tracerMunition = new ItemStack(munition);
			tracerMunition.set(CBCDataComponents.AUTOCANNON_TRACER, true);
            ResourceLocation id = CreateBigCannons.resource(group + "." + munition.getDescriptionId());
			NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(tracerMunition));
			recipes.add(new RecipeHolder<>(id, new ShapelessRecipe(group, CraftingBookCategory.MISC, CBCItems.TRACER_TIP.asStack(), inputs)));

			if (munition instanceof AutocannonRoundItem round) {
				ItemStack tracerCartridge = round.getCreativeTabCartridgeItem();
				CBCItems.AUTOCANNON_CARTRIDGE.get().setTracer(tracerCartridge, true);
				NonNullList<Ingredient> inputs1 = NonNullList.of(Ingredient.EMPTY, Ingredient.of(tracerCartridge));
                ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + munition.getDescriptionId());
				recipes.add(new RecipeHolder<>(id1,new ShapelessRecipe(group + ".autocannon_round", CraftingBookCategory.MISC, CBCItems.TRACER_TIP.asStack(), inputs1)));
			}
		}
		return recipes;
	}

	private MunitionAssemblyRecipes() {}

}
