package rbasamoyai.createbigcannons.cannonloading;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CannonLoaderBlock extends DirectionalAxisKineticBlock implements ITE<CannonLoaderBlockEntity> {

	public static final BooleanProperty MOVING = BooleanProperty.create("moving");
	public static final DirectionProperty FACING = DirectionalAxisKineticBlock.FACING;
	
	private final VoxelShaper shapes;
	
	public CannonLoaderBlock(Properties properties) {
		super(properties);
		this.shapes = this.makeShapes();
		this.registerDefaultState(this.getStateDefinition().any().setValue(MOVING, false));
	}
	
	private VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(box(0, 0, 0, 16, 12, 16), box(6, 12, 6, 10, 16, 10));
		return new AllShapes.Builder(base).forDirectional();
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(MOVING);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}
	
	public static int maxAllowedLoaderLength() {
		return CBCConfigs.SERVER.kinetics.maxLoaderLength.get();
	}

	@Override public Class<CannonLoaderBlockEntity> getTileEntityClass() { return CannonLoaderBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonLoaderBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_LOADER.get(); }

}
