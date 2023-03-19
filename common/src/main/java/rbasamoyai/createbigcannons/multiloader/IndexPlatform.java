package rbasamoyai.createbigcannons.multiloader;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.architectury.injectables.annotations.ExpectPlatform;
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
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.AbstractCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.carriage.AbstractCannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.AbstractCannonCastBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.AbstractFluidShellBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.EndFluidStack;

public class IndexPlatform {

	@ExpectPlatform public static boolean isFakePlayer(Player player) { throw new AssertionError(); }

	@ExpectPlatform
	public static AbstractCannonDrillBlockEntity makeDrill(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static AbstractCannonCastBlockEntity makeCast(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static AbstractCannonMountBlockEntity makeCannonMount(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static AbstractAutocannonBreechBlockEntity makeAutocannonBreech(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static AbstractFluidShellBlockEntity makeFluidShellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		throw new AssertionError();
	}

	@ExpectPlatform public static AbstractMountedAutocannonContraption makeAutocannon() { throw new AssertionError(); }

	@ExpectPlatform
	public static AbstractPitchOrientedContraptionEntity makePitchContraption(EntityType<?> type, Level level) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static AbstractCannonCarriageEntity makeCannonCarriage(EntityType<?> type, Level level) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static ParticleOptions createFluidDripParticle(EndFluidStack stack) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context,
					BlockEntityRenderer<? super AbstractCannonCastBlockEntity>>> getCastRenderer() {
		throw new AssertionError();
	}

	@ExpectPlatform
	@SuppressWarnings("rawtypes")
	public static IIngredientType getFluidType() { throw new AssertionError(); }

	@ExpectPlatform public static int getModGroupId() { throw new AssertionError(); }

}
