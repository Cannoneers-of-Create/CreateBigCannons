package rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;
import java.util.Map;

public class FluidBlobEffectRegistry {

	/**
	 Fired for all hits of any type.
	 */
	public interface OnHit {
		void hit(EndFluidStack fstack, FluidBlob projectile, Level level, HitResult result);
	}

	private static final Map<Fluid, OnHit> ON_HIT = new HashMap<>();

	public static void registerAllHit(Fluid fluid, OnHit cons) { ON_HIT.put(fluid, cons); }

	private static OnHit getHitEffect(Fluid fluid) {
		for (Map.Entry<Fluid, OnHit> entry : ON_HIT.entrySet()) {
			if (entry.getKey().isSame(fluid)) return entry.getValue();
		}
		return null;
	}

	public static void effectOnAllHit(FluidBlob projectile, HitResult result) {
		EndFluidStack fstack = projectile.getFluidStack();
		OnHit cons = getHitEffect(fstack.fluid());
		if (cons != null) cons.hit(fstack, projectile, projectile.level(), result);
	}

	/**
	 Fired only for block hits.
	 */
	public interface OnHitBlock {
		void hit(EndFluidStack fstack, FluidBlob projectile, Level level, BlockHitResult result);
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

	public static void effectOnHitBlock(FluidBlob projectile, BlockHitResult result) {
		EndFluidStack fstack = projectile.getFluidStack();
		OnHitBlock cons = getHitBlockEffect(fstack.fluid());
		if (cons != null) cons.hit(fstack, projectile, projectile.level(), result);
	}

	/**
	 Fired only for entity hits.
	 */
	public interface OnHitEntity {
		void hit(EndFluidStack fstack, FluidBlob projectile, Level level, EntityHitResult result);
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

	public static void effectOnHitEntity(FluidBlob projectile, EntityHitResult result) {
		EndFluidStack fstack = projectile.getFluidStack();
		OnHitEntity cons = getHitEntityEffect(fstack.fluid());
		if (cons != null) cons.hit(fstack, projectile, projectile.level(), result);
	}

}
