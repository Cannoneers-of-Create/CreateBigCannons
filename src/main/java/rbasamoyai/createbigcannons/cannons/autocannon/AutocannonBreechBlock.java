package rbasamoyai.createbigcannons.cannons.autocannon;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class AutocannonBreechBlock extends AutocannonBaseBlock implements ITE<AutocannonBreechBlockEntity> {

    public AutocannonBreechBlock(Properties properties, AutocannonMaterial material) {
        super(properties, material);
    }

    @Override public Class<AutocannonBreechBlockEntity> getTileEntityClass() { return AutocannonBreechBlockEntity.class; }
    @Override public BlockEntityType<? extends AutocannonBreechBlockEntity> getTileEntityType() { return CBCBlockEntities.AUTOCANNON_BREECH.get(); }

    @Override public CannonCastShape getCannonShape() { return CannonCastShape.AUTOCANNON_BREECH.get(); }

    @Override
    public boolean canConnectToSide(LevelAccessor level, BlockState state, BlockPos pos, Direction face) {
        return this.getFacing(state) == face;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
    }

    @Override public boolean isBreechMechanism(BlockState state) { return true; }
    @Override public boolean isComplete(BlockState state) { return true; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AllShapes.EIGHT_VOXEL_POLE.get(this.getFacing(state).getAxis());
    }

}
