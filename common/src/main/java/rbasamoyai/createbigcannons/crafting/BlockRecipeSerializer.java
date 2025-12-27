package rbasamoyai.createbigcannons.crafting;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;

public interface BlockRecipeSerializer<T extends BlockRecipe> {

    MapCodec<T> codec();
    StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

	BlockRecipeSerializer<CannonCastingRecipe> CANNON_CASTING = register("cannon_casting", new CannonCastingRecipe.Serializer());
	BlockRecipeSerializer<BuiltUpHeatingRecipe> BUILT_UP_HEATING = register("built_up_heating", new BuiltUpHeatingRecipe.Serializer());
	BlockRecipeSerializer<DrillBoringBlockRecipe> DRILL_BORING = register("drill_boring", new DrillBoringBlockRecipe.Serializer());

	private static <T extends BlockRecipe> BlockRecipeSerializer<T> register(String id, BlockRecipeSerializer<T> ser) {
		return Registry.register(CBCRegistries.blockRecipeSerializers(), CreateBigCannons.resource(id), ser);
	}

	static void register() {}

}
