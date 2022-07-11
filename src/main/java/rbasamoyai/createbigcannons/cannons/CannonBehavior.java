package rbasamoyai.createbigcannons.cannons;

import java.util.Optional;
import java.util.function.Predicate;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.BehaviourType;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class CannonBehavior extends TileEntityBehaviour {
	
	public static final BehaviourType<CannonBehavior> TYPE = new BehaviourType<>();
	public static final StructureBlockInfo EMPTY = new StructureBlockInfo(BlockPos.ZERO, Blocks.AIR.defaultBlockState(), null);
	
	private Optional<StructureBlockInfo> containedBlockInfo = Optional.empty();
	private Predicate<StructureBlockInfo> predicate;
	
	public CannonBehavior(SmartTileEntity te, Predicate<StructureBlockInfo> predicate) {
		super(te);
		this.predicate = predicate;
	}
	
	public StructureBlockInfo block() { return this.containedBlockInfo.orElse(EMPTY); }
	
	public boolean tryLoadingBlock(StructureBlockInfo info) {
		if (!this.canLoadBlock(info)) return false;
		this.loadBlock(info);
		return true;
	}
	
	public boolean canLoadBlock(StructureBlockInfo info) {
		return this.predicate.test(info);
	}
	
	public void loadBlock(StructureBlockInfo info) {
		this.containedBlockInfo = Optional.ofNullable(info);
	}
	
	public void removeBlock() {
		this.loadBlock(null);
	}
	
	@Override public boolean isSafeNBT() { return true; }
	
	@Override
	public void write(CompoundTag nbt, boolean spawnPacket) {
		if (this.containedBlockInfo.isPresent()) {
			StructureBlockInfo blockInfo = this.containedBlockInfo.get();
			if (!blockInfo.state.isAir()) {
				nbt.putLong("Pos", blockInfo.pos.asLong());
				nbt.put("State", NbtUtils.writeBlockState(blockInfo.state));
				if (blockInfo.nbt != null) {
					nbt.put("Data", blockInfo.nbt);
				}
			}
		}
		super.write(nbt, spawnPacket);
	}
	
	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		BlockPos pos = BlockPos.of(nbt.getLong("Pos"));
		BlockState state = NbtUtils.readBlockState(nbt.getCompound("State"));
		CompoundTag tag = nbt.contains("Data") ? nbt.getCompound("Data") : null;
		this.containedBlockInfo = Optional.of(new StructureBlockInfo(pos, state, tag));
		super.read(nbt, clientPacket);
	}

	@Override public BehaviourType<?> getType() { return TYPE; }

}
