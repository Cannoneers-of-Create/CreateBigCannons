package rbasamoyai.createbigcannons.cannons.big_cannons;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredBigCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.welding.WeldableBlock;
import rbasamoyai.createbigcannons.manualloading.HandloadingTool;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.network.ClientboundUpdateContraptionPacket;

public interface BigCannonBlock extends WeldableBlock {

	BigCannonMaterial getCannonMaterial();

	CannonCastShape getCannonShape();

	Direction getFacing(BlockState state);

	default BigCannonEnd getOpeningType(@Nullable Level level, BlockState state, BlockPos pos) {
		return this.getDefaultOpeningType();
	}

	default BigCannonEnd getOpeningType(MountedBigCannonContraption contraption, BlockState state, BlockPos pos) {
		return this.getDefaultOpeningType();
	}

	BigCannonEnd getDefaultOpeningType();

	boolean isComplete(BlockState state);

	default BigCannonMaterial getCannonMaterialInLevel(LevelAccessor level, BlockState state, BlockPos pos) {
		return this.getCannonMaterial();
	}

	default CannonCastShape getShapeInLevel(LevelAccessor level, BlockState state, BlockPos pos) {
		return this.getCannonShape();
	}

	default boolean canConnectToSide(BlockState state, Direction dir) { return this.getFacing(state).getAxis() == dir.getAxis(); }

	default boolean isImmovable(BlockState state) {
		return false;
	}

	default void onRemoveCannon(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.is(newState.getBlock())) return;
		Direction facing = this.getFacing(state);
		Direction opposite = facing.getOpposite();
		BigCannonMaterial material = this.getCannonMaterial();

		BlockPos pos1 = pos.relative(facing);
		BlockState state1 = level.getBlockState(pos1);
		BlockEntity be1 = level.getBlockEntity(pos1);

		if (this.canConnectToSide(state, facing)
			&& state1.getBlock() instanceof BigCannonBlock cBlock1
			&& cBlock1.getCannonMaterialInLevel(level, state1, pos1) == material
			&& be1 instanceof IBigCannonBlockEntity cbe1
			&& cBlock1.canConnectToSide(state1, opposite)) {
			cbe1.cannonBehavior().setConnectedFace(opposite, false);
			if (cbe1 instanceof LayeredBigCannonBlockEntity layered) {
				for (CannonCastShape layer : layered.getLayers().keySet()) {
					layered.setLayerConnectedTo(opposite, layer, false);
				}
			}
			be1.setChanged();
		}
		BlockPos pos2 = pos.relative(opposite);
		BlockState state2 = level.getBlockState(pos2);
		BlockEntity be2 = level.getBlockEntity(pos2);

		if (this.canConnectToSide(state, opposite)
			&& state2.getBlock() instanceof BigCannonBlock cBlock2
			&& cBlock2.getCannonMaterialInLevel(level, state2, pos2) == material
			&& be2 instanceof IBigCannonBlockEntity cbe2
			&& cBlock2.canConnectToSide(state2, facing)) {
			cbe2.cannonBehavior().setConnectedFace(facing, false);
			if (cbe2 instanceof LayeredBigCannonBlockEntity layered) {
				for (CannonCastShape layer : layered.getLayers().keySet()) {
					layered.setLayerConnectedTo(facing, layer, false);
				}
			}
			be2.setChanged();
		}
	}

	static void onPlace(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() instanceof BigCannonBlock cBlock) {
			Direction facing = cBlock.getFacing(state);
			Direction opposite = facing.getOpposite();
			Vec3 center = Vec3.atCenterOf(pos);
			Vec3 offset = Vec3.atBottomCenterOf(facing.getNormal()).scale(0.5d);
			BigCannonMaterial material = cBlock.getCannonMaterial();
			CannonCastShape shape = cBlock.getCannonShape();

			if (level.getBlockEntity(pos) instanceof LayeredBigCannonBlockEntity layered) {
				layered.setBaseMaterial(material);
				layered.setLayer(shape, state.getBlock());
			}

			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof IBigCannonBlockEntity cbe) {
				BlockPos pos1 = pos.relative(facing);
				BlockState state1 = level.getBlockState(pos1);
				BlockEntity be1 = level.getBlockEntity(pos1);

				if (cBlock.canConnectToSide(state, facing)
					&& state1.getBlock() instanceof BigCannonBlock cBlock1
					&& cBlock1.getCannonMaterialInLevel(level, state1, pos1) == material
					&& level.getBlockEntity(pos1) instanceof IBigCannonBlockEntity cbe1
					&& cBlock1.canConnectToSide(state1, opposite)) {
					cbe.cannonBehavior().setConnectedFace(facing, true);
					cbe1.cannonBehavior().setConnectedFace(opposite, true);

					if (cbe instanceof LayeredBigCannonBlockEntity layered && cbe1 instanceof LayeredBigCannonBlockEntity layered1) {
						for (CannonCastShape layer : layered.getLayers().keySet()) {
							layered.setLayerConnectedTo(facing, layer, true);
							layered1.setLayerConnectedTo(opposite, layer, true);
						}
					} else if (cbe instanceof LayeredBigCannonBlockEntity layered) {
						CannonCastShape shape1 = cBlock1.getCannonShape();
						for (CannonCastShape layer : layered.getLayers().keySet()) {
							if (layer.diameter() > shape1.diameter()) continue;
							layered.setLayerConnectedTo(facing, layer, true);
						}
					} else if (cbe1 instanceof LayeredBigCannonBlockEntity layered) {
						CannonCastShape shape1 = cBlock.getCannonShape();
						for (CannonCastShape layer : layered.getLayers().keySet()) {
							if (layer.diameter() > shape1.diameter()) continue;
							layered.setLayerConnectedTo(opposite, layer, true);
						}
					}
					be1.setChanged();
					if (level instanceof ServerLevel slevel) {
						Vec3 particlePos = center.add(offset);
						slevel.sendParticles(ParticleTypes.CRIT, particlePos.x, particlePos.y, particlePos.z, 10, 0.5d, 0.5d, 0.5d, 0.1d);
					}
				}

				BlockPos pos2 = pos.relative(opposite);
				BlockState state2 = level.getBlockState(pos2);
				BlockEntity be2 = level.getBlockEntity(pos2);

				if (cBlock.canConnectToSide(state, opposite)
					&& state2.getBlock() instanceof BigCannonBlock cBlock2
					&& cBlock2.getCannonMaterialInLevel(level, state2, pos2) == material
					&& level.getBlockEntity(pos2) instanceof IBigCannonBlockEntity cbe2
					&& cBlock2.canConnectToSide(state2, facing)) {
					cbe.cannonBehavior().setConnectedFace(opposite, true);
					cbe2.cannonBehavior().setConnectedFace(facing, true);

					if (cbe instanceof LayeredBigCannonBlockEntity layered && cbe2 instanceof LayeredBigCannonBlockEntity layered1) {
						for (CannonCastShape layer : layered.getLayers().keySet()) {
							layered.setLayerConnectedTo(opposite, layer, true);
							layered1.setLayerConnectedTo(facing, layer, true);
						}
					} else if (cbe instanceof LayeredBigCannonBlockEntity layered) {
						CannonCastShape shape1 = cBlock2.getCannonShape();
						for (CannonCastShape layer : layered.getLayers().keySet()) {
							if (layer.diameter() > shape1.diameter()) continue;
							layered.setLayerConnectedTo(opposite, layer, true);
						}
					} else if (cbe2 instanceof LayeredBigCannonBlockEntity layered) {
						CannonCastShape shape1 = cBlock.getCannonShape();
						for (CannonCastShape layer : layered.getLayers().keySet()) {
							if (layer.diameter() > shape1.diameter()) continue;
							layered.setLayerConnectedTo(facing, layer, true);
						}
					}
					be2.setChanged();
					if (level instanceof ServerLevel slevel) {
						Vec3 particlePos = center.add(offset.reverse());
						slevel.sendParticles(ParticleTypes.CRIT, particlePos.x, particlePos.y, particlePos.z, 10, 0.5d, 0.5d, 0.5d, 0.1d);
					}
				}
				be.setChanged();
			}
		}
	}

	default <T extends BlockEntity & IBigCannonBlockEntity> boolean onInteractWhileAssembled(Player player, BlockPos localPos,
			Direction side, InteractionHand interactionHand, Level level, MountedBigCannonContraption cannon, T be,
			StructureBlockInfo info, AbstractContraptionEntity entity) {
		boolean flag = ((BigCannonBlock) info.state.getBlock()).getFacing(info.state).getAxis() == side.getAxis()
			&& !be.cannonBehavior().isConnectedTo(side);

		ItemStack stack = player.getItemInHand(interactionHand);
		if (flag && Block.byItem(stack.getItem()) instanceof BigCannonMunitionBlock munition) {
			StructureBlockInfo loadInfo = munition.getHandloadingInfo(stack, localPos, side);
			if (!level.isClientSide && be.cannonBehavior().tryLoadingBlock(loadInfo)) {
				writeAndSyncSingleBlockData(be, info, entity, cannon);

				SoundType sound = loadInfo.state.getSoundType();
				level.playSound(null, player.blockPosition(), sound.getPlaceSound(), SoundSource.BLOCKS, sound.getVolume(), sound.getPitch());
				if (!player.isCreative()) stack.shrink(1);
			}
			return true;
		}
		if (flag && stack.getItem() instanceof HandloadingTool tool && !player.getCooldowns().isOnCooldown(stack.getItem())) {
			tool.onUseOnCannon(player, level, localPos, side, cannon);
			return true;
		}
		return false;
	}

	static void writeAndSyncSingleBlockData(BlockEntity be, StructureBlockInfo oldInfo, AbstractContraptionEntity entity, Contraption contraption) {
		CompoundTag tag = be.saveWithFullMetadata();
		tag.remove("x");
		tag.remove("y");
		tag.remove("z");
		StructureBlockInfo newInfo = new StructureBlockInfo(oldInfo.pos, oldInfo.state, tag);
		contraption.getBlocks().put(oldInfo.pos, newInfo);
		NetworkPlatform.sendToClientTracking(new ClientboundUpdateContraptionPacket(entity, oldInfo.pos, newInfo), entity);
	}

	static void writeAndSyncMultipleBlockData(Set<BlockPos> changed, AbstractContraptionEntity entity, Contraption contraption) {
		Map<BlockPos, StructureBlockInfo> changes = new HashMap<>(changed.size());
		Map<BlockPos, StructureBlockInfo> blocks = contraption.getBlocks();
		for (BlockPos pos : changed) {
			StructureBlockInfo oldInfo = blocks.get(pos);
			CompoundTag tag = null;
			BlockEntity be = contraption.presentBlockEntities.get(pos);
			if (be != null) {
				tag = be.saveWithFullMetadata();
				tag.remove("x");
				tag.remove("y");
				tag.remove("z");
			}
			StructureBlockInfo newInfo = new StructureBlockInfo(oldInfo.pos, oldInfo.state, tag);
			changes.put(pos, newInfo);
		}
		blocks.putAll(changes);
		NetworkPlatform.sendToClientTracking(new ClientboundUpdateContraptionPacket(entity, changes), entity);
	}

	@Override default boolean isWeldable(BlockState state) { return this.getCannonMaterial().properties().isWeldable(); }
	@Override default int weldDamage() { return this.getCannonMaterial().properties().weldDamage(); }

	@Override
	default boolean canWeldSide(Level level, Direction dir, BlockState state, BlockState otherState, BlockPos pos) {
		return otherState.getBlock() instanceof BigCannonBlock cblock
			&& cblock.getCannonMaterial() == this.getCannonMaterial()
			&& this.isWeldable(state)
			&& cblock.isWeldable(otherState)
			&& this.canConnectToSide(state, dir)
			&& cblock.canConnectToSide(otherState, dir.getOpposite())
			&& level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe
			&& level.getBlockEntity(pos.relative(dir)) instanceof IBigCannonBlockEntity cbe1
			&& (!cbe.cannonBehavior().isConnectedTo(dir) || !cbe1.cannonBehavior().isConnectedTo(dir.getOpposite()));
	}

	@Override
	default void weldBlock(Level level, BlockState state, BlockPos pos, Direction dir) {
		if (!(level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe)) return;
		BigCannonBehavior behavior = cbe.cannonBehavior();
		behavior.setConnectedFace(dir, true);
		behavior.setWelded(dir, true);
		behavior.blockEntity.notifyUpdate();
	}

}
