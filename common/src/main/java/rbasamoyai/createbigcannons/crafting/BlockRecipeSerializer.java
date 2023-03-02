package rbasamoyai.createbigcannons.crafting;

import com.google.gson.JsonObject;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;

public interface BlockRecipeSerializer<T extends BlockRecipe> extends IForgeRegistryEntry<BlockRecipeSerializer<?>> {

	T fromJson(ResourceLocation id, JsonObject obj);
	T fromNetwork(ResourceLocation id, FriendlyByteBuf buf);
	void toNetwork(FriendlyByteBuf buf, T recipe);
	
	Entry<CannonCastingRecipe> CANNON_CASTING = register("cannon_casting", CannonCastingRecipe.Serializer::new);
	Entry<BuiltUpHeatingRecipe> BUILT_UP_HEATING = register("built_up_heating", BuiltUpHeatingRecipe.Serializer::new);
	Entry<DrillBoringBlockRecipe> DRILL_BORING = register("drill_boring", DrillBoringBlockRecipe.Serializer::new);
	
	private static <T extends BlockRecipe> Entry<T> register(String id, NonNullSupplier<BlockRecipeSerializer<T>> fac) {
		AbstractRegistrate<?> reg = CreateBigCannons.REGISTRATE;
		return reg.entry(id, cb -> new Builder<>(reg, reg, id, cb, fac)).register();
	}
			
	class Entry<T extends BlockRecipe> extends RegistryEntry<BlockRecipeSerializer<T>> {
		public Entry(AbstractRegistrate<?> owner, RegistryObject<BlockRecipeSerializer<T>> delegate) {
			super(owner, delegate);
		}
		
		public T fromJson(ResourceLocation id, JsonObject obj) { return this.get().fromJson(id, obj); }
		public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) { return this.get().fromNetwork(id, buf); }
		public void toNetwork(FriendlyByteBuf buf, T recipe) { this.get().toNetwork(buf, recipe); }
	}
	
	class Builder<T extends BlockRecipe, P> extends AbstractBuilder<BlockRecipeSerializer<?>, BlockRecipeSerializer<T>, P, Builder<T, P>> {
		private final NonNullSupplier<BlockRecipeSerializer<T>> factory;
		
		public Builder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullSupplier<BlockRecipeSerializer<T>> factory) {
			super(owner, parent, name, callback, CBCRegistries.Keys.BLOCK_RECIPE_SERIALIZERS);
			this.factory = factory;
		}

		@Override protected @NonnullType BlockRecipeSerializer<T> createEntry() { return this.factory.get(); }
		
		@Override public Entry<T> register() { return (Entry<T>) super.register(); }
		@Override protected Entry<T> createEntryWrapper(RegistryObject<BlockRecipeSerializer<T>> delegate) { return new Entry<>(this.getOwner(), delegate); }
	}
	
	static void register() {}
	
}
