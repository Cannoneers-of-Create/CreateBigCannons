package rbasamoyai.createbigcannons.crafting.builtup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionType;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.PistonExtensionPoleBlock;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCContraptionTypes;
import rbasamoyai.createbigcannons.base.PoleContraption;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock.BuilderState;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CannonBuildingContraption extends PoleContraption {

	protected boolean isActivated = false;
	protected CannonMaterial material = null;
	
	public CannonBuildingContraption() {}
	
	public CannonBuildingContraption(Direction orientation, boolean retract) {
		super(orientation, retract);
	}
	
	private static final DirectionProperty FACING = BlockStateProperties.FACING;
	
	@Override
	protected boolean collectExtensions(Level level, BlockPos pos, Direction direction) throws AssemblyException {
		if (!CBCBlocks.CANNON_BUILDER.has(level.getBlockState(pos))) return false;
		
		List<StructureBlockInfo> poles = new ArrayList<>();
		BlockPos start = pos;
		BlockState nextBlock = level.getBlockState(start.relative(direction));
		int extensionsInFront = 0;
		Direction.Axis blockAxis = direction.getAxis();
		
		PistonExtensionPoleBlock.PlacementHelper matcher = PistonExtensionPoleBlock.PlacementHelper.get();
		if (level.getBlockState(pos).getValue(CannonBuilderBlock.STATE) == BuilderState.EXTENDED) {
			while (matcher.matchesAxis(nextBlock, blockAxis) || isBuilderHead(nextBlock) && nextBlock.getValue(FACING) == direction) {
				start = start.relative(direction);
				poles.add(new StructureBlockInfo(start, nextBlock.setValue(FACING, direction), null));
				
				extensionsInFront++;
				if (isBuilderHead(nextBlock)) {
					this.isActivated = nextBlock.getValue(CannonBuilderHeadBlock.ATTACHED);
					break;
				}
				
				nextBlock = level.getBlockState(start.relative(direction));
				
				if (extensionsInFront > CannonBuilderBlock.maxAllowedBuilderLength()) {
					throw AssemblyException.tooManyPistonPoles();
				}
			}
		}
		
		if (extensionsInFront == 0) {
			this.isActivated = level.getBlockState(pos).getValue(CannonBuilderBlock.STATE) == BuilderState.ACTIVATED;
			poles.add(new StructureBlockInfo(pos, CBCBlocks.CANNON_BUILDER_HEAD.getDefaultState()
					.setValue(FACING, direction)
					.setValue(CannonBuilderHeadBlock.ATTACHED, this.isActivated), null));
		} else {
			poles.add(new StructureBlockInfo(pos, AllBlocks.PISTON_EXTENSION_POLE.getDefaultState().setValue(FACING, direction), null));
		}
		
		BlockPos end = pos;
		int extensionsInBack = 0;
		Direction opposite = direction.getOpposite();
		nextBlock = level.getBlockState(end.relative(opposite));
		
		while (matcher.matchesAxis(nextBlock, blockAxis)) {
			end = end.relative(opposite);
			poles.add(new StructureBlockInfo(end, nextBlock.setValue(FACING, direction), null));
			extensionsInBack++;
			nextBlock = level.getBlockState(end.relative(opposite));
			
			if (extensionsInFront + extensionsInBack > CannonBuilderBlock.maxAllowedBuilderLength()) {
				throw AssemblyException.tooManyPistonPoles();
			}
		}
		
		this.extensionLength = extensionsInFront + extensionsInBack;
		
		if (this.extensionLength == 0) {
			throw AssemblyException.noPistonPoles();
		}
		
		this.anchor = pos.relative(direction, this.initialExtensionProgress + 1);
		this.initialExtensionProgress = extensionsInFront;
		this.pistonContraptionHitbox = new AABB(
				BlockPos.ZERO,
				BlockPos.ZERO.relative(direction, -this.extensionLength - 1))
				.expandTowards(1, 1, 1);
		
		this.bounds = new AABB(0, 0, 0, 0, 0, 0);
		
		for (StructureBlockInfo pole : poles) {
			BlockPos relPos = pole.pos.relative(direction, -extensionsInFront);
			BlockPos localPos = relPos.subtract(this.anchor);
			this.getBlocks().put(localPos, new StructureBlockInfo(localPos, pole.state, null));
		}
		
		return true;
	}
	
	@Override
	protected boolean addToInitialFrontier(Level level, BlockPos pos, Direction forcedDirection, Queue<BlockPos> frontier) throws AssemblyException {
		frontier.clear();
		if (!this.isActivated) return true;
		boolean retracting = forcedDirection != this.orientation;
		
		Set<CannonCastShape> connectedShapes = new HashSet<>();
		CannonCastShape fullShape = null;
		Map<BlockPos, Pair<StructureBlockInfo, BlockEntity>> preAddedBlocks = new HashMap<>();
		Direction opposite = this.orientation.getOpposite();
		boolean firstBlock = true;
		
		for (int offset = 0; offset <= CBCConfigs.SERVER.crafting.maxCannonBuilderRange.get(); ++offset) {
			BlockPos currentPos = pos.relative(this.orientation, offset + this.initialExtensionProgress);
			if (retracting && level.isOutsideBuildHeight(currentPos)) {
				preAddedBlocks.forEach(this::addBlock);
				return this.material != null && !firstBlock;
			}
			if (!level.isLoaded(currentPos)) throw AssemblyException.unloadedChunk(currentPos);
			
			BlockState state = level.getBlockState(currentPos);
			if (this.isValidCannonBlock(level, state, currentPos) && matchesCannonAxis(state, this.orientation.getAxis()) && this.matchesCurrentMaterial(level, state, currentPos)) {
				CannonBlock cBlock = ((CannonBlock) state.getBlock());
				if (this.material == null) this.material = cBlock.getCannonMaterialInLevel(level, state, currentPos);
				
				if (level.getBlockEntity(currentPos) instanceof LayeredCannonBlockEntity layered) {				
					if (fullShape == null && connectedShapes.isEmpty()) {
						if (firstBlock) {
							CannonCastShape topShape = layered.getTopCannonShape();
							connectedShapes.add(topShape);
							firstBlock = false;
						} else if (!preAddedBlocks.isEmpty()) {
							break;
						} else {
							return false;
						}
					}
					LayeredCannonBlockEntity split = fullShape == null ? layered.getSplitBlockEntity(connectedShapes, offset == 0 ? null : opposite) : layered.getSplitBlockEntity(fullShape, opposite);
					if (split.isEmpty()) break;
					if (layered.getLayers().size() > 1 && offset == 0 && forcedDirection == this.orientation) {
						return false;
					}
					
					BlockState simpleState = split.getSimplifiedBlock().defaultBlockState();
					if (!simpleState.hasProperty(BlockStateProperties.FACING)) return false;
					simpleState = simpleState.setValue(BlockStateProperties.FACING, cBlock.getFacing(state));
					CompoundTag tag = this.getBlockEntityTag(split);
					preAddedBlocks.put(currentPos, Pair.of(new StructureBlockInfo(currentPos, simpleState, tag), split));
					
					connectedShapes.addAll(split.getLayers().keySet());
					Collection<CannonCastShape> connectedShapes1 = split.getConnectedTo(this.orientation);
					connectedShapes.removeIf(s -> !connectedShapes1.contains(s));
					fullShape = null;
				} else {
					CannonCastShape shape = cBlock.getShapeInLevel(level, state, currentPos);
					if (fullShape == null && !isConnected(connectedShapes, shape)) {
						if (firstBlock) {
							connectedShapes.add(shape);
							firstBlock = false;
						} else if (!preAddedBlocks.isEmpty()) {
							break;
						} else {
							return false;
						}
					}
					
					for (int back = 1; back < offset; ++back) {
						BlockPos backPos = currentPos.relative(opposite, back);
						if (!(level.getBlockEntity(backPos) instanceof LayeredCannonBlockEntity layered)) break;
						Pair<StructureBlockInfo, BlockEntity> backPair = preAddedBlocks.get(backPos);
						if (!(backPair.getRight() instanceof LayeredCannonBlockEntity layered1)) break;
						
						Set<CannonCastShape> backConnected = layered.getConnectedTo(this.orientation)
								.stream()
								.filter(s -> s.diameter() < shape.diameter())
								.collect(Collectors.toCollection(HashSet::new));
						if (backConnected.isEmpty()) break;
						Set<CannonCastShape> allSet = layered.getLayers().keySet()
								.stream()
								.filter(s -> s.diameter() < shape.diameter())
								.collect(Collectors.toCollection(HashSet::new));
						allSet.removeAll(backConnected);
						if (!allSet.isEmpty() && forcedDirection != this.orientation) return false;
						
						for (CannonCastShape shape1 : backConnected) {
							if (shape1.diameter() >= shape.diameter()) continue;
							layered1.setLayer(shape1, layered.getLayer(shape1));
							for (Direction dir : Iterate.directions) {
								layered1.setLayerConnectedTo(dir, shape1, layered.isLayerConnectedTo(dir, shape1));
							}
						}
						
						BlockState simpleState = layered1.getSimplifiedBlock().defaultBlockState();
						if (!simpleState.hasProperty(BlockStateProperties.FACING)) return false;
						simpleState = simpleState.setValue(BlockStateProperties.FACING, cBlock.getFacing(state));
						
						CompoundTag tag = this.getBlockEntityTag(layered1);
						preAddedBlocks.put(backPos, Pair.of(new StructureBlockInfo(backPos, simpleState, tag), layered1));
					}
					
					BlockEntity be = level.getBlockEntity(currentPos);
					preAddedBlocks.put(currentPos, Pair.of(new StructureBlockInfo(currentPos, state, this.getBlockEntityTag(be)), be));
					fullShape = isConnectedTo(level, shape, cBlock, state, currentPos, this.orientation) ? shape : null;
					connectedShapes.clear();
				}
			} else if (forcedDirection == this.orientation && (!state.getMaterial().isReplaceable() || !state.getCollisionShape(level, currentPos).isEmpty())) {
				return false;
			} else {
				break;
			}
		}
		
		if (this.material != null && !firstBlock) {
			preAddedBlocks.forEach(this::addBlock);
		} else {
			this.isActivated = false;
		}
		return true;
	}
	
	protected CompoundTag getBlockEntityTag(BlockEntity be) {
		if (be == null) return null;
		CompoundTag tag = be.saveWithFullMetadata();
		tag.remove("x");
		tag.remove("y");
		tag.remove("z");
		return tag;
	}
	
	private boolean isValidCannonBlock(Level level, BlockState state, BlockPos currentPos) {
		return state.getBlock() instanceof CannonBlock && level.getBlockEntity(currentPos) instanceof ICannonBlockEntity;
	}
	
	private static boolean matchesCannonAxis(BlockState state, Direction.Axis axis) {
		return ((CannonBlock) state.getBlock()).getFacing(state).getAxis() == axis;
	}
	
	private boolean matchesCurrentMaterial(Level level, BlockState state, BlockPos pos) {
		return this.material == null ? true : ((CannonBlock) state.getBlock()).getCannonMaterialInLevel(level, state, pos) == this.material;
	}
	
	private static boolean isConnectedTo(Level level, CannonCastShape shape, CannonBlock cBlock, BlockState state, BlockPos pos, Direction dir) {
		if (cBlock.getFacing(state).getAxis() != dir.getAxis()
		|| cBlock.getShapeInLevel(level, state, pos) != shape
		|| !(level.getBlockEntity(pos) instanceof ICannonBlockEntity cbe)
		|| !cbe.cannonBehavior().isConnectedTo(dir)) return false;
		
		BlockEntity be = level.getBlockEntity(pos.relative(dir));
		if (be instanceof LayeredCannonBlockEntity layered) {
			return layered.isLayerConnectedTo(dir.getOpposite(), shape);
		} else if (be instanceof ICannonBlockEntity cbe1) {
			return cbe1.cannonBehavior().isConnectedTo(dir.getOpposite());
		} else {
			return false;
		}
	}
	
	private static boolean isConnected(Collection<CannonCastShape> connected, CannonCastShape full) {
		return connected.stream().anyMatch(s -> s.diameter() <= full.diameter());
	}
	
	@Override
	protected void addBlock(BlockPos pos, Pair<StructureBlockInfo, BlockEntity> pair) {
		BlockPos offset = pos.relative(this.orientation, -this.initialExtensionProgress);
		super.addBlock(offset, pair);
		BlockEntity te = pair.getRight();
		if (te != null) this.presentTileEntities.put(offset.subtract(this.anchor), te);
	}
	
	@Override
	protected boolean customBlockPlacement(LevelAccessor level, BlockPos pos, BlockState state) {
		BlockPos levelPos = this.anchor.relative(this.orientation, -1);
		BlockState builderState = level.getBlockState(levelPos);
		BlockEntity blockEntity = level.getBlockEntity(levelPos);
		if (!(blockEntity instanceof CannonBuilderBlockEntity builder) || blockEntity.isRemoved()) return true;
		
		if (pos.equals(levelPos)) {
			if (CBCBlocks.CANNON_BUILDER_HEAD.has(state) && CBCBlocks.CANNON_BUILDER.has(builderState)) {
				BuilderState bState = state.getValue(CannonBuilderHeadBlock.ATTACHED) ? BuilderState.ACTIVATED : BuilderState.UNACTIVATED;
				level.setBlock(levelPos, builderState.setValue(CannonBuilderBlock.STATE, bState), 3 | 16);
			}
			return true;
		}
		
		if (builder.movedContraption != null) {
			BlockPos entityAnchor = new BlockPos(builder.movedContraption.getAnchorVec().add(0.5d, 0.5d, 0.5d));
			
			BlockPos blockPos = pos.subtract(entityAnchor);
			StructureBlockInfo blockInfo = this.getBlocks().get(blockPos);
			BlockEntity blockEntity1 = level.getBlockEntity(pos);
			
			if (blockEntity1 instanceof LayeredCannonBlockEntity layered) {
				if (blockInfo.nbt == null || !blockInfo.nbt.contains("id")) return true;
				BlockEntity infoBE = BlockEntity.loadStatic(blockPos, builderState, blockInfo.nbt);
				if (!(infoBE instanceof LayeredCannonBlockEntity layered1)) return true;
				layered.addLayersOfOther(layered1);
				layered.updateBlockstate();
				layered.sendData();
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected boolean customBlockRemoval(LevelAccessor level, BlockPos pos, BlockState state) {
		BlockPos levelPos = this.anchor.relative(this.orientation, -1);
		BlockState builderState = level.getBlockState(levelPos);
		if (!(level.getBlockEntity(levelPos) instanceof CannonBuilderBlockEntity builder)) return true;
		
		if (pos.equals(levelPos) && CBCBlocks.CANNON_BUILDER.has(builderState)) {
			level.setBlock(levelPos, builderState.setValue(CannonBuilderBlock.STATE, BuilderState.MOVING), 66 | 16);
			return true;
		}
		
		BlockPos blockPos = pos.relative(this.orientation, -this.initialExtensionProgress).subtract(this.anchor);
		StructureBlockInfo blockInfo = this.getBlocks().get(blockPos);
		BlockEntity blockEntity1 = level.getBlockEntity(pos);
		
		if (blockInfo != null && blockEntity1 instanceof LayeredCannonBlockEntity layered) {
			if (blockInfo.nbt == null || !blockInfo.nbt.contains("id")) return true;
			BlockEntity infoBE = BlockEntity.loadStatic(blockPos, builderState, blockInfo.nbt);
			if (!(infoBE instanceof LayeredCannonBlockEntity layered1)) return true;
			layered.removeLayersOfOther(layered1);
			layered.updateBlockstate();
			layered.sendData();
			return true;
		}
		
		return false;
	}
	
	@Override
	public Set<BlockPos> getColliders(Level level, Direction movementDirection) {
		if (!this.isActivated) return super.getColliders(level, movementDirection);
		if (this.getBlocks() == null) return Collections.emptySet();
		if (this.cachedColliders == null || this.cachedColliderDirection != movementDirection) {
			this.cachedColliders = new HashSet<>();
			this.cachedColliderDirection = movementDirection;
			
			for (StructureBlockInfo blockInfo : this.getBlocks().values()) {
				BlockPos offsetPos = blockInfo.pos.relative(movementDirection);
				if (blockInfo.state.getCollisionShape(level, offsetPos).isEmpty()) continue;
				if (CBCBlocks.CANNON_BUILDER_HEAD.has(blockInfo.state) && movementDirection != this.orientation) continue;
				
				if (this.getBlocks().containsKey(offsetPos)) {
					StructureBlockInfo offsetInfo = this.getBlocks().get(offsetPos);
					if (!offsetInfo.state.getCollisionShape(level, offsetPos).isEmpty()
						&& (AllBlocks.PISTON_EXTENSION_POLE.has(blockInfo.state) || CBCBlocks.CANNON_BUILDER_HEAD.has(offsetInfo.state))) continue;
				}
				
				this.cachedColliders.add(blockInfo.pos);
			}
		}
		return this.cachedColliders;
	}
	
	private static boolean isBuilderHead(BlockState state) {
		return CBCBlocks.CANNON_BUILDER_HEAD.has(state);
	}
	
	@Override
	public CompoundTag writeNBT(boolean spawnPacket) {
		CompoundTag tag = super.writeNBT(spawnPacket);
		tag.putBoolean("Activated", this.isActivated);
		if (this.material != null) tag.putString("Material", this.material.name().toString());
		return tag;
	}
	
	@Override
	public void readNBT(Level level, CompoundTag tag, boolean spawnData) {
		super.readNBT(level, tag, spawnData);
		this.isActivated = tag.getBoolean("Activated");
		
		this.material = CannonMaterial.fromName(new ResourceLocation(tag.getString("Material")));
		if (this.material == null) this.material = CannonMaterial.STEEL;
	}

	@Override protected ContraptionType getType() { return CBCContraptionTypes.CANNON_BUILDER; }

}
