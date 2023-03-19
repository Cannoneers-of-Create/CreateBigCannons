package rbasamoyai.createbigcannons.forge.multiloader.forge;

import com.simibubi.create.content.contraptions.fluids.FluidFX;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.AbstractCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.carriage.AbstractCannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.forge.cannon_control.CannonCarriageEntity;
import rbasamoyai.createbigcannons.forge.cannon_control.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.forge.cannon_control.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.forge.cannon_control.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.forge.cannons.AutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.forge.crafting.CannonCastBlockEntity;
import rbasamoyai.createbigcannons.forge.crafting.CannonCastBlockEntityRenderer;
import rbasamoyai.createbigcannons.forge.crafting.CannonDrillBlockEntity;
import rbasamoyai.createbigcannons.forge.munitions.fluid_shell.FluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;

public class IndexPlatformImpl {

	public static boolean isFakePlayer(Player player) { return player instanceof FakePlayer; }

	public static AbstractCannonDrillBlockEntity makeDrill(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CannonDrillBlockEntity(type, pos, state);
	}

	public static AbstractCannonCastBlockEntity makeCast(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CannonCastBlockEntity(type, pos, state);
	}

	public static AbstractCannonMountBlockEntity makeCannonMount(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new CannonMountBlockEntity(type, pos, state);
	}

	public static AbstractAutocannonBreechBlockEntity makeAutocannonBreech(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new AutocannonBreechBlockEntity(type, pos, state);
	}

	public static AbstractFluidShellBlockEntity makeFluidShellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		return new FluidShellBlockEntity(type, pos, state);
	}

	public static AbstractMountedAutocannonContraption makeAutocannon() {
		return new MountedAutocannonContraption();
	}

	public static AbstractPitchOrientedContraptionEntity makePitchContraption(EntityType<?> type, Level level) {
		return new PitchOrientedContraptionEntity(type, level);
	}

	public static AbstractCannonCarriageEntity makeCannonCarriage(EntityType<?> type, Level level) {
		return new CannonCarriageEntity(type, level);
	}

	public static ParticleOptions createFluidDripParticle(EndFluidStack stack) {
		return FluidFX.getFluidParticle(new FluidStack(stack.fluid(), stack.amount(), stack.data()));
	}

	public static NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context,
			BlockEntityRenderer<? super AbstractCannonCastBlockEntity>>> getCastRenderer() {
		return () -> CannonCastBlockEntityRenderer::new;
	}

	@SuppressWarnings("rawtypes")
	public static IIngredientType getFluidType() { return ForgeTypes.FLUID_STACK; }

}
