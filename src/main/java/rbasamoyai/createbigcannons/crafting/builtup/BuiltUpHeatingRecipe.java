package rbasamoyai.createbigcannons.crafting.builtup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;

public class BuiltUpHeatingRecipe implements BlockRecipe {

	private final Set<LayerPredicate> layers;
	private final Block result;
	private final ResourceLocation id;
	
	public BuiltUpHeatingRecipe(Set<LayerPredicate> requiredLayers, Block result, ResourceLocation id) {
		this.layers = requiredLayers;
		this.result = result;
		this.id = id;
	}
	
	public Set<LayerPredicate> layers() { return this.layers; }
	public Block result() { return this.result; }
	
	@Override
	public boolean matches(Level level, BlockPos pos) {
		if (!(level.getBlockEntity(pos) instanceof LayeredCannonBlockEntity layered)) return false;
		if (layered.getLayers().size() != this.layers.size()) return false;
		Set<LayerPredicate> copy = new HashSet<>(this.layers);
		for (Block block : layered.getLayers().values()) {
			for (Iterator<LayerPredicate> iter = copy.iterator(); iter.hasNext(); ) {
				LayerPredicate pred = iter.next();
				if (pred.test(block)) {
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
		if (!(level.getBlockEntity(pos) instanceof LayeredCannonBlockEntity layered)) return;
		BlockState oldState = level.getBlockState(pos);
		if (!oldState.hasProperty(FACING)) return;
		layered.setRemoved();
		BlockState state = this.result.defaultBlockState();
		if (state.hasProperty(FACING)) {
			state = state.setValue(FACING, oldState.getValue(FACING));
		}
		level.setBlock(pos, state, 11);
		level.playSound(null, pos, SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1.0f, 2.0f);
		if (!(level.getBlockEntity(pos) instanceof ICannonBlockEntity cbe)) return;
		CannonBlock.onPlace(level, pos);
	}

	@Override public Block getResultBlock() { return this.result; }
	@Override public ResourceLocation getId() { return this.id; }
	@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.BUILT_UP_HEATING.get(); }
	@Override public BlockRecipeType<?> getType() { return BlockRecipeType.BUILT_UP_HEATING.get(); }

	public static class Serializer extends ForgeRegistryEntry<BlockRecipeSerializer<?>> implements BlockRecipeSerializer<BuiltUpHeatingRecipe> {
		@Override
		public BuiltUpHeatingRecipe fromJson(ResourceLocation id, JsonObject obj) {
			JsonArray layerArr = obj.getAsJsonArray("layers");
			Set<LayerPredicate> layers = new HashSet<>();
			if (layerArr != null) {
				for (JsonElement el : layerArr) {
					layers.add(LayerPredicate.fromJson(el.getAsJsonObject()));
				}
			}
			Block result = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj.get("result").getAsString()));
			return new BuiltUpHeatingRecipe(layers, result, id);
		}

		@Override
		public BuiltUpHeatingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
			int sz = buf.readVarInt();
			Set<LayerPredicate> layers = sz == 0 ? null : new HashSet<>();
			for (int i = 0; i < sz; ++i) {
				layers.add(LayerPredicate.fromNetwork(buf));
			}
			Block result = ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation());
			return new BuiltUpHeatingRecipe(layers, result, id);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buf, BuiltUpHeatingRecipe recipe) {
			buf.writeVarInt(recipe.layers == null ? 0 : recipe.layers.size());
			if (recipe.layers != null && !recipe.layers.isEmpty()) {
				recipe.layers.forEach(p -> p.toNetwork(buf));
			}
			buf.writeResourceLocation(ForgeRegistries.BLOCKS.getKey(recipe.result));
		}
	}
	
	public static class LayerPredicate implements Predicate<Block> {
		public static final LayerPredicate ANY = new LayerPredicate(null, null);
		
		private final Set<Block> blocks;
		private final TagKey<Block> tag;
		private List<Block> matchingBlocks;
		
		public LayerPredicate(Set<Block> blocks, TagKey<Block> tag) {
			this.blocks = blocks;
			this.tag = tag;
		}
		
		public List<Block> blocks() {
			if (this.matchingBlocks != null) return this.matchingBlocks;
			this.matchingBlocks = new ArrayList<>();
			if (this.blocks != null) this.matchingBlocks.addAll(this.blocks);
			else if (this.tag != null) {
				ForgeRegistries.BLOCKS
				.tags()
				.getTag(this.tag)
				.forEach(this.matchingBlocks::add);
			}
			return this.matchingBlocks;
		}
		
		@Override
		public boolean test(Block t) {
			if (this == ANY) return true;
			if (this.blocks != null) return this.blocks.contains(t);
			return this.tag == null ? true : ForgeRegistries.BLOCKS.tags().getTag(this.tag).contains(t);
		}
		
		public void serializeJson(JsonObject obj) {
			if (this.blocks != null && !this.blocks.isEmpty()) {
				JsonArray blockArr = new JsonArray(this.blocks.size());
				for (Block block : this.blocks) {
					blockArr.add(ForgeRegistries.BLOCKS.getKey(block).toString());
				}
				obj.add("blocks", blockArr);
			}
			if (this.tag != null) {
				obj.addProperty("tag", this.tag.location().toString());
			}
		}
		
		public static LayerPredicate fromJson(JsonObject obj) {
			JsonArray blockArr = obj.getAsJsonArray("blocks");
			Set<Block> blocks = blockArr == null || blockArr.isEmpty() ? null : new HashSet<>();
			if (blocks != null) {
				for (JsonElement el : blockArr) {
					blocks.add(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(el.getAsString())));
				}
			}
			TagKey<Block> tag = obj.has("tag") ? BlockTags.create(new ResourceLocation(obj.get("tag").getAsString())) : null;
			return new LayerPredicate(blocks, tag);
		}
		
		public void toNetwork(FriendlyByteBuf buf) {
			buf.writeVarInt(this.blocks == null ? 0 : this.blocks.size());
			if (this.blocks != null) {
				this.blocks.stream()
				.map(ForgeRegistries.BLOCKS::getKey)
				.forEach(buf::writeResourceLocation);
			}
			buf.writeBoolean(this.tag != null);
			if (this.tag != null) {
				buf.writeResourceLocation(this.tag.location());
			}
		}
		
		public static LayerPredicate fromNetwork(FriendlyByteBuf buf) {
			int sz = buf.readVarInt();
			Set<Block> blocks = sz == 0 ? null : new HashSet<>();
			if (blocks != null) {
				for (int i = 0; i < sz; ++i) {
					blocks.add(ForgeRegistries.BLOCKS.getValue(buf.readResourceLocation()));
				}
			}
			TagKey<Block> tag = buf.readBoolean() ? BlockTags.create(buf.readResourceLocation()) : null;
			return new LayerPredicate(blocks, tag);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (!(obj instanceof LayerPredicate other)) return false;
			if (!(this.blocks == null && other.blocks == null || this.blocks != null && this.blocks.equals(other.blocks))) return false;
			return this.tag == null && other.tag == null || this.tag != null && other.tag != null && this.tag.location().equals(other.tag.location());
		}
	}
	
}
