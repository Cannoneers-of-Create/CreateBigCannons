package rbasamoyai.createbigcannons.cannons;

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

public class CannonTubeBlock extends CannonBaseBlock implements ITE<CannonBlockEntity> {
	
	private final VoxelShaper shapes;
	
	public CannonTubeBlock(Properties properties, CannonMaterial material, VoxelShape base) {
		super(properties, material);
		this.shapes = new AllShapes.Builder(base).forDirectional();
	}
	
	public static CannonTubeBlock medium(Properties properties, CannonMaterial material) {
		return new CannonTubeBlock(properties, material, Shapes.block());
	}
	
	public static CannonTubeBlock verySmall(Properties properties, CannonMaterial material) {
		return new CannonTubeBlock(properties, material, Block.box(2, 0, 2, 14, 16, 14));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(this.getFacing(state));
	}
	
	@Override public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) { return CannonEnd.OPEN; }
	
	@Override public Class<CannonBlockEntity> getTileEntityClass() { return CannonBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON.get(); }
	
}
