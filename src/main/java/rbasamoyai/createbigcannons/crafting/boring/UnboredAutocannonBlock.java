package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.utility.VoxelShaper;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBaseBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonMaterial;
import rbasamoyai.createbigcannons.cannons.autocannon.IncompleteAutocannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

import java.util.function.Supplier;

public class UnboredAutocannonBlock extends AutocannonBaseBlock implements ITE<IncompleteAutocannonBlockEntity>, TransformableByBoring {

	private final NonNullSupplier<? extends Block> boredBlockSup;
	private Block boredBlock;
	private final VoxelShaper shapes;
	private final Supplier<CannonCastShape> cannonShape;

	public UnboredAutocannonBlock(Properties properties, AutocannonMaterial material, VoxelShape shape, Supplier<CannonCastShape> castShape, NonNullSupplier<? extends Block> boredBlockSup) {
		super(properties, material);
		this.shapes = new AllShapes.Builder(shape).forDirectional();
		this.cannonShape = castShape;
		this.boredBlockSup = boredBlockSup;
	}

	public static UnboredAutocannonBlock barrel(Properties properties, AutocannonMaterial material, NonNullSupplier<? extends Block> boredBlock) {
		return new UnboredAutocannonBlock(properties, material, box(6, 0, 6, 10, 16, 10), CannonCastShape.AUTOCANNON_BARREL, boredBlock);
	}

	public static UnboredAutocannonBlock recoilSpring(Properties properties, AutocannonMaterial material, NonNullSupplier<? extends Block> boredBlock) {
		return new UnboredAutocannonBlock(properties, material, box(5, 0, 5, 11, 16, 11), CannonCastShape.AUTOCANNON_RECOIL_SPRING, boredBlock);
	}

	public static UnboredAutocannonBlock breech(Properties properties, AutocannonMaterial material, NonNullSupplier<? extends Block> boredBlock) {
		return new UnboredAutocannonBlock(properties, material, box(4, 0, 4, 12, 16, 12), CannonCastShape.AUTOCANNON_BREECH, boredBlock);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}

	@Override public boolean isComplete(BlockState state) { return false; }
	@Override public boolean isBreechMechanism(BlockState state) { return false; }
	@Override public CannonCastShape getCannonShape() { return this.cannonShape.get(); }

	@Override
	public BlockState getBoredBlockState(BlockState state) {
		if (this.boredBlock == null) {
			this.boredBlock = this.boredBlockSup.get();
		}
		BlockState bored = this.boredBlock.delegate.get().defaultBlockState();
		return bored.hasProperty(FACING) ? bored.setValue(FACING, state.getValue(FACING)) : bored;
	}

	@Override public Class<IncompleteAutocannonBlockEntity> getTileEntityClass() { return IncompleteAutocannonBlockEntity.class; }
	@Override public BlockEntityType<? extends IncompleteAutocannonBlockEntity> getTileEntityType() { return CBCBlockEntities.INCOMPLETE_AUTOCANNON.get(); }

}
