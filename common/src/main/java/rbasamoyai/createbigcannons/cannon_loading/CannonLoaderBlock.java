package rbasamoyai.createbigcannons.cannon_loading;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;

public class CannonLoaderBlock extends DirectionalAxisKineticBlock implements IBE<CannonLoaderBlockEntity> {

	public static final BooleanProperty MOVING = BooleanProperty.create("moving");
	public static final DirectionProperty FACING = DirectionalAxisKineticBlock.FACING;

	public CannonLoaderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(MOVING, false));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(MOVING);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return AllShapes.MECHANICAL_PISTON_EXTENDED.get(state.getValue(FACING));
	}

	public static int maxAllowedLoaderLength() {
		return CBCConfigs.SERVER.kinetics.maxLoaderLength.get();
	}

	@Override
	public Class<CannonLoaderBlockEntity> getBlockEntityClass() {
		return CannonLoaderBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends CannonLoaderBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.CANNON_LOADER.get();
	}

	// Copied and adapted from MechanicalPistonBlock#playerWillDestroy
	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		Direction direction = state.getValue(FACING);
		BlockPos loaderHead = null;
		boolean dropBlocks = player == null || !player.isCreative();

		int maxPoles = maxAllowedLoaderLength();
		for (int offset = 1; offset < maxPoles; offset++) {
			BlockPos currentPos = pos.relative(direction, offset);
			BlockState block = level.getBlockState(currentPos);
			block = ContraptionRemix.getInnerCannonState(level, block, currentPos, direction);

			if (MechanicalPistonBlock.isExtensionPole(block) && direction.getAxis() == block.getValue(BlockStateProperties.FACING).getAxis())
				continue;
			if (isLoaderHead(block) && block.getValue(BlockStateProperties.FACING) == direction) {
				loaderHead = currentPos;
			}
			break;
		}

		if (loaderHead != null) {
			BlockPos.betweenClosedStream(pos, loaderHead)
				.filter(p -> !p.equals(pos))
				.forEach(p -> {
					if (!ContraptionRemix.removeCannonContentsOnBreak(level, p, dropBlocks))
						level.destroyBlock(p, dropBlocks);
				});
		}

		for (int offset = 1; offset < maxPoles; offset++) {
			BlockPos currentPos = pos.relative(direction.getOpposite(), offset);
			BlockState block = level.getBlockState(currentPos);
			block = ContraptionRemix.getInnerCannonState(level, block, currentPos, direction);

			if (MechanicalPistonBlock.isExtensionPole(block) && direction.getAxis() == block.getValue(BlockStateProperties.FACING).getAxis()) {
				if (!ContraptionRemix.removeCannonContentsOnBreak(level, currentPos, dropBlocks)) level.destroyBlock(currentPos, dropBlocks);
				continue;
			}
			break;
		}
		super.playerWillDestroy(level, pos, state, player);
	}

	public static boolean isLoaderHead(BlockState state) {
		return CBCBlocks.WORM_HEAD.has(state) || CBCBlocks.RAM_HEAD.has(state);
	}

}
