package rbasamoyai.createbigcannons.compat.emi;

import static com.simibubi.create.compat.emi.recipes.CreateEmiRecipe.addSlot;
import static com.simibubi.create.compat.emi.recipes.CreateEmiRecipe.fluidStack;

import java.util.ArrayList;
import java.util.List;

import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;
import rbasamoyai.createbigcannons.crafting.casting.FluidCastingTimeHandler;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.index.CBCGuiTextures;

public class CannonCastingEmiRecipe extends CBCEmiBlockRecipe<CannonCastingRecipe> {

	private final List<EmiIngredient> catalysts;

	public CannonCastingEmiRecipe(EmiRecipeCategory category, CannonCastingRecipe recipe, int width, int height) {
		super(category, recipe, width, height);
		this.inputs = new ArrayList<>();
		int sz = recipe.shape().fluidSize();
		for (FluidStack fstack : recipe.ingredient().getMatchingFluidStacks()) {
			this.inputs.add(fluidStack(fstack.setAmount(sz)));
		}
		this.catalysts = List.of(EmiIngredient.of(Ingredient.of(recipe.shape().castMould())));
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		List<FluidStack> matchingStacks = this.recipe.ingredient().getMatchingFluidStacks();
		addSlot(widgets, this.inputs.isEmpty() ? fluidStack(FluidStack.EMPTY) : this.inputs.get(0), 16, 27);
		addSlot(widgets, EmiStack.of(this.recipe.getResultBlock()), 142, 62).recipeContext(this);
		CannonCastShape shape = this.recipe.shape();
		addSlot(widgets, EmiStack.of(shape.castMould()), 80, 5).recipeContext(this);

		widgets.addDrawable(0, 0, 0, 0, ((poseStack, mouseX, mouseY, delta) -> {
			CBCGuiTextures.CANNON_CAST_SHADOW.render(poseStack, 40, 45);
			poseStack.pushPose();
			poseStack.translate(this.width / 2 - 15, 55, 200);
			poseStack.mulPose(Vector3f.XP.rotationDegrees(-22.5f));
			poseStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
			int scale = 23;

			GuiGameElement.of(CBCBlockPartials.cannonCastFor(shape))
				.atLocal(0, 0.25, 0)
				.scale(scale)
				.render(poseStack);

			poseStack.popPose();

			CBCGuiTextures.CASTING_ARROW.render(poseStack, 21, 47);
			CBCGuiTextures.CASTING_ARROW_1.render(poseStack, 124, 27);

			float castingTime = 0;
			if (!matchingStacks.isEmpty()) {
				FluidStack fstack = matchingStacks.get(0);
				if (fstack.getFluid() != Fluids.EMPTY) castingTime = (float) FluidCastingTimeHandler.getCastingTime(fstack.getFluid());
			}
			Minecraft mc = Minecraft.getInstance();
			Component text = Component.translatable("recipe." + CreateBigCannons.MOD_ID + ".casting_time", String.format("%.2f", castingTime / 20.0f));
			mc.font.draw(poseStack, text, (177 - mc.font.width(text)) / 2, 90, 4210752);
		}));
	}

	@Override public List<EmiIngredient> getCatalysts() { return this.catalysts; }

}
