package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import javax.annotation.Nullable;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;

import java.util.List;

public class BigCartridgeBlock extends DirectionalBlock implements IWrenchable, BigCannonPropellantBlock, IBE<BigCartridgeBlockEntity> {

	public static final BooleanProperty FILLED = BooleanProperty.create("filled");

	private final VoxelShaper shapes;

	public BigCartridgeBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FILLED, false));
		this.shapes = this.makeShapes();
	}

	private VoxelShaper makeShapes() {
		VoxelShape base = Block.box(3, 0, 3, 13, 16, 13);
		return new AllShapes.Builder(base).forDirectional();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(FILLED);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getClickedFace();
		Player player = context.getPlayer();
		boolean flag = player != null && player.isShiftKeyDown();

		BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos().relative(facing.getOpposite()));
		if (clickedState.getBlock() instanceof BigCannonBlock cblock
			&& cblock.getFacing(clickedState).getAxis() == facing.getAxis()
			&& !flag) {
			facing = facing.getOpposite();
		} else if (clickedState.getBlock() instanceof ProjectileBlock
			&& clickedState.getValue(ProjectileBlock.FACING).getAxis() == facing.getAxis()
			&& !flag) {
			facing = facing.getOpposite();
		}
		return this.defaultBlockState().setValue(FILLED, BigCartridgeBlockItem.getPower(context.getItemInHand()) > 0).setValue(FACING, facing);
	}

	@Override
	public float getChargePower(StructureBlockInfo data) {
		return getPowerFromData(data) * CBCConfigs.SERVER.munitions.bigCartridgeStrength.getF();
	}

	@Override
	public float getChargePower(ItemStack stack) {
		return BigCartridgeBlockItem.getPower(stack) * CBCConfigs.SERVER.munitions.bigCartridgeStrength.getF();
	}

	@Override
	public float getStressOnCannon(StructureBlockInfo data) {
		return getPowerFromData(data) * CBCConfigs.SERVER.munitions.bigCartridgeStress.getF();
	}

	@Override
	public float getStressOnCannon(ItemStack stack) {
		return BigCartridgeBlockItem.getPower(stack) * CBCConfigs.SERVER.munitions.bigCartridgeStress.getF();
	}

	@Override
	public float getSpread(StructureBlockInfo data) {
		return getPowerFromData(data) * CBCConfigs.SERVER.munitions.addedSpread.getF();
	}

	@Override
	public boolean canBeLoaded(BlockState state, Direction.Axis facing) {
		return state.getValue(FACING).getAxis() == facing;
	}

	@Override
	public void consumePropellant(BigCannonBehavior behavior) {
		StructureBlockInfo oldData = behavior.block();
		behavior.loadBlock(new StructureBlockInfo(oldData.pos(), oldData.state().setValue(FILLED, false), new CompoundTag()));
	}

	@Override
	public boolean isCompatibleWith(List<StructureBlockInfo> total, StructureBlockInfo self, int index, Direction dir) {
		return total.size() == 1 && total.get(0) == self;
	}

	@Override
	public boolean canBeIgnited(StructureBlockInfo data, Direction dir) {
		return data.state().getValue(FACING) == dir && getPowerFromData(data) > 0;
	}

	public static float getPowerFromData(StructureBlockInfo data) {
		return data.nbt() == null ? 0 : data.nbt().getInt("Power");
	}

	@Override
	public Class<BigCartridgeBlockEntity> getBlockEntityClass() {
		return BigCartridgeBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends BigCartridgeBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.BIG_CARTRIDGE.get();
	}

	@Override
	public BlockState onCannonRotate(BlockState oldState, Direction.Axis rotationAxis, Rotation rotation) {
		Direction facing = oldState.getValue(BlockStateProperties.FACING);
		for (int i = 0; i < rotation.ordinal(); ++i) {
			facing = facing.getClockWise(rotationAxis);
		}
		return oldState.setValue(BlockStateProperties.FACING, facing);
	}

	@Override
	public StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation) {
		BlockState state = this.defaultBlockState().setValue(FACING, cannonOrientation);
		CompoundTag tag = new CompoundTag();
		tag.putInt("Power", stack.getOrCreateTag().getInt("Power"));
		return new StructureBlockInfo(localPos, state, tag);
	}

	@Override
	public ItemStack getExtractedItem(StructureBlockInfo info) {
		ItemStack stack = new ItemStack(this);
		if (info.nbt() != null) {
			stack.getOrCreateTag().putInt("Power", info.nbt().getInt("Power"));
		}
		return stack;
	}

}
