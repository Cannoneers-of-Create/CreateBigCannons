package rbasamoyai.createbigcannons.cannons.cannonend;

import com.simibubi.create.content.contraptions.components.structureMovement.ControlledContraptionEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.cannons.CannonBehavior;

public class SlidingBreechBlockEntity extends AbstractCannonBreechBlockEntity {
	
	private CannonBehavior cannonBehavior;
	
	private float openProgress;
	
	public SlidingBreechBlockEntity(BlockEntityType<? extends SlidingBreechBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override public boolean isOpen() { return this.openProgress >= 1.0f; }
	public float getOpenProgress() { return this.openProgress; }
	public void setOpenProgress(float openProgress) { this.openProgress = openProgress; }
	public CannonEnd getOpeningType() { return CannonEnd.getOpeningType(this.openProgress); } 

	@Override
	public CannonBehavior cannonBehavior() {
		if (this.cannonBehavior == null) {
			this.cannonBehavior = new CannonBehavior(this, this::canLoadBlock);
		}
		return this.cannonBehavior;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (this.getSpeed() == 0) return;
		float progress = this.getOpeningSpeed();
		if (progress > 0 || this.canClose()) {
			this.openProgress = Mth.clamp(this.openProgress + progress, 0.0f, 1.0f);
		}
	}
	
	public boolean canClose() {
		return this.cannonBehavior.block().state.isAir() && this.level.getEntitiesOfClass(ControlledContraptionEntity.class, new AABB(this.worldPosition)).isEmpty();
	}
	
	public float getOpeningSpeed() {
		return this.getSpeed() > 0 || this.canClose() ? this.getSpeed() / 512.0f : 0.0f;
	}
	
	public float getRenderedBlockOffset(float partialTicks) {
		return Mth.clamp(this.openProgress + this.getOpeningSpeed() * partialTicks, 0.0f, 1.0f);
	}
	
	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.putFloat("Progress", this.openProgress);
	}
	
	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		this.openProgress = tag.getFloat("Progress");
	}

}
