package rbasamoyai.createbigcannons.crafting.builtup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BuiltUpHeatingRecipe implements BlockRecipe {

	private final Set<BlockRecipeIngredient> layers;
	private final Block result;
	private final ResourceLocation id;
	
	public BuiltUpHeatingRecipe(Set<BlockRecipeIngredient> requiredLayers, Block result, ResourceLocation id) {
		this.layers = requiredLayers;
		this.result = result;
		this.id = id;
	}
	
	public Set<BlockRecipeIngredient> layers() { return this.layers; }
	public Block result() { return this.result; }
	
	@Override
	public boolean matches(Level level, BlockPos pos) {
		if (!(level.getBlockEntity(pos) instanceof LayeredBigCannonBlockEntity layered)) return false;
		if (layered.getLayers().size() != this.layers.size()) return false;
		Set<BlockRecipeIngredient> copy = new HashSet<>(this.layers);
		for (Block block : layered.getLayers().values()) {
			for (Iterator<BlockRecipeIngredient> iter = copy.iterator(); iter.hasNext(); ) {
				BlockRecipeIngredient pred = iter.next();
				if (pred.test(block.defaultBlockState())) {
					iter.remove();
					break;
				}
			}
			if (copy.isEmpty()) return true;
		}
		return false;
	}
	
	private static final DirectionProperty FACING = BlockStateProperties.FACING;

	@Override
	public void assembleInWorld(Level level, BlockPos pos) {
		if (!(level.getBlockEntity(pos) instanceof LayeredBigCannonBlockEntity layered)) return;
		BlockState oldState = level.getBlockState(pos);
		if (!oldState.hasProperty(FACING)) return;
		layered.setRemoved();
		BlockState state = this.result.defaultBlockState();
		if (state.hasProperty(FACING)) {
			state = state.setValue(FACING, oldState.getValue(FACING));
		}
		level.setBlock(pos, state, 11);
		level.playSound(null, pos, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 2.0f);
		if (!(level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe)) return;
		BigCannonBlock.onPlace(level, pos);
	}

	@Override public Block getResultBlock() { return this.result; }
	@Override public ResourceLocation getId() { return this.id; }
	@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.BUILT_UP_HEATING.get(); }
	@Override public BlockRecipeType<?> getType() { return BlockRecipeType.BUILT_UP_HEATING.get(); }

	public static class Serializer implements BlockRecipeSerializer<BuiltUpHeatingRecipe> {
		@Override
		public BuiltUpHeatingRecipe fromJson(ResourceLocation id, JsonObject obj) {
			JsonArray layerArr = obj.getAsJsonArray("layers");
			Set<BlockRecipeIngredient> layers = new HashSet<>();
			if (layerArr != null) {
				for (JsonElement el : layerArr) layers.add(BlockRecipeIngredient.fromJson(el));
			}
			Block result = Registry.BLOCK.get(new ResourceLocation(obj.get("result").getAsString()));
			return new BuiltUpHeatingRecipe(layers, result, id);
		}

		@Override
		public BuiltUpHeatingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			int sz = buf.readVarInt();
			Set<BlockRecipeIngredient> layers = sz == 0 ? null : new HashSet<>();
			for (int i = 0; i < sz; ++i) layers.add(BlockRecipeIngredient.fromNetwork(buf));
			Block result = Registry.BLOCK.get(buf.readResourceLocation());
			return new BuiltUpHeatingRecipe(layers, result, id);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, BuiltUpHeatingRecipe recipe) {
			buf.writeVarInt(recipe.layers == null ? 0 : recipe.layers.size());
			if (recipe.layers != null && !recipe.layers.isEmpty()) {
				recipe.layers.forEach(p -> p.toNetwork(buf));
			}
			buf.writeResourceLocation(Registry.BLOCK.getKey(recipe.result));
		}
	}

}
