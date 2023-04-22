package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEndBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.SolidBigCannonBlock;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

import java.util.function.Supplier;

public class UnboredBigCannonBlock extends SolidBigCannonBlock<BigCannonEndBlockEntity> {

	private final VoxelShaper shapes;
	private final Supplier<CannonCastShape> cannonShape;
	
	public UnboredBigCannonBlock(Properties properties, BigCannonMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape baseShape) {
		super(properties, material);
		this.shapes = new AllShapes.Builder(baseShape).forDirectional();
		this.cannonShape = cannonShape;
	}
	
	public static UnboredBigCannonBlock verySmall(Properties properties, BigCannonMaterial material) {
		return new UnboredBigCannonBlock(properties, material, () -> CannonCastShape.VERY_SMALL, box(2, 0, 2, 14, 16, 14));
	}

	public static UnboredBigCannonBlock small(Properties properties, BigCannonMaterial material) {
		return new UnboredBigCannonBlock(properties, material, () -> CannonCastShape.SMALL, box(1, 0, 1, 15, 16, 15));
	}
	
	public static UnboredBigCannonBlock medium(Properties properties, BigCannonMaterial material) {
		return new UnboredBigCannonBlock(properties, material, () -> CannonCastShape.MEDIUM, Shapes.block());
	}

	public static UnboredBigCannonBlock large(Properties properties, BigCannonMaterial material) {
		return new UnboredBigCannonBlock(properties, material, () -> CannonCastShape.LARGE, box(-1, 0, -1, 17, 16, 17));
	}

	public static UnboredBigCannonBlock veryLarge(Properties properties, BigCannonMaterial material) {
		return new UnboredBigCannonBlock(properties, material, () -> CannonCastShape.VERY_LARGE, box(-2, 0, -2, 18, 16, 18));
	}
		
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}
	
	@Override public CannonCastShape getCannonShape() { return this.cannonShape.get(); }
	
	@Override public boolean isComplete(BlockState state) { return false; }

	@Override public Class<BigCannonEndBlockEntity> getTileEntityClass() { return BigCannonEndBlockEntity.class; }
	@Override public BlockEntityType<? extends BigCannonEndBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_END.get(); }

}
