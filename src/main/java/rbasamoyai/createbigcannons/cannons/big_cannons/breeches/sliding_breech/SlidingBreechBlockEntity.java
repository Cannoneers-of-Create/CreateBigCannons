package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech;

import java.util.List;

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
    private boolean canClose = true;

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

        boolean oldClose = this.canClose;
        this.canClose = this.canClose();

        if (progress > 0 || this.canClose)
            this.openProgress = Mth.clamp(this.openProgress + progress, 0.0f, 1.0f);

        if (oldClose != this.canClose)
            this.notifyUpdate();
	}

    public boolean canClose() {
        if (!this.cannonBehavior.block().state().isAir())
            return false;
        List<ControlledContraptionEntity> contraptions = this.getLevel().getEntitiesOfClass(ControlledContraptionEntity.class, new AABB(this.worldPosition));
        for (ControlledContraptionEntity cce : contraptions) {
            if (cce.getContraption() instanceof TranslatingContraption)
                return false;
        }
        return true;
    }

	public float getOpeningSpeed() {
		return this.getSpeed() > 0 || this.canClose ? this.getSpeed() / 512.0f : 0.0f;
	}

	public float getRenderedBlockOffset(float partialTicks) {
		return Mth.clamp(this.openProgress + this.getOpeningSpeed() * partialTicks, 0.0f, 1.0f);
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.putFloat("Progress", this.openProgress);
        if (!clientPacket)
            return;
        tag.putBoolean("CanClose", this.canClose);
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		this.openProgress = tag.getFloat("Progress");
        if (!clientPacket)
            return;
        this.canClose = tag.getBoolean("CanClose");
	}

}
