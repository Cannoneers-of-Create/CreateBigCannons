package rbasamoyai.createbigcannons.cannonloading;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class GantryRammerBlockEntity extends KineticBlockEntity implements IDisplayAssemblyExceptions {

    boolean assembleNextTick;
    protected AssemblyException lastException;

    public GantryRammerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        registerAwardables(behaviours, AllAdvancements.CONTRAPTION_ACTORS);
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
    }

    public void checkValidGantryShaft() {
        if (shouldAssemble())
            queueAssembly();
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!getBlockState().canSurvive(level, worldPosition))
            level.destroyBlock(worldPosition, true);
    }

    public void queueAssembly() {
        assembleNextTick = true;
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide)
            return;

        if (assembleNextTick) {
            tryAssemble();
            assembleNextTick = false;
        }
    }

    @Override
    public AssemblyException getLastAssemblyException() {
        return lastException;
    }

    private void tryAssemble() {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof GantryRammerBlock))
            return;

        Direction direction = blockState.getValue(GantryRammerBlock.FACING);

        BlockEntity shaftTe = level.getBlockEntity(worldPosition.relative(direction.getOpposite()));
        if (!(shaftTe instanceof GantryShaftBlockEntity))
            return;
        BlockState shaftState = shaftTe.getBlockState();
        if (!AllBlocks.GANTRY_SHAFT.has(shaftState))
            return;

        float pinionMovementSpeed = ((GantryShaftBlockEntity) shaftTe).getPinionMovementSpeed();
        Direction shaftOrientation = shaftState.getValue(GantryShaftBlock.FACING);
        Direction movementDirection = shaftOrientation;
        if (pinionMovementSpeed < 0)
            movementDirection = movementDirection.getOpposite();

        GantryRammerContraption contraption = new GantryRammerContraption(direction, movementDirection);

        try {
            lastException = null;
            if (!contraption.assemble(level, worldPosition))
                return;

            sendData();
        } catch (AssemblyException e) {
            lastException = e;
            sendData();
            return;
        }
        if (GantryRammerCollider.isCollidingWithWorld(level, contraption, worldPosition.relative(movementDirection),
                movementDirection)) {
            return;
        }

        contraption.removeBlocksFromWorld(level, BlockPos.ZERO);
        GantryRammerContraptionEntity movedContraption =
                GantryRammerContraptionEntity.create(level, contraption, shaftOrientation);
        BlockPos anchor = worldPosition;
        movedContraption.setPos(anchor.getX(), anchor.getY(), anchor.getZ());
        AllSoundEvents.CONTRAPTION_ASSEMBLE.playOnServer(level, worldPosition);
        level.addFreshEntity(movedContraption);
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        AssemblyException.write(compound, lastException);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        lastException = AssemblyException.read(compound);
        super.read(compound, clientPacket);
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff,
                                     boolean connectedViaAxes, boolean connectedViaCogs) {
        float defaultModifier =
                super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);

        if (connectedViaAxes)
            return defaultModifier;
        if (!AllBlocks.GANTRY_SHAFT.has(stateTo))
            return defaultModifier;
        if (!stateTo.getValue(GantryShaftBlock.POWERED))
            return defaultModifier;

        Direction direction = Direction.getNearest(diff.getX(), diff.getY(), diff.getZ());
        if (stateFrom.getValue(GantryRammerBlock.FACING) != direction.getOpposite())
            return defaultModifier;
        return getGantryPinionModifier(stateTo.getValue(GantryShaftBlock.FACING), stateFrom.getValue(GantryRammerBlock.FACING));
    }

    public static float getGantryPinionModifier(Direction shaft, Direction pinionDirection) {
        Direction.Axis shaftAxis = shaft.getAxis();
        float directionModifier = shaft.getAxisDirection()
                .getStep();
        if (shaftAxis == Direction.Axis.Y)
            if (pinionDirection == Direction.NORTH || pinionDirection == Direction.EAST)
                return -directionModifier;
        if (shaftAxis == Direction.Axis.X)
            if (pinionDirection == Direction.DOWN || pinionDirection == Direction.SOUTH)
                return -directionModifier;
        if (shaftAxis == Direction.Axis.Z)
            if (pinionDirection == Direction.UP || pinionDirection == Direction.WEST)
                return -directionModifier;
        return directionModifier;
    }

    private boolean shouldAssemble() {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof GantryRammerBlock))
            return false;
        Direction facing = blockState.getValue(GantryRammerBlock.FACING)
                .getOpposite();
        BlockState shaftState = level.getBlockState(worldPosition.relative(facing));
        if (!(shaftState.getBlock() instanceof GantryShaftBlock))
            return false;
        if (shaftState.getValue(GantryShaftBlock.POWERED))
            return false;
        BlockEntity te = level.getBlockEntity(worldPosition.relative(facing));
        return te instanceof GantryShaftBlockEntity && ((GantryShaftBlockEntity) te).canAssembleOn();
    }
}
