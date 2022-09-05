package rbasamoyai.createbigcannons.cannons;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.BehaviourType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class CannonBehavior extends TileEntityBehaviour {
	
	public static final BehaviourType<CannonBehavior> TYPE = new BehaviourType<>();
	public static final StructureBlockInfo EMPTY = new StructureBlockInfo(BlockPos.ZERO, Blocks.AIR.defaultBlockState(), null);
	
	protected Optional<StructureBlockInfo> containedBlockInfo = Optional.empty();
	protected Predicate<StructureBlockInfo> predicate;
	protected final Set<Direction> connectedTowards = EnumSet.noneOf(Direction.class);
	protected Direction currentFacing;
	
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
	
	@Override
	public void tick() {
		super.tick();
		
		BlockState state = this.tileEntity.getBlockState();
		if (state.hasProperty(BlockStateProperties.FACING)) {
			Direction previousFacing = this.currentFacing;
			this.currentFacing = state.getValue(BlockStateProperties.FACING);
			if (previousFacing != null && previousFacing != this.currentFacing) {
				Direction.Axis rotationAxis = getRotationAxis(previousFacing, currentFacing);
				Rotation rotation = getRotationBetween(previousFacing, this.currentFacing, rotationAxis);
				
				EnumSet<Direction> copyFrom = EnumSet.noneOf(Direction.class);
				this.connectedTowards.forEach(d -> {
					Direction dc = d;
					for (int i = 0; i < rotation.ordinal(); ++i) {
						dc = dc.getClockWise(rotationAxis);
					}
					copyFrom.add(dc);
				});
				this.connectedTowards.clear();
				this.connectedTowards.addAll(copyFrom);
				this.tileEntity.setChanged();
			}
		}
	}
	
	public boolean isConnectedTo(Direction face) {
		return this.connectedTowards.contains(face);
	}
	
	public void setConnectedFace(Direction face, boolean isConnected) {
		if (isConnected) {
			if (this.connectedTowards.add(face)) this.tileEntity.setChanged();
		} else {
			if (this.connectedTowards.remove(face)) this.tileEntity.setChanged();
		}
	}
	
	private static Direction.Axis getRotationAxis(Direction prev, Direction current) {
		Set<Direction.Axis> axes = EnumSet.allOf(Direction.Axis.class);
		axes.remove(prev.getAxis());
		axes.remove(current.getAxis());
		return axes.stream().findFirst().orElseThrow(() -> new IllegalStateException("Failed to find the rotation axes of two different axes"));
	}
	
	private static Rotation getRotationBetween(Direction prev, Direction current, Direction.Axis axis) {
		if (prev == current) return Rotation.NONE;
		if (prev == current.getOpposite()) return Rotation.CLOCKWISE_180;
		return prev.getClockWise(axis) == current ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90;
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
		if (this.currentFacing != null) {
			nbt.putString("Facing", this.currentFacing.getSerializedName());
		}

		ListTag connectionTag = new ListTag();
		this.connectedTowards.stream()
		.map(Direction::getSerializedName)
		.map(StringTag::valueOf)
		.forEach(connectionTag::add);
		nbt.put("Connections", connectionTag);
		
		super.write(nbt, spawnPacket);
	}
	
	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		BlockPos pos = BlockPos.of(nbt.getLong("Pos"));
		BlockState state = NbtUtils.readBlockState(nbt.getCompound("State"));
		CompoundTag tag = nbt.contains("Data") ? nbt.getCompound("Data") : null;
		this.containedBlockInfo = Optional.of(new StructureBlockInfo(pos, state, tag));
		
		this.currentFacing = nbt.contains("Facing") ? Direction.byName(nbt.getString("Facing")) : null;
		
		this.connectedTowards.clear();
		ListTag connectionTag = nbt.getList("Connections", Tag.TAG_STRING);
		connectionTag.stream()
		.map(Tag::getAsString)
		.map(Direction::byName)
		.filter(d -> d != null)
		.forEach(this.connectedTowards::add);
		
		super.read(nbt, clientPacket);
	}

	@Override public BehaviourType<?> getType() { return TYPE; }

}
