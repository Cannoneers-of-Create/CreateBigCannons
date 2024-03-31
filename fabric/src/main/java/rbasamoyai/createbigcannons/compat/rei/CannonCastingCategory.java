package rbasamoyai.createbigcannons.compat.rei;

import static com.simibubi.create.compat.rei.category.CreateRecipeCategory.basicSlot;
import static com.simibubi.create.compat.rei.category.CreateRecipeCategory.convertToREIFluid;
import static com.simibubi.create.compat.rei.category.CreateRecipeCategory.setFluidRenderRatio;
import static com.simibubi.create.compat.rei.category.CreateRecipeCategory.setFluidTooltip;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.compat.rei.animated.CannonCastGuiElement;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;
import rbasamoyai.createbigcannons.crafting.casting.FluidCastingTimeHandler;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;

public class CannonCastingCategory extends CBCBlockRecipeCategory<CannonCastingRecipe> {

	private final CannonCastGuiElement cannonCast = new CannonCastGuiElement();

	public CannonCastingCategory(CBCBlockRecipeCategory.Info<CannonCastingRecipe> info) {
		super(info);
	}

	@Override
	public void draw(CannonCastingRecipe recipe, PoseStack stack, double mouseX, double mouseY) {
		CBCGuiTextures.CANNON_CAST_SHADOW.render(stack, 40, 45);
		stack.pushPose();
		stack.translate(this.getDisplayWidth(null) / 2 - 15, 55, 0);
		this.cannonCast.withShape(recipe.shape()).render(stack, (int) mouseX, (int) mouseY, 0);
		stack.popPose();
		CBCGuiTextures.CASTING_ARROW.render(stack, 21, 47);
		CBCGuiTextures.CASTING_ARROW_1.render(stack, 124, 27);

		float castingTime = 0;
		FluidIngredient input = recipe.ingredient();
		List<FluidStack> matchingStacks = input.getMatchingFluidStacks();
		if (!matchingStacks.isEmpty()) {
			FluidStack fstack = matchingStacks.get(0);
			if (fstack.getFluid() != Fluids.EMPTY) castingTime = (float) FluidCastingTimeHandler.getCastingTime(fstack.getFluid());
		}
		Minecraft mc = Minecraft.getInstance();
		Component text = Component.translatable("recipe." + CreateBigCannons.MOD_ID + ".casting_time", String.format("%.2f", castingTime / 20.0f));
		mc.font.draw(stack, text, (177 - mc.font.width(text)) / 2, 90, 4210752);
	}

	@Override
	public void addWidgets(CBCDisplay<CannonCastingRecipe> display, List<Widget> ingredients, Point origin) {
		CannonCastingRecipe recipe = display.getRecipe();
		FluidIngredient input = recipe.ingredient();
		Block output = recipe.getResultBlock();
		CannonCastShape catalyst = recipe.shape();

		List<FluidStack> matchingStacks = input.getMatchingFluidStacks();
		Slot inputSlot = basicSlot(16, 27, origin).markInput();
		if (matchingStacks.isEmpty()) {
			inputSlot.entries(EntryIngredients.of(convertToREIFluid(FluidStack.EMPTY)));
		} else {
			inputSlot.entries(EntryIngredients.of(convertToREIFluid(matchingStacks.get(0).setAmount(recipe.shape().fluidSize()))));
		}
		setFluidRenderRatio(inputSlot);
		setFluidTooltip(inputSlot);
		ingredients.add(inputSlot.backgroundEnabled(true));

		ingredients.add(basicSlot(142, 62, origin).markOutput().entry(EntryStacks.of(output)).backgroundEnabled(true));

		ingredients.add(basicSlot(80, 5, origin).entry(EntryStacks.of(catalyst.castMould())).backgroundEnabled(true));
	}

}
