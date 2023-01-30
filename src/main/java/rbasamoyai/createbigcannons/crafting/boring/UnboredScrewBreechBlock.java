package rbasamoyai.createbigcannons.crafting.boring;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class UnboredScrewBreechBlock extends UnboredBigCannonBlock {

	public UnboredScrewBreechBlock(Properties properties, BigCannonMaterial material) {
		super(properties, material, CannonCastShape.SCREW_BREECH, box(0, 0, 0, 16, 8, 16));
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction nearestLookingDirection = context.getNearestLookingDirection();
		return defaultBlockState().setValue(FACING, context.getPlayer() != null && context.getPlayer().isShiftKeyDown() ? nearestLookingDirection : nearestLookingDirection.getOpposite());
	}
	
}
