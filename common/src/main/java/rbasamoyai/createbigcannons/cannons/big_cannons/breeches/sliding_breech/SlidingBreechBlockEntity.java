package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech;

import com.simibubi.create.content.contraptions.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.TranslatingContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.AbstractBigCannonBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;

public class SlidingBreechBlockEntity extends AbstractBigCannonBreechBlockEntity {

	private float openProgress;

	public SlidingBreechBlockEntity(BlockEntityType<? extends SlidingBreechBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public boolean isOpen() {
		return this.openProgress >= 1.0f;
	}

	public BigCannonEnd getOpeningType() {
		return BigCannonEnd.getOpeningType(this.openProgress);
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
		return this.cannonBehavior.block().state().isAir() && this.getLevel().getEntitiesOfClass(ControlledContraptionEntity.class, new AABB(this.worldPosition))
			.stream().noneMatch(cce -> cce.getContraption() instanceof TranslatingContraption);
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
