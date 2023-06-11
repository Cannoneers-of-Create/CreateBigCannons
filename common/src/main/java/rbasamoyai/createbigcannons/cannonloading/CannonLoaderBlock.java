package rbasamoyai.createbigcannons.cannonloading;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class CannonLoaderBlock extends DirectionalAxisKineticBlock implements IBE<CannonLoaderBlockEntity> {

	public static final BooleanProperty MOVING = BooleanProperty.create("moving");
	public static final DirectionProperty FACING = DirectionalAxisKineticBlock.FACING;

	public CannonLoaderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(MOVING, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(MOVING);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return AllShapes.MECHANICAL_PISTON_EXTENDED.get(state.getValue(FACING));
	}

	public static int maxAllowedLoaderLength() {
		return CBCConfigs.SERVER.kinetics.maxLoaderLength.get();
	}

	@Override
	public Class<CannonLoaderBlockEntity> getBlockEntityClass() {
		return CannonLoaderBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends CannonLoaderBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.CANNON_LOADER.get();
	}

}
