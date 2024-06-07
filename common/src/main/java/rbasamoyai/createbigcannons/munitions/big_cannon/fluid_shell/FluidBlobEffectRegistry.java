package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import java.util.HashMap;
import java.util.Map;

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
	public interface OnHit {
		void hit(Context context);

		record Context(EndFluidStack fstack, FluidBlobBurst burst, SubProjectile subProjectile, Level level, HitResult result) {
		}
	}

	private static final Map<Fluid, OnHit> ON_HIT = new HashMap<>();

	public static void registerAllHit(Fluid fluid, OnHit cons) { ON_HIT.put(fluid, cons); }

	private static OnHit getHitEffect(Fluid fluid) {
		for (Map.Entry<Fluid, OnHit> entry : ON_HIT.entrySet()) {
			if (entry.getKey().isSame(fluid)) return entry.getValue();
		}
		return null;
	}

	public static void effectOnAllHit(FluidBlobBurst burst, SubProjectile subProjectile, HitResult result) {
		EndFluidStack fstack = burst.getFluidStack();
		OnHit cons = getHitEffect(fstack.fluid());
		if (cons != null)
			cons.hit(new OnHit.Context(fstack, burst, subProjectile, burst.getLevel(), result));
	}

	/**
	 * Fired only for block hits.
	 */
	public interface OnHitBlock {
		void hit(Context context);

		record Context(EndFluidStack fstack, FluidBlobBurst burst, SubProjectile subProjectile, Level level, BlockHitResult result) {
		}
	}

	private static final Map<Fluid, OnHitBlock> ON_HIT_BLOCK = new HashMap<>();

	public static void registerHitBlock(Fluid fluid, OnHitBlock cons) {
		ON_HIT_BLOCK.put(fluid, cons);
	}

	private static OnHitBlock getHitBlockEffect(Fluid fluid) {
		for (Map.Entry<Fluid, OnHitBlock> entry : ON_HIT_BLOCK.entrySet()) {
			if (entry.getKey().isSame(fluid)) return entry.getValue();
		}
		return null;
	}

	public static void effectOnHitBlock(FluidBlobBurst burst, SubProjectile subProjectile, BlockHitResult result) {
		EndFluidStack fstack = burst.getFluidStack();
		OnHitBlock cons = getHitBlockEffect(fstack.fluid());
		if (cons != null)
			cons.hit(new OnHitBlock.Context(fstack, burst, subProjectile, burst.getLevel(), result));
	}

	/**
	 * Fired only for entity hits.
	 */
	public interface OnHitEntity {
		void hit(Context context);

		record Context(EndFluidStack fstack, FluidBlobBurst burst, SubProjectile subProjectile, Level level, EntityHitResult result) {
		}
	}

	private static final Map<Fluid, OnHitEntity> ON_HIT_ENTITY = new HashMap<>();

	public static void registerHitEntity(Fluid fluid, OnHitEntity cons) {
		ON_HIT_ENTITY.put(fluid, cons);
	}

	private static OnHitEntity getHitEntityEffect(Fluid fluid) {
		for (Map.Entry<Fluid, OnHitEntity> entry : ON_HIT_ENTITY.entrySet()) {
			if (entry.getKey().isSame(fluid)) return entry.getValue();
		}
		return null;
	}

	public static void effectOnHitEntity(FluidBlobBurst burst, SubProjectile subProjectile, EntityHitResult result) {
		EndFluidStack fstack = burst.getFluidStack();
		OnHitEntity cons = getHitEntityEffect(fstack.fluid());
		if (cons != null)
			cons.hit(new OnHitEntity.Context(fstack, burst, subProjectile, burst.getLevel(), result));
	}

}
