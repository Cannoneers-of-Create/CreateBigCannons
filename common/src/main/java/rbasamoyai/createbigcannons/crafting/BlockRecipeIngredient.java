package rbasamoyai.createbigcannons.crafting;

import com.google.gson.JsonElement;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class BlockRecipeIngredient implements Predicate<BlockState> {

	public static final BlockRecipeIngredient NONE = new BlockRecipeIngredient() {
		private List<ItemStack> ingredient;

		@Override
		public List<ItemStack> getBlockItems() {
			if (this.ingredient == null) {
				this.ingredient = new ArrayList<>(1);
				this.ingredient.add(new ItemStack(Blocks.BARRIER).setHoverName(Component.translatable("Invalid block")));
			}
			return this.ingredient;
		}

		@Override public boolean test(BlockState blockState) { return false; }
		@Override public String stringForSerialization() { return "/"; }
	};

	public static BlockRecipeIngredient of(Block block) { return new BlockIngredient(block); }
	public static BlockRecipeIngredient of(TagKey<Block> tag) { return new TagIngredient(tag); }

	public static BlockRecipeIngredient fromJson(JsonElement el) {
		return el.isJsonPrimitive() && el.getAsJsonPrimitive().isString() ? fromString(el.getAsJsonPrimitive().getAsString()) : NONE;
	}

	public static BlockRecipeIngredient fromNetwork(FriendlyByteBuf buf) { return fromString(buf.readUtf()); }

	public static BlockRecipeIngredient fromString(String s) {
		return s.charAt(0) == '/' ? NONE :
				s.charAt(0) == '#' ? of(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(s.substring(1)))) :
				Registry.BLOCK.getOptional(new ResourceLocation(s)).map(BlockRecipeIngredient::of).orElse(NONE);
	}

	public abstract List<ItemStack> getBlockItems();
	public void toNetwork(FriendlyByteBuf buf) { buf.writeUtf(this.stringForSerialization()); }
	public abstract String stringForSerialization();

	public static class BlockIngredient extends BlockRecipeIngredient {
		private final Block block;
		private final List<ItemStack> blocks = new ArrayList<>(1);

		protected BlockIngredient(Block block) {
			this.block = block;
			this.blocks.add(new ItemStack(this.block));
		}

		@Override public boolean test(BlockState blockState) { return blockState.is(this.block); }
		@Override public List<ItemStack> getBlockItems() { return this.blocks; }
		@Override public String stringForSerialization() { return Registry.BLOCK.getKey(this.block).toString(); }
	}

	public static class TagIngredient extends BlockRecipeIngredient {
		private final TagKey<Block> tag;
		private List<ItemStack> blocks = null;

		protected TagIngredient(TagKey<Block> tag) {
			this.tag = tag;
		}

		@Override public boolean test(BlockState blockState) { return blockState.is(this.tag); }

		@Override
		public List<ItemStack> getBlockItems() {
			if (this.blocks == null) {
				this.blocks = new ArrayList<>();
				for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(this.tag)) {
					this.blocks.add(new ItemStack(holder.value()));
				}
				if (this.blocks.isEmpty()) {
					this.blocks.add(new ItemStack(Blocks.BARRIER).setHoverName(Component.literal("Empty Tag: " + this.tag.location())));
				}
			}
			return this.blocks;
		}

		@Override public String stringForSerialization() { return "#" + this.tag.location(); }
	}

}
