package rbasamoyai.createbigcannons.cannonmount;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionType;
import com.simibubi.create.content.contraptions.components.structureMovement.bearing.AnchoredLighter;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionLighter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCContraptionTypes;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;

public class MountedCannonContraption extends Contraption {
	
	private CannonMaterial cannonMaterial;
	private Direction initialOrientation = Direction.NORTH;
	
	public MountedCannonContraption() {}
	
	@Override
	public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
		if (!this.collectCannonBlocks(level, pos)) return false;
		this.bounds = new AABB(BlockPos.ZERO);
		this.expandBoundsAroundAxis(Direction.Axis.Y);
		return !this.blocks.isEmpty();
	}
	
	private boolean collectCannonBlocks(Level level, BlockPos pos) throws AssemblyException {
		Direction facing = level.getBlockState(pos.below(2)).getValue(BlockStateProperties.HORIZONTAL_FACING);
		
		BlockState startState = level.getBlockState(pos);
		
		if (!(startState.getBlock() instanceof CannonBlock)) {
			return false;
		}
		CannonBlock startCannon = (CannonBlock) startState.getBlock();
		Direction.Axis axis = startCannon.getAxis(startState);
		if (axis != facing.getAxis() && axis.isHorizontal()) {
			return false;
		}
		CannonMaterial material = startCannon.getCannonMaterial();
		CannonEnd startEnd = startCannon.getOpeningType(level, startState, pos);
		
		List<StructureBlockInfo> cannonBlocks = new ArrayList<>();
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getTileEntityNBT(level, pos)));
		
		int cannonLength = 1;
		
		Optional<Direction> facingOp = startCannon.getFacing(startState);
		if (facingOp.isPresent()) {			
			Direction startFacing = facingOp.get();
			if (axis != startFacing.getAxis() || startEnd != CannonEnd.CLOSED) {
				return false;
			}
			
			BlockPos start = pos;
			BlockState nextState = level.getBlockState(pos.relative(startFacing));
			
			CannonEnd cannonEnd = CannonEnd.CLOSED;
			
			while (this.isValidCannonBlock(level, nextState, start.relative(startFacing)) && this.isConnectedToCannon(level, nextState, startFacing, material)) {
				start = start.relative(startFacing);
				cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getTileEntityNBT(level, start)));
				cannonLength++;
				
				cannonEnd = ((CannonBlock) nextState.getBlock()).getOpeningType(level, nextState, start);
				if (cannonEnd == CannonEnd.CLOSED) {
					throw invalidCannon();
				}
				
				if (this.hasCannonLoaderInside(level, nextState, start)) {
					throw cannonLoaderInsideDuringAssembly(start);
				}
				
				nextState = level.getBlockState(start.relative(startFacing));
				
				if (cannonLength > getMaxCannonLength()) {
					throw cannonTooLarge();
				}
			}
			this.initialOrientation = startFacing;
		} else {
			Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, axis);
			Direction negative = positive.getOpposite();
			
			BlockPos start = pos;
			BlockState nextState = level.getBlockState(pos.relative(positive));
			
			CannonEnd positiveEnd = startEnd;
			while (this.isValidCannonBlock(level, nextState, start.relative(positive)) && this.isConnectedToCannon(level, startState, positive, material)) {
				start = start.relative(positive);
				cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getTileEntityNBT(level, start)));
				cannonLength++;
				
				positiveEnd = ((CannonBlock) nextState.getBlock()).getOpeningType(level, nextState, start);
				
				if (this.hasCannonLoaderInside(level, nextState, start)) {
					throw cannonLoaderInsideDuringAssembly(start);
				}
				
				nextState = level.getBlockState(start.relative(positive));
				
				if (cannonLength > getMaxCannonLength()) {
					throw cannonTooLarge();
				}
				if (positiveEnd == CannonEnd.CLOSED) break;
			}
			
			start = pos;
			nextState = level.getBlockState(pos.relative(negative));
			
			CannonEnd negativeEnd = startEnd;
			while (this.isValidCannonBlock(level, nextState, start.relative(negative)) && this.isConnectedToCannon(level, nextState, negative, material)) {
				start = start.relative(negative);
				cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getTileEntityNBT(level, start)));
				cannonLength++;
				
				negativeEnd = ((CannonBlock) nextState.getBlock()).getOpeningType(level, nextState, start);
				
				if (this.hasCannonLoaderInside(level, nextState, start)) {
					throw cannonLoaderInsideDuringAssembly(start);
				}
				
				nextState = level.getBlockState(start.relative(negative));
				
				if (cannonLength > getMaxCannonLength()) {
					throw cannonTooLarge();
				}
				if (negativeEnd == CannonEnd.CLOSED) break;
			}
			
			if (positiveEnd == negativeEnd) {
				throw invalidCannon();
			}
			
			this.initialOrientation = positiveEnd == CannonEnd.OPEN ? positive : negative;
		}
		
		this.anchor = pos;
		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos.subtract(this.anchor);
			this.getBlocks().put(localPos, new StructureBlockInfo(localPos, blockInfo.state, blockInfo.nbt));
		}
		this.cannonMaterial = material;
		
		return true;
	}
	
	private boolean isValidCannonBlock(LevelAccessor level, BlockState state, BlockPos pos) {
		return state.getBlock() instanceof CannonBlock;
	}
	
	private boolean hasCannonLoaderInside(LevelAccessor level, BlockState state, BlockPos pos) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof ICannonBlockEntity)) return false;
		BlockState containedState = ((ICannonBlockEntity) be).cannonBehavior().block().state;
		return CBCBlocks.RAM_HEAD.has(containedState) || CBCBlocks.WORM_HEAD.has(containedState) || AllBlocks.PISTON_EXTENSION_POLE.has(containedState);
	}
	
	private boolean isConnectedToCannon(LevelAccessor level, BlockState state, Direction connection, CannonMaterial material) {
		CannonBlock cannonBlock = (CannonBlock) state.getBlock();
		return cannonBlock.getCannonMaterial() == material && cannonBlock.getFacing(state).map(connection.getOpposite()::equals).orElseGet(() -> cannonBlock.getAxis(state) == connection.getAxis());
	}
	
	public float getWeightForStress() {
		if (this.cannonMaterial == null) {
			return this.blocks.size();
		}
		return this.blocks.size() * this.cannonMaterial.weight();
	}
	
	public Direction initialOrientation() { return this.initialOrientation; }
	
	@Override
	public void onEntityTick(Level level) {
		super.onEntityTick(level);
		
		if (this.anchor != null) {
			BlockEntity possibleMount = level.getBlockEntity(this.anchor.below(2));
			if (possibleMount instanceof CannonMountBlockEntity && this.entity instanceof PitchOrientedContraptionEntity) {
				CannonMountBlockEntity mount = (CannonMountBlockEntity) possibleMount;
				PitchOrientedContraptionEntity poce = (PitchOrientedContraptionEntity) this.entity;
				if (!mount.isAttachedTo(poce)) {
					mount.attach(poce);
				}
			}
		}
	}
	
	@Override
	public CompoundTag writeNBT(boolean clientData) {
		CompoundTag tag = super.writeNBT(clientData);
		tag.putString("CannonMaterial", this.cannonMaterial == null ? CannonMaterial.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
		if (this.initialOrientation != null) {
			tag.putString("InitialOrientation", this.initialOrientation.getSerializedName());
		}
		return tag;
	}
	
	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = CannonMaterial.fromName(new ResourceLocation(tag.getString("CannonMaterial")));
		this.initialOrientation = tag.contains("InitialOrientation", Tag.TAG_STRING) ? Direction.byName(tag.getString("InitialOrientation")) : Direction.NORTH;
	}
	
	@Override
	public ContraptionLighter<?> makeLighter() {
		return new AnchoredLighter(this);
	}
	
	private static int getMaxCannonLength() {
		return 32; // TODO: config max cannon length
	}

	@Override public boolean canBeStabilized(Direction direction, BlockPos pos) { return true; }
	
	@Override protected ContraptionType getType() { return CBCContraptionTypes.MOUNTED_CANNON; }
	
	public static AssemblyException cannonTooLarge() {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonTooLarge", getMaxCannonLength()));
	}
	
	public static AssemblyException invalidCannon() {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.invalidCannon"));
	}
	
	public static AssemblyException cannonLoaderInsideDuringAssembly(BlockPos pos) {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonLoaderInsideDuringAssembly", pos.getX(), pos.getY(), pos.getZ()));
	}
	
}
