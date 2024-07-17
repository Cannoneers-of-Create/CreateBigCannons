package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCBlocks;

public class CannonDrillBlock extends DirectionalAxisKineticBlock implements IBE<AbstractCannonDrillBlockEntity> {

	public static final EnumProperty<MechanicalPistonBlock.PistonState> STATE = MechanicalPistonBlock.STATE;

	private final VoxelShaper shapesRetracted;

	public CannonDrillBlock(Properties properties) {
		super(properties);
		this.shapesRetracted = new AllShapes.Builder(Block.box(5, 16, 5, 11, 22, 11)).forDirectional();
		this.registerDefaultState(this.getStateDefinition().any().setValue(STATE, MechanicalPistonBlock.PistonState.RETRACTED));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		destroyExtensionPoles(level, pos, state, !player.isCreative());
		super.playerWillDestroy(level, pos, state, player);
	}

	public static void destroyExtensionPoles(Level level, BlockPos pos, BlockState state, boolean dropBlocks) {
		dropBlocks &= level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS);
		Direction dir = state.getValue(FACING);
		BlockPos headPos = null;

		int max = maxAllowedDrillLength();
		for (int i = 1; i < max; ++i) {
			BlockPos cPos = pos.relative(dir, i);
			BlockState cState = level.getBlockState(cPos);

			if (AllBlocks.PISTON_EXTENSION_POLE.has(cState) && dir.getAxis() == cState.getValue(BlockStateProperties.FACING).getAxis()) {
				continue;
			}
			if (CBCBlocks.CANNON_DRILL_BIT.has(cState) && dir == cState.getValue(BlockStateProperties.FACING)) {
				headPos = cPos;
			}
			break;
		}

		if (headPos != null) {
			boolean dropBlocks1 = dropBlocks;
			BlockPos.betweenClosedStream(headPos, pos)
				.filter(p -> !p.equals(pos))
				.forEach(p -> level.destroyBlock(p, dropBlocks1));
		}

		for (int i = 1; i < max; ++i) {
			BlockPos cPos = pos.relative(dir, -i);
			BlockState cState = level.getBlockState(cPos);
			if (AllBlocks.PISTON_EXTENSION_POLE.has(cState) && dir.getAxis() == cState.getValue(BlockStateProperties.FACING).getAxis()) {
				level.destroyBlock(cPos, dropBlocks);
				continue;
			}
			break;
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		VoxelShape base = AllShapes.MECHANICAL_PISTON_EXTENDED.get(state.getValue(FACING));
		return state.getValue(STATE) == MechanicalPistonBlock.PistonState.RETRACTED ? Shapes.or(base, this.shapesRetracted.get(state.getValue(FACING))) : base;
	}

	@Override
	public Class<AbstractCannonDrillBlockEntity> getBlockEntityClass() {
		return AbstractCannonDrillBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends AbstractCannonDrillBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.CANNON_DRILL.get();
	}

	public static int maxAllowedDrillLength() {
		return CBCConfigs.SERVER.crafting.maxCannonDrillLength.get();
	}

}
