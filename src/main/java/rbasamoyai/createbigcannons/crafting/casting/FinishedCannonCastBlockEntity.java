package rbasamoyai.createbigcannons.crafting.casting;

import java.util.List;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.base.CBCRegistries;

public class FinishedCannonCastBlockEntity extends SmartTileEntity {

	private CannonCastShape renderedShape = CannonCastShape.VERY_SMALL.get();
	private BlockPos centralBlock;
	
	public FinishedCannonCastBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override public void addBehaviours(List<TileEntityBehaviour> behaviours) {}
	
	public void setCentralBlock(BlockPos pos) { this.centralBlock = pos; }
	public boolean isCentralBlock() { return this.centralBlock == null; }
	public void setRenderedShape(CannonCastShape shape) { this.renderedShape = shape; }
	public CannonCastShape getRenderedShape() { return this.renderedShape; }
	
	public void removeCast() {
		this.setRemoved();
		if (this.isCentralBlock()) {
			for (BlockPos pos : BlockPos.betweenClosed(this.worldPosition, this.worldPosition.offset(2, 0, 2))) {
				BlockPos posI = pos.immutable();
				if (posI.equals(this.worldPosition.offset(1, 0, 1))) continue;
				this.level.removeBlockEntity(posI);
				this.level.setBlock(posI, Blocks.AIR.defaultBlockState(), 11);
			}
		} else if (this.level.getBlockEntity(this.centralBlock) instanceof FinishedCannonCastBlockEntity cast && cast.isCentralBlock()) {
			cast.removeCast();
		}
	}
	
	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		if (!this.isCentralBlock()) {
			tag.put("CentralBlock", NbtUtils.writeBlockPos(this.centralBlock));
		} else {
			tag.putString("RenderedShape", CBCRegistries.CANNON_CAST_SHAPES.get().getKey(this.renderedShape).toString());
		}
	}
	
	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		this.centralBlock = tag.contains("CentralBlock") ? NbtUtils.readBlockPos(tag.getCompound("CentralBlock")) : null;
		this.renderedShape = tag.contains("RenderedShape") ? CBCRegistries.CANNON_CAST_SHAPES.get().getValue(new ResourceLocation(tag.getString("RenderedShape"))) : CannonCastShape.VERY_SMALL.get();
	}

}
