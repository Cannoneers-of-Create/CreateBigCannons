package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

import java.util.List;

public class ScrewBreechBlockEntity extends KineticTileEntity implements IBigCannonBlockEntity {

	private BigCannonBehavior cannonBehavior;
	private float openProgress;
	
	public ScrewBreechBlockEntity(BlockEntityType<? extends ScrewBreechBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		behaviours.add(this.cannonBehavior = new BigCannonBehavior(this, this::canLoadBlock));
	}

	@Override public boolean canLoadBlock(StructureBlockInfo blockInfo) { return false; }

	@Override public BigCannonBehavior cannonBehavior() { return this.cannonBehavior; }
	
	@Override
	public void tick() {
		super.tick();
		
		if (this.getSpeed() == 0) return;
		float progress = this.getOpeningSpeed();
		if (Math.abs(progress) > 0) {
			this.openProgress = Mth.clamp(this.openProgress + progress, 0.0f, 1.0f);
		}
		
		BigCannonEnd openState = this.getBlockState().getValue(ScrewBreechBlock.OPEN);
		Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING).getOpposite();
		if (this.openProgress <= 0 && openState != BigCannonEnd.CLOSED) {
			this.level.setBlock(this.worldPosition, this.getBlockState().setValue(ScrewBreechBlock.OPEN, BigCannonEnd.CLOSED), 3);
			this.cannonBehavior.setConnectedFace(facing, true);
			if (this.level.getBlockEntity(this.worldPosition.relative(facing)) instanceof IBigCannonBlockEntity cbe) {
				cbe.cannonBehavior().setConnectedFace(facing.getOpposite(), true);
			}
		} else if (this.openProgress >= 1 && openState != BigCannonEnd.OPEN) {
			this.level.setBlock(this.worldPosition, this.getBlockState().setValue(ScrewBreechBlock.OPEN, BigCannonEnd.OPEN), 3);
			this.cannonBehavior.setConnectedFace(facing, false);
			if (this.level.getBlockEntity(this.worldPosition.relative(facing)) instanceof IBigCannonBlockEntity cbe) {
				cbe.cannonBehavior().setConnectedFace(facing.getOpposite(), false);
			}
		} else if (this.openProgress > 0 && this.openProgress < 1 && openState != BigCannonEnd.PARTIAL) {
			boolean previouslyConnected = this.cannonBehavior.isConnectedTo(facing);
			this.level.setBlock(this.worldPosition, this.getBlockState().setValue(ScrewBreechBlock.OPEN, BigCannonEnd.PARTIAL), 3);
			this.cannonBehavior.setConnectedFace(facing, previouslyConnected);
			if (this.level.getBlockEntity(this.worldPosition.relative(facing)) instanceof IBigCannonBlockEntity cbe) {
				cbe.cannonBehavior().setConnectedFace(facing.getOpposite(), previouslyConnected);
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
