package rbasamoyai.createbigcannons.cannon_loading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.piston.PistonExtensionPoleBlock;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import rbasamoyai.createbigcannons.base.PoleContraption;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCContraptionTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;
import rbasamoyai.createbigcannons.remix.HasFragileContraption;

public class CannonLoadingContraption extends PoleContraption implements CanLoadBigCannon, HasFragileContraption {

	protected LoadingHead loadingHead = LoadingHead.NOTHING;

	private static final DirectionProperty FACING = BlockStateProperties.FACING;
	private static final BooleanProperty MOVING = CannonLoaderBlock.MOVING;

	private boolean brokenDisassembly = false;
	private final Set<BlockPos> fragileBlocks = new HashSet<>();
	private final Set<BlockPos> colliderBlocks = new HashSet<>();
	private final Map<BlockPos, BlockState> encounteredBlocks = new HashMap<>();

	public CannonLoadingContraption() {
	}

	public CannonLoadingContraption(Direction direction, boolean retract) {
		super(direction, retract);
	}

	@Override
	protected boolean collectExtensions(Level level, BlockPos pos, Direction direction) throws AssemblyException {
		if (!CBCBlocks.CANNON_LOADER.has(level.getBlockState(pos))) return false;

		List<StructureBlockInfo> poles = new ArrayList<>();
		BlockPos start = pos;
		BlockState nextBlock = level.getBlockState(start.relative(direction));
		int extensionsInFront = 0;
		Direction.Axis blockAxis = direction.getAxis();
		this.loadingHead = LoadingHead.NOTHING;

		PistonExtensionPoleBlock.PlacementHelper matcher = PistonExtensionPoleBlock.PlacementHelper.get();
		while (matcher.matchesAxis(nextBlock, blockAxis)
			|| this.isValidLoaderHead(nextBlock) && nextBlock.getValue(FACING).getAxis() == blockAxis
			|| this.isValidCannonBlock(level, nextBlock, start.relative(direction)) && this.matchesCannonAxis(nextBlock, blockAxis)) {
			start = start.relative(direction);

			if (this.isValidCannonBlock(level, nextBlock, start)) {
				StructureBlockInfo containedBlock = ((IBigCannonBlockEntity) level.getBlockEntity(start)).cannonBehavior().block();
				nextBlock = containedBlock.state();
				if (matcher.matchesAxis(nextBlock, blockAxis)) {
					poles.add(new StructureBlockInfo(start, nextBlock.setValue(FACING, direction), null));
				} else if (this.isValidLoaderHead(nextBlock)) {
					poles.add(new StructureBlockInfo(start, nextBlock.setValue(FACING, direction), null));
					if (CBCBlocks.RAM_HEAD.has(nextBlock)) {
						this.loadingHead = LoadingHead.RAM_HEAD;
					} else {
						this.loadingHead = LoadingHead.WORM_HEAD;
					}
					break;
				} else if (nextBlock.isAir()) {
					break;
				}
			} else {
				poles.add(new StructureBlockInfo(start, nextBlock.setValue(FACING, direction), null));

				if (this.isValidLoaderHead(nextBlock)) {
					if (CBCBlocks.RAM_HEAD.has(nextBlock)) {
						this.loadingHead = LoadingHead.RAM_HEAD;
					} else {
						this.loadingHead = LoadingHead.WORM_HEAD;
					}
					break;
				}
			}

			extensionsInFront++;

			nextBlock = level.getBlockState(start.relative(direction));

			if (extensionsInFront > CannonLoaderBlock.maxAllowedLoaderLength()) {
				throw AssemblyException.tooManyPistonPoles();
			}
		}

		poles.add(new StructureBlockInfo(pos, AllBlocks.PISTON_EXTENSION_POLE.getDefaultState().setValue(FACING, direction), null));

		BlockPos end = pos;
		int extensionsInBack = 0;
		Direction opposite = direction.getOpposite();
		nextBlock = level.getBlockState(end.relative(opposite));

		while (matcher.matchesAxis(nextBlock, blockAxis)) {
			end = end.relative(opposite);
			poles.add(new StructureBlockInfo(end, nextBlock.setValue(FACING, direction), null));
			extensionsInBack++;
			nextBlock = level.getBlockState(end.relative(opposite));
			nextBlock = ContraptionRemix.getInnerCannonState(level, nextBlock, end.relative(opposite), direction);

			if (extensionsInFront + extensionsInBack > CannonLoaderBlock.maxAllowedLoaderLength()) {
				throw AssemblyException.tooManyPistonPoles();
			}
		}

		this.extensionLength = extensionsInFront + extensionsInBack;

		if (this.extensionLength == 0) {
			throw AssemblyException.noPistonPoles();
		}

		this.anchor = pos.relative(direction, this.initialExtensionProgress + 2);
		if (this.loadingHead == LoadingHead.NOTHING) this.anchor = this.anchor.relative(direction, -1);
		this.initialExtensionProgress = extensionsInFront;
		this.pistonContraptionHitbox = new AABB(
			BlockPos.ZERO.relative(direction, -1),
			BlockPos.ZERO.relative(direction, -this.extensionLength - 2))
			.expandTowards(1, 1, 1);

		this.bounds = new AABB(0, 0, 0, 0, 0, 0);

		for (StructureBlockInfo pole : poles) {
			BlockPos relPos = pole.pos().relative(direction, -extensionsInFront);
			BlockPos localPos = relPos.subtract(this.anchor);
			this.getBlocks().put(localPos, new StructureBlockInfo(localPos, pole.state(), null));
		}

		return true;
	}

	@Override
	protected boolean moveBlock(Level level, Direction direction, Queue<BlockPos> frontier, Set<BlockPos> visited) throws AssemblyException {
		BlockPos pos = frontier.poll();
		if (pos == null) return false;
		visited.add(pos);

		if (level.isOutsideBuildHeight(pos)) return true;
		if (!level.isLoaded(pos)) throw AssemblyException.unloadedChunk(pos);
		if (this.isAnchoringBlockAt(pos)) return true;

		BlockPos ahead = pos.relative(direction);
		BlockState state = level.getBlockState(ahead);
		if (this.isAnchoringBlockAt(ahead)) return true;
		if (!visited.contains(ahead)) {
			if (this.isValidLoadBlock(state, level, ahead)) {
				frontier.add(ahead);
			}

			if (this.isValidCannonBlock(level, state, ahead) && this.matchesCannonAxis(state, direction.getAxis())) {
				BlockEntity blockEntity = level.getBlockEntity(ahead);
				if (!(blockEntity instanceof IBigCannonBlockEntity cannon)) return true;
				StructureBlockInfo blockInfo = cannon.cannonBehavior().block();
				if (this.isValidLoadBlock(blockInfo.state(), level, ahead)) {
					frontier.add(ahead);
				}
			}
		}

		this.addBlock(pos, this.capture(level, pos));
		if (this.blocks.size() <= AllConfigs.server().kinetics.maxBlocksMoved.get()) {
			return true;
		}
		throw AssemblyException.structureTooLarge();
	}

	private boolean isValidLoaderHead(BlockState state) {
		return CBCBlocks.RAM_HEAD.has(state) || CBCBlocks.WORM_HEAD.has(state);
	}

	private boolean isValidCannonBlock(LevelAccessor level, BlockState state, BlockPos pos) {
		return state.getBlock() instanceof BigCannonBlock && level.getBlockEntity(pos) instanceof IBigCannonBlockEntity;
	}

	private boolean matchesCannonAxis(BlockState state, Direction.Axis axis) {
		return ((BigCannonBlock) state.getBlock()).getFacing(state).getAxis() == axis;
	}

	@Override
	protected void addBlock(BlockPos pos, Pair<StructureBlockInfo, BlockEntity> pair) {
		BlockEntity blockEntity = pair.getRight();
		if (blockEntity instanceof IBigCannonBlockEntity cannon) {
			StructureBlockInfo containedInfo = cannon.cannonBehavior().block();
			BlockEntity containedBlockEntity = null;
			CompoundTag infoNbt = containedInfo.nbt();
			if (infoNbt != null) {
				infoNbt.putInt("x", pos.getX());
				infoNbt.putInt("y", pos.getY());
				infoNbt.putInt("z", pos.getZ());
				containedBlockEntity = BlockEntity.loadStatic(pos, containedInfo.state(), infoNbt);
			}
			pair = Pair.of(containedInfo, containedBlockEntity);
		}
		super.addBlock(pos.relative(this.orientation, -this.initialExtensionProgress), pair);
	}

	@Override
	protected boolean addToInitialFrontier(Level level, BlockPos pos, Direction forcedDirection, Queue<BlockPos> frontier) throws AssemblyException {
		frontier.clear();
		if (this.loadingHead == LoadingHead.NOTHING) return true;
		boolean retracting = forcedDirection != this.orientation;
		if (retracting != (this.loadingHead == LoadingHead.WORM_HEAD)) return true;

		for (int offset = 0; offset <= AllConfigs.server().kinetics.maxChassisRange.get(); ++offset) {
			if (offset == 1 && retracting) return true;
			BlockPos currentPos = pos.relative(this.orientation, offset + this.initialExtensionProgress);
			if (retracting && level.isOutsideBuildHeight(currentPos)) {
				return true;
			}
			if (!level.isLoaded(currentPos)) {
				throw AssemblyException.unloadedChunk(currentPos);
			}
			BlockState state = level.getBlockState(currentPos);
			if (this.isValidLoadBlock(state, level, currentPos)) {
				frontier.add(currentPos);
			} else if (this.isValidCannonBlock(level, state, currentPos) && this.matchesCannonAxis(state, forcedDirection.getAxis())) {
				BlockEntity blockEntity = level.getBlockEntity(currentPos);
				if (!(blockEntity instanceof IBigCannonBlockEntity cannon)) return true;
				StructureBlockInfo blockInfo = cannon.cannonBehavior().block();
				if (this.isValidLoadBlock(blockInfo.state(), level, currentPos)) {
					frontier.add(currentPos);
				} else {
					return true;
				}
			} else {
				return true;
			}
		}

		return true;
	}

	private boolean isValidLoadBlock(BlockState state, Level level, BlockPos pos) {
		Direction.Axis axis = this.orientation.getAxis();
		if (state.getBlock() instanceof BigCannonPropellantBlock propellant) return propellant.canBeLoaded(state, axis);
		if (state.getBlock() instanceof ProjectileBlock) {
			return state.getValue(FACING).getAxis() == axis;
		}
		return false;
	}

	@Override
	protected boolean customBlockPlacement(LevelAccessor level, BlockPos pos, BlockState state) {
		BlockPos levelPos = this.anchor.relative(this.orientation, this.loadingHead == LoadingHead.NOTHING ? -1 : -2);
		BlockState loaderState = level.getBlockState(levelPos);
		BlockEntity blockEntity = level.getBlockEntity(levelPos);

		if (pos.equals(levelPos)) {
			if (blockEntity instanceof CannonLoaderBlockEntity && !blockEntity.isRemoved())
				level.setBlock(levelPos, loaderState.setValue(MOVING, false), 3 | 16);
			return true;
		}
		// Loading code moved to ContraptionMixin and ContraptionRemix
		return false;
	}

	@Override
	protected boolean customBlockRemoval(LevelAccessor level, BlockPos pos, BlockState state) {
		BlockPos loaderPos = this.anchor.relative(this.orientation, this.loadingHead == LoadingHead.NOTHING ? -1 : -2);
		BlockState loaderState = level.getBlockState(loaderPos);
		if (pos.equals(loaderPos) && CBCBlocks.CANNON_LOADER.has(loaderState)) {
			level.setBlock(loaderPos, loaderState.setValue(MOVING, true), 66 | 16);
			return true;
		}
		// Loading code moved to ContraptionMixin and ContraptionRemix
		return false;
	}

	@Override public void createbigcannons$setBrokenDisassembly(boolean flag) { this.brokenDisassembly = flag; }

	@Override public boolean createbigcannons$isBrokenDisassembly() { return this.brokenDisassembly; }

	@Override public BlockPos createbigcannons$toLocalPos(BlockPos globalPos) { return this.toLocalPos(globalPos); }

	@Override public Set<BlockPos> createbigcannons$getFragileBlockPositions() { return this.fragileBlocks; }

    @Override public Set<BlockPos> createbigcannons$getCannonLoadingColliders() { return this.colliderBlocks; }

    @Override
	@Nullable
	public Direction createbigcannons$getAssemblyMovementDirection(Level level) {
		return this.orientation != null && this.retract ? this.orientation.getOpposite() : this.orientation;
	}

	@Nullable
	@Override
	public Direction createbigcannons$getOriginalForcedDirection(Level level) {
		return this.orientation != null && this.retract ? this.orientation.getOpposite() : this.orientation;
	}

	@Override
	public void readNBT(Level level, CompoundTag tag, boolean spawnData) {
		super.readNBT(level, tag, spawnData);
		this.loadingHead = LoadingHead.fromOrdinal(tag.getInt("LoadingHead"));
	}

	@Override
	public CompoundTag writeNBT(boolean spawnPacket) {
		CompoundTag tag = super.writeNBT(spawnPacket);
		tag.putInt("LoadingHead", this.loadingHead == null ? LoadingHead.NOTHING.ordinal() : this.loadingHead.ordinal());
		return tag;
	}

	@Override
	public ContraptionType getType() {
		return CBCContraptionTypes.CANNON_LOADER;
	}

	@Override public Map<BlockPos, BlockState> createbigcannons$getEncounteredBlocks() { return this.encounteredBlocks; }

	@Override
	public boolean createbigcannons$blockBreaksDisassembly(Level level, BlockPos pos, BlockState newState) {
		BlockPos loaderPos = this.anchor.relative(this.orientation, this.loadingHead == LoadingHead.NOTHING ? -1 : -2);
		BlockState loaderState = level.getBlockState(loaderPos);
		if (pos.equals(loaderPos) && CBCBlocks.CANNON_LOADER.has(loaderState))
			return false;
		return HasFragileContraption.defaultBlockBreaksAssembly(level, pos, newState, this);
	}

	@Override public boolean createbigcannons$shouldCheckFragility() { return HasFragileContraption.defaultShouldCheck(); }

	@Override
	public void createbigcannons$fragileDisassemble() {
		BlockPos loaderPos = this.anchor.relative(this.orientation, -1);
		if (this.world.getBlockEntity(loaderPos) instanceof CannonLoaderBlockEntity loaderBE) {
			loaderBE.disassemble();
		} else {
			this.entity.disassemble();
		}
	}

	public enum LoadingHead {
		RAM_HEAD,
		WORM_HEAD,
		NOTHING;

		private static final LoadingHead[] VALUES = values();

		public static LoadingHead fromOrdinal(int ordinal) {
			return ordinal <= 0 && ordinal < VALUES.length ? VALUES[ordinal] : NOTHING;
		}
	}

}
