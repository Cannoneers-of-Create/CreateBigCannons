package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.ITransformableBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.StructureTransform;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.manualloading.HandloadingTool;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class QuickfiringBreechBlock extends BigCannonBaseBlock implements ITE<QuickfiringBreechBlockEntity>, ITransformableBlock {

	public static final BooleanProperty AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

	public QuickfiringBreechBlock(Properties properties, BigCannonMaterial material) {
		super(properties, material);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AXIS);
	}

	@Override public Class<QuickfiringBreechBlockEntity> getTileEntityClass() { return QuickfiringBreechBlockEntity.class; }
	@Override public BlockEntityType<? extends QuickfiringBreechBlockEntity> getTileEntityType() { return CBCBlockEntities.QUICKFIRING_BREECH.get(); }

	@Override public CannonCastShape getCannonShape() { return CannonCastShape.SLIDING_BREECH; }

	@Override public boolean isComplete(BlockState state) { return true; }
	@Override public BigCannonEnd getOpeningType(@Nullable Level level, BlockState state, BlockPos pos) { return BigCannonEnd.CLOSED; }

	@Override
	public <T extends BlockEntity & IBigCannonBlockEntity> boolean onInteractWhileAssembled(Player player, BlockPos localPos,
			Direction side, InteractionHand interactionHand, Level level, MountedBigCannonContraption cannon, T be,
			StructureBlockInfo info, AbstractContraptionEntity entity) {
		if (!(be instanceof QuickfiringBreechBlockEntity breech)) return false;
		ItemStack stack = player.getItemInHand(interactionHand);

		Direction pushDirection = side.getOpposite();
		BlockPos nextPos = localPos.relative(pushDirection);

		if (stack.isEmpty()) {
			if (!level.isClientSide) {
				if (!breech.onCooldown()) {
					SoundEvent sound = breech.getOpenProgress() == 0 ? SoundEvents.IRON_TRAPDOOR_OPEN : SoundEvents.IRON_TRAPDOOR_CLOSE;
					level.playSound(null, player.blockPosition(), sound, SoundSource.BLOCKS, 1.0f, 1.0f);
				}
				breech.toggleOpening();
				Set<BlockPos> changed = new HashSet<>(2);
				changed.add(localPos);

				if (breech.getOpenDirection() > 0) {
					BlockEntity be1 = cannon.presentTileEntities.get(nextPos);
					if (be1 instanceof IBigCannonBlockEntity cbe1) {
						StructureBlockInfo info1 = cbe1.cannonBehavior().block();
						ItemStack extract = info1.state.getBlock() instanceof BigCannonMunitionBlock munition ? munition.getExtractedItem(info1) : ItemStack.EMPTY;
						if (!extract.isEmpty()) player.addItem(extract);
						cbe1.cannonBehavior().removeBlock();
						changed.add(nextPos);
					}
				}
				BigCannonBlock.writeAndSyncMultipleBlockData(changed, entity, cannon);
			}
			return true;
		}
		if (!breech.isOpen() || breech.onCooldown()) return false;

		if (Block.byItem(stack.getItem()) instanceof BigCannonMunitionBlock munition) {
			BlockEntity be1 = cannon.presentTileEntities.get(nextPos);
			if (!(be1 instanceof IBigCannonBlockEntity cbe1)) return false;

			StructureBlockInfo loadInfo = munition.getHandloadingInfo(stack, nextPos, pushDirection);
			StructureBlockInfo info1 = cbe1.cannonBehavior().block();

			if (!level.isClientSide) {
				Set<BlockPos> changes = new HashSet<>(2);

				if (info1 != null && !info1.state.isAir()) {
					BlockPos posAfter = nextPos.relative(pushDirection);
					BlockEntity be2 = cannon.presentTileEntities.get(posAfter);
					if (!(be2 instanceof IBigCannonBlockEntity cbe2) || !cbe2.cannonBehavior().canLoadBlock(info1)) return false;
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

}
