package rbasamoyai.createbigcannons.crafting.builtup;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.BlockRecipe;
import rbasamoyai.createbigcannons.crafting.BlockRecipeIngredient;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

public class BuiltUpHeatingRecipe implements BlockRecipe {

	private final Set<BlockRecipeIngredient> layers;
    private final List<BlockRecipeIngredient> layerList; // For codec writing
	private final Block result;

    public BuiltUpHeatingRecipe(List<BlockRecipeIngredient> requiredLayers, Block result) {
        this.layerList = requiredLayers;
        this.layers = new HashSet<>(requiredLayers);
        this.result = result;
    }

	public Set<BlockRecipeIngredient> layers() { return this.layers; }
    public List<BlockRecipeIngredient> layerList() { return this.layerList; }

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
	@Override public BlockRecipeSerializer<?> getSerializer() { return BlockRecipeSerializer.BUILT_UP_HEATING; }
	@Override public BlockRecipeType<?> getType() { return BlockRecipeType.BUILT_UP_HEATING; }

	public static class Serializer implements BlockRecipeSerializer<BuiltUpHeatingRecipe> { // TODO c6 playtest
        public static final Codec<Block> BLOCK_CODEC = CBCRegistryUtils.getBlockRegistry().byNameCodec()
            .validate(block -> block == Blocks.AIR ? DataResult.error(() -> "Invalid block for built-up heating recipe") : DataResult.success(block));

        public static final MapCodec<BuiltUpHeatingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BlockRecipeIngredient.CODEC.listOf().fieldOf("layers").forGetter(BuiltUpHeatingRecipe::layerList),
                BLOCK_CODEC.fieldOf("result").forGetter(BuiltUpHeatingRecipe::getResultBlock)
            ).apply(instance, BuiltUpHeatingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BuiltUpHeatingRecipe> STREAM_CODEC = StreamCodec.composite(
            BlockRecipeIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), BuiltUpHeatingRecipe::layerList,
            ByteBufCodecs.registry(CBCRegistryUtils.getBlockRegistryKey()), BuiltUpHeatingRecipe::getResultBlock,
            BuiltUpHeatingRecipe::new);

        @Override public MapCodec<BuiltUpHeatingRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, BuiltUpHeatingRecipe> streamCodec() { return STREAM_CODEC; }
	}

}
