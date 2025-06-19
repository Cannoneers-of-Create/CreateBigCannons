package rbasamoyai.createbigcannons.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public abstract sealed class BlockRecipeIngredient implements Predicate<BlockState> {
    // TODO needs datagen on NeoForge working
    // TODO c6 playtest

    public static final Codec<BlockRecipeIngredient> CODEC = Type.CODEC.dispatch(BlockRecipeIngredient::ingredientType, type -> type.codec);
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockRecipeIngredient> STREAM_CODEC = Type.STREAM_CODEC.dispatch(BlockRecipeIngredient::ingredientType, type -> type.streamCodec);

	public static BlockRecipeIngredient of(Block block) { return new BlockIngredient(block); }

	public static BlockRecipeIngredient of(TagKey<Block> tag) { return new TagIngredient(tag); }

	public abstract List<ItemStack> getBlockItems();

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeUtf(this.stringForSerialization());
	}

	public abstract String stringForSerialization();

    public abstract Type ingredientType();

	public static final class BlockIngredient extends BlockRecipeIngredient {
        public static final Codec<Block> BLOCK_CODEC = CBCRegistryUtils.getBlockRegistry().byNameCodec()
            .validate(block -> block == Blocks.AIR ? DataResult.error(() -> "Invalid block ingredient block") : DataResult.success(block));

        public static final MapCodec<BlockIngredient> CODEC = BLOCK_CODEC.fieldOf("block").xmap(BlockIngredient::new, i -> i.block);
        public static final StreamCodec<RegistryFriendlyByteBuf, BlockIngredient> STREAM_CODEC = ByteBufCodecs.registry(CBCRegistryUtils.getBlockRegistryKey())
            .map(BlockIngredient::new, i -> i.block);

        public static final BlockIngredient NONE = new BlockIngredient(Blocks.AIR);
		private final Block block;
		private final List<ItemStack> blocks = new ArrayList<>(1);

		public BlockIngredient(Block block) {
			this.block = block;
			this.blocks.add(new ItemStack(this.block));
		}

		@Override
		public boolean test(BlockState blockState) {
			return this.block != Blocks.AIR && blockState.is(this.block);
		}

		@Override
		public List<ItemStack> getBlockItems() {
			return this.blocks;
		}

		@Override
		public String stringForSerialization() {
			return CBCRegistryUtils.getBlockLocation(this.block).toString();
		}

        @Override public Type ingredientType() { return Type.BLOCK; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof BlockIngredient other))
                return false;
            return this.block == other.block;
        }
    }

	public static final class TagIngredient extends BlockRecipeIngredient {
        public static final MapCodec<TagIngredient> CODEC = TagKey.codec(CBCRegistryUtils.getBlockRegistryKey()).fieldOf("tag").xmap(TagIngredient::new, i -> i.tag);
        public static final StreamCodec<RegistryFriendlyByteBuf, TagIngredient> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, i -> i.tag.location(),
            rl -> new TagIngredient(TagKey.create(CBCRegistryUtils.getBlockRegistryKey(), rl)));

		private final TagKey<Block> tag;
		private List<ItemStack> blocks = null;

		public TagIngredient(TagKey<Block> tag) {
			this.tag = tag;
		}

		@Override
		public boolean test(BlockState blockState) {
			return blockState.is(this.tag);
		}

		@Override
		public List<ItemStack> getBlockItems() {
			if (this.blocks == null) {
				this.blocks = new ArrayList<>();
				for (Holder<Block> holder : CBCRegistryUtils.getBlockTagEntries(this.tag))
                    this.blocks.add(new ItemStack(holder.value()));
				if (this.blocks.isEmpty()) {
                    ItemStack stack = new ItemStack(Blocks.BARRIER);
                    stack.applyComponents(DataComponentPatch.builder().set(DataComponents.ITEM_NAME, Component.literal("Empty Tag: " + this.tag.location())).build());
                    this.blocks.add(stack);
				}
			}
			return this.blocks;
		}

		@Override
		public String stringForSerialization() {
			return "#" + this.tag.location();
		}

        @Override public Type ingredientType() { return Type.TAG; }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof TagIngredient other))
                return false;
            return this.tag.equals(other.tag);
        }
    }

    public enum Type implements StringRepresentable {
        BLOCK(BlockIngredient.CODEC, BlockIngredient.STREAM_CODEC),
        TAG(TagIngredient.CODEC, TagIngredient.STREAM_CODEC);

        public static final Codec<Type> CODEC = StringRepresentable.fromValues(Type::values);
        public static final StreamCodec<RegistryFriendlyByteBuf, Type> STREAM_CODEC = CatnipStreamCodecBuilders.ofEnum(Type.class);

        private final String id = this.name().toLowerCase(Locale.ROOT);

        private final MapCodec<? extends BlockRecipeIngredient> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, ? extends BlockRecipeIngredient> streamCodec;

        Type(MapCodec<? extends BlockRecipeIngredient> codec, StreamCodec<RegistryFriendlyByteBuf, ? extends BlockRecipeIngredient> streamCodec) {
            this.codec = codec;
            this.streamCodec = streamCodec;
        }

        @Override public String getSerializedName() { return this.id; }
    }

}
