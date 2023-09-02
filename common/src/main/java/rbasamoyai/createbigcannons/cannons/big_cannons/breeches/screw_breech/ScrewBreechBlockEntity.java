package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech;

import java.util.List;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;

public class ScrewBreechBlockEntity extends KineticBlockEntity implements IBigCannonBlockEntity {

	private BigCannonBehavior cannonBehavior;
	private float openProgress;

	public ScrewBreechBlockEntity(BlockEntityType<? extends ScrewBreechBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		behaviours.add(this.cannonBehavior = new BigCannonBehavior(this, this::canLoadBlock));
	}

	@Override
	public boolean canLoadBlock(StructureBlockInfo blockInfo) {
		return false;
	}

	@Override
	public BigCannonBehavior cannonBehavior() {
		return this.cannonBehavior;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getSpeed() == 0) return;
		float progress = this.getOpeningSpeed();
		if (Math.abs(progress) > 0) {
			this.openProgress = Mth.clamp(this.openProgress + progress, 0.0f, 1.0f);
		}

		if (this.getLevel() != null && !this.getLevel().isClientSide) {
			BigCannonEnd openState = this.getBlockState().getValue(ScrewBreechBlock.OPEN);
			Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING).getOpposite();
			if (this.openProgress <= 0 && openState != BigCannonEnd.CLOSED) {
				this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(ScrewBreechBlock.OPEN, BigCannonEnd.CLOSED), 3);
				this.cannonBehavior.setConnectedFace(facing, true);
				this.setChanged();
				BlockEntity be = this.getLevel().getBlockEntity(this.worldPosition.relative(facing));
				if (be instanceof IBigCannonBlockEntity cbe) {
					cbe.cannonBehavior().setConnectedFace(facing.getOpposite(), true);
					be.setChanged();
				}
			} else if (this.openProgress >= 1 && openState != BigCannonEnd.OPEN) {
				this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(ScrewBreechBlock.OPEN, BigCannonEnd.OPEN), 3);
				this.cannonBehavior.setConnectedFace(facing, false);
				this.setChanged();
				BlockEntity be = this.getLevel().getBlockEntity(this.worldPosition.relative(facing));
				if (be instanceof IBigCannonBlockEntity cbe) {
					cbe.cannonBehavior().setConnectedFace(facing.getOpposite(), false);
					be.setChanged();
				}
			} else if (this.openProgress > 0 && this.openProgress < 1 && openState != BigCannonEnd.PARTIAL) {
				boolean previouslyConnected = this.cannonBehavior.isConnectedTo(facing);
				this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(ScrewBreechBlock.OPEN, BigCannonEnd.PARTIAL), 3);
				this.cannonBehavior.setConnectedFace(facing, previouslyConnected);
				BlockEntity be = this.getLevel().getBlockEntity(this.worldPosition.relative(facing));
				if (be instanceof IBigCannonBlockEntity cbe) {
					cbe.cannonBehavior().setConnectedFace(facing.getOpposite(), previouslyConnected);
					be.setChanged();
				}
			}
		}
	}

	public float getOpeningSpeed() {
		return Math.abs(this.getSpeed()) > 0 ? this.getSpeed() / 512.0f : 0.0f;
	}

	public float getRenderedBlockOffset(float partialTicks) {
		return Mth.clamp(this.openProgress + this.getOpeningSpeed() * partialTicks, 0.0f, 1.0f);
	}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putFloat("Progress", this.openProgress);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		this.openProgress = compound.getFloat("Progress");
	}

}
