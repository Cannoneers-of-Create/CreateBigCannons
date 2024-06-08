package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.ritchiesprojectilelib.projectile_burst.ProjectileBurst.SubProjectile;

public class FluidBlobEffectRegistry {

	/**
	 * Fired for all hits of any type.
	 */
	public interface OnHit extends Consumer<OnHit.Context> {
		record Context(EndFluidStack fstack, FluidBlobBurst burst, SubProjectile subProjectile, Level level, HitResult result) {
		}
	}

	private static final FluidBlobEffectTypeHandler<OnHit.Context, OnHit> ON_HIT = new FluidBlobEffectTypeHandler<>();

	public static void registerAllHit(Fluid fluid, OnHit handler) { ON_HIT.registerHandler(fluid, handler); }

	public static void effectOnAllHit(FluidBlobBurst burst, SubProjectile subProjectile, HitResult result) {
		EndFluidStack fstack = burst.getFluidStack();
		ON_HIT.run(fstack.fluid(), new OnHit.Context(fstack, burst, subProjectile, burst.getLevel(), result));
	}

	/**
	 * Fired only for block hits.
	 */
	public interface OnHitBlock extends Consumer<OnHitBlock.Context> {
		record Context(EndFluidStack fstack, FluidBlobBurst burst, SubProjectile subProjectile, Level level, BlockHitResult result) {
		}
	}

	private static final FluidBlobEffectTypeHandler<OnHitBlock.Context, OnHitBlock> ON_HIT_BLOCK = new FluidBlobEffectTypeHandler<>();

	public static void registerHitBlock(Fluid fluid, OnHitBlock handler) { ON_HIT_BLOCK.registerHandler(fluid, handler); }

	public static void effectOnHitBlock(FluidBlobBurst burst, SubProjectile subProjectile, BlockHitResult result) {
		EndFluidStack fstack = burst.getFluidStack();
		ON_HIT_BLOCK.run(fstack.fluid(), new OnHitBlock.Context(fstack, burst, subProjectile, burst.getLevel(), result));
	}

	/**
	 * Fired only for entity hits.
	 */
	public interface OnHitEntity extends Consumer<OnHitEntity.Context> {
		record Context(EndFluidStack fstack, FluidBlobBurst burst, SubProjectile subProjectile, Level level, EntityHitResult result) {
		}
	}

	private static final FluidBlobEffectTypeHandler<OnHitEntity.Context, OnHitEntity> ON_HIT_ENTITY = new FluidBlobEffectTypeHandler<>();

	public static void registerHitEntity(Fluid fluid, OnHitEntity handler) { ON_HIT_ENTITY.registerHandler(fluid, handler); }

	public static void effectOnHitEntity(FluidBlobBurst burst, SubProjectile subProjectile, EntityHitResult result) {
		EndFluidStack fstack = burst.getFluidStack();
		ON_HIT_ENTITY.run(fstack.fluid(), new OnHitEntity.Context(fstack, burst, subProjectile, burst.getLevel(), result));
	}

	private static class FluidBlobEffectTypeHandler<C, T extends Consumer<C>> {
		private final Map<Fluid, List<T>> handlers = new Reference2ReferenceOpenHashMap<>();
		private final Set<Fluid> fluidsWithNoHandlers = new ReferenceOpenHashSet<>();

		public void registerHandler(Fluid fluid, T handler) {
			List<T> alreadyExisting = this.getMatchingHandlerCollection(fluid);
			if (alreadyExisting == null)
				this.handlers.put(fluid, alreadyExisting = new ObjectArrayList<>());
			alreadyExisting.add(handler);
			this.handlers.put(fluid, alreadyExisting);
			this.fluidsWithNoHandlers.remove(fluid);
		}

		private void run(Fluid fluid, C context) {
			if (this.fluidsWithNoHandlers.contains(fluid))
				return;
			List<T> alreadyExisting = this.getMatchingHandlerCollection(fluid);
			if (alreadyExisting == null) {
				this.fluidsWithNoHandlers.add(fluid);
				return;
			}
			if (!this.handlers.containsKey(fluid))
				this.handlers.put(fluid, alreadyExisting);
			for (T handler : alreadyExisting)
				handler.accept(context);
		}

		@Nullable
		private List<T> getMatchingHandlerCollection(Fluid fluid) {
			if (this.handlers.containsKey(fluid))
				return this.handlers.get(fluid);
			for (Map.Entry<Fluid, List<T>> entry : this.handlers.entrySet()) {
				if (entry.getKey().isSame(fluid))
					return entry.getValue();
			}
			return null;
		}
	}

}
