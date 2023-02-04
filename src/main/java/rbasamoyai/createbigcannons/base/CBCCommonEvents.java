package rbasamoyai.createbigcannons.base;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock.PistonState;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlockEntity;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock.BuilderState;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlockEntity;

public class CBCCommonEvents {

	public static void register(IEventBus forgeEventBus) {
		forgeEventBus.addListener(CBCCommonEvents::onPlayerBreakBlock);
		forgeEventBus.addListener(CBCCommonEvents::onPlayerLogin);
		forgeEventBus.addListener(CBCCommonEvents::onPlayerLogout);
		forgeEventBus.addListener(CBCCommonEvents::onLoadWorld);
		forgeEventBus.addListener(CBCCommonEvents::onServerWorldTick);
	}

	public static void onServerWorldTick(TickEvent.WorldTickEvent evt) {
		if (evt.phase == TickEvent.Phase.START) return;
		if (evt.side == LogicalSide.CLIENT) return;
		CreateBigCannons.BLOCK_DAMAGE.tick(evt.world);
	}

	public static void onPlayerBreakBlock(BreakEvent event) {
		BlockState state = event.getState();
		LevelAccessor level = event.getWorld();
		BlockPos pos = event.getPos();
		if (AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
			BlockPos drillPos = destroyPoleContraption(CBCBlocks.CANNON_DRILL_BIT.get(), CBCBlocks.CANNON_DRILL.get(), CannonDrillBlock.maxAllowedDrillLength(), event);
			if (drillPos != null) {
				level.setBlock(drillPos, level.getBlockState(drillPos).setValue(CannonDrillBlock.STATE, PistonState.RETRACTED), 3);
				if (level.getBlockEntity(pos) instanceof CannonDrillBlockEntity drill) {
					drill.onLengthBroken();
				}
				return;
			}
			BlockPos builderPos = destroyPoleContraption(CBCBlocks.CANNON_BUILDER_HEAD.get(), CBCBlocks.CANNON_BUILDER.get(), CannonBuilderBlock.maxAllowedBuilderLength(), event);
			if (builderPos != null) {
				level.setBlock(builderPos, level.getBlockState(builderPos).setValue(CannonBuilderBlock.STATE, BuilderState.UNACTIVATED), 3);
				if (level.getBlockEntity(pos) instanceof CannonBuilderBlockEntity builder) {
					builder.onLengthBroken();
				}
				return;			
			}
		}
	}
	
	private static BlockPos destroyPoleContraption(Block head, Block base, int limit, BreakEvent event) {
		LevelAccessor level = event.getWorld();
		BlockPos pos = event.getPos();
		Direction.Axis axis = event.getState().getValue(BlockStateProperties.FACING).getAxis();
		Direction positive = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
		
		BlockPos headPos = null;
		BlockPos basePos = null;
		
		for (int mod : new int[] { 1, -1 }) {
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
		Player player = event.getPlayer();
		BlockPos baseCopy = basePos.immutable();
		BlockPos.betweenClosedStream(headPos, basePos)
		.filter(p -> !p.equals(pos) && !p.equals(baseCopy))
		.forEach(p -> level.destroyBlock(p, !player.isCreative()));
		return baseCopy;
	}

	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent evt) {
		Player player = evt.getPlayer();
		CreateBigCannons.BLOCK_DAMAGE.playerLogin(player);
	}

	public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent evt) {
		Player player = evt.getPlayer();
		CreateBigCannons.BLOCK_DAMAGE.playerLogout(player);
	}

	public static void onLoadWorld(WorldEvent.Load evt) {
		LevelAccessor level = evt.getWorld();
		CreateBigCannons.BLOCK_DAMAGE.levelLoaded(level);
	}
	
}
