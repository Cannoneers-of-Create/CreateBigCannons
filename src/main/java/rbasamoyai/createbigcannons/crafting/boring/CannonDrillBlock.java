package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock.PistonState;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CannonDrillBlock extends DirectionalAxisKineticBlock implements ITE<CannonDrillBlockEntity> {

	public static final EnumProperty<PistonState> STATE = MechanicalPistonBlock.STATE;
	
	private final VoxelShaper shapes;
	private final VoxelShaper shapesRectracted;
	
	public CannonDrillBlock(Properties properties) {
		super(properties);
		VoxelShape base = Shapes.or(Block.box(0, 0, 0, 16, 12, 16), Block.box(6, 12, 6, 10, 16, 10));
		this.shapes = new AllShapes.Builder(base).forDirectional();
		this.shapesRectracted = new AllShapes.Builder(Shapes.or(base, Block.box(5, 16, 5, 11, 22, 11))).forDirectional();
		this.registerDefaultState(this.getStateDefinition().any().setValue(STATE, PistonState.RETRACTED));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return state.getValue(STATE) == PistonState.RETRACTED ? this.shapesRectracted.get(state.getValue(FACING)) : this.shapes.get(state.getValue(FACING));
	}

	@Override public Class<CannonDrillBlockEntity> getTileEntityClass() { return CannonDrillBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonDrillBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_DRILL.get(); }
	
	public static int maxAllowedDrillLength() {
		return CBCConfigs.SERVER.crafting.maxCannonDrillLength.get();
	}

}
