package rbasamoyai.createbigcannons.crafting;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.base.CBCRegistries;

public interface BlockRecipe {
    Codec<BlockRecipe> CODEC = CBCRegistries.blockRecipeSerializers().byNameCodec().dispatch(BlockRecipe::getSerializer, BlockRecipeSerializer::codec);
    StreamCodec<RegistryFriendlyByteBuf, BlockRecipe> STREAM_CODEC = ByteBufCodecs.registry(CBCRegistries.BLOCK_RECIPE_SERIALIZERS)
        .dispatch(BlockRecipe::getSerializer, BlockRecipeSerializer::streamCodec);

	boolean matches(Level level, BlockPos pos);

	void assembleInWorld(Level level, BlockPos pos);

	Block getResultBlock();

	BlockRecipeSerializer<?> getSerializer();

	BlockRecipeType<?> getType();

}
