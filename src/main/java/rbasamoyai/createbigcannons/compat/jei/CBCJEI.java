package rbasamoyai.createbigcannons.compat.jei;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.CreateJEI;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.content.contraptions.processing.BasinRecipe;
import com.simibubi.create.foundation.utility.Components;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.CBCRecipeTypes;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;
import rbasamoyai.createbigcannons.crafting.foundry.MeltingRecipe;

@JeiPlugin
@SuppressWarnings("unused")
public class CBCJEI implements IModPlugin {

	private final List<PackedCategory<?>> allCategories = new ArrayList<>();
	private IIngredientManager ingredientManager;
	
	private void loadCategories() {
		this.allCategories.clear();
		
		Supplier<List<BasinRecipe>> meltingSupplier = () -> {
			List<BasinRecipe> list = new ArrayList<>();
			CreateJEI.<MeltingRecipe>consumeTypedRecipes(list::add, CBCRecipeTypes.MELTING.getType());
			return list;
		};
		List<Supplier<? extends ItemStack>> meltingCatalysts = new ArrayList<>();
		meltingCatalysts.add(CBCBlocks.BASIN_FOUNDRY_LID::asStack);
		meltingCatalysts.add(AllBlocks.BASIN::asStack);
		CreateRecipeCategory.Info<BasinRecipe> meltingInfo = new CreateRecipeCategory.Info<BasinRecipe>(
				new mezz.jei.api.recipe.RecipeType<>(CreateBigCannons.resource("melting"), BasinRecipe.class),
				Components.translatable("recipe." + CreateBigCannons.MOD_ID + ".melting"),
				new EmptyBackground(177, 103),
				new DoubleItemIcon(AllBlocks.BASIN::asStack, CBCBlocks.BASIN_FOUNDRY_LID::asStack),
				meltingSupplier,
				meltingCatalysts);
		this.allCategories.add(PackedCategory.packCreateCategory(new MeltingCategory(meltingInfo)));
		
		PackedCategory<?>
		
		cannon_casting = builder(CannonCastingRecipe.class)
			.addTypedRecipes(BlockRecipeType.CANNON_CASTING.get())
			.catalyst(CBCBlocks.CASTING_SAND::asStack)
			.itemIcon(CBCBlocks.CASTING_SAND.get())
			.emptyBackground(177, 103)
			.build("cannon_casting", CannonCastingCategory::new),
			
		built_up_heating = builder(BuiltUpHeatingRecipe.class)
			.addTypedRecipes(BlockRecipeType.BUILT_UP_HEATING.get())
			.catalyst(CBCBlocks.CANNON_BUILDER::asStack)
			.catalyst(ProcessingViaFanCategory.getFan("fan_blasting"))
			.itemIcon(CBCBlocks.CANNON_BUILDER.get())
			.emptyBackground(177, 103)
			.build("built_up_heating", BuiltUpHeatingCategory::new),
			
		cannon_boring_blocks = builder(CannonBoringRecipe.class)
			.addRecipes(CannonBoringRecipe::makeAllBoringRecipes)
			.catalyst(CBCBlocks.CANNON_DRILL::asStack)
			.catalyst(AllBlocks.MECHANICAL_BEARING::asStack)
			.catalyst(AllBlocks.WINDMILL_BEARING::asStack)
			.itemIcon(CBCBlocks.CANNON_DRILL.get())
			.emptyBackground(177, 77)
			.build("cannon_boring", CannonBoringCategory::new),
			
		incomplete_cannon_blocks = builder(IncompleteCannonBlockRecipe.class)
			.addRecipes(IncompleteCannonBlockRecipe::makeAllIncompleteRecipes)
			.itemIcon(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH.get())
			.emptyBackground(177, 60)
			.build("incomplete_cannon_blocks", IncompleteCannonBlockCategory::new);
		
	}
	
	private static final ResourceLocation PLUGIN_ID = CreateBigCannons.resource("jei_plugin");	
	@Override public ResourceLocation getPluginUid() { return PLUGIN_ID; }
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		this.loadCategories();
		registration.addRecipeCategories(this.allCategories.stream().map(PackedCategory::asCategory).toArray(IRecipeCategory[]::new));
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		this.ingredientManager = registration.getIngredientManager();
		this.allCategories.forEach(c -> c.registerRecipes(registration));
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		this.allCategories.forEach(c -> c.registerCatalysts(registration));
	}
	
	private class BlockRecipeCategoryBuilder<T extends BlockRecipe> {
		private final Class<? extends T> recipeClass;
		
		private IDrawable background;
		private IDrawable icon;
		private final List<Consumer<List<T>>> recipeConsumers = new ArrayList<>();
		private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();
		
		public BlockRecipeCategoryBuilder(Class<? extends T> recipeClass) {
			this.recipeClass = recipeClass;
		}
		
		public BlockRecipeCategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> cons) {
			this.recipeConsumers.add(cons);
			return this;
		}
		
		public BlockRecipeCategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> sup) {
			return this.addRecipeListConsumer(list -> list.addAll(sup.get()));
		}
		
		public BlockRecipeCategoryBuilder<T> addTypedRecipes(BlockRecipeType<?> type) {
			return this.addRecipeListConsumer(list -> CBCJEI.<T>consumeTypedRecipes(list::add, type));
		}
		
		public BlockRecipeCategoryBuilder<T> catalyst(Supplier<? extends ItemStack> catalyst) {
			this.catalysts.add(catalyst);
			return this;
		}
		
		public BlockRecipeCategoryBuilder<T> background(IDrawable background) {
			this.background = background;
			return this;
		}
		
		public BlockRecipeCategoryBuilder<T> emptyBackground(int width, int height) {
			return this.background(new EmptyBackground(width, height));
		}
		
		public BlockRecipeCategoryBuilder<T> icon(IDrawable icon) {
			this.icon = icon;
			return this;
		}
		
		public BlockRecipeCategoryBuilder<T> itemIcon(ItemLike item) {
			return this.icon(new ItemIcon(() -> new ItemStack(item)));
		}
		
		public BlockRecipeCategoryBuilder<T> doubleItemIcon(ItemLike item, ItemLike item1) {
			return this.icon(new DoubleItemIcon(() -> new ItemStack(item), () -> new ItemStack(item1)));
		}
		
		public PackedCategory<T> build(String id, CBCBlockRecipeCategory.Factory<T> fac) {
			Supplier<List<T>> recipesSupplier = () -> {
				List<T> recipes = new ArrayList<>();
				for (Consumer<List<T>> cons : this.recipeConsumers) cons.accept(recipes);
				return recipes;
			};
			CBCBlockRecipeCategory.Info<T> info = new CBCBlockRecipeCategory.Info<>(
					new mezz.jei.api.recipe.RecipeType<>(CreateBigCannons.resource(id), this.recipeClass),
					Components.translatable("recipe." + CreateBigCannons.MOD_ID + "." + id),
					this.background,
					this.icon,
					recipesSupplier,
					this.catalysts);
			PackedCategory<T> packedCat = PackedCategory.packBlockRecipeCategory(fac.create(info));
			allCategories.add(packedCat);
			return packedCat;
		}
	}
	
	private <T extends BlockRecipe> BlockRecipeCategoryBuilder<T> builder(Class<? extends T> recipeClass) {
		return new BlockRecipeCategoryBuilder<>(recipeClass);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BlockRecipe> void consumeTypedRecipes(Consumer<T> cons, BlockRecipeType<?> type) {
		Collection<BlockRecipe> col = BlockRecipesManager.getRecipesOfType(type);
		col.forEach(r -> cons.accept((T) r));
	}
	
	interface PackedCategory<T> {
		IRecipeCategory<T> asCategory();
		RecipeType<T> getType();
		void registerRecipes(IRecipeRegistration reg);
		void registerCatalysts(IRecipeCatalystRegistration reg);
		
		public static <T extends Recipe<?>> PackedCategory<T> packCreateCategory(CreateRecipeCategory<T> cat) {
			return new PackedCategory<>() {
				@Override public IRecipeCategory<T> asCategory() { return cat; }
				@Override public RecipeType<T> getType() { return cat.getRecipeType(); }
				@Override public void registerRecipes(IRecipeRegistration reg) { cat.registerRecipes(reg); }
				@Override public void registerCatalysts(IRecipeCatalystRegistration reg) { cat.registerCatalysts(reg); }
			};
		}
		
		public static <T extends BlockRecipe> PackedCategory<T> packBlockRecipeCategory(CBCBlockRecipeCategory<T> cat) {
			return new PackedCategory<>() {
				@Override public IRecipeCategory<T> asCategory() { return cat; }
				@Override public RecipeType<T> getType() { return cat.getRecipeType(); }
				@Override public void registerRecipes(IRecipeRegistration reg) { cat.registerRecipes(reg); }
				@Override public void registerCatalysts(IRecipeCatalystRegistration reg) { cat.registerCatalysts(reg); }
			};
		}
	}
	
}
