package rbasamoyai.createbigcannons.crafting.casting;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidIngredient;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;

public class CannonCastingRecipe implements BlockRecipe {
	
	private final CannonCastShape requiredShape;
	private final FluidIngredient ingredient;
	private final Block result;
	private final int castingTime;
	private final ResourceLocation id;
	
	public CannonCastingRecipe(CannonCastShape requiredShape, FluidIngredient ingredient, Block result, int castingTime, ResourceLocation id) {
		this.requiredShape = requiredShape;
		this.ingredient = ingredient;
		this.result = result;
		this.castingTime = castingTime;
		this.id = id;
	}
	
	public CannonCastShape shape() { return this.requiredShape; }
	public FluidIngredient ingredient() { return this.ingredient; }
	public int castingTime() { return this.castingTime; }
	public ResourceLocation id() { return this.id; }
	
	@Override
	public boolean matches(Level level, BlockPos pos) {
		if (!(level.getBlockEntity(pos) instanceof CannonCastBlockEntity cast)) return false;
		if (!cast.getControllerTE().structure.contains(this.requiredShape)) return false;
		if (cast.getControllerTE().fluid.getFluid().isEmpty()) return false;
		return this.ingredient.test(cast.getControllerTE().fluid.getFluid());
	}

	@Override
	public void assembleInWorld(Level level, BlockPos pos) {
		if (!(level.getBlockEntity(pos) instanceof CannonCastBlockEntity cast) || !cast.canRenderCastModel()) return;
		cast.setRemoved();
		BlockState state = this.result.defaultBlockState();
		if (state.hasProperty(BlockStateProperties.FACING)) {
			state = state.setValue(BlockStateProperties.FACING, Direction.DOWN);
		}
		this.requiredShape.applyTo(state);
		level.setBlock(pos, state, 11);
	}

	@Override public Block getResultBlock() { return this.result; }
	@Override public ResourceLocation getId() { return this.id; }
	@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.CANNON_CASTING.get(); }
	@Override public BlockRecipeType<?> getType() { return BlockRecipeType.CANNON_CASTING.get(); }
	
	public static class Serializer extends ForgeRegistryEntry<BlockRecipeSerializer<?>> implements BlockRecipeSerializer<CannonCastingRecipe> {
		@Override
		public CannonCastingRecipe fromJson(ResourceLocation id, JsonObject obj) {
			CannonCastShape shape = CBCRegistries.CANNON_CAST_SHAPES.get().getValue(new ResourceLocation(obj.get("cast_shape").getAsString()));
			FluidIngredient ingredient = FluidIngredient.deserialize(obj.get("fluid"));
			int castingTime = obj.get("casting_time").getAsInt();
			Block result = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj.get("result").getAsString()));
			return new CannonCastingRecipe(shape, ingredient, result, castingTime, id);
		}

		@Override
		public CannonCastingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			CannonCastShape shape = CBCRegistries.CANNON_CAST_SHAPES.get().getValue(buf.readResourceLocation());
			int castingTime = buf.readVarInt();
			Block result = buf.readRegistryIdUnsafe(ForgeRegistries.BLOCKS);
			FluidIngredient ingredient = FluidIngredient.read(buf);
			return new CannonCastingRecipe(shape, ingredient, result, castingTime, id);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, CannonCastingRecipe recipe) {
			buf.writeResourceLocation(CBCRegistries.CANNON_CAST_SHAPES.get().getKey(recipe.shape()))
			.writeVarInt(recipe.castingTime())
			.writeRegistryIdUnsafe(ForgeRegistries.BLOCKS, recipe.getResultBlock());
			recipe.ingredient().write(buf);
		}
	}
	
}
