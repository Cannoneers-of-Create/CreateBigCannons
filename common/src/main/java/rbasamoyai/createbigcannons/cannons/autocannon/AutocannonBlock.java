package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public interface AutocannonBlock {

    AutocannonMaterial getAutocannonMaterial();
    default AutocannonMaterial getAutocannonMaterialInLevel(LevelAccessor level, BlockState state, BlockPos pos) { return this.getAutocannonMaterial(); }

    CannonCastShape getCannonShape();
    default CannonCastShape getCannonShapeInLevel(LevelAccessor level, BlockState state, BlockPos pos) { return this.getCannonShape(); }

    Direction getFacing(BlockState state);

    default boolean canConnectToSide(LevelAccessor level, BlockState state, BlockPos pos, Direction face) {
        return this.getFacing(state).getAxis() == face.getAxis();
    }

    boolean isBreechMechanism(BlockState state);
    boolean isComplete(BlockState state);

    default void onRemoveCannon(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!(state.getBlock() instanceof AutocannonBlock cBlock) || state.is(newState.getBlock())) return;
        Direction facing = cBlock.getFacing(state);
        Direction opposite = facing.getOpposite();
        AutocannonMaterial material = cBlock.getAutocannonMaterial();

        if (level.getBlockEntity(pos) instanceof IAutocannonBlockEntity cbe) {
            for (ItemStack stack : cbe.getDrops()) {
                Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy());
            }
        }

        if (cBlock.canConnectToSide(level, state, pos, facing)) {
            BlockPos pos1 = pos.relative(facing);
            BlockState state1 = level.getBlockState(pos1);
            BlockEntity be1 = level.getBlockEntity(pos1);

            if (state1.getBlock() instanceof AutocannonBlock cBlock1
                    && cBlock1.getAutocannonMaterialInLevel(level, state1, pos1) == material
                    && cBlock1.canConnectToSide(level, state1, pos1, opposite)
                    && be1 instanceof IAutocannonBlockEntity cbe1) {
                cbe1.cannonBehavior().setConnectedFace(opposite, false);
                be1.setChanged();
            }
        }

        if (cBlock.canConnectToSide(level, state, pos, opposite)) {
            BlockPos pos2 = pos.relative(opposite);
            BlockState state2 = level.getBlockState(pos2);
            BlockEntity be2 = level.getBlockEntity(pos2);

            if (state2.getBlock() instanceof AutocannonBlock cBlock2
                    && cBlock2.getAutocannonMaterialInLevel(level, state2, pos2) == material
                    && cBlock2.canConnectToSide(level, state2, pos2, facing)
                    && be2 instanceof IAutocannonBlockEntity cbe2) {
                cbe2.cannonBehavior().setConnectedFace(facing, false);
                be2.setChanged();
            }
        }
    }

    public static void onPlace(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel slevel)) return;
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof AutocannonBlock cBlock)) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof IAutocannonBlockEntity cbe)) return;

        Direction facing = cBlock.getFacing(state);
        Direction opposite = facing.getOpposite();

        Vec3 center = Vec3.atCenterOf(pos);
        Vec3 offset = Vec3.atBottomCenterOf(facing.getNormal()).scale(0.5d);
        AutocannonMaterial material = cBlock.getAutocannonMaterialInLevel(level, state, pos);

        BlockPos pos1 = pos.relative(facing);
        BlockState state1 = level.getBlockState(pos1);
        BlockEntity be1 = level.getBlockEntity(pos1);

        if (state1.getBlock() instanceof AutocannonBlock cBlock1
                && cBlock1.getAutocannonMaterialInLevel(level, state1, pos1) == material
                && cBlock1.canConnectToSide(level, state1, pos1, opposite)) {
            if (state1.hasProperty(AutocannonBarrelBlock.BARREL_END)) {
                level.setBlock(pos1, state1.setValue(AutocannonBarrelBlock.BARREL_END, AutocannonBarrelBlock.AutocannonBarrelEnd.NOTHING), 3);
            }
            if (level.getBlockEntity(pos1) instanceof IAutocannonBlockEntity cbe1) {
                cbe.cannonBehavior().setConnectedFace(facing, true);
                cbe1.cannonBehavior().setConnectedFace(facing.getOpposite(), true);
                be1.setChanged();
                Vec3 particlePos = center.add(offset);
                slevel.sendParticles(ParticleTypes.CRIT, particlePos.x, particlePos.y, particlePos.z, 10, 0.5d, 0.5d, 0.5d, 0.1d);
            }
        }

        BlockPos pos2 = pos.relative(opposite);
        BlockState state2 = level.getBlockState(pos2);
        BlockEntity be2 = level.getBlockEntity(pos2);

        if (state2.getBlock() instanceof AutocannonBlock cBlock2
                && cBlock2.getAutocannonMaterialInLevel(level, state2, pos2) == material
                && cBlock2.canConnectToSide(level, state2, pos2, facing)) {
            if (state2.hasProperty(AutocannonBarrelBlock.BARREL_END)) {
                level.setBlock(pos2, state2.setValue(AutocannonBarrelBlock.BARREL_END, AutocannonBarrelBlock.AutocannonBarrelEnd.NOTHING), 3);
            }
            if (level.getBlockEntity(pos2) instanceof IAutocannonBlockEntity cbe2) {
                cbe.cannonBehavior().setConnectedFace(facing.getOpposite(), true);
                cbe2.cannonBehavior().setConnectedFace(facing, true);
                be2.setChanged();
                Vec3 particlePos = center.add(offset.reverse());
                slevel.sendParticles(ParticleTypes.CRIT, particlePos.x, particlePos.y, particlePos.z, 10, 0.5d, 0.5d, 0.5d, 0.1d);
            }
        }

        be.setChanged();
    }

}
