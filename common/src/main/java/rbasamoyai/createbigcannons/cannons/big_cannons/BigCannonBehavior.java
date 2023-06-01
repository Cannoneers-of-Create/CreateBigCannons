package rbasamoyai.createbigcannons.cannons.big_cannons;

import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.CannonBehavior;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

public class BigCannonBehavior extends CannonBehavior {

	public static final BehaviourType<?> TYPE = new BehaviourType<>();

	public static final StructureBlockInfo EMPTY = new StructureBlockInfo(BlockPos.ZERO, Blocks.AIR.defaultBlockState(), null);

	protected Optional<StructureBlockInfo> containedBlockInfo = Optional.empty();
	protected Predicate<StructureBlockInfo> predicate;

	public BigCannonBehavior(SmartBlockEntity te, Predicate<StructureBlockInfo> predicate) {
		super(te);
		this.predicate = predicate;
	}

	@Nonnull
	public StructureBlockInfo block() {
		return this.containedBlockInfo.orElse(EMPTY);
	}

	public boolean tryLoadingBlock(StructureBlockInfo info) {
		if (!this.canLoadBlock(info)) return false;
		this.loadBlock(info);
		return true;
	}

	@Override
	protected void onRotate(Direction.Axis rotationAxis, Rotation rotation) {
		if (this.containedBlockInfo.isPresent()) {
			StructureBlockInfo oldInfo = this.containedBlockInfo.get();
			if (oldInfo.state.getBlock() instanceof BigCannonMunitionBlock mblock) {
				this.loadBlock(new StructureBlockInfo(oldInfo.pos, mblock.onCannonRotate(oldInfo.state, rotationAxis, rotation), oldInfo.nbt));
			}
			this.blockEntity.setChanged();
		}
		super.onRotate(rotationAxis, rotation);
	}

	@Override
	public void write(CompoundTag nbt, boolean spawnPacket) {
		super.write(nbt, spawnPacket);
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
	}

	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);
		BlockPos pos = BlockPos.of(nbt.getLong("Pos"));
		BlockState state = NbtUtils.readBlockState(nbt.getCompound("State"));
		CompoundTag tag = nbt.contains("Data") ? nbt.getCompound("Data") : null;
		this.containedBlockInfo = Optional.of(new StructureBlockInfo(pos, state, tag));
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

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

}
