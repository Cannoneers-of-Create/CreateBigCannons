package rbasamoyai.createbigcannons.crafting.foundry;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class BasinFoundryLidBlock extends Block implements IBE<BasinFoundryBlockEntity>, IWrenchable {

	private static final VoxelShape SHAPE = Shapes.or(box(0, 0, 0, 16, 4, 16), box(4, 4, 4, 12, 5, 12));

	public BasinFoundryLidBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public Class<BasinFoundryBlockEntity> getBlockEntityClass() {
		return BasinFoundryBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends BasinFoundryBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.BASIN_FOUNDRY.get();
	}

}
