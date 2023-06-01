package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBaseBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.manualloading.HandloadingTool;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

public class QuickfiringBreechBlock extends BigCannonBaseBlock implements IBE<QuickfiringBreechBlockEntity>, ITransformableBlock, IWrenchable {

	public static final BooleanProperty AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

	private final NonNullSupplier<? extends Block> slidingConversion;

	public QuickfiringBreechBlock(Properties properties, BigCannonMaterial material, NonNullSupplier<? extends Block> slidingConversion) {
		super(properties, material);
		this.slidingConversion = slidingConversion;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AXIS);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(AXIS, context.getNearestLookingDirection().getAxis() == Direction.Axis.Z);
	}

	@Override
	public Class<QuickfiringBreechBlockEntity> getBlockEntityClass() {
		return QuickfiringBreechBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends QuickfiringBreechBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.QUICKFIRING_BREECH.get();
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.SLIDING_BREECH;
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public BigCannonEnd getOpeningType(@Nullable Level level, BlockState state, BlockPos pos) {
		return BigCannonEnd.CLOSED;
	}

	@Override
	public <T extends BlockEntity & IBigCannonBlockEntity> boolean onInteractWhileAssembled(Player player, BlockPos localPos,
																							Direction side, InteractionHand interactionHand, Level level, MountedBigCannonContraption cannon, T be,
																							StructureBlockInfo info, AbstractContraptionEntity entity) {
		if (!(be instanceof QuickfiringBreechBlockEntity breech)) return false;
		ItemStack stack = player.getItemInHand(interactionHand);

		Direction pushDirection = side.getOpposite();
		BlockPos nextPos = localPos.relative(pushDirection);

		if (stack.isEmpty()) {
			if (level instanceof ServerLevel slevel) {
				if (!breech.onInteractionCooldown()) {
					SoundEvent sound = breech.getOpenProgress() == 0 ? SoundEvents.IRON_TRAPDOOR_OPEN : SoundEvents.IRON_TRAPDOOR_CLOSE;
					level.playSound(null, player.blockPosition(), sound, SoundSource.BLOCKS, 1.0f, 1.5f);
				}
				breech.toggleOpening();
				Set<BlockPos> changed = new HashSet<>(2);
				changed.add(localPos);

				if (breech.getOpenDirection() > 0) {
					BlockEntity be1 = cannon.presentBlockEntities.get(nextPos);
					if (be1 instanceof IBigCannonBlockEntity cbe1) {
						StructureBlockInfo info1 = cbe1.cannonBehavior().block();
						ItemStack extract = info1.state.getBlock() instanceof BigCannonMunitionBlock munition ? munition.getExtractedItem(info1) : ItemStack.EMPTY;
						if (!player.addItem(extract) && !player.isCreative()) {
							ItemEntity item = player.drop(extract, false);
							if (item != null) {
								item.setNoPickUpDelay();
								item.setOwner(player.getUUID());
							}
						}
						cbe1.cannonBehavior().removeBlock();
						changed.add(nextPos);
						if (cannon.hasFired) {
							Vec3 normal = new Vec3(side.step());
							Vec3 smokePos = Vec3.atCenterOf(localPos).add(normal.scale(0.6));
							Vec3 globalPos = entity.toGlobalVector(smokePos, 1);

							slevel.sendParticles(ParticleTypes.POOF, globalPos.x, globalPos.y, globalPos.z, 10, 0.1, 0.1, 0.1, 0.01);

							cannon.hasFired = false;
						}
					}
				}
				BigCannonBlock.writeAndSyncMultipleBlockData(changed, entity, cannon);
			}
			return true;
		}
		if (!breech.isOpen() || breech.onInteractionCooldown()) return false;

		if (Block.byItem(stack.getItem()) instanceof BigCannonMunitionBlock munition) {
			BlockEntity be1 = cannon.presentBlockEntities.get(nextPos);
			if (!(be1 instanceof IBigCannonBlockEntity cbe1)) return false;

			StructureBlockInfo loadInfo = munition.getHandloadingInfo(stack, nextPos, pushDirection);
			StructureBlockInfo info1 = cbe1.cannonBehavior().block();

			if (!level.isClientSide) {
				Set<BlockPos> changes = new HashSet<>(2);

				if (!info1.state.isAir()) {
					BlockPos posAfter = nextPos.relative(pushDirection);
					BlockEntity be2 = cannon.presentBlockEntities.get(posAfter);
					if (!(be2 instanceof IBigCannonBlockEntity cbe2) || !cbe2.cannonBehavior().canLoadBlock(info1))
						return false;
					cbe2.cannonBehavior().loadBlock(info1);
					cbe1.cannonBehavior().removeBlock();
					changes.add(posAfter);
				}
				cbe1.cannonBehavior().tryLoadingBlock(loadInfo);
				changes.add(nextPos);

				BigCannonBlock.writeAndSyncMultipleBlockData(changes, entity, cannon);

				SoundType sound = loadInfo.state.getSoundType();
				level.playSound(null, player.blockPosition(), sound.getPlaceSound(), SoundSource.BLOCKS, sound.getVolume(), sound.getPitch());
				if (!player.isCreative()) stack.shrink(1);
			}
			return true;
		}
		if (stack.getItem() instanceof HandloadingTool tool && !player.getCooldowns().isOnCooldown(stack.getItem())) {
			tool.onUseOnCannon(player, level, nextPos, side, cannon);
			return true;
		}

		return false;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		if (rot.ordinal() % 2 == 1) state = state.cycle(AXIS);
		return super.rotate(state, rot);
	}

	@Override
	public BlockState transform(BlockState state, StructureTransform transform) {
		if (transform.mirror != null) {
			state = mirror(state, transform.mirror);
		}

		if (transform.rotationAxis == Direction.Axis.Y) {
			return rotate(state, transform.rotation);
		}

		Direction newFacing = transform.rotateFacing(state.getValue(FACING));
		if (transform.rotationAxis == newFacing.getAxis() && transform.rotation.ordinal() % 2 == 1) {
			state = state.cycle(AXIS);
		}
		return state.setValue(FACING, newFacing);
	}

	protected BlockState getConversion(BlockState old) {
		return this.slidingConversion.get().defaultBlockState()
			.setValue(FACING, old.getValue(FACING))
			.setValue(AXIS, old.getValue(AXIS));
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		if (state.getBlock() instanceof QuickfiringBreechBlock qfb) {
			Player player = context.getPlayer();
			BlockState newState = qfb.getConversion(state);
			BlockEntity oldBe = level.getBlockEntity(pos);
			if (!(oldBe instanceof IBigCannonBlockEntity cbe)) return InteractionResult.PASS;
			if (!level.isClientSide) {
				level.setBlock(pos, newState, 11);
				BlockEntity newBe = level.getBlockEntity(pos);

				StructureBlockInfo loaded = cbe.cannonBehavior().block();
				if (player != null) {
					if (loaded != null) {
						Block block = loaded.state.getBlock();
						ItemStack resultStack = block instanceof BigCannonMunitionBlock munition ? munition.getExtractedItem(loaded) : new ItemStack(block);
						if (!player.addItem(resultStack) && !player.isCreative()) {
							ItemEntity item = player.drop(resultStack, false);
							if (item != null) {
								item.setNoPickUpDelay();
								item.setOwner(player.getUUID());
							}
						}
					}
					ItemStack resultStack = CBCItems.QUICKFIRING_MECHANISM.asStack();
					if (!player.addItem(resultStack) && !player.isCreative()) {
						ItemEntity item = player.drop(resultStack, false);
						if (item != null) {
							item.setNoPickUpDelay();
							item.setOwner(player.getUUID());
						}
					}
				}

				if (newBe instanceof IBigCannonBlockEntity cbe1) {
					for (Direction dir : Iterate.directions) {
						boolean flag = cbe.cannonBehavior().isConnectedTo(dir);
						cbe1.cannonBehavior().setConnectedFace(dir, flag);
						if (level.getBlockEntity(pos.relative(dir)) instanceof IBigCannonBlockEntity cbe2) {
							cbe2.cannonBehavior().setConnectedFace(dir.getOpposite(), flag);
						}
					}
					newBe.setChanged();
				}
				this.playRemoveSound(level, pos);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		return InteractionResult.PASS;
	}

}
