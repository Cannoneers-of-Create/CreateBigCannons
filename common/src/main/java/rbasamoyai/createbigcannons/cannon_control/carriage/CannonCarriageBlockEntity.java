package rbasamoyai.createbigcannons.cannon_control.carriage;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.foundation.tileEntity.SyncedTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.AbstractCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractPitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;

public class CannonCarriageBlockEntity extends SyncedTileEntity {

	public CannonCarriageBlockEntity(BlockEntityType<? extends CannonCarriageBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void tryAssemble() {
		Direction facing = this.getBlockState().getValue(CannonCarriageBlock.FACING);
		AbstractCannonCarriageEntity carriage = CBCEntityTypes.CANNON_CARRIAGE.create(this.level);
		carriage.setPos(Vec3.atBottomCenterOf(this.getBlockPos()).add(0, 0.125, 0));
		carriage.setYRot(facing.toYRot());
		carriage.setCannonRider(this.getBlockState().getValue(CannonCarriageBlock.SADDLED));
		this.level.addFreshEntity(carriage);

		try {
			this.assemble(carriage);
		} catch (AssemblyException e) {

		}

		AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(this.level, this.worldPosition);

		this.setRemoved();
		this.level.removeBlock(this.worldPosition, false);
	}

	protected void assemble(AbstractCannonCarriageEntity carriage) throws AssemblyException {
		if (!CBCBlocks.CANNON_CARRIAGE.has(this.getBlockState())) return;
		BlockPos assemblyPos = this.worldPosition.above();
		if (this.level.isOutsideBuildHeight(assemblyPos)) {
			throw AbstractCannonMountBlockEntity.cannonBlockOutsideOfWorld(assemblyPos);
		}

		Direction facing = this.getBlockState().getValue(CannonCarriageBlock.FACING);

		AbstractMountedCannonContraption mountedCannon = this.getContraption(assemblyPos);
		if (mountedCannon != null && mountedCannon.assemble(this.level, assemblyPos)) {
			if (mountedCannon.initialOrientation() == facing) {
				mountedCannon.removeBlocksFromWorld(this.level, BlockPos.ZERO);
				AbstractPitchOrientedContraptionEntity contraptionEntity = AbstractPitchOrientedContraptionEntity.create(this.level, mountedCannon, facing, false);
				carriage.attach(contraptionEntity);
				carriage.applyRotation();
				this.level.addFreshEntity(contraptionEntity);
			}
		}
	}

	private AbstractMountedCannonContraption getContraption(BlockPos pos) {
		Block block = this.level.getBlockState(pos).getBlock();
		if (block instanceof BigCannonBlock) return new MountedBigCannonContraption();
		if (block instanceof AutocannonBlock) return new AbstractMountedAutocannonContraption();
		return null;
	}

}
