package rbasamoyai.createbigcannons.compat.rei;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.gui.AllGuiTextures;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;

public abstract class CBCBlockRecipeCategory<T extends BlockRecipe> implements DisplayCategory<CBCDisplay<T>> {

	protected final CategoryIdentifier<CBCDisplay<T>> type;
	protected final Component title;
	protected final Renderer background;
	protected final Renderer icon;

	private final Supplier<List<T>> recipes;
	private final List<Supplier<? extends ItemStack>> catalysts;

	private final int width;
	private final int height;

	private final Function<T, ? extends CBCDisplay<T>> displayFactory;

	public CBCBlockRecipeCategory(CBCBlockRecipeCategory.Info<T> info) {
		this.type = info.recipeType();
		this.title = info.title();
		this.background = info.background();
		this.icon = info.icon();
		this.recipes = info.recipes();
		this.catalysts = info.catalysts();
		this.width = info.width();
		this.height = info.height();
		this.displayFactory = info.displayFactory();
	}

	@Override
	public CategoryIdentifier<CBCDisplay<T>> getCategoryIdentifier() {
		return type;
	}

	public void registerRecipes(DisplayRegistry registry) {
		for (T recipe : this.recipes.get()) {
			registry.add(this.displayFactory.apply(recipe), recipe);
		}
	}

	public void registerCatalysts(CategoryRegistry registry) {
		this.catalysts.forEach(s -> registry.addWorkstations(type, EntryStack.of(VanillaEntryTypes.ITEM, s.get())));
	}

	@Override public Component getTitle() { return this.title; }
	@Override public int getDisplayHeight() { return this.height; }
	@Override public int getDisplayWidth(CBCDisplay<T> display) { return this.width; }
	@Override public Renderer getIcon() { return this.icon; }

	public static AllGuiTextures getRenderedSlot(Recipe<?> recipe, int index) {
		AllGuiTextures jeiSlot = AllGuiTextures.JEI_SLOT;
		if (!(recipe instanceof ProcessingRecipe))
			return jeiSlot;
		ProcessingRecipe<?> processingRecipe = (ProcessingRecipe<?>) recipe;
		List<ProcessingOutput> rollableResults = processingRecipe.getRollableResults();
		if (rollableResults.size() <= index)
			return jeiSlot;
		if (processingRecipe.getRollableResults()
			.get(index)
			.getChance() == 1)
			return jeiSlot;
		return AllGuiTextures.JEI_CHANCE_SLOT;
	}

	public void addWidgets(CBCDisplay<T> display, List<Widget> ingredients, Point origin) {

	}

	public void addWidgets(CBCDisplay<T> display, List<Widget> ingredients, Point origin, Rectangle bounds) {

	}

	@Override
	public List<Widget> setupDisplay(CBCDisplay<T> display, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		widgets.add(Widgets.createRecipeBase(bounds));
		widgets.add(Widgets.createDrawableWidget((helper, poseStack, mouseX, mouseY, partialTick) -> {
			poseStack.pushPose();
			poseStack.translate(bounds.getX(), bounds.getY() + 4, 0);
			draw(display.getRecipe(), poseStack, mouseX, mouseY);
			draw(display.getRecipe(), display, poseStack, mouseX, mouseY);
			poseStack.popPose();
		}));
		this.addWidgets(display, widgets, new Point(bounds.getX(), bounds.getY() + 4));
		this.addWidgets(display, widgets, new Point(bounds.getX(), bounds.getY() + 4), bounds);
		return widgets;
	}

	public void draw(T recipe, PoseStack matrixStack, double mouseX, double mouseY) {}

	public void draw(T recipe, CBCDisplay<T> display, PoseStack matrixStack, double mouseX, double mouseY) {}

	public record Info<T extends BlockRecipe>(CategoryIdentifier<CBCDisplay<T>> recipeType, Component title, Renderer background,
											  Renderer icon, Supplier<List<T>> recipes, List<Supplier<? extends ItemStack>> catalysts,
											  int width, int height, Function<T, ? extends CBCDisplay<T>> displayFactory) {
	}

	public interface Factory<T extends BlockRecipe> {
		CBCBlockRecipeCategory<T> create(CBCBlockRecipeCategory.Info<T> info);
	}

}
