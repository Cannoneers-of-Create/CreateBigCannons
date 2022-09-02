package rbasamoyai.createbigcannons.crafting.builtup;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.crafting.boring.CannonDrillBlock;
import rbasamoyai.createbigcannons.crafting.builtup.CannonBuilderBlock.BuilderState;

public class CannonBuilderHeadBlock extends WrenchableDirectionalBlock implements SimpleWaterloggedBlock {

	public static final BooleanProperty ATTACHED = BooleanProperty.create("attached");
	
	public CannonBuilderHeadBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(ATTACHED, false).setValue(BlockStateProperties.WATERLOGGED, false));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ATTACHED);
		builder.add(BlockStateProperties.WATERLOGGED);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AllShapes.MECHANICAL_PISTON_HEAD.get(state.getValue(FACING));
	}
	
	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		Direction direction = state.getValue(FACING);
		BlockPos builderBase = null;
		
		for (int i = 1; i < CannonDrillBlock.maxAllowedDrillLength(); ++i) {
			BlockPos currentPos = pos.relative(direction.getOpposite(), i);
			BlockState block = level.getBlockState(currentPos);
			
			if (isExtensionPole(block) && direction.getAxis() == block.getValue(BlockStateProperties.FACING).getAxis()) continue;
			if (isBuilderBlock(block) && block.getValue(FACING) == direction) builderBase = currentPos;
			
			break;
		}
		
		if (builderBase != null) {
			BlockPos basePos = builderBase.immutable();
			BlockPos.betweenClosedStream(builderBase, pos.immutable())
			.filter(p -> !p.equals(pos) && !p.equals(basePos))
			.forEach(p -> level.destroyBlock(p, !player.isCreative()));
			level.setBlockAndUpdate(basePos, level.getBlockState(basePos).setValue(CannonBuilderBlock.STATE, BuilderState.UNACTIVATED));
		}
		
		super.playerWillDestroy(level, pos, state, player);
	}
	
	private static boolean isExtensionPole(BlockState state) { return AllBlocks.PISTON_EXTENSION_POLE.has(state); }
	private static boolean isBuilderBlock(BlockState state) { return CBCBlocks.CANNON_BUILDER.has(state); }
	
}
