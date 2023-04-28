package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.logistics.block.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.logistics.block.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlock;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;

public class QuickfiringBreechPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {

	public QuickfiringBreechPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		super(type, level, pos, state);
	}

	@Override
	protected Vec3 getInteractionPositionVector() {
		return this.level.getBlockEntity(this.pos) instanceof CannonMountBlockEntity mount ? mount.getInteractionLocation() : super.getInteractionPositionVector();
	}

	public ItemStack getInsertedResultAndDoSomething(ItemStack stack, MountedBigCannonContraption cannon,
													 MunitionConsumer loadProjectile, MunitionConsumer loadCartridge) {
		if (!(stack.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof BigCannonMunitionBlock munition))
			return stack;

		Direction pushDirection = cannon.initialOrientation();
		Direction extractDirection = pushDirection.getOpposite();
		BlockPos startPos = cannon.getStartPos();
		BlockPos breechPos = startPos.relative(extractDirection);
		if (!(cannon.presentTileEntities.get(breechPos) instanceof QuickfiringBreechBlockEntity breech) || !breech.canBeAutomaticallyLoaded()) return stack;

		BlockPos nextPos = startPos.relative(pushDirection);
		int barrelLength = 0;
		while (cannon.presentTileEntities.get(nextPos) instanceof IBigCannonBlockEntity cbe2) {
			StructureBlockInfo info = cbe2.cannonBehavior().block();
			if (!info.state.isAir()) return stack;
			nextPos = nextPos.relative(pushDirection);
			++barrelLength;
		}

		BlockEntity be1 = cannon.presentTileEntities.get(startPos);
		if (!(be1 instanceof IBigCannonBlockEntity cbe1)) return stack;
		StructureBlockInfo firstInfo = cbe1.cannonBehavior().block();

		if (munition instanceof ProjectileBlock) {
			if (barrelLength == 0) return stack;
			if (firstInfo.state.getBlock() instanceof BigCartridgeBlock cartridge) {
				if (loadProjectile.test(stack, munition)) breech.setLoadingCooldown(getLoadingCooldown());
				return BigCartridgeBlock.getPowerFromData(firstInfo) == 0 ? cartridge.getExtractedItem(firstInfo) : stack;
			}
			if (!firstInfo.state.isAir()) return stack;
			if (loadProjectile.test(stack, munition)) breech.setLoadingCooldown(getLoadingCooldown());
			ItemStack copy = stack.copy();
			copy.shrink(1);
			return copy;
		}
		if (munition instanceof BigCartridgeBlock) {
			if (!(firstInfo.state.getBlock() instanceof ProjectileBlock)) return stack;
			if (loadCartridge.test(stack, munition)) breech.setLoadingCooldown(getLoadingCooldown());
			ItemStack copy = stack.copy();
			copy.shrink(1);
			return copy;
		}
		return stack;
	}

	public interface MunitionConsumer extends BiPredicate<ItemStack, BigCannonMunitionBlock> {
	}

	public static boolean loadProjectile(ItemStack stack, BigCannonMunitionBlock munition, boolean simulate, AbstractContraptionEntity entity,
									   MountedBigCannonContraption cannon) {
		if (simulate) return false;
		BlockPos startPos = cannon.getStartPos();
		Direction dir = cannon.initialOrientation();
		BlockEntity be = cannon.presentTileEntities.get(startPos);
		IBigCannonBlockEntity cbe = (IBigCannonBlockEntity) be;
		cbe.cannonBehavior().removeBlock();
		cbe.cannonBehavior().tryLoadingBlock(munition.getHandloadingInfo(stack, startPos, dir));
		BigCannonBlock.writeAndSyncSingleBlockData(be, cannon.getBlocks().get(startPos), entity, cannon);
		return true;
	}

	public static boolean loadCartridge(ItemStack stack, BigCannonMunitionBlock munition, boolean simulate, AbstractContraptionEntity entity,
									  MountedBigCannonContraption cannon) {
		if (simulate) return false;
		Direction dir = cannon.initialOrientation();
		BlockPos startPos = cannon.getStartPos();

		Set<BlockPos> changes = new HashSet<>(2);
		changes.add(startPos);

		BlockEntity be = cannon.presentTileEntities.get(startPos);
		IBigCannonBlockEntity cbe = (IBigCannonBlockEntity) be;

		StructureBlockInfo oldInfo = cbe.cannonBehavior().block();
		if (!oldInfo.state.isAir()) {
			BlockPos nextPos = startPos.relative(dir);
			BlockEntity be1 = cannon.presentTileEntities.get(nextPos);
			IBigCannonBlockEntity cbe1 = (IBigCannonBlockEntity) be1;
			cbe1.cannonBehavior().loadBlock(oldInfo);
			cbe.cannonBehavior().removeBlock();
			changes.add(nextPos);
		}
		cbe.cannonBehavior().tryLoadingBlock(munition.getHandloadingInfo(stack, startPos, dir));

		BigCannonBlock.writeAndSyncMultipleBlockData(changes, entity, cannon);
		return true;
	}

	private static int getLoadingCooldown() { return CBCConfigs.SERVER.cannons.quickfiringBreechLoadingCooldown.get(); }

}
