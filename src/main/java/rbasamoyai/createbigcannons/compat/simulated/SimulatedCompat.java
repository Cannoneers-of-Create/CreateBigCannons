package rbasamoyai.createbigcannons.compat.simulated;

import java.util.List;
import java.util.Set;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.contraption.BlockMovementChecks;

import dev.simulated_team.simulated.index.SimBlockMovementChecks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.ExtendsCannonMount;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlock;
import rbasamoyai.createbigcannons.cannons.CannonContraptionProviderBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.IAutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.screw_breech.ScrewBreechBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class SimulatedCompat {

    public static void onModCtor(IEventBus modBus, IEventBus forgeBus) {
        SimBlockMovementChecks.registerAdditionalBlocks(SimulatedCompat::attachCannonBlocksFromMount);

        SimBlockMovementChecks.registerAttachedCheck(SimulatedCompat::attachedCheckMountExtensionBlocks);
        SimBlockMovementChecks.registerAttachedCheck(SimulatedCompat::attachedCheckCannons);
        SimBlockMovementChecks.registerAttachedCheck(SimulatedCompat::attachedCheckAutocannons);
        SimBlockMovementChecks.registerAttachedCheck(SimulatedCompat::attachedCheckCannonLoader);
        SimBlockMovementChecks.registerAttachedCheck(SimulatedCompat::attachedCheckCannonDrill);
        SimBlockMovementChecks.registerAttachedCheck(SimulatedCompat::attachedCheckCannonBuilder);
    }

    private static Iterable<BlockPos> attachCannonBlocksFromMount(BlockState state, Level level, BlockPos pos, Set<BlockPos> visited) {
        BlockPos assemblyPos = null;

        if (CBCBlocks.CANNON_MOUNT.has(state)) {
            Direction vertical = state.getValue(BlockStateProperties.VERTICAL_DIRECTION);
            assemblyPos = pos.relative(vertical, -2);
        } else if (CBCBlocks.FIXED_CANNON_MOUNT.has(state)) {
            assemblyPos = pos.relative(state.getValue(FixedCannonMountBlock.FACING));
        } else if (CBCBlocks.CANNON_CARRIAGE.has(state)) {
            assemblyPos = pos.above();
        }

        if (assemblyPos == null || level.isOutsideBuildHeight(assemblyPos))
            return List.of();
        BlockState state1 = level.getBlockState(assemblyPos);
        if (!(state1.getBlock() instanceof CannonContraptionProviderBlock provider))
            return List.of();

        Direction facing = provider.getFacing(state1);
        if (CBCBlocks.CANNON_MOUNT.has(state) && facing.getAxis().isHorizontal()
            && facing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis()) {
            return List.of();
        } else if (CBCBlocks.CANNON_CARRIAGE.has(state) && facing.getAxis().isHorizontal()
            && facing.getAxis() != state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis()) {
            return List.of();
        }
        return visited.contains(assemblyPos) ? List.of() : List.of(assemblyPos);
    }

    private static BlockMovementChecks.CheckResult attachedCheckMountExtensionBlocks(BlockState state, Level level, BlockPos pos, BlockPos posDirection) {
        Direction attached = Direction.fromDelta(posDirection.getX(), posDirection.getY(), posDirection.getZ());
        if (attached == null)
            return BlockMovementChecks.CheckResult.PASS;
        BlockPos attachedPos = pos.relative(attached);
        BlockState attachedTo = level.getBlockState(attachedPos);
        if (CBCBlocks.CANNON_MOUNT.has(state) && level.getBlockEntity(attachedPos) instanceof ExtendsCannonMount extension) {
            CannonMountBlockEntity mount = extension.getCannonMount();
            return mount != null && mount.getBlockPos().equals(pos) ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.PASS;
        }
        if (CBCBlocks.CANNON_MOUNT.has(attachedTo) && level.getBlockEntity(pos) instanceof ExtendsCannonMount extension) {
            CannonMountBlockEntity mount = extension.getCannonMount();
            return mount != null && mount.getBlockPos().equals(attachedPos) ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.PASS;
        }
        return BlockMovementChecks.CheckResult.PASS;
    }

    private static BlockMovementChecks.CheckResult attachedCheckCannons(BlockState state, Level level, BlockPos pos, BlockPos posDirection) {
        if (!(state.getBlock() instanceof BigCannonBlock cannonBlock))
            return BlockMovementChecks.CheckResult.PASS;
        Direction attached = Direction.fromDelta(posDirection.getX(), posDirection.getY(), posDirection.getZ());
        if (attached == null)
            return BlockMovementChecks.CheckResult.PASS;
        BlockPos otherPos = pos.relative(attached);
        BlockState attachedState = level.getBlockState(otherPos);
        if (!(attachedState.getBlock() instanceof BigCannonBlock otherBlock))
            return BlockMovementChecks.CheckResult.PASS;

        if (!(level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe) || !(level.getBlockEntity(otherPos) instanceof IBigCannonBlockEntity cbe1)) {
            return BlockMovementChecks.CheckResult.PASS;
        }

        boolean result = cbe.cannonBehavior().isConnectedTo(attached) && cbe1.cannonBehavior().isConnectedTo(attached.getOpposite());

        if (cannonBlock instanceof ScrewBreechBlock) {
            result &= cannonBlock.getOpeningType(level, state, pos) != BigCannonEnd.OPEN;
        }
        if (otherBlock instanceof ScrewBreechBlock) {
            result &= otherBlock.getOpeningType(level, attachedState, otherPos) != BigCannonEnd.OPEN && attachedState.getValue(BlockStateProperties.FACING) == attached;
        }
        return BlockMovementChecks.CheckResult.of(result);
    }

    private static BlockMovementChecks.CheckResult attachedCheckAutocannons(BlockState state, Level level, BlockPos pos, BlockPos posDirection) {
        if (!(state.getBlock() instanceof AutocannonBlock)) return BlockMovementChecks.CheckResult.PASS;
        Direction attached = Direction.fromDelta(posDirection.getX(), posDirection.getY(), posDirection.getZ());
        if (attached == null)
            return BlockMovementChecks.CheckResult.PASS;
        BlockPos otherPos = pos.relative(attached);
        BlockState attachedState = level.getBlockState(otherPos);
        if (!(attachedState.getBlock() instanceof AutocannonBlock)) return BlockMovementChecks.CheckResult.PASS;

        if (!(level.getBlockEntity(pos) instanceof IAutocannonBlockEntity cbe) || !(level.getBlockEntity(otherPos) instanceof IAutocannonBlockEntity cbe1)) {
            return BlockMovementChecks.CheckResult.PASS;
        }

        boolean result = cbe.cannonBehavior().isConnectedTo(attached) && cbe1.cannonBehavior().isConnectedTo(attached.getOpposite());
        return BlockMovementChecks.CheckResult.of(result);
    }

    private static BlockMovementChecks.CheckResult attachedCheckCannonLoader(BlockState state, Level level, BlockPos pos, BlockPos posDirection) {
        Direction attached = Direction.fromDelta(posDirection.getX(), posDirection.getY(), posDirection.getZ());
        if (attached == null)
            return BlockMovementChecks.CheckResult.PASS;

        BlockState rootState = level.getBlockState(pos.relative(attached));
        state = IBigCannonBlockEntity.getInnerCannonBlockState(level, pos, state);
        rootState = IBigCannonBlockEntity.getInnerCannonBlockState(level, pos.relative(attached), rootState);

        if (CBCBlocks.CANNON_LOADER.has(state)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (CBCBlocks.RAM_HEAD.has(rootState) || CBCBlocks.WORM_HEAD.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing == facing1 && facing == attached);
            }
            if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
            }
        }
        if (AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (CBCBlocks.RAM_HEAD.has(rootState) || CBCBlocks.WORM_HEAD.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing1 == attached);
            }
            if (CBCBlocks.CANNON_LOADER.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
            }
            if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
            }
        }
        if (CBCBlocks.WORM_HEAD.has(state) || CBCBlocks.RAM_HEAD.has(state)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (CBCBlocks.CANNON_LOADER.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing == facing1 && facing == attached.getOpposite());
            }
            if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing == attached.getOpposite());
            }
        }

        return BlockMovementChecks.CheckResult.PASS;
    }

    private static BlockMovementChecks.CheckResult attachedCheckCannonDrill(BlockState state, Level level, BlockPos pos, BlockPos posDirection) {
        Direction attached = Direction.fromDelta(posDirection.getX(), posDirection.getY(), posDirection.getZ());
        if (attached == null)
            return BlockMovementChecks.CheckResult.PASS;
        BlockState rootState = level.getBlockState(pos.relative(attached));
        state = IBigCannonBlockEntity.getInnerCannonBlockState(level, pos, state);
        rootState = IBigCannonBlockEntity.getInnerCannonBlockState(level, pos.relative(attached), rootState);

        if (CBCBlocks.CANNON_DRILL.has(state)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (CBCBlocks.CANNON_DRILL_BIT.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing == facing1 && facing == attached);
            }
            if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
            }
        }
        if (AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (CBCBlocks.CANNON_DRILL_BIT.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing1 == attached);
            }
            if (CBCBlocks.CANNON_DRILL.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
            }
            if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
            }
        }
        if (CBCBlocks.CANNON_DRILL_BIT.has(state)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (CBCBlocks.CANNON_DRILL.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing == facing1 && facing == attached.getOpposite());
            }
            if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing == attached.getOpposite());
            }
        }

        return BlockMovementChecks.CheckResult.PASS;
    }

    private static BlockMovementChecks.CheckResult attachedCheckCannonBuilder(BlockState state, Level level, BlockPos pos, BlockPos posDirection) {
        Direction attached = Direction.fromDelta(posDirection.getX(), posDirection.getY(), posDirection.getZ());
        if (attached == null)
            return BlockMovementChecks.CheckResult.PASS;
        BlockState rootState = level.getBlockState(pos.relative(attached));
        state = IBigCannonBlockEntity.getInnerCannonBlockState(level, pos, state);
        rootState = IBigCannonBlockEntity.getInnerCannonBlockState(level, pos.relative(attached), rootState);

        if (CBCBlocks.CANNON_BUILDER.has(state)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (CBCBlocks.CANNON_BUILDER_HEAD.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing == facing1 && facing == attached);
            }
            if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
            }
        }
        if (AllBlocks.PISTON_EXTENSION_POLE.has(state)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (CBCBlocks.CANNON_BUILDER_HEAD.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing1 == attached);
            }
            if (CBCBlocks.CANNON_BUILDER.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
            }
            if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing.getAxis() == attached.getAxis());
            }
        }
        if (CBCBlocks.CANNON_BUILDER_HEAD.has(state)) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (CBCBlocks.CANNON_BUILDER.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing == facing1 && facing == attached.getOpposite());
            }
            if (AllBlocks.PISTON_EXTENSION_POLE.has(rootState)) {
                Direction facing1 = rootState.getValue(BlockStateProperties.FACING);
                return BlockMovementChecks.CheckResult.of(facing.getAxis() == facing1.getAxis() && facing == attached.getOpposite());
            }
        }

        return BlockMovementChecks.CheckResult.PASS;
    }

}
