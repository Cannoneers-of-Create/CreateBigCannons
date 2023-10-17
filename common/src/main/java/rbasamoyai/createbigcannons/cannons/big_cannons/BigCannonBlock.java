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
import rbasamoyai.createbigcannons.manualloading.HandloadingTool;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.network.ClientboundUpdateContraptionPacket;

public interface BigCannonBlock {

	BigCannonMaterial getCannonMaterial();

	CannonCastShape getCannonShape();

	Direction getFacing(BlockState state);

	BigCannonEnd getOpeningType(@Nullable Level level, BlockState state, BlockPos pos);

	default BigCannonEnd getOpeningType(MountedBigCannonContraption contraption, BlockState state, BlockPos pos) {
		return this.getOpeningType((Level) null, state, pos);
	}

	boolean isComplete(BlockState state);

	default BigCannonMaterial getCannonMaterialInLevel(LevelAccessor level, BlockState state, BlockPos pos) {
		return this.getCannonMaterial();
	}

	default CannonCastShape getShapeInLevel(LevelAccessor level, BlockState state, BlockPos pos) {
		return this.getCannonShape();
	}

	default boolean isDoubleSidedCannon(BlockState state) {
		return true;
	}

	default boolean isImmovable(BlockState state) {
		return false;
	}

	default void onRemoveCannon(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!(state.getBlock() instanceof BigCannonBlock cBlock) || state.is(newState.getBlock())) return;
		Direction facing = cBlock.getFacing(state);
		BigCannonMaterial material = cBlock.getCannonMaterial();

		BlockPos pos1 = pos.relative(facing);
		BlockState state1 = level.getBlockState(pos1);
		BlockEntity be1 = level.getBlockEntity(pos1);

		if (state1.getBlock() instanceof BigCannonBlock cBlock1
			&& cBlock1.getCannonMaterialInLevel(level, state1, pos1) == material
			&& be1 instanceof IBigCannonBlockEntity cbe1) {
			Direction facing1 = cBlock1.getFacing(state1);
			if (facing == facing1.getOpposite() || cBlock1.isDoubleSidedCannon(state1) && facing.getAxis() == facing1.getAxis()) {
				Direction opposite = facing.getOpposite();
				cbe1.cannonBehavior().setConnectedFace(opposite, false);
				if (cbe1 instanceof LayeredBigCannonBlockEntity layered) {
					for (CannonCastShape layer : layered.getLayers().keySet()) {
						layered.setLayerConnectedTo(opposite, layer, false);
					}
				}
				be1.setChanged();
			}
		}
		BlockPos pos2 = pos.relative(facing.getOpposite());
		BlockState state2 = level.getBlockState(pos2);
		BlockEntity be2 = level.getBlockEntity(pos2);

		if (cBlock.isDoubleSidedCannon(state)
			&& state2.getBlock() instanceof BigCannonBlock cBlock2
			&& cBlock2.getCannonMaterialInLevel(level, state2, pos2) == material
			&& be2 instanceof IBigCannonBlockEntity cbe2) {
			Direction facing2 = cBlock2.getFacing(state2);
			if (facing == facing2 || cBlock2.isDoubleSidedCannon(state2) && facing.getAxis() == facing2.getAxis()) {
				cbe2.cannonBehavior().setConnectedFace(facing, false);
				if (cbe2 instanceof LayeredBigCannonBlockEntity layered) {
					for (CannonCastShape layer : layered.getLayers().keySet()) {
						layered.setLayerConnectedTo(facing, layer, false);
					}
				}
				be2.setChanged();
			}
		}
	}

	static void onPlace(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		if (state.getBlock() instanceof BigCannonBlock cBlock) {
			Direction facing = cBlock.getFacing(state);
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

				if (state1.getBlock() instanceof BigCannonBlock cBlock1
					&& cBlock1.getCannonMaterialInLevel(level, state1, pos1) == material
					&& level.getBlockEntity(pos1) instanceof IBigCannonBlockEntity cbe1) {
					Direction facing1 = cBlock1.getFacing(state1);
					if (facing == facing1.getOpposite() || cBlock1.isDoubleSidedCannon(state1) && facing.getAxis() == facing1.getAxis()) {
						cbe.cannonBehavior().setConnectedFace(facing, true);
						cbe1.cannonBehavior().setConnectedFace(facing.getOpposite(), true);

						if (cbe instanceof LayeredBigCannonBlockEntity layered && cbe1 instanceof LayeredBigCannonBlockEntity layered1) {
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								layered.setLayerConnectedTo(facing, layer, true);
								layered1.setLayerConnectedTo(facing.getOpposite(), layer, true);
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
								layered.setLayerConnectedTo(facing.getOpposite(), layer, true);
							}
						}

						be1.setChanged();

						if (level instanceof ServerLevel slevel) {
							Vec3 particlePos = center.add(offset);
							slevel.sendParticles(ParticleTypes.CRIT, particlePos.x, particlePos.y, particlePos.z, 10, 0.5d, 0.5d, 0.5d, 0.1d);
						}
					}
				}

				BlockPos pos2 = pos.relative(facing.getOpposite());
				BlockState state2 = level.getBlockState(pos2);
				BlockEntity be2 = level.getBlockEntity(pos2);

				if (cBlock.isDoubleSidedCannon(state)
					&& state2.getBlock() instanceof BigCannonBlock cBlock2
					&& cBlock2.getCannonMaterialInLevel(level, state2, pos2) == material
					&& level.getBlockEntity(pos2) instanceof IBigCannonBlockEntity cbe2) {
					Direction facing2 = cBlock2.getFacing(state2);
					if (facing == facing2 || cBlock2.isDoubleSidedCannon(state2) && facing.getAxis() == facing2.getAxis()) {
						cbe.cannonBehavior().setConnectedFace(facing.getOpposite(), true);
						cbe2.cannonBehavior().setConnectedFace(facing, true);

						if (cbe instanceof LayeredBigCannonBlockEntity layered && cbe2 instanceof LayeredBigCannonBlockEntity layered1) {
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								layered.setLayerConnectedTo(facing.getOpposite(), layer, true);
								layered1.setLayerConnectedTo(facing, layer, true);
							}
						} else if (cbe instanceof LayeredBigCannonBlockEntity layered) {
							CannonCastShape shape1 = cBlock2.getCannonShape();
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								if (layer.diameter() > shape1.diameter()) continue;
								layered.setLayerConnectedTo(facing.getOpposite(), layer, true);
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

}
