package rbasamoyai.createbigcannons.crafting.casting;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;

public class CannonCastingRecipe implements BlockRecipe {

	private final CannonCastShape requiredShape;
	private final FluidIngredient ingredient;
	private final Block result;
	private final ResourceLocation id;

	public CannonCastingRecipe(CannonCastShape requiredShape, FluidIngredient ingredient, Block result, ResourceLocation id) {
		this.requiredShape = requiredShape;
		this.ingredient = ingredient;
		this.result = result;
		this.id = id;
	}

	public CannonCastShape shape() { return this.requiredShape; }
	public FluidIngredient ingredient() { return this.ingredient; }
	public ResourceLocation id() { return this.id; }

	@Override
	public boolean matches(Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof AbstractCannonCastBlockEntity cast
				&& cast.matchesRecipe(this);
	}

	@Override
	public void assembleInWorld(Level level, BlockPos pos) {
		if (!(level.getBlockEntity(pos) instanceof AbstractCannonCastBlockEntity cast) || !cast.canRenderCastModel()) return;
		cast.setRemoved();
		BlockState state = this.result.defaultBlockState();
		if (state.hasProperty(BlockStateProperties.FACING)) {
			state = state.setValue(BlockStateProperties.FACING, Direction.DOWN);
		}
		state = this.requiredShape.applyTo(state);
		level.setBlock(pos, state, 11);
	}

	@Override public Block getResultBlock() { return this.result; }
	@Override public ResourceLocation getId() { return this.id; }
	@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.CANNON_CASTING; }
	@Override public BlockRecipeType<?> getType() { return BlockRecipeType.CANNON_CASTING; }

	public static class Serializer implements BlockRecipeSerializer<CannonCastingRecipe> {
		@Override
		public CannonCastingRecipe fromJson(ResourceLocation id, JsonObject obj) {
			CannonCastShape shape = CBCRegistries.cannonCastShapes().get(new ResourceLocation(obj.get("cast_shape").getAsString()));
			FluidIngredient ingredient = FluidIngredient.deserialize(obj.get("fluid"));
			Block result = BuiltInRegistries.BLOCK.get(new ResourceLocation(obj.get("result").getAsString()));
			return new CannonCastingRecipe(shape, ingredient, result, id);
		}

		@Override
		public CannonCastingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			CannonCastShape shape = CBCRegistries.cannonCastShapes().byId(buf.readVarInt());
			Block result = BuiltInRegistries.BLOCK.byId(buf.readVarInt());
			FluidIngredient ingredient = FluidIngredient.read(buf);
			return new CannonCastingRecipe(shape, ingredient, result, id);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, CannonCastingRecipe recipe) {
			buf.writeVarInt(CBCRegistries.cannonCastShapes().getId(recipe.shape()))
			.writeVarInt(BuiltInRegistries.BLOCK.getId(recipe.getResultBlock()));
			recipe.ingredient().write(buf);
		}
	}

}
