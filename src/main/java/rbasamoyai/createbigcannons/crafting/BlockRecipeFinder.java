package rbasamoyai.createbigcannons.crafting;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BlockRecipeFinder {

	private static Cache<Object, List<BlockRecipe>> blockRecipeCache = CacheBuilder.newBuilder().build();
	
	public static List<BlockRecipe> get(@Nullable Object cacheKey, Level level, Predicate<BlockRecipe> predicates) {
		if (cacheKey == null) return startSearch(level, predicates);
		
		try {
			return blockRecipeCache.get(cacheKey, () -> startSearch(level, predicates));
		} catch (ExecutionException e) {
			e.printStackTrace();
		}		
		return Collections.emptyList();
	}
	
	private static List<BlockRecipe> startSearch(Level level, Predicate<? super BlockRecipe> predicates) {
		return BlockRecipesManager.getRecipes().stream().filter(predicates).collect(Collectors.toList());
	}
	
	public static final ResourceManagerReloadListener LISTENER = manager -> {
		blockRecipeCache.invalidateAll();
	};
	
}
