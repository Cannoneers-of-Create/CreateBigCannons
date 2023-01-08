package rbasamoyai.createbigcannons.cannonmount.carriage;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.foundation.tileEntity.SyncedTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCEntityTypes;
import rbasamoyai.createbigcannons.cannonmount.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannonmount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannonmount.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannonmount.MountedAutocannonContraption;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;

public class CannonCarriageBlockEntity extends SyncedTileEntity {

	public CannonCarriageBlockEntity(BlockEntityType<? extends CannonCarriageBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void tryAssemble() {
		Direction facing = this.getBlockState().getValue(CannonCarriageBlock.FACING);
		CannonCarriageEntity carriage = CBCEntityTypes.CANNON_CARRIAGE.create(this.level);
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

	protected void assemble(CannonCarriageEntity carriage) throws AssemblyException {
		if (!CBCBlocks.CANNON_CARRIAGE.has(this.getBlockState())) return;
		BlockPos assemblyPos = this.worldPosition.above();
		if (this.level.isOutsideBuildHeight(assemblyPos)) {
			throw CannonMountBlockEntity.cannonBlockOutsideOfWorld(assemblyPos);
		}

		Direction facing = this.getBlockState().getValue(CannonCarriageBlock.FACING);

		AbstractMountedCannonContraption mountedCannon = this.getContraption(assemblyPos);
		if (mountedCannon != null && mountedCannon.assemble(this.level, assemblyPos)) {
			if (mountedCannon.initialOrientation() == facing) {
				mountedCannon.removeBlocksFromWorld(this.level, BlockPos.ZERO);
				PitchOrientedContraptionEntity contraptionEntity = PitchOrientedContraptionEntity.create(this.level, mountedCannon, facing, false);
				carriage.attach(contraptionEntity);
				carriage.applyRotation();
				this.level.addFreshEntity(contraptionEntity);
			}
		}
	}

	private AbstractMountedCannonContraption getContraption(BlockPos pos) {
		Block block = this.level.getBlockState(pos).getBlock();
		if (block instanceof CannonBlock) return new MountedBigCannonContraption();
		if (block instanceof AutocannonBlock) return new MountedAutocannonContraption();
		return null;
	}

}
