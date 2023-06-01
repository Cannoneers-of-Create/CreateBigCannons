package rbasamoyai.createbigcannons.base;

import java.util.function.BiConsumer;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeFinder;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.boring.AbstractCannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.multiloader.EventsPlatform;
import rbasamoyai.createbigcannons.munitions.config.BlockHardnessHandler;
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

	public static void onPlayerBreakBlock(BlockState state, LevelAccessor level, BlockPos pos, Player player) {
		if (AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
			BlockPos drillPos = destroyPoleContraption(CBCBlocks.CANNON_DRILL_BIT.get(), CBCBlocks.CANNON_DRILL.get(),
				CannonDrillBlock.maxAllowedDrillLength(), state, level, pos, player);
			if (drillPos != null) {
				level.setBlock(drillPos, level.getBlockState(drillPos).setValue(CannonDrillBlock.STATE, MechanicalPistonBlock.PistonState.RETRACTED), 3);
				if (level.getBlockEntity(pos) instanceof AbstractCannonDrillBlockEntity drill) {
					drill.onLengthBroken();
				}
				return;
			}
			BlockPos builderPos = destroyPoleContraption(CBCBlocks.CANNON_BUILDER_HEAD.get(), CBCBlocks.CANNON_BUILDER.get(),
				CannonBuilderBlock.maxAllowedBuilderLength(), state, level, pos, player);
			if (builderPos != null) {
				level.setBlock(builderPos, level.getBlockState(builderPos).setValue(CannonBuilderBlock.STATE, CannonBuilderBlock.BuilderState.UNACTIVATED), 3);
				if (level.getBlockEntity(pos) instanceof CannonBuilderBlockEntity builder) {
					builder.onLengthBroken();
				}
				return;
			}
		}
	}

	public static void onCannonBreakBlock(LevelAccessor level, BlockPos blockPos) {
		EventsPlatform.postOnCannonBreakBlockEvent(EventsPlatform.createOnCannonBreakBlockEvent(blockPos, level.getBlockState(blockPos), level.dimensionType().effectsLocation(), level.getBlockEntity(blockPos)));
		if (!level.isClientSide()) level.destroyBlock(blockPos, false);
	}

	private static BlockPos destroyPoleContraption(Block head, Block base, int limit, BlockState state, LevelAccessor level,
												   BlockPos pos, Player player) {
		Direction.Axis axis = state.getValue(BlockStateProperties.FACING).getAxis();
		Direction positive = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);

		BlockPos headPos = null;
		BlockPos basePos = null;

		for (int mod : new int[]{1, -1}) {
			for (int offs = mod; mod * offs < limit; offs += mod) {
				BlockPos pos1 = pos.relative(positive, offs);
				BlockState state1 = level.getBlockState(pos1);

				if (AllBlocks.PISTON_EXTENSION_POLE.has(state1) && axis == state1.getValue(BlockStateProperties.FACING).getAxis()) {
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
		if (headPos == null || basePos == null) return null;
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
	}

	public static void onAddReloadListeners(BiConsumer<PreparableReloadListener, ResourceLocation> cons) {
		cons.accept(BlockRecipeFinder.LISTENER, CreateBigCannons.resource("block_recipe_finder"));
		cons.accept(BlockRecipesManager.ReloadListener.INSTANCE, CreateBigCannons.resource("block_recipe_manager"));
		cons.accept(BlockHardnessHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("block_hardness_handler"));
		cons.accept(MunitionPropertiesHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("munition_properties_handler"));
	}

}
