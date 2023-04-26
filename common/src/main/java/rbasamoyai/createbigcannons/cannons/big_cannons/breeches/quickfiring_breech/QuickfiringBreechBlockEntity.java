package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;

import java.util.List;

public class QuickfiringBreechBlockEntity extends SmartTileEntity implements IBigCannonBlockEntity {

	private BigCannonBehavior cannonBehavior;
	private int openProgress;
	private int openDirection;

	public QuickfiringBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<TileEntityBehaviour> behaviours) {
		behaviours.add(this.cannonBehavior = new BigCannonBehavior(this, this::canLoadBlock));
	}

	@Override public boolean canLoadBlock(StructureBlockInfo blockInfo) { return false; }

	@Override public BigCannonBehavior cannonBehavior() { return this.cannonBehavior; }

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.putInt("OpenProgress", this.openProgress);
		tag.putInt("OpenDirection", this.openDirection);
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		this.openProgress = tag.getInt("OpenProgress");
		this.openDirection = Mth.clamp(tag.getInt("OpenDirection"), -1, 1);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level != null) { // Don't reset when ponder, just in case
			if (this.openProgress != 0) this.openProgress = 0;
			if (this.openDirection != 0) this.openDirection = 0;
		}
	}

	public void tickAnimation() {
		if (this.openDirection != 0) {
			this.openProgress = Mth.clamp(this.openProgress + this.openDirection, 0, getOpeningTime());
			if (!this.onCooldown()) this.openDirection = 0;
		}
	}

	public boolean isOpen() { return this.openProgress >= getOpeningTime(); }
	public int getOpenDirection() { return this.openDirection; }

	public void toggleOpening() {
		if (!this.onCooldown()) this.openDirection = this.isOpen() ? -1 : 1;
	}

	public float getOpenProgress(float partialTicks) {
		return Mth.clamp((this.openProgress + this.openDirection * partialTicks) / getOpeningTime(), 0.0f, 1.0f);
	}

	public int getOpenProgress() { return this.openProgress; }

	public boolean onCooldown() { return 0 < this.openProgress && this.openProgress < getOpeningTime(); }

	public static int getOpeningTime() { return 5; }

}