package rbasamoyai.createbigcannons.crafting.boring;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class DrillBoringBlockRecipe implements BlockRecipe {

	private final BlockRecipeIngredient input;
	private final Block result;
	private final boolean obeyFacingOrAxis;

	public DrillBoringBlockRecipe(BlockRecipeIngredient input, Block result, boolean obeyFacingOrAxis) {
		this.input = input;
		this.result = result;
		this.obeyFacingOrAxis = obeyFacingOrAxis;
	}

	public boolean matches(BlockState state, Direction dir) {
		if (this.obeyFacingOrAxis) {
			if (state.hasProperty(BlockStateProperties.FACING) && state.getValue(BlockStateProperties.FACING).getAxis() != dir.getAxis())
				return false;
			if (state.hasProperty(BlockStateProperties.AXIS) && state.getValue(BlockStateProperties.AXIS) != dir.getAxis())
				return false;
		}
		return this.input.test(state);
	}

	// Not used because this recipe type changes contraption blocks
	@Override public boolean matches(Level level, BlockPos pos) { return false; }
	@Override public void assembleInWorld(Level level, BlockPos pos) {}

	@Override public Block getResultBlock() { return this.result; }
	@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.DRILL_BORING; }
	@Override public BlockRecipeType<?> getType() { return BlockRecipeType.DRILL_BORING; }

	public List<ItemStack> ingredients() { return this.input.getBlockItems(); }

	public BlockState getResultState(BlockState input) {
		BlockState state = this.getResultBlock().defaultBlockState();
		for (Property<?> property : input.getProperties()) {
			if (state.hasProperty(property)) state = copyProperty(input, state, property);
		}
		return state;
	}

	private static <T extends Comparable<T>> BlockState copyProperty(BlockState src, BlockState dest, Property<T> property) {
		return dest.setValue(property, src.getValue(property));
	}

	public static class Serializer implements BlockRecipeSerializer<DrillBoringBlockRecipe> { // TODO c6 playtest
        public static final Codec<Block> BLOCK_CODEC = CBCRegistryUtils.getBlockRegistry().byNameCodec()
            .validate(block -> block == Blocks.AIR ? DataResult.error(() -> "Invalid block for drilling recipe") : DataResult.success(block));

        public static final MapCodec<DrillBoringBlockRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BlockRecipeIngredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
                BLOCK_CODEC.fieldOf("result").forGetter(DrillBoringBlockRecipe::getResultBlock),
                Codec.BOOL.optionalFieldOf("obey_facing_or_axis", true).forGetter(recipe -> recipe.obeyFacingOrAxis)
            ).apply(instance, DrillBoringBlockRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, DrillBoringBlockRecipe> STREAM_CODEC = StreamCodec.composite(
            BlockRecipeIngredient.STREAM_CODEC, i -> i.input,
            ByteBufCodecs.registry(CBCRegistryUtils.getBlockRegistryKey()), DrillBoringBlockRecipe::getResultBlock,
            ByteBufCodecs.BOOL, i -> i.obeyFacingOrAxis,
            DrillBoringBlockRecipe::new);

        @Override public MapCodec<DrillBoringBlockRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, DrillBoringBlockRecipe> streamCodec() { return STREAM_CODEC; }
	}

}
