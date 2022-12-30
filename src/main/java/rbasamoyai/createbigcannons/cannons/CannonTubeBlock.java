package rbasamoyai.createbigcannons.cannons;

import java.util.function.Supplier;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.ITE;
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
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CannonTubeBlock extends CannonBaseBlock implements ITE<CannonBlockEntity> {
	
	private final VoxelShaper shapes;
	private final Supplier<CannonCastShape> cannonShape;
	
	public CannonTubeBlock(Properties properties, CannonMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape base) {
		super(properties, material);
		this.shapes = new AllShapes.Builder(base).forDirectional();
		this.cannonShape = cannonShape;
	}
	
	public static CannonTubeBlock verySmall(Properties properties, CannonMaterial material) {
		return new CannonTubeBlock(properties, material, CannonCastShape.VERY_SMALL, Block.box(2, 0, 2, 14, 16, 14));
	}
	
	public static CannonTubeBlock small(Properties properties, CannonMaterial material) {
		return new CannonTubeBlock(properties, material, CannonCastShape.SMALL, Block.box(1, 0, 1, 15, 16, 15));
	}
	
	public static CannonTubeBlock medium(Properties properties, CannonMaterial material) {
		return new CannonTubeBlock(properties, material, CannonCastShape.MEDIUM, Shapes.block());
	}
	
	public static CannonTubeBlock large(Properties properties, CannonMaterial material) {
		return new CannonTubeBlock(properties, material, CannonCastShape.LARGE, Block.box(-1, 0, -1, 17, 16, 17));
	}
	
	public static CannonTubeBlock veryLarge(Properties properties, CannonMaterial material) {
		return new CannonTubeBlock(properties, material, CannonCastShape.VERY_LARGE, Block.box(-2, 0, -2, 18, 16, 18));
	}
	
	@Override public CannonCastShape getCannonShape() { return this.cannonShape.get(); }
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(this.getFacing(state));
	}
	
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.OPEN; }
	@Override public boolean isComplete(BlockState state) { return true; }
	
	@Override public Class<CannonBlockEntity> getTileEntityClass() { return CannonBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON.get(); }
	
}
