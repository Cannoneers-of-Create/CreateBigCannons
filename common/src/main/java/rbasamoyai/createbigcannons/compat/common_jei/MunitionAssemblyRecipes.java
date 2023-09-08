package rbasamoyai.createbigcannons.compat.common_jei;

import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.ArrayList;
import java.util.List;

public class MunitionAssemblyRecipes {

	public static List<CraftingRecipe> getFuzingRecipes() {
		List<Item> fuzes = new ArrayList<>();
		List<Item> munitions = new ArrayList<>();

		Registry.ITEM.stream()
		.forEach(i -> {
			if (i instanceof FuzeItem) fuzes.add(i);
			else if (i instanceof FuzedItemMunition) munitions.add(i);
		});

		Ingredient fuzeIngredient = Ingredient.of(fuzes.toArray(new Item[]{}));

		String group = CreateBigCannons.MOD_ID + ".fuzing";

		ListTag loreTag = new ListTag();
		String loc = I18n.get("tooltip." + CreateBigCannons.MOD_ID + ".jei_info.added_fuze");
		loreTag.add(StringTag.valueOf("\"" + loc + "\""));
		CompoundTag displayTag = new CompoundTag();
		displayTag.put("Lore", loreTag);

		List<CraftingRecipe> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(munition), fuzeIngredient);
			ResourceLocation id = CreateBigCannons.resource(group + "." + munition.getDescriptionId());
			ItemStack fuzedMunition = new ItemStack(munition);
			fuzedMunition.getOrCreateTag().put("display", displayTag.copy());
			recipes.add(new ShapelessRecipe(id, group, fuzedMunition, inputs));

			if (munition instanceof AutocannonRoundItem round) {
				NonNullList<Ingredient> inputs1 = NonNullList.of(Ingredient.EMPTY, Ingredient.of(round.getCreativeTabCartridgeItem()), fuzeIngredient);
				ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + munition.getDescriptionId());
				ItemStack fuzedCartridge = round.getCreativeTabCartridgeItem();
				fuzedCartridge.getOrCreateTag().put("display", displayTag.copy());
				recipes.add(new ShapelessRecipe(id1, group + ".autocannon_round", fuzedCartridge, inputs1));
			}
		}
		return recipes;
	}

	public static List<CraftingRecipe> getAutocannonRoundRecipes() {
		String group = CreateBigCannons.MOD_ID + ".autocannon_round";
		Ingredient cartridge = Ingredient.of(CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get());

		List<Item> fuzes = new ArrayList<>();
		List<AutocannonRoundItem> munitions = new ArrayList<>();

		Registry.ITEM.stream()
		.forEach(i -> {
			if (i instanceof FuzeItem) fuzes.add(i);
			else if (i instanceof AutocannonRoundItem acr) munitions.add(acr);
		});

		Ingredient fuzeIngredient = Ingredient.of(fuzes.toArray(new Item[]{}));

		ListTag loreTag = new ListTag();
		String loc = I18n.get("tooltip." + CreateBigCannons.MOD_ID + ".jei_info.added_fuze");
		loreTag.add(StringTag.valueOf("\"" + loc + "\""));
		CompoundTag displayTag = new CompoundTag();
		displayTag.put("Lore", loreTag);

		List<CraftingRecipe> recipes = new ArrayList<>();
		for (AutocannonRoundItem round : munitions) {
			NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(round), cartridge);
			ResourceLocation id = CreateBigCannons.resource(group + "." + round.getDescriptionId());
			recipes.add(new ShapedRecipe(id, group, 1, 2, inputs, round.getCreativeTabCartridgeItem()));

			if (round instanceof FuzedItemMunition) {
				NonNullList<Ingredient> inputs1 = NonNullList.of(Ingredient.EMPTY, fuzeIngredient, Ingredient.of(round), cartridge);
				ResourceLocation id1 = CreateBigCannons.resource(group + ".fuzed." + round.getDescriptionId());
				ItemStack fuzedRound = round.getCreativeTabCartridgeItem();
				fuzedRound.getOrCreateTag().put("display", displayTag.copy());
				recipes.add(new ShapedRecipe(id1, group, 1, 3, inputs1, fuzedRound));
			}
		}
		return recipes;
	}

	public static List<CraftingRecipe> getBigCartridgeFillingRecipe() {
		String group = CreateBigCannons.MOD_ID + ".big_cartridge_filling";
		ResourceLocation id = CreateBigCannons.resource(group + "." + CBCBlocks.BIG_CARTRIDGE.get().getDescriptionId());
		NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(BigCartridgeBlockItem.getWithPower(0)),
				Ingredient.of(CBCTags.CBCItemTags.NITROPOWDER));
		return List.of(new ShapelessRecipe(id, group, BigCartridgeBlockItem.getWithPower(1), inputs));
	}

	public static List<CraftingRecipe> getTracerRecipes() {
		List<Item> munitions = new ArrayList<>();

		Registry.ITEM.stream()
		.forEach(i -> {
			if (i instanceof AutocannonRoundItem || CBCItems.MACHINE_GUN_ROUND.is(i)) munitions.add(i);
		});

		Ingredient tracerIngredient = Ingredient.of(CBCItems.TRACER_TIP.get());

		String group = CreateBigCannons.MOD_ID + ".tracer";

		List<CraftingRecipe> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, Ingredient.of(munition), tracerIngredient);
			ResourceLocation id = CreateBigCannons.resource(group + "." + munition.getDescriptionId());
			ItemStack tracerMunition = new ItemStack(munition);
			tracerMunition.getOrCreateTag().putBoolean("Tracer", true);
			recipes.add(new ShapelessRecipe(id, group, tracerMunition, inputs));

			if (munition instanceof AutocannonRoundItem round) {
				NonNullList<Ingredient> inputs1 = NonNullList.of(Ingredient.EMPTY, Ingredient.of(round.getCreativeTabCartridgeItem()), tracerIngredient);
				ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + munition.getDescriptionId());
				ItemStack tracerCartridge = round.getCreativeTabCartridgeItem();
				CBCItems.AUTOCANNON_CARTRIDGE.get().setTracer(tracerCartridge, true);
				recipes.add(new ShapelessRecipe(id1, group + ".autocannon_round", tracerCartridge, inputs1));
			}
		}
		return recipes;
	}

	public static List<DeployerApplicationRecipe> getFuzingDeployerRecipes() {
		List<Item> fuzes = new ArrayList<>();
		List<Item> munitions = new ArrayList<>();

		Registry.ITEM.stream()
			.forEach(i -> {
				if (i instanceof FuzeItem) fuzes.add(i);
				else if (i instanceof FuzedItemMunition) munitions.add(i);
			});

		Ingredient fuzeIngredient = Ingredient.of(fuzes.toArray(new Item[]{}));

		String group = CreateBigCannons.MOD_ID + ".fuzing_deployer";

		ListTag loreTag = new ListTag();
		String loc = I18n.get("tooltip." + CreateBigCannons.MOD_ID + ".jei_info.added_fuze");
		loreTag.add(StringTag.valueOf("\"" + loc + "\""));
		CompoundTag displayTag = new CompoundTag();
		displayTag.put("Lore", loreTag);

		List<DeployerApplicationRecipe> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			ResourceLocation id = CreateBigCannons.resource(group + "." + munition.getDescriptionId());
			ItemStack fuzedMunition = new ItemStack(munition);
			fuzedMunition.getOrCreateTag().put("display", displayTag.copy());

			recipes.add(new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, id)
				.require(Ingredient.of(munition))
				.require(fuzeIngredient)
				.output(fuzedMunition)
				.build());

			if (munition instanceof AutocannonRoundItem round) {
				ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + munition.getDescriptionId());
				ItemStack fuzedCartridge = round.getCreativeTabCartridgeItem();
				fuzedCartridge.getOrCreateTag().put("display", displayTag.copy());

				recipes.add(new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, id1)
					.require(Ingredient.of(round.getCreativeTabCartridgeItem()))
					.require(fuzeIngredient)
					.output(fuzedCartridge)
					.build());
			}
		}
		return recipes;
	}

	public static List<DeployerApplicationRecipe> getAutocannonRoundDeployerRecipes() {
		String group = CreateBigCannons.MOD_ID + ".autocannon_round_deployer";

		List<AutocannonRoundItem> munitions = new ArrayList<>();

		Registry.ITEM.stream()
		.forEach(i -> {
			if (i instanceof AutocannonRoundItem acr) munitions.add(acr);
		});

		List<DeployerApplicationRecipe> recipes = new ArrayList<>();
		for (AutocannonRoundItem round : munitions) {
			ResourceLocation id = CreateBigCannons.resource(group + "." + round.getDescriptionId());

			recipes.add(new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, id)
				.require(CBCItems.FILLED_AUTOCANNON_CARTRIDGE.get())
				.require(round)
				.output(round.getCreativeTabCartridgeItem())
				.build());
		}
		return recipes;
	}

	public static List<DeployerApplicationRecipe> getBigCartridgeDeployerRecipe() {
		String group = CreateBigCannons.MOD_ID + ".big_cartridge_filling_deployer";
		ResourceLocation id = CreateBigCannons.resource(group + "." + CBCBlocks.BIG_CARTRIDGE.get().getDescriptionId());

		ItemStack result = BigCartridgeBlockItem.getWithPower(1);
		ListTag loreTag = new ListTag();
		String loc = I18n.get("tooltip." + CreateBigCannons.MOD_ID + ".jei_info.added_power");
		loreTag.add(StringTag.valueOf("\"" + loc + "\""));
		CompoundTag displayTag = new CompoundTag();
		displayTag.put("Lore", loreTag);
		result.getOrCreateTag().put("display", displayTag);

		return List.of(new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, id)
			.require(Ingredient.of(BigCartridgeBlockItem.getWithPower(0)))
			.require(CBCTags.CBCItemTags.NITROPOWDER)
			.output(result)
			.build());
	}

	public static List<DeployerApplicationRecipe> getTracerDeployerRecipes() {
		List<Item> munitions = new ArrayList<>();

		Registry.ITEM.stream()
		.forEach(i -> {
			if (i instanceof AutocannonRoundItem || CBCItems.MACHINE_GUN_ROUND.is(i)) munitions.add(i);
		});

		Ingredient tracerIngredient = Ingredient.of(CBCItems.TRACER_TIP.get());

		String group = CreateBigCannons.MOD_ID + ".tracer_deployer";

		List<DeployerApplicationRecipe> recipes = new ArrayList<>();
		for (Item munition : munitions) {
			ResourceLocation id = CreateBigCannons.resource(group + "." + munition.getDescriptionId());
			ItemStack tracerMunition = new ItemStack(munition);
			tracerMunition.getOrCreateTag().putBoolean("Tracer", true);

			recipes.add(new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, id)
				.require(Ingredient.of(munition))
				.require(tracerIngredient)
				.output(tracerMunition)
				.build());

			if (munition instanceof AutocannonRoundItem round) {
				ResourceLocation id1 = CreateBigCannons.resource(group + ".autocannon_round." + munition.getDescriptionId());
				ItemStack tracerCartridge = round.getCreativeTabCartridgeItem();
				CBCItems.AUTOCANNON_CARTRIDGE.get().setTracer(tracerCartridge, true);

				recipes.add(new ProcessingRecipeBuilder<>(DeployerApplicationRecipe::new, id1)
					.require(Ingredient.of(round.getCreativeTabCartridgeItem()))
					.require(tracerIngredient)
					.output(tracerCartridge)
					.build());
			}
		}
		return recipes;
	}

	private MunitionAssemblyRecipes() {}

}
