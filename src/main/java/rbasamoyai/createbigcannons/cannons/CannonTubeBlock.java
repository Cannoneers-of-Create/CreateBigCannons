package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import rbasamoyai.createbigcannons.CBCBlockEntities;

public class CannonTubeBlock extends RotatedPillarBlock implements ITE<CannonBlockEntity>, IWrenchable, CannonBlock {
	
	public static final EnumProperty<Axis> AXIS = RotatedPillarBlock.AXIS;
	
	private final CannonMaterial material;
	
	public CannonTubeBlock(Properties properties, CannonMaterial material) {
		super(properties);
		this.material = material;
	}
	
	public CannonMaterial getCannonMaterial() { return this.material; }
	
	@Override
	public Axis getAxis(BlockState state) {
		return state.getValue(AXIS);
	}
	
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
