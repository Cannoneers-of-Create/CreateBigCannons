package rbasamoyai.createbigcannons.cannonloading;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.simibubi.create.content.contraptions.AssemblyException;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.base.PoleContraption;
import rbasamoyai.createbigcannons.base.PoleMoverBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CannonLoaderBlockEntity extends PoleMoverBlockEntity {

	protected int extensionLength;

	private Map<BlockPos, BlockState> encounteredBlocks = new HashMap<>();

	public CannonLoaderBlockEntity(BlockEntityType<? extends CannonLoaderBlockEntity> typeIn, BlockPos pos, BlockState state) {
		super(typeIn, pos, state);
	}

	@Override
	protected PoleContraption innerAssemble() throws AssemblyException {
		if (!(this.level.getBlockState(this.worldPosition).getBlock() instanceof CannonLoaderBlock)) return null;
		this.encounteredBlocks.clear();

		Direction facing = this.getBlockState().getValue(CannonLoaderBlock.FACING);
		CannonLoadingContraption contraption = new CannonLoadingContraption(facing, this.getMovementSpeed() < 0);
		if (!contraption.assemble(this.level, this.worldPosition)) return null;

		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, facing.getAxis());
		Direction movementDirection = (this.getSpeed() > 0) ^ facing.getAxis() != Direction.Axis.Z ? positive : positive.getOpposite();
		BlockPos anchor = contraption.anchor.relative(facing, contraption.initialExtensionProgress());
		return CannonLoaderCollider.isCollidingWithWorld(this.level, contraption, anchor.relative(movementDirection), movementDirection) ? null : contraption;
	}

	@Override
	public void disassemble() {
		if (!this.running && this.movedContraption == null) return;
		if (!this.remove) {
			this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(CannonLoaderBlock.MOVING, false), 3 | 16);
		}
		super.disassemble();
		this.encounteredBlocks.clear();
	}

	@Override
	protected boolean moveAndCollideContraption() {
		if (this.movedContraption == null) {
			return false;
		}
		if (this.movedContraption.isStalled()) {
			this.movedContraption.setDeltaMovement(Vec3.ZERO);
			return false;
		}
		Vec3 motion = this.getMotionVector();
		this.movedContraption.setContraptionMotion(motion);
		this.movedContraption.move(motion.x, motion.y, motion.z);
		return CannonLoaderCollider.collideBlocks(this.movedContraption);
	}

	@Override
	public void tick() {
		super.tick();

		if (this.level != null && !this.level.isClientSide && !intersectionLoadingEnabled()
			&& this.movedContraption != null && this.movedContraption.getContraption() instanceof CannonLoadingContraption loader
			&& this.checkForNewPlacedBlocks()) {
			loader.setBrokenDisassembly();
			this.disassemble();
		}
	}

	private boolean checkForNewPlacedBlocks() {
		BlockPos anchor = new BlockPos(this.movedContraption.getAnchorVec());
		Map<BlockPos, StructureBlockInfo> contraptionBlocks = this.movedContraption.getContraption().getBlocks();
		Set<BlockPos> encountered = new HashSet<>();
		for (BlockPos lpos : contraptionBlocks.keySet()) {
			BlockPos gpos = anchor.offset(lpos);
			encountered.add(gpos);
			if (this.encounteredBlocks.containsKey(gpos)) {
				BlockState newState = this.level.getBlockState(gpos);
				if (!this.encounteredBlocks.get(gpos).equals(newState) && canBreakLoader(newState)) {
					return true;
				}
			}
			this.encounteredBlocks.put(gpos, this.level.getBlockState(gpos));
		}
		this.encounteredBlocks.entrySet().removeIf(e -> !encountered.contains(e.getKey()));
		return false;
	}

	private static boolean canBreakLoader(BlockState state) {
		return !state.getMaterial().isReplaceable() || state.getMaterial().isSolid();
	}

	private static boolean intersectionLoadingEnabled() {
		return CBCConfigs.SERVER.kinetics.enableIntersectionLoading.get();
	}

}
