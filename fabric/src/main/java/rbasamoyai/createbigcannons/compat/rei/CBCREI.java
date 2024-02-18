package rbasamoyai.createbigcannons.compat.rei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.compat.rei.CreateREI;
import com.simibubi.create.compat.rei.DoubleItemIcon;
import com.simibubi.create.compat.rei.EmptyBackground;
import com.simibubi.create.compat.rei.ItemIcon;
import com.simibubi.create.compat.rei.category.CreateRecipeCategory;
import com.simibubi.create.compat.rei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.rei.display.BasinDisplay;
import com.simibubi.create.compat.rei.display.CreateDisplay;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;

import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.ItemLike;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.compat.common_jei.IncompleteCannonBlockRecipe;
import rbasamoyai.createbigcannons.compat.common_jei.MunitionAssemblyRecipes;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;
import rbasamoyai.createbigcannons.crafting.foundry.MeltingRecipe;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCRecipeTypes;

public class CBCREI implements REIClientPlugin {

	private static final ResourceLocation ID = CreateBigCannons.resource("rei_plugin");

	private final List<CreateRecipeCategory<?>> allItemRecipeCategories = new ArrayList<>();
	private final List<CBCBlockRecipeCategory<?>> allBlockRecipeCategories = new ArrayList<>();

	private void loadCategories() {
		this.allItemRecipeCategories.clear();
		this.allBlockRecipeCategories.clear();

		Supplier<List<BasinRecipe>> meltingSupplier = () -> {
			List<BasinRecipe> list = new ArrayList<>();
			CreateREI.<MeltingRecipe>consumeTypedRecipes(list::add, CBCRecipeTypes.MELTING.getType());
			return list;
		};
		List<Supplier<? extends ItemStack>> meltingCatalysts = new ArrayList<>();
		meltingCatalysts.add(CBCBlocks.BASIN_FOUNDRY_LID::asStack);
		meltingCatalysts.add(AllBlocks.BASIN::asStack);
		CreateRecipeCategory.Info<BasinRecipe> meltingInfo = new CreateRecipeCategory.Info<>(
			CategoryIdentifier.of(CreateBigCannons.resource("melting")),
			Components.translatable("recipe." + CreateBigCannons.MOD_ID + ".melting"),
			new EmptyBackground(177, 103),
			new DoubleItemIcon(AllBlocks.BASIN::asStack, CBCBlocks.BASIN_FOUNDRY_LID::asStack),
			meltingSupplier, meltingCatalysts,
			177, 103,
			CBCREI::melting);
		this.allItemRecipeCategories.add(new MeltingCategory(meltingInfo, true));

		CBCBlockRecipeCategory<?>

			cannon_casting = builder(CannonCastingRecipe.class)
				.addTypedRecipes(BlockRecipeType.CANNON_CASTING)
				.catalystStack(CBCBlocks.CASTING_SAND::asStack)
				.itemIcon(CBCBlocks.CASTING_SAND.get())
				.emptyBackground(177, 103)
				.build("cannon_casting", CannonCastingCategory::new),

			built_up_heating = builder(BuiltUpHeatingRecipe.class)
				.addTypedRecipes(BlockRecipeType.BUILT_UP_HEATING)
				.catalystStack(CBCBlocks.CANNON_BUILDER::asStack)
				.catalystStack(ProcessingViaFanCategory.getFan("fan_blasting"))
				.itemIcon(CBCBlocks.CANNON_BUILDER.get())
				.emptyBackground(177, 103)
				.build("built_up_heating", BuiltUpHeatingCategory::new),

			drill_boring_blocks = builder(DrillBoringBlockRecipe.class)
				.addTypedRecipes(BlockRecipeType.DRILL_BORING)
				.catalystStack(CBCBlocks.CANNON_DRILL::asStack)
				.catalystStack(AllBlocks.MECHANICAL_BEARING::asStack)
				.catalystStack(AllBlocks.WINDMILL_BEARING::asStack)
				.itemIcon(CBCBlocks.CANNON_DRILL.get())
				.emptyBackground(177, 77)
				.build("drill_boring", DrillBoringCategory::new),

			incomplete_cannon_blocks = builder(IncompleteCannonBlockRecipe.class)
				.addRecipes(IncompleteCannonBlockRecipe::makeAllIncompleteRecipes)
				.itemIcon(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH.get())
				.emptyBackground(177, 60)
				.build("incomplete_cannon_blocks", IncompleteCannonBlockCategory::new);
	}

	public static BasinDisplay melting(BasinRecipe recipe) {
		return new BasinDisplay(recipe, CategoryIdentifier.of(CreateBigCannons.resource("melting")));
	}

	private <T extends BlockRecipe> CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
		return new CategoryBuilder<>(recipeClass);
	}

	@Override public String getPluginProviderName() { return ID.toString(); }

	@Override
	public void registerCategories(CategoryRegistry registry) {
		this.loadCategories();
		for (CreateRecipeCategory<?> cat : this.allItemRecipeCategories) {
			registry.add(cat);
			cat.registerCatalysts(registry);
		}
		for (CBCBlockRecipeCategory<?> cat : this.allBlockRecipeCategories) {
			registry.add(cat);
			cat.registerCatalysts(registry);
		}
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		for (CreateRecipeCategory<?> cat : this.allItemRecipeCategories) {
			cat.registerRecipes(registry);
		}
		for (CBCBlockRecipeCategory<?> cat : this.allBlockRecipeCategories) {
			cat.registerRecipes(registry);
		}

		List<CraftingRecipe> munitionCraftingRecipes = new ArrayList<>();
		munitionCraftingRecipes.addAll(MunitionAssemblyRecipes.getFuzingRecipes());
		munitionCraftingRecipes.addAll(MunitionAssemblyRecipes.getAutocannonRoundRecipes());
		munitionCraftingRecipes.addAll(MunitionAssemblyRecipes.getBigCartridgeFillingRecipe());
		munitionCraftingRecipes.addAll(MunitionAssemblyRecipes.getTracerRecipes());
		for (CraftingRecipe r : munitionCraftingRecipes) {
			Collection<Display> displays = registry.tryFillDisplay(r);
			for (Display display : displays) {
				if (Objects.equals(display.getCategoryIdentifier(), BuiltinPlugin.CRAFTING)) {
					registry.add(display, r);
				}
			}
		}
		CategoryIdentifier<CreateDisplay<DeployerApplicationRecipe>> DEPLOYING_TYPE = CategoryIdentifier.of(Create.asResource("deploying"));
		List<DeployerApplicationRecipe> munitionDeployerRecipes = new ArrayList<>();
		munitionDeployerRecipes.addAll(MunitionAssemblyRecipes.getFuzingDeployerRecipes());
		munitionDeployerRecipes.addAll(MunitionAssemblyRecipes.getAutocannonRoundDeployerRecipes());
		munitionDeployerRecipes.addAll(MunitionAssemblyRecipes.getBigCartridgeDeployerRecipe());
		munitionDeployerRecipes.addAll(MunitionAssemblyRecipes.getTracerDeployerRecipes());
		for (DeployerApplicationRecipe r : munitionDeployerRecipes) {
			Collection<Display> displays = registry.tryFillDisplay(r);
			for (Display display : displays) {
				if (Objects.equals(display.getCategoryIdentifier(), DEPLOYING_TYPE)) {
					registry.add(display, r);
				}
			}
		}
	}

	private class CategoryBuilder<T extends BlockRecipe> {
		private final Class<? extends T> recipeClass;
		private Predicate<CRecipes> predicate = cRecipes -> true;

		private Renderer background;
		private Renderer icon;

		private int width;
		private int height;

		private Function<T, ? extends CBCDisplay<T>> displayFactory;

		private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
		private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

		public CategoryBuilder(Class<? extends T> recipeClass) {
			this.recipeClass = recipeClass;
		}

		public CategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> collection) {
			return addRecipeListConsumer(recipes -> recipes.addAll(collection.get()));
		}

		public CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
			this.recipeListConsumers.add(consumer);
			return this;
		}

		public CategoryBuilder<T> addTypedRecipes(BlockRecipeType<? extends T> recipeType) {
			return addRecipeListConsumer(recipes -> CBCREI.<T>consumeTypedRecipes(recipes::add, recipeType));
		}

		public CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
			this.catalysts.add(supplier);
			return this;
		}

		public CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
			return this.catalystStack(() -> new ItemStack(supplier.get().asItem()));
		}

		public CategoryBuilder<T> icon(Renderer icon) {
			this.icon = icon;
			return this;
		}

		public CategoryBuilder<T> itemIcon(ItemLike item) {
			icon(new ItemIcon(() -> new ItemStack(item)));
			return this;
		}

		public CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
			icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
			return this;
		}

		public CategoryBuilder<T> background(Renderer background) {
			this.background = background;
			return this;
		}

		public CategoryBuilder<T> emptyBackground(int width, int height) {
			background(new EmptyBackground(width, height));
			dimensions(width, height);
			return this;
		}

		public CategoryBuilder<T> width(int width) {
			this.width = width;
			return this;
		}

		public CategoryBuilder<T> height(int height) {
			this.height = height;
			return this;
		}

		public CategoryBuilder<T> dimensions(int width, int height) {
			width(width);
			height(height);
			return this;
		}

		public CategoryBuilder<T> displayFactory(Function<T, ? extends CBCDisplay<T>> factory) {
			this.displayFactory = factory;
			return this;
		}

		public CBCBlockRecipeCategory<T> build(String name, CBCBlockRecipeCategory.Factory<T> factory) {
			Supplier<List<T>> recipesSupplier;
			if (predicate.test(AllConfigs.server().recipes)) {
				recipesSupplier = () -> {
					List<T> recipes = new ArrayList<>();
					if (predicate.test(AllConfigs.server().recipes)) {
						for (Consumer<List<T>> consumer : recipeListConsumers)
							consumer.accept(recipes);
					}
					return recipes;
				};
			} else {
				recipesSupplier = Collections::emptyList;
			}

			if (this.width <= 0 || height <= 0) {
				CreateBigCannons.LOGGER.warn("Create Big Cannons REI category [{}] has weird dimensions: {}x{}", name, this.width, this.height);
			}

			CBCBlockRecipeCategory.Info<T> info = new CBCBlockRecipeCategory.Info<>(
				CategoryIdentifier.of(CreateBigCannons.resource(name)),
				new TranslatableComponent("recipe." + CreateBigCannons.MOD_ID + "." + name),
				this.background,
				this.icon,
				recipesSupplier,
				this.catalysts,
				this.width,
				this.height,
				this.displayFactory == null ? recipe -> new CBCDisplay<>(recipe, CategoryIdentifier.of(CreateBigCannons.resource(name))) : this.displayFactory);
			CBCBlockRecipeCategory<T> category = factory.create(info);
			CBCREI.this.allBlockRecipeCategories.add(category);
			return category;
		}
	}

	public static <T extends BlockRecipe> void consumeTypedRecipes(Consumer<T> cons, BlockRecipeType<?> type) {
		Collection<BlockRecipe> col = BlockRecipesManager.getRecipesOfType(type);
		col.forEach(r -> cons.accept((T) r));
	}

}
