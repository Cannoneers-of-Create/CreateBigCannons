package rbasamoyai.createbigcannons.block_hit_effects;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ProjectileParticleEffectModifiers {

	private static final Map<ParticleType<?>, List<Modifier>> MODIFIERS = new Reference2ObjectOpenHashMap<>();

	public static void register(ParticleType<?> type, Modifier modifier) {
		if (!MODIFIERS.containsKey(type))
			MODIFIERS.put(type, new LinkedList<>());
		List<Modifier> list = MODIFIERS.get(type);
		list.add(modifier);
	}

	public static ParticleOptions applyEffects(ParticleOptions original, EntityType<?> entity, BlockState blockState, ProjectileHitEffect effect) {
		ParticleType<?> type = original.getType();
		if (!MODIFIERS.containsKey(type))
			return original;
		List<Modifier> list = MODIFIERS.get(type);
		for (Modifier mod : list)
			original = mod.modify(new Modifier.Context(original, entity, blockState, effect));
		return original;
	}

	@FunctionalInterface
	public interface Modifier {
		ParticleOptions modify(Context context);
		record Context(ParticleOptions particle, EntityType<?> type, BlockState blockState, ProjectileHitEffect effect) {
		}
	}

	private ProjectileParticleEffectModifiers() {}

}
