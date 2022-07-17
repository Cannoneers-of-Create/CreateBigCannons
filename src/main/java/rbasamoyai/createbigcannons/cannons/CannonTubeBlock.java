package rbasamoyai.createbigcannons.cannons;

import java.util.Optional;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;

public class CannonTubeBlock extends RotatedPillarBlock implements ITE<CannonBlockEntity>, IWrenchable, CannonBlock {
	
	public static final EnumProperty<Axis> AXIS = RotatedPillarBlock.AXIS;
	
	private final CannonMaterial material;
	
	public CannonTubeBlock(Properties properties, CannonMaterial material) {
		super(properties);
		this.material = material;
	}
	
	@Override public CannonMaterial getCannonMaterial() { return this.material; }
	@Override public Direction.Axis getAxis(BlockState state) { return state.getValue(AXIS); }
	@Override public Optional<Direction> getFacing(BlockState state) { return Optional.empty(); }
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.OPEN; }
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(AXIS, context.getNearestLookingDirection().getAxis());
	}

	@Override
	public Class<CannonBlockEntity> getTileEntityClass() {
		return CannonBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends CannonBlockEntity> getTileEntityType() {
		return CBCBlockEntities.CANNON.get();
	}
	
}
