package rbasamoyai.createbigcannons.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.Components;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.casting.FluidCastingTimeHandler;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;
import rbasamoyai.createbigcannons.compat.jei.animated.CannonCastGuiElement;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;

import java.util.List;
import java.util.Optional;

import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.addFluidTooltip;
import static com.simibubi.create.compat.jei.category.CreateRecipeCategory.getRenderedSlot;

public class CannonCastingCategory extends CBCBlockRecipeCategory<CannonCastingRecipe> {

	private final CannonCastGuiElement cannonCast = new CannonCastGuiElement();

	public CannonCastingCategory(Info<CannonCastingRecipe> info) {
		super(info);
	}

	@Override
	public void draw(CannonCastingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		CBCGuiTextures.CANNON_CAST_SHADOW.render(stack, 40, 45);
		this.cannonCast.withShape(recipe.shape()).draw(stack, this.getBackground().getWidth() / 2 - 15, 55);
		CBCGuiTextures.CASTING_ARROW.render(stack, 21, 47);
		CBCGuiTextures.CASTING_ARROW_1.render(stack, 124, 27);

		float castingTime = 0;
		List<IRecipeSlotView> inputViews = recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT);
		if (!inputViews.isEmpty()) {
			IRecipeSlotView view = inputViews.get(0);
			Optional<FluidStack> ing = view.getDisplayedIngredient(ForgeTypes.FLUID_STACK);
			if (ing.isPresent()) castingTime = (float) FluidCastingTimeHandler.getCastingTime(ing.get().getFluid());
		}
		Minecraft mc = Minecraft.getInstance();
		Component text = Components.translatable("recipe." + CreateBigCannons.MOD_ID + ".casting_time", String.format("%.2f", castingTime / 20.0f));
		mc.font.draw(stack, text, (177 - mc.font.width(text)) / 2, 90, 4210752);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CannonCastingRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 16, 27)
			.setBackground(getRenderedSlot(), -1, -1)
			.addIngredients(ForgeTypes.FLUID_STACK, recipe.ingredient().getMatchingFluidStacks().stream().map(fs -> {
				fs.setAmount(recipe.shape().fluidSize());
				return fs;
			}).toList())
			.addTooltipCallback(addFluidTooltip());

		builder.addSlot(RecipeIngredientRole.OUTPUT, 142, 62)
			.setBackground(getRenderedSlot(), -1, -1)
			.addItemStack(new ItemStack(recipe.getResultBlock()));

		builder.addSlot(RecipeIngredientRole.CATALYST, 80, 5)
			.setBackground(getRenderedSlot(), -1, -1)
			.addItemStack(new ItemStack(recipe.shape().castMould()));
	}

}
