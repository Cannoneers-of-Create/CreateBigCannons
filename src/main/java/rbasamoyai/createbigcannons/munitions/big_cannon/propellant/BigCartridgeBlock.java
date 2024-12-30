package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import javax.annotation.Nullable;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.config.BigCartridgeProperties;

public class BigCartridgeBlock extends DirectionalBlock implements IWrenchable, BigCannonPropellantBlock, IBE<BigCartridgeBlockEntity>,
	SimpleWaterloggedBlock {

	public static final BooleanProperty FILLED = BooleanProperty.create("filled");

	private final VoxelShaper shapes;

	public BigCartridgeBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FILLED, false)
			.setValue(WATERLOGGED, false)
			.setValue(DAMP, false));
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
		builder.add(WATERLOGGED);
		builder.add(DAMP);
	}

	@Override public boolean isRandomlyTicking(BlockState state) { return BigCannonMunitionBlock.canDry(state); }

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (BigCannonMunitionBlock.canDry(state))
			level.setBlock(pos, state.setValue(DAMP, false), 3);
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
		boolean result = SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
		if (result && !level.isClientSide())
			level.setBlock(pos, level.getBlockState(pos).setValue(DAMP, true), 3);
		return result;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
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
		ItemStack itemStack = context.getItemInHand();
		boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		boolean damp = (itemStack.getOrCreateTag().getBoolean("Damp") || waterlogged) && !context.getLevel().dimensionType().ultraWarm();
		return this.defaultBlockState()
			.setValue(FILLED, BigCartridgeBlockItem.getPower(itemStack) > 0)
			.setValue(DAMP, damp)
			.setValue(FACING, facing)
			.setValue(WATERLOGGED, waterlogged);
	}

	@Override
	public boolean canBeIgnited(StructureBlockInfo data, Direction dir) {
		if (data.state().getValue(DAMP)
			&& CBCConfigs.SERVER.munitions.dampPropellantBlocksStartingIgnition.get()
			&& this.getProperties().propellantProperties().dampAmmoDoesntIgniteAsStarter())
			return false;
		return data.state().getValue(FACING) == dir && getPowerFromData(data) > 0;
	}

	public static float getPowerFromData(StructureBlockInfo data) {
		return data.nbt() == null ? 0 : data.nbt().getInt("Power");
	}

	public float getPowerMultiplier(StructureBlockInfo data) {
		return CBCConfigs.SERVER.munitions.dampPropellantWeakensPropellant.get() && data.state().getValue(DAMP)
			? this.getProperties().propellantProperties().dampAmmoStrengthDebuff()
			: 1;
	}

	public float getPowerMultiplier(ItemStack stack) {
		return CBCConfigs.SERVER.munitions.dampPropellantWeakensPropellant.get() && stack.getOrCreateTag().getBoolean("Damp")
			? this.getProperties().propellantProperties().dampAmmoStrengthDebuff()
			: 1;
	}

	@Override
	public float getChargePower(StructureBlockInfo data) {
		return this.getPowerMultiplier(data) * getPowerFromData(data) * this.getProperties().propellantProperties().strength();
	}

	@Override
	public float getChargePower(ItemStack stack) {
		return this.getPowerMultiplier(stack) * BigCartridgeBlockItem.getPower(stack) * this.getProperties().propellantProperties().strength();
	}

	@Override
	public float getStressOnCannon(StructureBlockInfo data) {
		return this.getPowerMultiplier(data) * getPowerFromData(data) * this.getProperties().propellantProperties().addedStress();
	}

	@Override
	public float getStressOnCannon(ItemStack stack) {
		return this.getPowerMultiplier(stack) * BigCartridgeBlockItem.getPower(stack) * this.getProperties().propellantProperties().addedStress();
	}

	@Override
	public float getSpread(StructureBlockInfo data) {
		return this.getPowerMultiplier(data) * getPowerFromData(data) * this.getProperties().propellantProperties().addedSpread();
	}

	@Override
	public float getRecoil(StructureBlockInfo data) {
		return this.getPowerMultiplier(data) * getPowerFromData(data) * this.getProperties().propellantProperties().addedRecoil();
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
	public boolean isValidAddition(StructureBlockInfo self, int index, Direction dir) {
		return this.canBeIgnited(self, dir) && index == 0;
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

    @Override public Direction.Axis getAxis(BlockState state) { return state.getValue(BlockStateProperties.FACING).getAxis(); }

    @Override
	public StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation) {
		BlockState state = this.defaultBlockState().setValue(FACING, cannonOrientation);
		CompoundTag blockTag = new CompoundTag();
		CompoundTag stackTag = stack.getOrCreateTag();
		blockTag.putInt("Power", stackTag.getInt("Power"));
		if (stackTag.getBoolean("Damp"))
			state = state.setValue(DAMP, true);
		return new StructureBlockInfo(localPos, state, blockTag);
	}

	@Override
	public ItemStack getExtractedItem(StructureBlockInfo info) {
		ItemStack stack = new ItemStack(this);
		if (info.nbt() != null) {
			stack.getOrCreateTag().putInt("Power", info.nbt().getInt("Power"));
		}
		if (info.state().getValue(DAMP))
			stack.getOrCreateTag().putBoolean("Damp", true);
		return stack;
	}

	protected BigCartridgeProperties getProperties() {
		return CBCMunitionPropertiesHandlers.BIG_CARTRIDGE.getPropertiesOf(this);
	}

	public int getMaximumPowerLevels() {
		return CBCMunitionPropertiesHandlers.BIG_CARTRIDGE.getPropertiesOf(this).maxPowerLevels();
	}

	@Override
	public void createbigcannons$onBlockExplode(Level level, BlockPos pos, BlockState state, Explosion explosion) {
		if (!level.isClientSide && !BigCannonMunitionBlock.doesntIgnite(state))
			this.spawnPrimedPropellant(level, pos, state);
	}

	public PrimedPropellant spawnPrimedPropellant(Level level, BlockPos pos, BlockState state) {
		Vec3 entityPos = Vec3.atCenterOf(pos);
		PrimedPropellant propellant = PrimedPropellant.create(level, entityPos.x(), entityPos.y(), entityPos.z(), state);
		int i = level.getBlockEntity(pos) instanceof BigCartridgeBlockEntity cartridge ? cartridge.getPower() : 0;
		propellant.setExplosionPower(this.getProperties().propellantProperties().explosionPower() * (float) i);
		level.addFreshEntity(propellant);
		return propellant;
	}

	// Adapted from TntBlock#use
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (!PowderChargeBlock.isPropellantIgniter(itemStack, player) || BigCannonMunitionBlock.doesntIgnite(state))
			return super.use(state, level, pos, player, hand, hit);
		if (!(level.getBlockEntity(pos) instanceof BigCartridgeBlockEntity cartridge) || cartridge.getPower() < 1)
			return super.use(state, level, pos, player, hand, hit);
		PrimedPropellant propellant = this.spawnPrimedPropellant(level, pos, state);
		propellant.setFuse(10);
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
		Item item = itemStack.getItem();
		if (!player.isCreative()) {
			if (itemStack.is(Items.FLINT_AND_STEEL)) {
				itemStack.hurtAndBreak(1, player, playerx -> playerx.broadcastBreakEvent(hand));
			} else {
				itemStack.shrink(1);
			}
		}
		player.awardStat(Stats.ITEM_USED.get(item));
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	// Adapted from TntBlock#onProjectileHit
	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
		if (level.isClientSide)
			return;
		BlockPos blockPos = hit.getBlockPos();
		if (!projectile.isOnFire() || !projectile.mayInteract(level, blockPos) || BigCannonMunitionBlock.doesntIgnite(state))
			return;
		this.spawnPrimedPropellant(level, blockPos, state);
		level.removeBlock(blockPos, false);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		ItemStack result = super.getCloneItemStack(level, pos, state);
		CompoundTag tag = result.getOrCreateTag();
		if (state.getValue(DAMP))
			tag.putBoolean("Damp", true);
		tag.putInt("Power", level.getBlockEntity(pos) instanceof BigCartridgeBlockEntity cartridge ? cartridge.getPower() : 0);
		return result;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		// Adapted from FireBlock#animateTick
		if (state.getValue(DAMP) && random.nextInt(5) < 2) {
			Direction.Axis axis = this.getAxis(state);
			for (int i = 0; i < random.nextInt(1) + 1; i++) {
				level.addParticle(ParticleTypes.DRIPPING_WATER,
					(double) pos.getX() + (axis == Direction.Axis.X ? random.nextFloat() : 0.5 + (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 0.42f),
					(double) pos.getY() + (axis == Direction.Axis.Y ? random.nextFloat() : 0.5 + (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 0.42f),
					(double) pos.getZ() + (axis == Direction.Axis.Z ? random.nextFloat() : 0.5 + (random.nextBoolean() ? -1 : 1) * random.nextFloat() * 0.42f),
					random.nextFloat() * 0.02f, 5.0E-5, random.nextFloat() * 0.02f);
			}
		}
	}

}
