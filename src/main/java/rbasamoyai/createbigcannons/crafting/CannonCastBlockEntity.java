package rbasamoyai.createbigcannons.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.tileEntity.IMultiTileContainer;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import rbasamoyai.createbigcannons.CBCBlocks;

public class CannonCastBlockEntity extends SmartTileEntity implements IMultiTileContainer {

	protected FluidTank fluid;
	protected LazyOptional<IFluidHandler> fluidOptional = null;
	protected List<CannonCastShape> structure = new ArrayList<>();
	protected CannonCastShape renderedSize = CannonCastShape.VERY_SMALL;
	protected BlockPos controllerPos;
	protected BlockPos lastKnownPos;
	protected boolean updateConnectivity;
	protected int height;
	
	private LerpedFloat fluidLevel; 
	
	public CannonCastBlockEntity(BlockEntityType<? extends CannonCastBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.fluid = new FluidTank(1);
		this.height = 1;
	}
	
	@Override public void addBehaviours(List<TileEntityBehaviour> behaviours) {}
	
	@Override
	public void initialize() {
		super.initialize();
		this.sendData();
		if (level.isClientSide) this.invalidateRenderBoundingBox();
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == Direction.UP) {
			if (this.fluidOptional == null) {
				this.fluidOptional = LazyOptional.of(this::createHandlerForCap);
			}
			return this.fluidOptional.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if (this.fluidOptional != null) {
			this.fluidOptional.invalidate();
		}
	}
	
	public void refreshCap() {
		if (this.fluidOptional == null) {
			this.fluidOptional = LazyOptional.of(this::createHandlerForCap);
		} else {
			LazyOptional<IFluidHandler> oldOp = this.fluidOptional;
			this.fluidOptional = LazyOptional.of(this::createHandlerForCap);
			oldOp.invalidate();
		}
	}
	
	private IFluidHandler createHandlerForCap() {
		return this.isController() ? this.fluid : this.getControllerTE().createHandlerForCap();
	}
	
	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		this.fluid.writeToNBT(tag);
		if (this.canRenderCastModel()) {
			tag.putString("RenderedSize", this.renderedSize.getSerializedName());
		}
		if (this.lastKnownPos != null) {
			tag.put("LastKnownPos", NbtUtils.writeBlockPos(this.lastKnownPos));
		}
		if (this.updateConnectivity) {
			tag.putBoolean("Uninitialized", true);
		}
		tag.putInt("Height", this.height);
		
		if (this.isController()) {
			if (!this.structure.isEmpty()) {
				ListTag structureTag = new ListTag();
				for (CannonCastShape sz : this.structure) {
					structureTag.add(StringTag.valueOf(sz.getSerializedName()));
				}
				tag.put("Structure", structureTag);
			}
		} else {
			tag.put("Controller", NbtUtils.writeBlockPos(this.controllerPos));
		}
	}
	
	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		this.fluid.readFromNBT(tag);
		if (tag.contains("RenderedSize")) {
			this.renderedSize = CannonCastShape.byId(tag.getString("RenderedSize"));
		}
		this.updateConnectivity = tag.contains("Uninitialized");
		if (tag.contains("LastKnownPos")) {
			this.lastKnownPos = NbtUtils.readBlockPos(tag.getCompound("LastKnownPos"));
		}
		this.height = tag.getInt("Height");
		
		this.structure.clear();
		if (tag.contains("Structure")) {
			ListTag list = tag.getList("Structure", Tag.TAG_STRING);
			for (int i = 0; i < list.size(); ++i) {
				this.structure.add(CannonCastShape.byId(list.getString(i)));
			}
		}
		if (tag.contains("Controller")) {
			this.controllerPos = NbtUtils.readBlockPos(tag.getCompound("Controller"));
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (this.lastKnownPos == null) {
			this.lastKnownPos = this.getBlockPos();
		} else if (!this.lastKnownPos.equals(this.worldPosition) && this.worldPosition != null) {
			this.onPositionChanged();
			return;
		}
		this.invalidateRenderBoundingBox();
		
		if (this.updateConnectivity) {
			this.updateConnectivity();
		}
		if (this.fluidLevel != null) {
			this.fluidLevel.tickChaser();
		}
		if (this.isController()) {
			this.tickCastingBehavior();
		}
	}
	
	private void onPositionChanged() {
		this.removeController(true);
		this.lastKnownPos = this.worldPosition;
	}
	
	protected void tickCastingBehavior() {
		if (this.level.isClientSide) return;
	}
	
	public void initializeCastMultiblock(CannonCastShape size) {
		this.renderedSize = size;
		if (this.level.getBlockEntity(this.worldPosition.below()) instanceof CannonCastBlockEntity otherCast
			&& otherCast.getType() == this.getType()
			&& otherCast.getHeight() < getMaxHeight()
			/*&& false*/) {
			this.controllerPos = otherCast.getController();
			CannonCastBlockEntity controller = otherCast.getControllerTE();
			controller.fluid.setCapacity(controller.fluid.getCapacity() + this.renderedSize.fluidSize());
			controller.height += 1;
			controller.notifyUpdate();
		} else {
			this.fluid = new FluidTank(this.renderedSize.fluidSize());
		}
		for (Iterator<BlockPos> iter = BlockPos.betweenClosed(this.worldPosition.offset(-1, 0, -1), this.worldPosition.offset(1, 0, 1)).iterator(); iter.hasNext(); ) {
			BlockPos pos = iter.next();
			if (pos.equals(this.worldPosition)) continue;
			this.level.setBlock(pos, CBCBlocks.CANNON_CAST.getDefaultState(), 11);
			if (this.level.getBlockEntity(pos) instanceof CannonCastBlockEntity childCast && childCast.getType() == this.getType()) {
				childCast.controllerPos = this.getController();
				childCast.notifyUpdate();
			}
		}
		this.notifyUpdate();
	}
	
	public void destroyCastMultiblockAtLayer() {
		if (this.canRenderCastModel()) {
			CannonCastBlockEntity controller = this.getControllerTE();
			controller.height -= 1;
			controller.fluid.setCapacity(Math.max(1, controller.fluid.getCapacity() - this.renderedSize.fluidSize()));
			
			
			
			List<BlockPos> toRemove = new ArrayList<>();
			for (Iterator<BlockPos> iter = BlockPos.betweenClosed(this.worldPosition.offset(-1, 0, -1), this.worldPosition.offset(1, 0, 1)).iterator(); iter.hasNext(); ) {
				BlockPos pos = iter.next();
				if (CBCBlocks.CANNON_CAST.has(this.level.getBlockState(pos))) {
					toRemove.add(pos.immutable());
				}
			}
			for (BlockPos pos : toRemove) {
				this.level.removeBlockEntity(pos);
				this.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
			}
		} else if (this.level.getBlockEntity(this.getCenterBlock()) instanceof CannonCastBlockEntity otherCast) {
			this.level.removeBlockEntity(this.worldPosition);
			otherCast.destroyCastMultiblockAtLayer();
		}
	}
	
	public BlockPos getCenterBlock() {
		return this.isController() ? this.worldPosition : new BlockPos(this.controllerPos.getX(), this.worldPosition.getY(), this.controllerPos.getZ());
	}
	
	public boolean canRenderCastModel() {
		return this.isController() ? true : this.controllerPos.getX() == this.worldPosition.getX() && this.controllerPos.getZ() == this.worldPosition.getZ();
	}

	@Override
	public BlockPos getController() {
		return this.isController() ? this.worldPosition : this.controllerPos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CannonCastBlockEntity getControllerTE() {
		return this.isController() ? this : this.level.getBlockEntity(this.controllerPos) instanceof CannonCastBlockEntity cast ? cast : null;
	}

	@Override
	public boolean isController() {
		return this.controllerPos == null || this.worldPosition.equals(this.controllerPos);
	}

	@Override
	public void setController(BlockPos pos) {
		if (this.level.isClientSide || this.isVirtual() || pos.equals(this.controllerPos)) return;
		this.controllerPos = pos;
		this.refreshCap();
		this.notifyUpdate();
	}
	
	protected void updateConnectivity() {
		this.updateConnectivity = false;
		if (this.level.isClientSide || !this.isController()) return;
		ConnectivityHandler.formMulti(this);
	}

	@Override
	public void removeController(boolean keepContents) {
		if (this.level.isClientSide) return;
		this.updateConnectivity = true;
		if (!keepContents) {
			
		}
		this.controllerPos = null;
		this.height = 1;
		
		
		this.refreshCap();
		this.notifyUpdate();
	}

	@Override
	public BlockPos getLastKnownPos() {
		return this.lastKnownPos;
	}

	@Override
	public void preventConnectivityUpdate() {
		this.updateConnectivity = false;
	}

	@Override
	public void notifyMultiUpdated() {
		
	}
	
	public static int getMaxHeight() {
		return 32; // TODO: Config
	}

	@Override public Axis getMainConnectionAxis() { return Axis.Y; }
	@Override public int getMaxLength(Axis longAxis, int width) { return longAxis == Axis.Y ? getMaxHeight() : this.getMaxWidth(); }
	@Override public int getMaxWidth() { return 3; }
	@Override public int getHeight() { return this.height; }
	@Override public void setHeight(int height) { this.height = height; }

	@Override public int getWidth() { return 3; }
	@Override public void setWidth(int width) {}
	
	@Override
	protected AABB createRenderBoundingBox() {
		if (this.isController()) {
			return super.createRenderBoundingBox().move(-1, 0, -1).expandTowards(2, this.height - 1, 2);
		}
		if (this.canRenderCastModel() && this.getControllerTE() != null) {
			return this.getControllerTE().createRenderBoundingBox();
		}
		return super.createRenderBoundingBox();
	}
	
	public CannonCastShape getRenderedSize() { return this.renderedSize; }

}
