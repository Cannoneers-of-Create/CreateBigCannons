package rbasamoyai.createbigcannons.base;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterialPropertiesHandler;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.BigCannonBreechStrengthHandler;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialPropertiesHandler;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.BlockRecipeFinder;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlockEntity;
import rbasamoyai.createbigcannons.crafting.munition_assembly.AutocannonAmmoContainerFillingDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.BigCartridgeFillingDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.CartridgeAssemblyDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.MunitionFuzingDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.TracerApplicationDeployerRecipe;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;
import rbasamoyai.createbigcannons.munitions.config.DimensionMunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.createbigcannons.network.CBCRootNetwork;

public class CBCCommonEvents {

	public static void serverLevelTickEnd(Level level) {
		CreateBigCannons.BLOCK_DAMAGE.tick(level);
	}

	public static void onPlayerLogin(ServerPlayer player) {
		CBCRootNetwork.onPlayerJoin(player);
		CreateBigCannons.BLOCK_DAMAGE.playerLogin(player);
	}

	public static void onPlayerLogout(Player player) {
		CreateBigCannons.BLOCK_DAMAGE.playerLogout(player);
	}

	public static boolean onPlayerBreakBlock(BlockState state, LevelAccessor level, BlockPos pos, Player player) {
		if (player.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(player) != null) {
			return true;
		}

		if (AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
			BlockPos drillPos = destroyPoleContraption(CBCBlocks.CANNON_DRILL_BIT.get(), CBCBlocks.CANNON_DRILL.get(),
				CannonDrillBlock.maxAllowedDrillLength(), state, level, pos, player);
			if (drillPos != null) {
				level.setBlock(drillPos, level.getBlockState(drillPos)
					.setValue(CannonDrillBlock.STATE, MechanicalPistonBlock.PistonState.RETRACTED), 3);
				if (level.getBlockEntity(pos) instanceof AbstractCannonDrillBlockEntity drill) {
					drill.onLengthBroken();
				}
				return false;
			}
			BlockPos builderPos = destroyPoleContraption(CBCBlocks.CANNON_BUILDER_HEAD.get(),
				CBCBlocks.CANNON_BUILDER.get(),
				CannonBuilderBlock.maxAllowedBuilderLength(), state, level, pos, player);
			if (builderPos != null) {
				level.setBlock(builderPos, level.getBlockState(builderPos)
					.setValue(CannonBuilderBlock.STATE, CannonBuilderBlock.BuilderState.UNACTIVATED), 3);
				if (level.getBlockEntity(pos) instanceof CannonBuilderBlockEntity builder) {
					builder.onLengthBroken();
				}
			}
		}
		return false;
	}

	private static BlockPos destroyPoleContraption(Block head, Block base, int limit, BlockState state,
		LevelAccessor level,
		BlockPos pos, Player player) {
		Direction.Axis axis = state.getValue(BlockStateProperties.FACING).getAxis();
		Direction positive = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);

		BlockPos headPos = null;
		BlockPos basePos = null;

		for (int mod : new int[]{1, -1}) {
			for (int offs = mod; mod * offs < limit; offs += mod) {
				BlockPos pos1 = pos.relative(positive, offs);
				BlockState state1 = level.getBlockState(pos1);

				if (AllBlocks.PISTON_EXTENSION_POLE.has(state1) && axis == state1.getValue(BlockStateProperties.FACING)
					.getAxis()) {
					continue;
				}
				if (state1.is(head) && axis == state1.getValue(BlockStateProperties.FACING).getAxis()) {
					headPos = pos1;
				}
				if (state1.is(base) && axis == state1.getValue(BlockStateProperties.FACING).getAxis()) {
					basePos = pos1;
				}
				break;
			}
		}
		if (headPos == null || basePos == null) {
			return null;
		}
		BlockPos baseCopy = basePos.immutable();
		BlockPos.betweenClosedStream(headPos, basePos)
			.filter(p -> !p.equals(pos) && !p.equals(baseCopy))
			.forEach(p -> level.destroyBlock(p, !player.isCreative()));
		return baseCopy;
	}

	public static void onLoadLevel(LevelAccessor level) {
		CreateBigCannons.BLOCK_DAMAGE.levelLoaded(level);
		if (level.getServer() != null && !level.isClientSide() && level.getServer().overworld() == level) {
			BlockHardnessHandler.loadTags();
		}
	}

	public static void onDatapackReload(MinecraftServer server) {
		BlockHardnessHandler.loadTags();
		BlockRecipesManager.syncToAll(server);
	}

	public static void onDatapackSync(ServerPlayer player) {
		BlockRecipesManager.syncTo(player);
		MunitionPropertiesHandler.syncTo(player);
		AutocannonMaterialPropertiesHandler.syncTo(player);
		BigCannonMaterialPropertiesHandler.syncTo(player);
		BigCannonBreechStrengthHandler.syncTo(player);
	}

	public static void onAddReloadListeners(BiConsumer<PreparableReloadListener, ResourceLocation> cons) {
		cons.accept(BlockRecipeFinder.LISTENER, CreateBigCannons.resource("block_recipe_finder"));
		cons.accept(BlockRecipesManager.ReloadListener.INSTANCE, CreateBigCannons.resource("block_recipe_manager"));
		cons.accept(BlockHardnessHandler.BlockReloadListener.INSTANCE, CreateBigCannons.resource("block_hardness_handler"));
		cons.accept(MunitionPropertiesHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("munition_properties_handler"));
		cons.accept(DimensionMunitionPropertiesHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("dimension_munition_properties_handler"));
		cons.accept(AutocannonMaterialPropertiesHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("autocannon_material_properties_handler"));
		cons.accept(BigCannonMaterialPropertiesHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("big_cannon_material_properties_handler"));
		cons.accept(BigCannonBreechStrengthHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("big_cannon_breech_strength_handler"));
	}

	public static void onAddDeployerRecipes(DeployerBlockEntity deployer, Container container,
		BiConsumer<Supplier<Optional<? extends Recipe<? extends Container>>>, Integer> cons) {
		ItemStack containerItem = container.getItem(0);
		ItemStack deployerItem = container.getItem(1);

		if (CBCBlocks.BIG_CARTRIDGE.isIn(containerItem) && deployerItem.is(CBCTags.CBCItemTags.NITROPOWDER)) {
			int power = BigCartridgeBlockItem.getPower(containerItem);
			if (power < CBCConfigs.SERVER.munitions.maxBigCartridgePower.get()) {
				cons.accept(() -> Optional.of(new BigCartridgeFillingDeployerRecipe(power, power + 1)), 25);
			}
		}
		if (CBCItems.FILLED_AUTOCANNON_CARTRIDGE.isIn(containerItem)
			&& deployerItem.getItem() instanceof AutocannonRoundItem) {
			cons.accept(() -> Optional.of(new CartridgeAssemblyDeployerRecipe(deployerItem)), 25);
		}
		MunitionFuzingDeployerRecipe fuzingRecipe = new MunitionFuzingDeployerRecipe(containerItem, deployerItem);
		if (fuzingRecipe.matches(container, deployer.getLevel())) {
			cons.accept(() -> Optional.of(fuzingRecipe), 25);
		}
		TracerApplicationDeployerRecipe tracerRecipe = new TracerApplicationDeployerRecipe(containerItem, deployerItem);
		if (tracerRecipe.matches(container, deployer.getLevel())) {
			cons.accept(() -> Optional.of(tracerRecipe), 25);
		}
		AutocannonAmmoContainerFillingDeployerRecipe ammoContainerRecipe = new AutocannonAmmoContainerFillingDeployerRecipe(containerItem, deployerItem);
		if (ammoContainerRecipe.matches(container, deployer.getLevel())) {
			cons.accept(() -> Optional.of(ammoContainerRecipe), 25);
		}
	}

}
