package rbasamoyai.createbigcannons.munitions.big_cannon;

import java.util.List;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public abstract class ProjectileBlock<ENTITY extends AbstractBigCannonProjectile> extends DirectionalBlock
	implements IWrenchable, BigCannonMunitionBlock, SimpleWaterloggedBlock {

	private final VoxelShaper shapes;

	public ProjectileBlock(Properties properties) {
		super(properties.pushReaction(PushReaction.NORMAL));
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
		this.shapes = this.makeShapes();
	}

	public static ItemStack getTracerFromItemStack(ItemStack stack) {
		return ItemStack.of(stack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("Tracer"));
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(WATERLOGGED);
	}

	protected VoxelShaper makeShapes() {
		VoxelShape base = box(3, 0, 3, 13, 16, 13);
		return new AllShapes.Builder(base).forDirectional();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Player player = context.getPlayer();
		Direction facing = context.getClickedFace();
		boolean flag = player != null && player.isShiftKeyDown();
		BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos().relative(facing.getOpposite()));

		if (clickedState.getBlock() instanceof BigCannonBlock cblock
			&& cblock.getFacing(clickedState).getAxis() == facing.getAxis()
			&& !flag) {
			facing = facing.getOpposite();
		}
		boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		return this.defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, waterlogged);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(FACING));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}

	public AbstractBigCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		return this.getAssociatedEntityType().create(level);
	}

	public AbstractBigCannonProjectile getProjectile(Level level, ItemStack itemStack) {
		return this.getAssociatedEntityType().create(level);
	}

	public AbstractBigCannonProjectile getProjectile(Level level, BlockPos pos, BlockState state) {
		return this.getAssociatedEntityType().create(level);
	}

	protected AbstractBigCannonProjectile spawnFromExplosion(Level level, BlockPos pos, BlockState state, Explosion explosion) {
		return this.getProjectile(level, pos, state);
	}

	@Override
	public boolean canBeLoaded(BlockState state, Direction.Axis facing) {
		return state.getValue(FACING).getAxis() == facing;
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
		CompoundTag baseTag = stack.getOrCreateTag();
		if (baseTag.contains("BlockEntityTag")) {
			CompoundTag tag = baseTag.getCompound("BlockEntityTag").copy();
			tag.remove("x");
			tag.remove("y");
			tag.remove("z");
			return new StructureBlockInfo(localPos, state, tag);
		}
		return new StructureBlockInfo(localPos, state, null);
	}

	@Override
	public ItemStack getExtractedItem(StructureBlockInfo info) {
		ItemStack stack = new ItemStack(this);
		if (info.nbt() != null) {
			stack.getOrCreateTag().put("BlockEntityTag", info.nbt());
		}
		return stack;
	}

	public abstract EntityType<? extends ENTITY> getAssociatedEntityType();

	public boolean isValidAddition(List<StructureBlockInfo> total, StructureBlockInfo data, int index, Direction dir) {
		return total.size() == 1 && total.get(0) == data;
	}

	public int getExpectedSize() { return 1; }

	public boolean isComplete(List<StructureBlockInfo> total, Direction dir) {
		return total.size() == this.getExpectedSize();
	}

	@Override public Direction.Axis getAxis(BlockState state) { return state.getValue(FACING).getAxis(); }

	public static ItemStack getTracerFromBlocks(List<StructureBlockInfo> blocks) {
		if (blocks.isEmpty())
			return ItemStack.EMPTY;
		StructureBlockInfo info = blocks.get(0);
		if (info.nbt() == null)
			return ItemStack.EMPTY;
		BlockEntity load = BlockEntity.loadStatic(info.pos(), info.state(), info.nbt());
		return load instanceof BigCannonProjectileBlockEntity projectile ? projectile.getItem(0) : ItemStack.EMPTY;
	}

	public static ItemStack getTracerFromBlock(Level level, BlockPos pos, BlockState state) {
		return level.getBlockEntity(pos) instanceof BigCannonProjectileBlockEntity projectile ? projectile.getTracer() : ItemStack.EMPTY;
	}

	@Override
	public void createbigcannons$onBlockExplode(Level level, BlockPos pos, BlockState state, Explosion explosion) {
		if (level.isClientSide || !CBCConfigs.SERVER.munitions.munitionBlocksCanExplode.get())
			return;
		Vec3 entityPos = Vec3.atCenterOf(pos);
		AbstractBigCannonProjectile projectile = this.spawnFromExplosion(level, pos, state, explosion);
		projectile.setPos(entityPos);
		// Taken from PrimedTnt#<init>
		double d = level.random.nextDouble() * (float) (Math.PI * 2);
		projectile.setDeltaMovement(-Math.sin(d) * 0.02, 0.2F, -Math.cos(d) * 0.02);
		level.addFreshEntity(projectile);
	}

	@Override public boolean dropFromExplosion(Explosion explosion) { return false; }

}
