package rbasamoyai.createbigcannons.cannonloading;

import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.contraptions.render.NonStationaryLighter;
import com.simibubi.create.infrastructure.config.AllConfigs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannonloading.CannonLoadingContraption.LoadingHead;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCContraptionTypes;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;

public class GantryRammerContraption extends TranslatingContraption {
    protected Direction facing;
    protected Direction movementAxis;
    protected final LoadingHead loadingHead = LoadingHead.RAM_HEAD; // Kept for verbosity

    public GantryRammerContraption() {}
    public GantryRammerContraption(Direction faceDir, Direction moveAxis) {
        this.facing = faceDir;
        this.movementAxis = moveAxis;
    }

    @Override
    public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
        if (!searchMovedStructure(world, pos, this.movementAxis)) {
            return false;
        }

        startMoving(world);
        return true;
    }

    @Override
    protected boolean moveBlock(Level world, @Nullable Direction forcedDirection, Queue<BlockPos> frontier,
                                Set<BlockPos> visited) throws AssemblyException {
        BlockPos pos = frontier.poll();
        if (pos == null) {
            return false;
        }
        visited.add(pos);

        if (world.isOutsideBuildHeight(pos)) return true;
        if (!world.isLoaded(pos)) throw AssemblyException.unloadedChunk(pos);
        if (this.isAnchoringBlockAt(pos)) {
            return true;
        }
        if (forcedDirection == null) return true;

        BlockPos ahead = pos.relative(forcedDirection);
        BlockState state = world.getBlockState(ahead);
        if (this.isAnchoringBlockAt(ahead)) {
            return true;
        }
        if (!visited.contains(ahead)) {
            if (this.isValidLoadBlock(state, world, ahead)) {
                frontier.add(ahead);
            }

            if (this.isValidCannonBlock(world, state, ahead) && this.matchesCannonAxis(state, forcedDirection.getAxis())) {
                BlockEntity blockEntity = world.getBlockEntity(ahead);
                if (!(blockEntity instanceof IBigCannonBlockEntity cannon)) return true;
                StructureBlockInfo blockInfo = cannon.cannonBehavior().block();
                if (this.isValidLoadBlock(blockInfo.state, world, ahead)) {
                    frontier.add(ahead);
                }
            }
        }

        this.addBlock(pos, this.capture(world, pos));
        if (this.blocks.size() <= AllConfigs.server().kinetics.maxBlocksMoved.get()) {
            return true;
        }
        throw AssemblyException.structureTooLarge();
    }

    @Override
    protected void addBlock(BlockPos pos, Pair<StructureBlockInfo, BlockEntity> pair) {
        BlockEntity blockEntity = pair.getRight();
        if (blockEntity instanceof IBigCannonBlockEntity cannon) {
            StructureBlockInfo containedInfo = cannon.cannonBehavior().block();
            BlockEntity containedBlockEntity = null;
            if (containedInfo.nbt != null) {
                containedInfo.nbt.putInt("x", pos.getX());
                containedInfo.nbt.putInt("y", pos.getY());
                containedInfo.nbt.putInt("z", pos.getZ());
                containedBlockEntity = BlockEntity.loadStatic(pos, containedInfo.state, containedInfo.nbt);
            }
            pair = Pair.of(containedInfo, containedBlockEntity);
        }
        super.addBlock(pos, pair);
    }

    @Override
    protected boolean addToInitialFrontier(Level level, BlockPos start, Direction forcedDirection, Queue<BlockPos> frontier) throws AssemblyException {
        for (int offset = 1; offset <= CBCConfigs.SERVER.kinetics.maxLoaderLength.get(); ++offset) {
            BlockPos currentPos = start.relative(forcedDirection, offset);
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
                if (this.isValidLoadBlock(blockInfo.state, level, currentPos)) {
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

    @Override
    protected boolean customBlockPlacement(LevelAccessor level, BlockPos pos, BlockState state) {
        AbstractContraptionEntity contraptionEntity = this.entity;
        if (contraptionEntity instanceof GantryRammerContraptionEntity grce) {
            BlockPos entityAnchor = new BlockPos(grce.getAnchorVec().add(0.5d, 0.5d, 0.5d));

            BlockPos blockPos = pos.subtract(entityAnchor);
            StructureBlockInfo blockInfo = this.getBlocks().get(blockPos);
            BlockEntity blockEntity1 = level.getBlockEntity(pos);

            if (blockEntity1 instanceof IBigCannonBlockEntity cannon) {
                return cannon.cannonBehavior().tryLoadingBlock(blockInfo);
            }
        }

        return false;
    }

    @Override
    protected boolean customBlockRemoval(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity1 = level.getBlockEntity(pos);

        if (blockEntity1 instanceof IBigCannonBlockEntity cannon) {
            cannon.cannonBehavior().removeBlock();
            return true;
        }

        return false;
    }

    private boolean isValidCannonBlock(LevelAccessor level, BlockState state, BlockPos pos) {
        return state.getBlock() instanceof BigCannonBlock && level.getBlockEntity(pos) instanceof IBigCannonBlockEntity;
    }

    private boolean matchesCannonAxis(BlockState state, Direction.Axis axis) {
        return ((BigCannonBlock) state.getBlock()).getFacing(state).getAxis() == axis;
    }

    private boolean isValidLoadBlock(BlockState state, Level level, BlockPos pos) {
        Direction.Axis axis = this.movementAxis.getAxis();
        if (state.getBlock() instanceof BigCannonPropellantBlock propellant) return propellant.canBeLoaded(state, axis);
        if (state.getBlock() instanceof ProjectileBlock) {
            return state.getValue(BlockStateProperties.FACING).getAxis() == axis;
        }
        return false;
    }


    @Override
    public CompoundTag writeNBT(boolean spawnPacket) {
        CompoundTag tag = super.writeNBT(spawnPacket);
        tag.putInt("Facing", facing.get3DDataValue());
        return tag;
    }

    @Override
    public void readNBT(Level world, CompoundTag tag, boolean spawnData) {
        facing = Direction.from3DDataValue(tag.getInt("Facing"));

        super.readNBT(world, tag, spawnData);
    }

    @Override
    protected boolean isAnchoringBlockAt(BlockPos pos) {
        return super.isAnchoringBlockAt(pos.relative(facing));
    }

    @Override
	public ContraptionType getType() {
        return CBCContraptionTypes.RAMMER;
    }

    public Direction getFacing() {
        return facing;
    }

    @Override
    protected boolean shouldUpdateAfterMovement(StructureBlockInfo info) {
        return super.shouldUpdateAfterMovement(info) && !CBCBlocks.GANTRY_RAMMER_CARRIAGE.has(info.state);
    }

	@Environment(EnvType.CLIENT)
    public ContraptionLighter<?> makeLighter() {
        return new NonStationaryLighter<>(this);
    }
}
