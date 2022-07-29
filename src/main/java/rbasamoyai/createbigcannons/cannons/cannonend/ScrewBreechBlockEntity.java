package rbasamoyai.createbigcannons.cannons.cannonend;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.cannonend.ScrewBreechBlock.OpenState;

public class ScrewBreechBlockEntity extends KineticTileEntity {

	private float openProgress;
	private float oldProgress;
	
	public ScrewBreechBlockEntity(BlockEntityType<? extends ScrewBreechBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		this.oldProgress = this.openProgress;
		
		if (this.getSpeed() == 0) return;
		float progress = this.getOpeningSpeed();
		if (Math.abs(progress) > 0) {
			this.openProgress = Mth.clamp(this.openProgress + progress, 0.0f, 1.0f);
		}
		
		if (this.oldProgress <= 0 && this.openProgress > 0) {
			this.level.setBlock(this.worldPosition, this.getBlockState().setValue(ScrewBreechBlock.OPEN, OpenState.PARTIAL), 3);
		} else if (this.oldProgress < 1 && this.openProgress >= 1) {
			this.level.setBlock(this.worldPosition, this.getBlockState().setValue(ScrewBreechBlock.OPEN, OpenState.OPEN), 3);
		} else if (this.oldProgress > 0 && this.openProgress <= 0) {
			this.level.setBlock(this.worldPosition, this.getBlockState().setValue(ScrewBreechBlock.OPEN, OpenState.CLOSED), 3);
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
