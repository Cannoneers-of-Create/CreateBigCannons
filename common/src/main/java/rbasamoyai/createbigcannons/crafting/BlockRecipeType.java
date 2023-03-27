package rbasamoyai.createbigcannons.crafting;

import net.minecraft.core.Registry;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.boring.DrillBoringBlockRecipe;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;
import rbasamoyai.createbigcannons.base.CBCRegistries;

public interface BlockRecipeType<T extends BlockRecipe>  {

	BlockRecipeType<CannonCastingRecipe> CANNON_CASTING = register("cannon_casting");
	BlockRecipeType<BuiltUpHeatingRecipe> BUILT_UP_HEATING = register("built_up_heating");
	BlockRecipeType<DrillBoringBlockRecipe> DRILL_BORING = register("drill_boring");
	
	private static <T extends BlockRecipe> BlockRecipeType<T> register(String id) {
		return Registry.register(CBCRegistries.BLOCK_RECIPE_TYPES, CreateBigCannons.resource(id), new Simple<T>(id));
	}
	
	class Simple<T extends BlockRecipe> implements BlockRecipeType<T> {
		private final String id;
		
		public Simple(String id) {
			this.id = id;
		}
		
		@Override public String toString() { return this.id; }
	}
	
	static void register() {}
	
}
