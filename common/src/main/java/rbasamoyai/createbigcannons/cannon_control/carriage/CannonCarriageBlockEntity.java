package rbasamoyai.createbigcannons.cannon_control.carriage;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;

public class CannonCarriageBlockEntity extends SyncedBlockEntity {

	public CannonCarriageBlockEntity(BlockEntityType<? extends CannonCarriageBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void tryAssemble() {
		Direction facing = this.getBlockState().getValue(CannonCarriageBlock.FACING);
		CannonCarriageEntity carriage = CBCEntityTypes.CANNON_CARRIAGE.create(this.getLevel());
		carriage.setPos(Vec3.atBottomCenterOf(this.getBlockPos()).add(0, 0.125, 0));
		carriage.setYRot(facing.toYRot());
		carriage.setCannonRider(this.getBlockState().getValue(CannonCarriageBlock.SADDLED));
		this.getLevel().addFreshEntity(carriage);

		try {
			this.assemble(carriage);
		} catch (AssemblyException e) {

		}

		AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(this.getLevel(), this.worldPosition);

		this.setRemoved();
		this.getLevel().removeBlock(this.worldPosition, false);
	}

	protected void assemble(CannonCarriageEntity carriage) throws AssemblyException {
		if (!CBCBlocks.CANNON_CARRIAGE.has(this.getBlockState())) return;
		BlockPos assemblyPos = this.worldPosition.above();
		if (this.getLevel().isOutsideBuildHeight(assemblyPos)) {
			throw CannonMountBlockEntity.cannonBlockOutsideOfWorld(assemblyPos);
		}

		Direction facing = this.getBlockState().getValue(CannonCarriageBlock.FACING);

		AbstractMountedCannonContraption mountedCannon = this.getContraption(assemblyPos);
		if (mountedCannon != null && mountedCannon.assemble(this.getLevel(), assemblyPos)) {
			if (mountedCannon.initialOrientation() == facing) {
				mountedCannon.removeBlocksFromWorld(this.getLevel(), BlockPos.ZERO);
				PitchOrientedContraptionEntity contraptionEntity = PitchOrientedContraptionEntity.create(this.getLevel(), mountedCannon, facing, false);
				carriage.attach(contraptionEntity);
				carriage.applyRotation();
				this.getLevel().addFreshEntity(contraptionEntity);
			}
		}
	}

	private AbstractMountedCannonContraption getContraption(BlockPos pos) {
		Block block = this.getLevel().getBlockState(pos).getBlock();
		if (block instanceof BigCannonBlock) return new MountedBigCannonContraption();
		if (block instanceof AutocannonBlock) return new MountedAutocannonContraption();
		return null;
	}

}
