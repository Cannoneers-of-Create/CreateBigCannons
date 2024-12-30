package rbasamoyai.createbigcannons.munitions.big_cannon.propellant;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.VoxelShaper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.config.PowderChargeProperties;

public class PowderChargeBlock extends RotatedPillarBlock implements IWrenchable, BigCannonPropellantBlock, SimpleWaterloggedBlock {

	private static final EnumProperty<Axis> AXIS = RotatedPillarBlock.AXIS;

	private final VoxelShaper shapes;

	public PowderChargeBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
			.setValue(WATERLOGGED, false)
			.setValue(DAMP, false));
		this.shapes = this.makeShapes();
	}

	private VoxelShaper makeShapes() {
		VoxelShape base = Block.box(3, 0, 3, 13, 16, 13);
		return new AllShapes.Builder(base).forAxis();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
		builder.add(DAMP);
	}

	@Override public boolean isRandomlyTicking(BlockState state) { return BigCannonMunitionBlock.canDry(state); }

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
		boolean result = SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
		if (result && !level.isClientSide())
			level.setBlock(pos, level.getBlockState(pos).setValue(DAMP, true), 3);
		return result;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (BigCannonMunitionBlock.canDry(state))
			level.setBlock(pos, state.setValue(DAMP, false), 3);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		ItemStack itemStack = context.getItemInHand();
		boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		boolean damp = (itemStack.getOrCreateTag().getBoolean("Damp") || waterlogged) && !context.getLevel().dimensionType().ultraWarm();
		return super.getStateForPlacement(context).setValue(DAMP, damp).setValue(WATERLOGGED, waterlogged);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return this.shapes.get(state.getValue(AXIS));
	}

	@Override
	public boolean canBeLoaded(BlockState state, Direction.Axis axis) {
		return axis == state.getValue(AXIS);
	}

	@Override
	public boolean canBeIgnited(StructureBlockInfo data, Direction dir) {
		return !data.state().getValue(DAMP)
			|| !CBCConfigs.SERVER.munitions.dampPropellantBlocksStartingIgnition.get()
			|| !this.getProperties().propellantProperties().dampAmmoDoesntIgniteAsStarter();
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
		return this.getPowerMultiplier(data) * this.getProperties().propellantProperties().strength();
	}

	@Override
	public float getChargePower(ItemStack stack) {
		return this.getPowerMultiplier(stack) * this.getProperties().propellantProperties().strength();
	}

	@Override
	public float getStressOnCannon(StructureBlockInfo data) {
		return this.getPowerMultiplier(data) * this.getProperties().propellantProperties().addedStress();
	}

	@Override
	public float getStressOnCannon(ItemStack stack) {
		return this.getPowerMultiplier(stack) * this.getProperties().propellantProperties().addedStress();
	}

	@Override
	public float getSpread(StructureBlockInfo data) {
		return this.getPowerMultiplier(data) * this.getProperties().propellantProperties().addedSpread();
	}

	@Override
	public float getRecoil(StructureBlockInfo data) {
		return this.getPowerMultiplier(data) * this.getProperties().propellantProperties().addedRecoil();
	}

	@Override
	public void consumePropellant(BigCannonBehavior behavior) {
		behavior.removeBlock();
	}

	@Override
	public BlockState onCannonRotate(BlockState oldState, Direction.Axis rotationAxis, Rotation rotation) {
		if (oldState.hasProperty(BlockStateProperties.AXIS)) {
			Direction.Axis axis = oldState.getValue(BlockStateProperties.AXIS);
			if (axis == rotationAxis) return oldState;
			for (int i = 0; i < rotation.ordinal(); ++i) {
				axis = switch (axis) {
					case X -> rotationAxis == Direction.Axis.Y ? Direction.Axis.Z : Direction.Axis.Y;
					case Y -> rotationAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
					case Z -> rotationAxis == Direction.Axis.Y ? Direction.Axis.X : Direction.Axis.Y;
				};
			}
			return oldState.setValue(BlockStateProperties.AXIS, axis);
		}
		return oldState;
	}

	@Override public Axis getAxis(BlockState state) { return state.getValue(AXIS); }

	@Override
	public StructureBlockInfo getHandloadingInfo(ItemStack stack, BlockPos localPos, Direction cannonOrientation) {
		BlockState state = this.defaultBlockState().setValue(AXIS, cannonOrientation.getAxis())
			.setValue(DAMP, stack.getOrCreateTag().getBoolean("Damp"));
		return new StructureBlockInfo(localPos, state, null);
	}

	@Override
	public ItemStack getExtractedItem(StructureBlockInfo info) {
		ItemStack result = new ItemStack(this);
		if (info.state().getValue(DAMP))
			result.getOrCreateTag().putBoolean("Damp", true);
		return result;
	}

	protected PowderChargeProperties getProperties() {
		return CBCMunitionPropertiesHandlers.POWDER_CHARGE.getPropertiesOf(this);
	}

	@Override
	public void createbigcannons$onBlockExplode(Level level, BlockPos pos, BlockState state, Explosion explosion) {
		if (!level.isClientSide && !BigCannonMunitionBlock.doesntIgnite(state))
			this.spawnPrimedPropellant(level, pos, state);
	}

	public PrimedPropellant spawnPrimedPropellant(Level level, BlockPos pos, BlockState state) {
		Vec3 entityPos = Vec3.atCenterOf(pos);
		PrimedPropellant propellant = PrimedPropellant.create(level, entityPos.x(), entityPos.y(), entityPos.z(), state);
		propellant.setExplosionPower(this.getProperties().propellantProperties().explosionPower());
		level.addFreshEntity(propellant);
		return propellant;
	}

	// Adapted from TntBlock#use
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (!isPropellantIgniter(itemStack, player) || BigCannonMunitionBlock.doesntIgnite(state))
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

	public static boolean isPropellantIgniter(ItemStack itemStack, Player player) {
		return itemStack.is(Items.FLINT_AND_STEEL) || itemStack.is(Items.FIRE_CHARGE);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		ItemStack result = super.getCloneItemStack(level, pos, state);
		if (state.getValue(DAMP))
			result.getOrCreateTag().putBoolean("Damp", true);
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
