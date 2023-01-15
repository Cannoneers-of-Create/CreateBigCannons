package rbasamoyai.createbigcannons.munitions.fluidshell;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class FluidBlobEffectRegistry {

	/**
	 Fired for all hits of any type.
	 */
	public interface OnHit {
		void hit(FluidStack fstack, FluidBlob projectile, Level level, HitResult result);
	}

	private static final Map<Fluid, OnHit> ON_HIT = new HashMap<>();

	public static void registerAllHit(Fluid fluid, OnHit cons) { ON_HIT.put(fluid, cons); }

	public static void effectOnAllHit(FluidBlob projectile, HitResult result) {
		FluidStack stack = projectile.getFluidStack();
		if (stack == null) return;
		OnHit cons = ON_HIT.get(stack.getFluid());
		if (cons != null) cons.hit(stack, projectile, projectile.getLevel(), result);
	}

	/**
	 Fired only for block hits.
	 */
	public interface OnHitBlock {
		void hit(FluidStack fstack, FluidBlob projectile, Level level, BlockHitResult result);
	}

	private static final Map<Fluid, OnHitBlock> ON_HIT_BLOCK = new HashMap<>();

	public static void registerHitBlock(Fluid fluid, OnHitBlock cons) {
		ON_HIT_BLOCK.put(fluid, cons);
	}

	public static void effectOnHitBlock(FluidBlob projectile, BlockHitResult result) {
		FluidStack stack = projectile.getFluidStack();
		if (stack == null) return;
		OnHitBlock cons = ON_HIT_BLOCK.get(stack.getFluid());
		if (cons != null) cons.hit(stack, projectile, projectile.getLevel(), result);
	}

	/**
	 Fired only for entity hits.
	 */
	public interface OnHitEntity {
		void hit(FluidStack fstack, FluidBlob projectile, Level level, EntityHitResult result);
	}

	private static final Map<Fluid, OnHitEntity> ON_HIT_ENTITY = new HashMap<>();

	public static void registerHitEntity(Fluid fluid, OnHitEntity cons) {
		ON_HIT_ENTITY.put(fluid, cons);
	}

	public static void effectOnHitEntity(FluidBlob projectile, EntityHitResult result) {
		FluidStack stack = projectile.getFluidStack();
		if (stack == null) return;
		OnHitEntity cons = ON_HIT_ENTITY.get(stack.getFluid());
		if (cons != null) cons.hit(stack, projectile, projectile.getLevel(), result);
	}

}
