package rbasamoyai.createbigcannons.crafting.builtup;

import java.util.Random;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.ITE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CannonBuilderBlock extends DirectionalAxisKineticBlock implements ITE<CannonBuilderBlockEntity> {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final EnumProperty<BuilderState> STATE = EnumProperty.create("state", BuilderState.class);
	
	public CannonBuilderBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(STATE, BuilderState.UNACTIVATED));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(POWERED);
		builder.add(STATE);
	}
	
	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		Direction dir = state.getValue(FACING);
		BlockPos headPos = null;
		boolean dropBlocks = player == null || !player.isCreative();
		
		int max = maxAllowedBuilderLength();
		for (int i = 1; i < max; ++i) {
			BlockPos cPos = pos.relative(dir, i);
			BlockState cState = level.getBlockState(cPos);
			
			if (AllBlocks.PISTON_EXTENSION_POLE.has(cState) && dir.getAxis() == cState.getValue(BlockStateProperties.FACING).getAxis()) {
				continue;
			}
			if (CBCBlocks.CANNON_BUILDER_HEAD.has(cState) && dir == cState.getValue(BlockStateProperties.FACING)) {
				headPos = cPos;
			}
			break;
		}
		
		if (headPos != null) {
			BlockPos.betweenClosedStream(headPos, pos)
			.filter(p -> !p.equals(pos))
			.forEach(p -> level.destroyBlock(p, dropBlocks));
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
		
		super.playerWillDestroy(level, pos, state, player);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(STATE).isFullBlock() ? Shapes.block() : AllShapes.MECHANICAL_PISTON_EXTENDED.get(state.getValue(FACING));
	}
	
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
		if (!level.isClientSide) {
			if (!level.getBlockTicks().willTickThisTick(pos, this)) {
				level.scheduleTick(pos, this, 0);
			}
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
		boolean prevPowered = state.getValue(POWERED);
		if (prevPowered != level.hasNeighborSignal(pos)) {
			state = state.cycle(POWERED);
			if (state.getValue(POWERED)) {
				level.playSound(null, pos, AllSoundEvents.CONTRAPTION_ASSEMBLE.getMainEvent(), SoundSource.BLOCKS, 1.0f, 0.0f);
				CannonBuilderBlockEntity builder = this.getTileEntity(level, pos);
				if (builder != null) state = builder.updateBlockstatesOnPowered(state);
			}
			level.setBlock(pos, state, 2);
		}
	}
	
	@Override public Class<CannonBuilderBlockEntity> getTileEntityClass() { return CannonBuilderBlockEntity.class; }
	@Override public BlockEntityType<? extends CannonBuilderBlockEntity> getTileEntityType() { return CBCBlockEntities.CANNON_BUILDER.get(); }
	
	public static int maxAllowedBuilderLength() { return CBCConfigs.SERVER.crafting.maxCannonBuilderLength.get(); }
	
	public enum BuilderState implements StringRepresentable {
		UNACTIVATED("unactivated", true),
		ACTIVATED("activated", true),
		MOVING("moving", false),
		EXTENDED("extended", false);
		
		private final String name;
		private final boolean fullBlock;
		
		private BuilderState(String name, boolean fullBlock) {
			this.name = name;
			this.fullBlock = fullBlock;
		}
		
		@Override public String getSerializedName() { return this.name; }
		public boolean isFullBlock() { return this.fullBlock; }
	}
	
}
