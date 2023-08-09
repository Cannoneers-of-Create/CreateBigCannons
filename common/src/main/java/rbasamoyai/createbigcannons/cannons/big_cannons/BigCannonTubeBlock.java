package rbasamoyai.createbigcannons.cannons.big_cannons;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class BigCannonTubeBlock extends BigCannonBaseBlock implements IBE<BigCannonBlockEntity> {

	private final VoxelShaper visualShapes;
	private final VoxelShaper collisionShapes;
	private final Supplier<CannonCastShape> cannonShape;

	public BigCannonTubeBlock(Properties properties, BigCannonMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape base) {
		this(properties, material, cannonShape, base, base);
	}

	public BigCannonTubeBlock(Properties properties, BigCannonMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape visualShape, VoxelShape collisionShape) {
		super(properties, material);
		this.visualShapes = new AllShapes.Builder(visualShape).forDirectional();
		this.collisionShapes = new AllShapes.Builder(collisionShape).forDirectional();
		this.cannonShape = cannonShape;
	}

	public static BigCannonTubeBlock verySmall(Properties properties, BigCannonMaterial material) {
		return new BigCannonTubeBlock(properties, material, () -> CannonCastShape.VERY_SMALL, Block.box(2, 0, 2, 14, 16, 14));
	}

	public static BigCannonTubeBlock small(Properties properties, BigCannonMaterial material) {
		return new BigCannonTubeBlock(properties, material, () -> CannonCastShape.SMALL, Block.box(1, 0, 1, 15, 16, 15));
	}

	public static BigCannonTubeBlock medium(Properties properties, BigCannonMaterial material) {
		return new BigCannonTubeBlock(properties, material, () -> CannonCastShape.MEDIUM, Shapes.block());
	}

	public static BigCannonTubeBlock large(Properties properties, BigCannonMaterial material) {
		return new BigCannonTubeBlock(properties, material, () -> CannonCastShape.LARGE, Block.box(-1, 0, -1, 17, 16, 17), Shapes.block());
	}

	public static BigCannonTubeBlock veryLarge(Properties properties, BigCannonMaterial material) {
		return new BigCannonTubeBlock(properties, material, () -> CannonCastShape.VERY_LARGE, Block.box(-2, 0, -2, 18, 16, 18), Shapes.block());
	}

	@Override
	public CannonCastShape getCannonShape() {
		return this.cannonShape.get();
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
	public BigCannonEnd getOpeningType(@Nullable Level level, BlockState state, BlockPos pos) {
		return BigCannonEnd.OPEN;
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public Class<BigCannonBlockEntity> getBlockEntityClass() {
		return BigCannonBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends BigCannonBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.CANNON.get();
	}

}
