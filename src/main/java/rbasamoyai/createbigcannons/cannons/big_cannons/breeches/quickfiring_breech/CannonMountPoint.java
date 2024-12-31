package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech;

import java.util.HashSet;
import java.util.Set;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

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
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

public class CannonMountPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {

	public CannonMountPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		super(type, level, pos, state);
	}

	@Override
	protected Vec3 getInteractionPositionVector() {
		BlockEntity be = this.getLevel().getBlockEntity(this.pos);
		if (be instanceof CannonMountBlockEntity mount)
			return mount.getInteractionLocation();
		if (be instanceof FixedCannonMountBlockEntity mount)
			return mount.getInteractionLocation();
		return super.getInteractionPositionVector();
	}

	public ItemStack getInsertedResultAndDoSomething(ItemStack stack, boolean simulate, AbstractMountedCannonContraption cannon,
													 PitchOrientedContraptionEntity poce) {
		if (cannon instanceof MountedBigCannonContraption bigCannon) {
			return bigCannonInsert(stack, simulate, bigCannon, poce);
		}
		if (cannon instanceof MountedAutocannonContraption autocannon) {
			return autocannonInsert(stack, simulate, autocannon, poce);
		}
		return stack;
	}

	public static ItemStack bigCannonInsert(ItemStack stack, boolean simulate, MountedBigCannonContraption bigCannon,
											PitchOrientedContraptionEntity poce) {
		if (!(stack.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof BigCannonMunitionBlock munition))
			return stack;

		Direction pushDirection = bigCannon.initialOrientation();
		Direction extractDirection = pushDirection.getOpposite();
		BlockPos startPos = bigCannon.getStartPos();
		BlockPos breechPos = startPos.relative(extractDirection);
		if (!(bigCannon.presentBlockEntities.get(breechPos) instanceof QuickfiringBreechBlockEntity breech) || !breech.canBeAutomaticallyLoaded())
			return stack;

		BlockPos nextPos = startPos.relative(pushDirection);
		int barrelLength = 0;
		while (bigCannon.presentBlockEntities.get(nextPos) instanceof IBigCannonBlockEntity cbe2) {
			StructureBlockInfo info = cbe2.cannonBehavior().block();
			if (!info.state().isAir()) return stack;
			nextPos = nextPos.relative(pushDirection);
			++barrelLength;
		}

		BlockEntity be1 = bigCannon.presentBlockEntities.get(startPos);
		if (!(be1 instanceof IBigCannonBlockEntity cbe1)) return stack;
		StructureBlockInfo firstInfo = cbe1.cannonBehavior().block();

		if (munition instanceof ProjectileBlock) {
			if (barrelLength == 0) return stack;
			if (firstInfo.state().getBlock() instanceof BigCartridgeBlock cartridge) {
				if (!simulate) {
					loadProjectile(stack, munition, poce, bigCannon);
					breech.setLoadingCooldown(getLoadingCooldown());
				}
				if (BigCartridgeBlock.getPowerFromData(firstInfo) == 0) {
					if (simulate) stack.setCount(1);
					return cartridge.getExtractedItem(firstInfo);
				} else {
					return stack;
				}
			}
			if (!firstInfo.state().isAir()) return stack;
			if (!simulate) {
				loadProjectile(stack, munition, poce, bigCannon);
				breech.setLoadingCooldown(getLoadingCooldown());
			}
			ItemStack copy = stack.copy();
			copy.shrink(1);
			return copy;
		}
		if (munition instanceof BigCartridgeBlock) {
			if (BigCartridgeBlockItem.getPower(stack) == 0 || !(firstInfo.state().getBlock() instanceof ProjectileBlock))
				return stack;
			if (!simulate) {
				loadCartridge(stack, munition, poce, bigCannon);
				breech.setLoadingCooldown(getLoadingCooldown());
			}
			ItemStack copy = stack.copy();
			copy.shrink(1);
			return copy;
		}
		return stack;
	}

	public static void loadProjectile(ItemStack stack, BigCannonMunitionBlock munition, AbstractContraptionEntity entity,
									  MountedBigCannonContraption bigCannon) {
		BlockPos startPos = bigCannon.getStartPos();
		Direction dir = bigCannon.initialOrientation();
		BlockEntity be = bigCannon.presentBlockEntities.get(startPos);
		IBigCannonBlockEntity cbe = (IBigCannonBlockEntity) be;
		cbe.cannonBehavior().removeBlock();
		cbe.cannonBehavior().tryLoadingBlock(munition.getHandloadingInfo(stack, startPos, dir));
		BigCannonBlock.writeAndSyncSingleBlockData(be, bigCannon.getBlocks().get(startPos), entity, bigCannon);
	}

	public static void loadCartridge(ItemStack stack, BigCannonMunitionBlock munition, AbstractContraptionEntity entity,
									 MountedBigCannonContraption cannon) {
		Direction dir = cannon.initialOrientation();
		BlockPos startPos = cannon.getStartPos();

		Set<BlockPos> changes = new HashSet<>(2);
		changes.add(startPos);

		BlockEntity be = cannon.presentBlockEntities.get(startPos);
		IBigCannonBlockEntity cbe = (IBigCannonBlockEntity) be;

		StructureBlockInfo oldInfo = cbe.cannonBehavior().block();
		if (!oldInfo.state().isAir()) {
			BlockPos nextPos = startPos.relative(dir);
			BlockEntity be1 = cannon.presentBlockEntities.get(nextPos);
			IBigCannonBlockEntity cbe1 = (IBigCannonBlockEntity) be1;
			cbe1.cannonBehavior().loadBlock(oldInfo);
			cbe.cannonBehavior().removeBlock();
			changes.add(nextPos);
		}
		cbe.cannonBehavior().tryLoadingBlock(munition.getHandloadingInfo(stack, startPos, dir));

		BigCannonBlock.writeAndSyncMultipleBlockData(changes, entity, cannon);
	}

	public static ItemStack autocannonInsert(ItemStack stack, boolean simulate, MountedAutocannonContraption autocannon,
											 PitchOrientedContraptionEntity poce) {
		if (!(stack.getItem() instanceof AutocannonAmmoContainerItem)) return stack;
		BlockEntity be = autocannon.presentBlockEntities.get(autocannon.getStartPos());
		if (!(be instanceof AbstractAutocannonBreechBlockEntity breech)) return stack;

		ItemStack oldContainer = breech.getMagazine();
		if (oldContainer.getItem() instanceof AutocannonAmmoContainerItem
			&& AutocannonAmmoContainerItem.getTotalAmmoCount(oldContainer) > 0) return stack;

		if (simulate) return ItemStack.EMPTY;
		breech.setMagazine(stack);
		return oldContainer.isEmpty() ? ItemStack.EMPTY : oldContainer.copy();
	}

	private static int getLoadingCooldown() {
		return CBCConfigs.SERVER.cannons.quickfiringBreechLoadingCooldown.get();
	}

}
