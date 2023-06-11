package rbasamoyai.createbigcannons.cannons;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class CannonBehavior extends BlockEntityBehaviour {

	protected final Set<Direction> connectedTowards = EnumSet.noneOf(Direction.class);
	protected Direction currentFacing;

	protected CannonBehavior(SmartBlockEntity te) {
		super(te);
	}

	@Override
	public void tick() {
		super.tick();

		BlockState state = this.blockEntity.getBlockState();
		if (state.hasProperty(BlockStateProperties.FACING)) {
			Direction previousFacing = this.currentFacing;
			this.currentFacing = state.getValue(BlockStateProperties.FACING);
			if (previousFacing != null && previousFacing != this.currentFacing) {
				Direction.Axis rotationAxis = getRotationAxis(previousFacing, this.currentFacing);
				Rotation rotation = getRotationBetween(previousFacing, this.currentFacing, rotationAxis);
				this.onRotate(rotationAxis, rotation);
			}
		}
	}

	protected void onRotate(Direction.Axis rotationAxis, Rotation rotation) {
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
		this.blockEntity.setChanged();
	}

	public boolean isConnectedTo(Direction face) {
		return this.connectedTowards.contains(face);
	}

	public void setConnectedFace(Direction face, boolean isConnected) {
		if (isConnected) {
			if (this.connectedTowards.add(face)) this.blockEntity.setChanged();
		} else {
			if (this.connectedTowards.remove(face)) this.blockEntity.setChanged();
		}
	}

	protected static Direction.Axis getRotationAxis(Direction prev, Direction current) {
		Set<Direction.Axis> axes = EnumSet.allOf(Direction.Axis.class);
		axes.remove(prev.getAxis());
		axes.remove(current.getAxis());
		return axes.stream().findFirst().orElseThrow(() -> new IllegalStateException("Failed to find the rotation axes of two different axes"));
	}

	protected static Rotation getRotationBetween(Direction prev, Direction current, Direction.Axis axis) {
		if (prev == current) return Rotation.NONE;
		if (prev == current.getOpposite()) return Rotation.CLOCKWISE_180;
		return prev.getClockWise(axis) == current ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90;
	}

	@Override
	public boolean isSafeNBT() {
		return true;
	}

	@Override
	public void write(CompoundTag nbt, boolean spawnPacket) {
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
		this.currentFacing = nbt.contains("Facing") ? Direction.byName(nbt.getString("Facing")) : null;

		this.connectedTowards.clear();
		ListTag connectionTag = nbt.getList("Connections", Tag.TAG_STRING);
		connectionTag.stream()
			.map(Tag::getAsString)
			.map(Direction::byName)
			.filter(Objects::nonNull)
			.forEach(this.connectedTowards::add);

		super.read(nbt, clientPacket);
	}

}
