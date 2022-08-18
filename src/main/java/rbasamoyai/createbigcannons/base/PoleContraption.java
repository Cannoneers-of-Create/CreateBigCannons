package rbasamoyai.createbigcannons.base;

import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.TranslatingContraption;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionLighter;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class PoleContraption extends TranslatingContraption {

	protected int extensionLength;
	protected int initialExtensionProgress;
	protected Direction orientation;
	protected AABB pistonContraptionHitbox;
	protected boolean retract;
	
	public PoleContraption() {}
	
	public PoleContraption(Direction orientation, boolean retract) {
		this.orientation = orientation;
		this.retract = retract;
	}
	
	@Override
	public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
		if (!this.collectExtensions(level, pos, this.orientation)) return false;
		int count = this.blocks.size();
		
		if (!this.searchMovedStructure(level, this.anchor, this.retract ? this.orientation.getOpposite() : this.orientation)) {
			return false;
		}
		this.bounds = this.blocks.size() == count ? this.pistonContraptionHitbox : this.bounds.minmax(this.pistonContraptionHitbox);
		return true;
	}
	
	protected abstract boolean collectExtensions(Level level, BlockPos pos, Direction direction) throws AssemblyException;
	
	@Override
	protected boolean isAnchoringBlockAt(BlockPos pos) {
		return this.pistonContraptionHitbox.contains(VecHelper.getCenterOf(pos.subtract(this.anchor)));
	}
	
	@Override
	public void readNBT(Level level, CompoundTag tag, boolean spawnData) {
		super.readNBT(level, tag, spawnData);
		this.initialExtensionProgress = tag.getInt("InitialLength");
		this.extensionLength = tag.getInt("ExtensionLength");
		this.orientation = Direction.from3DDataValue(tag.getInt("Orientation"));
	}
	
	@Override
	public CompoundTag writeNBT(boolean spawnPacket) {
		CompoundTag tag = super.writeNBT(spawnPacket);
		tag.putInt("InitialLength", this.initialExtensionProgress);
		tag.putInt("ExtensionLength", this.extensionLength);
		tag.putInt("Orientation", this.orientation.get3DDataValue());
		return tag;
	}
	
	public int extensionLength() { return this.extensionLength; }
	public int initialExtensionProgress() { return this.initialExtensionProgress; }
	public Direction orientation() { return this.orientation; }
	
	@OnlyIn(Dist.CLIENT) // disgusting.
	@Override
	public ContraptionLighter<?> makeLighter() {
		return new PoleContraptionLighter(this);
	}

}
