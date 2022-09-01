package rbasamoyai.createbigcannons.cannons.cannonend;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class ScrewBreechBlock extends DirectionalKineticBlock implements ITE<ScrewBreechBlockEntity>, CannonBlock {

	public static final EnumProperty<OpenState> OPEN = EnumProperty.create("open", OpenState.class);
	
	private final CannonMaterial material;
	private final VoxelShaper shapes;
	
	public ScrewBreechBlock(Properties properties, CannonMaterial material) {
		super(properties);
		this.material = material;
		this.shapes = this.makeShapes();
		this.registerDefaultState(this.getStateDefinition().any().setValue(OPEN, OpenState.CLOSED));
	}
	
	private VoxelShaper makeShapes() {
		VoxelShape base = Shapes.or(box(0, 0, 0, 16, 8, 16), box(6, 8, 6, 10, 16, 10));
		return new AllShapes.Builder(base).forDirectional();
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(OPEN);
		super.createBlockStateDefinition(builder);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}

	@Override public CannonMaterial getCannonMaterial() { return this.material; }
	@Override public CannonCastShape getCannonShape() { return CannonCastShape.MEDIUM; }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING).getOpposite(); }
	@Override
	public CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos) {
		OpenState open = state.getValue(OPEN);
		return open == OpenState.OPEN || open == OpenState.PARTIAL ? CannonEnd.OPEN : CannonEnd.CLOSED;
	}
	@Override public Axis getRotationAxis(BlockState state) { return state.getValue(FACING).getAxis(); }
	@Override public PushReaction getPistonPushReaction(BlockState state) { return PushReaction.BLOCK; }
	@Override public boolean isDoubleSidedCannon(BlockState state) { return false; }
	@Override public boolean isComplete(BlockState state) { return true; }
	
	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face == state.getValue(FACING);
	}
	
	public static boolean isOpen(BlockState state) {
		return state.hasProperty(OPEN) ? state.getValue(OPEN).isOpen() : false;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}
	
	@Override public Class<ScrewBreechBlockEntity> getTileEntityClass() { return ScrewBreechBlockEntity.class; }
	@Override public BlockEntityType<? extends ScrewBreechBlockEntity> getTileEntityType() { return CBCBlockEntities.SCREW_BREECH.get(); }
	
	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		return isOpen(state) ? super.onWrenched(state, context) : InteractionResult.PASS;
	}

	public enum OpenState implements StringRepresentable {
		CLOSED("closed"),
		PARTIAL("partial"),
		OPEN("open");
		
		private final String name;
		
		private OpenState(String name) {
			this.name = name;
		}
		
		public boolean isOpen() { return this == OPEN; }
		@Override public String getSerializedName() { return this.name; }
	}
	
	@Override public boolean canInteractWithDrill(BlockState state) { return false; }
	
}
