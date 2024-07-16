package rbasamoyai.createbigcannons;

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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_hit_effects.BlockImpactTransformationHandler;
import rbasamoyai.createbigcannons.cannon_control.config.CannonMountPropertiesHandler;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_loading.CannonLoaderBlock;
import rbasamoyai.createbigcannons.cannon_loading.CannonLoaderBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterialPropertiesHandler;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.BigCannonBreechStrengthHandler;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterialPropertiesHandler;
import rbasamoyai.createbigcannons.crafting.BlockRecipeFinder;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.FluidCastingTimeHandler;
import rbasamoyai.createbigcannons.crafting.munition_assembly.AutocannonAmmoContainerFillingDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.BigCartridgeFillingDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.CartridgeAssemblyDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.MunitionFuzingDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.munition_assembly.TracerApplicationDeployerRecipe;
import rbasamoyai.createbigcannons.crafting.welding.CannonWelderItem;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilityHandler;
import rbasamoyai.createbigcannons.munitions.config.DimensionMunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.FluidDragHandler;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.createbigcannons.network.CBCRootNetwork;
import rbasamoyai.createbigcannons.network.ClientboundNotifyTagReloadPacket;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

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

		state = ContraptionRemix.getInnerCannonState(level, state, pos, null);
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
			if (level instanceof Level llevel) {
				if (destroyCannonLoader(state, llevel, pos, player)) return true;
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

	public static boolean destroyCannonLoader(BlockState state, Level level, BlockPos pos, Player player) {
		Direction.Axis axis = state.getValue(BlockStateProperties.FACING).getAxis();
		Direction positive = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
		Direction dirFinal = null;

		BlockPos headPos = null;
		BlockState headState = null;
		BlockPos basePos = null;
		int LIMIT = CannonLoaderBlock.maxAllowedLoaderLength();

		for (int mod : new int[]{1, -1}) {
			for (int offs = mod; mod * offs < LIMIT; offs += mod) {
				BlockPos pos1 = pos.relative(positive, offs);
				BlockState currentState = ContraptionRemix.getInnerCannonState(level, level.getBlockState(pos1), pos1, positive);

				if (AllBlocks.PISTON_EXTENSION_POLE.has(currentState) && axis == currentState.getValue(BlockStateProperties.FACING).getAxis()) {
					continue;
				}
				if (CannonLoaderBlock.isLoaderHead(currentState) && axis == currentState.getValue(BlockStateProperties.FACING).getAxis()) {
					headPos = pos1;
					headState = currentState;
				}
				if (CBCBlocks.CANNON_LOADER.has(currentState) && axis == currentState.getValue(BlockStateProperties.FACING).getAxis()) {
					basePos = pos1;
					dirFinal = currentState.getValue(BlockStateProperties.FACING);
				}
				break;
			}
		}
		if (headPos == null || basePos == null) return false;
		final BlockPos baseCopy = basePos.immutable();
		final BlockPos headCopy = headPos.immutable();
		BlockPos.betweenClosedStream(headPos, basePos)
			.filter(p -> !p.equals(pos) && !p.equals(baseCopy))
			.forEach(p -> {
				boolean drop = !player.isCreative() && !p.equals(headCopy);
				if (!ContraptionRemix.removeCannonContentsOnBreak(level, p, drop))
					level.destroyBlock(p, drop);
			});

		if (level.getBlockEntity(basePos) instanceof CannonLoaderBlockEntity loader) loader.onLengthBroken();
		BlockPos aheadPos = basePos.relative(dirFinal);
		boolean cancel = pos.equals(aheadPos);
		if (level.getBlockEntity(aheadPos) instanceof IBigCannonBlockEntity cbe && !cancel) {
			cbe.cannonBehavior().loadBlock(new StructureBlockInfo(BlockPos.ZERO, headState, null));
		} else {
			level.setBlock(aheadPos, headState, 3);
		}
		return cancel;
	}

	public static void onLoadLevel(LevelAccessor level) {
		CreateBigCannons.BLOCK_DAMAGE.levelLoaded(level);
		if (level.getServer() != null && !level.isClientSide() && level.getServer().overworld() == level) {
			loadTags();
		}
	}

	public static void loadTags() {
		BlockArmorPropertiesHandler.loadTags();
		FluidCastingTimeHandler.loadTags();
		BlockImpactTransformationHandler.loadTags();
		FluidDragHandler.loadTags();
	}

	public static void onDatapackReload(MinecraftServer server) {
		loadTags();
		NetworkPlatform.sendToClientAll(new ClientboundNotifyTagReloadPacket(), server);

		BlockArmorPropertiesHandler.syncToAll(server);
		BlockRecipesManager.syncToAll(server);
		MunitionPropertiesHandler.syncToAll(server);
		AutocannonMaterialPropertiesHandler.syncToAll(server);
		BigCannonMaterialPropertiesHandler.syncToAll(server);
		BigCannonBreechStrengthHandler.syncToAll(server);
		FluidCastingTimeHandler.syncToAll(server);
		CannonMountPropertiesHandler.syncToAll(server);
		DimensionMunitionPropertiesHandler.syncToAll(server);
		FluidDragHandler.syncToAll(server);
	}

	public static void onDatapackSync(ServerPlayer player) {
		BlockArmorPropertiesHandler.syncTo(player);
		BlockRecipesManager.syncTo(player);
		MunitionPropertiesHandler.syncTo(player);
		AutocannonMaterialPropertiesHandler.syncTo(player);
		BigCannonMaterialPropertiesHandler.syncTo(player);
		BigCannonBreechStrengthHandler.syncTo(player);
		FluidCastingTimeHandler.syncTo(player);
		BigCannonPropellantCompatibilityHandler.syncTo(player);
		CannonMountPropertiesHandler.syncTo(player);
		DimensionMunitionPropertiesHandler.syncTo(player);
		FluidDragHandler.syncTo(player);
	}

	public static void onAddReloadListeners(BiConsumer<PreparableReloadListener, ResourceLocation> cons) {
		cons.accept(BlockRecipeFinder.LISTENER, CreateBigCannons.resource("block_recipe_finder"));
		cons.accept(BlockRecipesManager.ReloadListener.INSTANCE, CreateBigCannons.resource("block_recipe_manager"));
		cons.accept(BlockArmorPropertiesHandler.BlockReloadListener.INSTANCE, CreateBigCannons.resource("block_hardness_handler"));
		cons.accept(MunitionPropertiesHandler.ReloadListenerProjectiles.INSTANCE, CreateBigCannons.resource("projectile_properties_handler"));
		cons.accept(MunitionPropertiesHandler.ReloadListenerBlockPropellant.INSTANCE, CreateBigCannons.resource("block_propellant_properties_handler"));
		cons.accept(MunitionPropertiesHandler.ReloadListenerItemPropellant.INSTANCE, CreateBigCannons.resource("item_propellant_properties_handler"));
		cons.accept(DimensionMunitionPropertiesHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("dimension_munition_properties_handler"));
		cons.accept(AutocannonMaterialPropertiesHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("autocannon_material_properties_handler"));
		cons.accept(BigCannonMaterialPropertiesHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("big_cannon_material_properties_handler"));
		cons.accept(BigCannonBreechStrengthHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("big_cannon_breech_strength_handler"));
		cons.accept(FluidCastingTimeHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("fluid_casting_time_handler"));
		cons.accept(BigCannonPropellantCompatibilityHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("big_cannon_propellant_compatibility_handler"));
		cons.accept(CannonMountPropertiesHandler.BlockEntityReloadListener.INSTANCE, CreateBigCannons.resource("block_entity_cannon_mounts_config_handler"));
		cons.accept(CannonMountPropertiesHandler.EntityReloadListener.INSTANCE, CreateBigCannons.resource("entity_cannon_mounts_config_handler"));
		cons.accept(BlockImpactTransformationHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("block_impact_transformation_handler"));
		cons.accept(FluidDragHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("fluid_drag_handler"));
	}

	public static void onAddDeployerRecipes(DeployerBlockEntity deployer, Container container,
		BiConsumer<Supplier<Optional<? extends Recipe<? extends Container>>>, Integer> cons) {
		ItemStack containerItem = container.getItem(0);
		ItemStack deployerItem = container.getItem(1);

		if (containerItem.getItem() instanceof BigCartridgeBlockItem cartridge && deployerItem.is(CBCTags.CBCItemTags.NITROPOWDER)) {
			int power = BigCartridgeBlockItem.getPower(containerItem);
			if (power < cartridge.getMaximumPowerLevels()) {
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

	public static InteractionResult onUseItemOnBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof CannonWelderItem) return CannonWelderItem.welderItemAlwaysPlacesWhenUsed(player, level, hand, hitResult);
		return InteractionResult.PASS;
	}

}
