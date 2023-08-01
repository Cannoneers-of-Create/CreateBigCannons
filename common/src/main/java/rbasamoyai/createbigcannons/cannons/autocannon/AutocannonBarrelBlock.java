package rbasamoyai.createbigcannons.cannons.autocannon;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class AutocannonBarrelBlock extends AutocannonBaseBlock implements IBE<AutocannonBlockEntity>, IWrenchable {

	public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
	public static final EnumProperty<AutocannonBarrelEnd> BARREL_END = EnumProperty.create("end", AutocannonBarrelEnd.class);

	public AutocannonBarrelBlock(Properties properties, AutocannonMaterial material) {
		super(properties, material);
		this.registerDefaultState(this.defaultBlockState().setValue(BARREL_END, AutocannonBarrelEnd.NOTHING).setValue(ASSEMBLED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ASSEMBLED).add(BARREL_END);
	}

	@Override
	public Class<AutocannonBlockEntity> getBlockEntityClass() {
		return AutocannonBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends AutocannonBlockEntity> getBlockEntityType() {
		return CBCBlockEntities.AUTOCANNON.get();
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.AUTOCANNON_BARREL;
	}

	@Override
	public boolean isBreechMechanism(BlockState state) {
		return false;
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public CannonCastShape getCannonShapeInLevel(LevelAccessor level, BlockState state, BlockPos pos) {
		return switch (state.getValue(BARREL_END)) {
			case FLANGED -> CannonCastShape.AUTOCANNON_BARREL_FLANGED;
			default -> super.getCannonShapeInLevel(level, state, pos);
		};
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AllShapes.FOUR_VOXEL_POLE.get(this.getFacing(state).getAxis());
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		Level level = context.getLevel();
		if (level.isClientSide) return InteractionResult.SUCCESS;
		BlockPos pos = context.getClickedPos();
		AutocannonBlockEntity autocannon = this.getBlockEntity(level, pos);
		Direction facing = this.getFacing(state);

		if (!autocannon.cannonBehavior().isConnectedTo(facing)) {
			level.setBlock(context.getClickedPos(), state.cycle(BARREL_END), 3);
			AutocannonBlockEntity autocannon1 = this.getBlockEntity(level, pos);
			if (autocannon1 != null) {
				boolean previouslyConnected = autocannon.cannonBehavior().isConnectedTo(facing.getOpposite());
				autocannon1.cannonBehavior().setConnectedFace(facing.getOpposite(), previouslyConnected);
				if (level.getBlockEntity(pos.relative(facing.getOpposite())) instanceof AutocannonBlockEntity autocannon2) {
					autocannon2.cannonBehavior().setConnectedFace(facing, previouslyConnected);
				}
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		return InteractionResult.PASS;
	}

	public enum AutocannonBarrelEnd implements StringRepresentable {
		NOTHING("nothing"),
		FLANGED("flanged");

		private final String id;

		private AutocannonBarrelEnd(String id) {
			this.id = id;
		}

		@Override
		public String getSerializedName() {
			return this.id;
		}
	}

}
