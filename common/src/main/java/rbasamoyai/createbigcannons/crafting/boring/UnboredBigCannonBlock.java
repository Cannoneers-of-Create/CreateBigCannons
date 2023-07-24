package rbasamoyai.createbigcannons.crafting.boring;

import java.util.function.Supplier;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.cannons.big_cannons.SolidBigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEndBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class UnboredBigCannonBlock extends SolidBigCannonBlock<BigCannonEndBlockEntity> {

	private final VoxelShaper visualShapes;
	private final VoxelShaper collisionShapes;
	private final Supplier<CannonCastShape> cannonShape;

	public UnboredBigCannonBlock(Properties properties, BigCannonMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape base) {
		this(properties, material, cannonShape, base, base);
	}

	public UnboredBigCannonBlock(Properties properties, BigCannonMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape visualShape, VoxelShape collisionShape) {
		super(properties, material);
		this.visualShapes = new AllShapes.Builder(visualShape).forDirectional();
		this.collisionShapes = new AllShapes.Builder(collisionShape).forDirectional();
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
		return new UnboredBigCannonBlock(properties, material, () -> CannonCastShape.LARGE, box(-1, 0, -1, 17, 16, 17), Shapes.block());
	}

	public static UnboredBigCannonBlock veryLarge(Properties properties, BigCannonMaterial material) {
		return new UnboredBigCannonBlock(properties, material, () -> CannonCastShape.VERY_LARGE, box(-2, 0, -2, 18, 16, 18), Shapes.block());
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.collisionShapes.get(this.getFacing(state));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.visualShapes.get(this.getFacing(state));
	}

	@Override
	public CannonCastShape getCannonShape() {
		return this.cannonShape.get();
	}

	@Override
	public boolean isComplete(BlockState state) {
		return false;
	}

	@Override
	public Class<BigCannonEndBlockEntity> getBlockEntityClass() {
		return BigCannonEndBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends BigCannonEndBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.CANNON_END.get();
	}

}
