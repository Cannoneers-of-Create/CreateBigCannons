package rbasamoyai.createbigcannons.crafting.casting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class CannonCastingRecipe implements BlockRecipe {

	private final CannonCastShape requiredShape;
	private final FluidIngredient ingredient;
	private final Block result;

	public CannonCastingRecipe(CannonCastShape requiredShape, FluidIngredient ingredient, Block result) {
		this.requiredShape = requiredShape;
		this.ingredient = ingredient;
		this.result = result;
	}

	public CannonCastShape shape() { return this.requiredShape; }
	public FluidIngredient ingredient() { return this.ingredient; }

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
	@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.CANNON_CASTING; }
	@Override public BlockRecipeType<?> getType() { return BlockRecipeType.CANNON_CASTING; }

	public static class Serializer implements BlockRecipeSerializer<CannonCastingRecipe> { // TODO c6 playtest
        public static final Codec<CannonCastShape> CANNON_SHAPE_CODEC = CBCRegistries.cannonCastShapes().byNameCodec()
            .validate(shape -> shape == null ? DataResult.error(() -> "Invalid cannon cast shape") : DataResult.success(shape));
        public static final Codec<Block> BLOCK_CODEC = CBCRegistryUtils.getBlockRegistry().byNameCodec()
            .validate(block -> block == Blocks.AIR ? DataResult.error(() -> "Invalid block for built-up heating recipe") : DataResult.success(block));

        public static final MapCodec<CannonCastingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                CANNON_SHAPE_CODEC.fieldOf("cast_shape").forGetter(CannonCastingRecipe::shape),
                FluidIngredient.CODEC.fieldOf("fluid").forGetter(CannonCastingRecipe::ingredient),
                BLOCK_CODEC.fieldOf("result").forGetter(CannonCastingRecipe::getResultBlock)
            ).apply(instance, CannonCastingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CannonCastingRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(CBCRegistries.CANNON_CAST_SHAPES), CannonCastingRecipe::shape,
            FluidIngredient.STREAM_CODEC, CannonCastingRecipe::ingredient,
            ByteBufCodecs.registry(CBCRegistryUtils.getBlockRegistryKey()), CannonCastingRecipe::getResultBlock,
            CannonCastingRecipe::new);

        @Override public MapCodec<CannonCastingRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, CannonCastingRecipe> streamCodec() { return STREAM_CODEC; }
	}

}
