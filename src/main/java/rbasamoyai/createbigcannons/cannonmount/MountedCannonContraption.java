package rbasamoyai.createbigcannons.cannonmount;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionType;
import com.simibubi.create.content.contraptions.components.structureMovement.NonStationaryLighter;
import com.simibubi.create.content.contraptions.components.structureMovement.render.ContraptionLighter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCContraptionTypes;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.CannonBehavior;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.CannonMaterial;
import rbasamoyai.createbigcannons.cannons.CannonMaterial.FailureMode;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileBlock;

import java.util.*;

public class MountedCannonContraption extends Contraption {
	
	private CannonMaterial cannonMaterial;
	private Direction initialOrientation = Direction.NORTH;
	private BlockPos startPos = BlockPos.ZERO;
	private List<CannonBlockEntityHolder<?>> cannonBlockEntities = new ArrayList<>();
	private boolean isWeakBreech = false;
	
	public MountedCannonContraption() {}
	
	@Override
	public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
		if (!this.collectCannonBlocks(level, pos)) return false;
		this.bounds = new AABB(BlockPos.ZERO);
		this.bounds = this.bounds.inflate(Math.ceil(Math.sqrt(getRadius(this.getBlocks().keySet(), Direction.Axis.Y))));
		return !this.blocks.isEmpty();
	}
	
	private boolean collectCannonBlocks(Level level, BlockPos pos) throws AssemblyException {
		BlockState startState = level.getBlockState(pos);
		
		if (!(startState.getBlock() instanceof CannonBlock startCannon)) {
			return false;
		}
		if (!startCannon.isComplete(startState)) {
			throw hasIncompleteCannonBlocks(pos);
		}
		if (this.hasCannonLoaderInside(level, startState, pos)) {
			throw cannonLoaderInsideDuringAssembly(pos);
		}
		CannonMaterial material = startCannon.getCannonMaterial();
		CannonEnd startEnd = startCannon.getOpeningType(level, startState, pos);
		
		List<StructureBlockInfo> cannonBlocks = new ArrayList<>();
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getTileEntityNBT(level, pos)));
		
		int cannonLength = 1;
		
		Direction cannonFacing = startCannon.getFacing(startState);
		
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, cannonFacing.getAxis());
		Direction negative = positive.getOpposite();
		
		BlockPos start = pos;
		BlockState nextState = level.getBlockState(pos.relative(positive));
		
		CannonEnd positiveEnd = startEnd;
		while (this.isValidCannonBlock(level, nextState, start.relative(positive)) && this.isConnectedToCannon(level, nextState, start.relative(positive), positive, material)) {
			start = start.relative(positive);
			
			if (!((CannonBlock) nextState.getBlock()).isComplete(nextState)) {
				throw hasIncompleteCannonBlocks(start);
			}
			
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getTileEntityNBT(level, start)));
			cannonLength++;
			
			positiveEnd = ((CannonBlock) nextState.getBlock()).getOpeningType(level, nextState, start);
			
			if (this.hasCannonLoaderInside(level, nextState, start)) {
				throw cannonLoaderInsideDuringAssembly(start);
			}
			
			nextState = level.getBlockState(start.relative(positive));
			
			if (cannonLength > getMaxCannonLength()) {
				throw cannonTooLarge();
			}
			if (positiveEnd == CannonEnd.CLOSED) break;
		}
		BlockPos positiveEndPos = positiveEnd == CannonEnd.OPEN ? start : start.relative(negative);
		BlockState positiveEndState = level.getBlockState(start);
		
		start = pos;
		nextState = level.getBlockState(pos.relative(negative));
		
		CannonEnd negativeEnd = startEnd;
		while (this.isValidCannonBlock(level, nextState, start.relative(negative)) && this.isConnectedToCannon(level, nextState, start.relative(negative), negative, material)) {
			start = start.relative(negative);
			
			if (!((CannonBlock) nextState.getBlock()).isComplete(nextState)) {
				throw hasIncompleteCannonBlocks(start);
			}
			
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getTileEntityNBT(level, start)));
			cannonLength++;
			
			negativeEnd = ((CannonBlock) nextState.getBlock()).getOpeningType(level, nextState, start);
			
			if (this.hasCannonLoaderInside(level, nextState, start)) {
				throw cannonLoaderInsideDuringAssembly(start);
			}
			
			nextState = level.getBlockState(start.relative(negative));
			
			if (cannonLength > getMaxCannonLength()) {
				throw cannonTooLarge();
			}
			if (negativeEnd == CannonEnd.CLOSED) break;
		}
		BlockPos negativeEndPos = negativeEnd == CannonEnd.OPEN ? start : start.relative(positive);
		BlockState negativeEndState = level.getBlockState(start);
		
		if (positiveEnd == negativeEnd) {
			throw invalidCannon();
		}
		
		boolean openEndFlag = positiveEnd == CannonEnd.OPEN;
		this.initialOrientation = openEndFlag ? positive : negative;
		this.startPos = openEndFlag ? negativeEndPos : positiveEndPos;
		
		this.isWeakBreech = openEndFlag ? negativeEndState.is(CBCTags.BlockCBC.WEAK_CANNON_END) : positiveEndState.is(CBCTags.BlockCBC.WEAK_CANNON_END);
		
		this.isWeakBreech &= CBCConfigs.SERVER.cannons.weakBreechStrength.get() != -1;

		this.anchor = pos;

		this.startPos = this.startPos.subtract(pos);
		this.cannonBlockEntities.clear();
		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos.subtract(pos);
			StructureBlockInfo localBlockInfo = new StructureBlockInfo(localPos, blockInfo.state, blockInfo.nbt);
			this.getBlocks().put(localPos, localBlockInfo);
			
			if (blockInfo.nbt == null) continue;
			BlockEntity be = BlockEntity.loadStatic(localPos, blockInfo.state, blockInfo.nbt);
			if (!(be instanceof ICannonBlockEntity)) continue;
			this.cannonBlockEntities.add(new CannonBlockEntityHolder<>((BlockEntity & ICannonBlockEntity) be, localBlockInfo));
		}
		this.cannonBlockEntities.sort((a, b) -> Integer.compare(a.blockInfo.pos.distManhattan(this.startPos), b.blockInfo.pos.distManhattan(this.startPos)));
		this.cannonMaterial = material;
		
		return true;
	}
	
	private boolean isValidCannonBlock(LevelAccessor level, BlockState state, BlockPos pos) {
		return state.getBlock() instanceof CannonBlock;
	}
	
	private boolean hasCannonLoaderInside(LevelAccessor level, BlockState state, BlockPos pos) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof ICannonBlockEntity cannon)) return false;
		BlockState containedState = cannon.cannonBehavior().block().state;
		return CBCBlocks.RAM_HEAD.has(containedState) || CBCBlocks.WORM_HEAD.has(containedState) || AllBlocks.PISTON_EXTENSION_POLE.has(containedState);
	}
	
	private boolean isConnectedToCannon(LevelAccessor level, BlockState state, BlockPos pos, Direction connection, CannonMaterial material) {
		CannonBlock cBlock = (CannonBlock) state.getBlock();
		if (cBlock.getCannonMaterialInLevel(level, state, pos) != material) return false;
		return ((ICannonBlockEntity) level.getBlockEntity(pos)).cannonBehavior().isConnectedTo(connection.getOpposite());
	}
	
	public float getWeightForStress() {
		if (this.cannonMaterial == null) {
			return this.blocks.size();
		}
		return this.blocks.size() * this.cannonMaterial.weight();
	}
	
	public Direction initialOrientation() { return this.initialOrientation; }
	
	public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
		StructureBlockInfo foundProjectile = null;
		float chargesUsed = 0;
		float smokeScale = 0;
		int barrelTravelled = 0;
		BlockPos currentPos = BlockPos.ZERO;
		Random rand = level.getRandom();
		
		boolean failed = false;
		boolean canFail = !CBCConfigs.SERVER.failure.disableAllFailure.get();
		CannonBlockEntityHolder<?> failedHolder = null;
		int count = 0;
		
		float spread = 0.0f;
		float spreadAdd = CBCConfigs.SERVER.cannons.powderChargeSpread.getF();
		float spreadSub = CBCConfigs.SERVER.cannons.barrelSpreadReduction.getF();
		
		boolean emptyNoProjectile = false;
		
		int weakBreechStrength = CBCConfigs.SERVER.cannons.weakBreechStrength.get();
		int maxSafeCharges = this.isWeakBreech && weakBreechStrength > -1
				? Math.min(this.cannonMaterial.maxSafeCharges(), weakBreechStrength)
				: this.cannonMaterial.maxSafeCharges();
		
		for (ListIterator<CannonBlockEntityHolder<?>> iter = this.cannonBlockEntities.listIterator(); iter.hasNext(); ) {
			CannonBlockEntityHolder<?> cbeh = iter.next();
			
			Vec3 startDiff = Vec3.atLowerCornerOf(cbeh.blockInfo.pos.subtract(this.startPos));
			if (!cbeh.blockInfo.pos.equals(this.startPos) && Direction.getNearest(startDiff.x, startDiff.y, startDiff.z) != this.initialOrientation) {
				continue;
			}
			
			CannonBehavior behavior = cbeh.blockEntity.cannonBehavior();
			StructureBlockInfo containedBlockInfo = behavior.block();	
			
			if (CBCBlocks.POWDER_CHARGE.has(containedBlockInfo.state) && foundProjectile == null) {
				this.consumeBlock(behavior, cbeh, iter);
				++chargesUsed;
				++smokeScale;
				spread += spreadAdd;
				
				if (canFail && (!cbeh.blockInfo.state.is(CBCTags.BlockCBC.THICK_TUBING) && rollBarrelBurst(rand)
					|| chargesUsed > maxSafeCharges && rollOverloadBurst(rand))) {
					failed = true;
					failedHolder = cbeh;
					break;
				}
				if (emptyNoProjectile && rollFailToIgnite(rand) && canFail) {
					Vec3 failIgnitePos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
					level.playSound(null, failIgnitePos.x, failIgnitePos.y, failIgnitePos.z, cbeh.blockInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 5.0f, 0.0f);
					return;
				}
				emptyNoProjectile = false;
			} else if (containedBlockInfo.state.getBlock() instanceof ProjectileBlock && foundProjectile == null) {
				if (chargesUsed == 0) return;
				foundProjectile = containedBlockInfo;
				if (emptyNoProjectile && rollFailToIgnite(rand) && canFail) {
					Vec3 failIgnitePos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
					level.playSound(null, failIgnitePos.x, failIgnitePos.y, failIgnitePos.z, cbeh.blockInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 5.0f, 0.0f);
					return;
				}
				this.consumeBlock(behavior, cbeh, iter);
				emptyNoProjectile = false;
			} else if (!containedBlockInfo.state.isAir() && foundProjectile != null) {
				if (canFail) {
					failed = true;
					failedHolder = cbeh;
					break;
				} else {
					this.consumeBlock(behavior, cbeh, iter);
				}
			} else if (foundProjectile == null && containedBlockInfo.state.isAir()) {
				if (count == 0) return;
				emptyNoProjectile = true;
				chargesUsed = Math.max(chargesUsed - 1, 0);
			}
			
			currentPos = cbeh.blockInfo.pos;
			
			BlockState cannonState = cbeh.blockInfo.state;
			if (cannonState.getBlock() instanceof CannonBlock cannon && cannon.getOpeningType(level, cannonState, currentPos) == CannonEnd.OPEN) {
				++count;
			}
			
			if (foundProjectile != null) {
				++barrelTravelled;
				if (cbeh.blockInfo.state.is(CBCTags.BlockCBC.REDUCES_SPREAD)) {
					spread = Math.max(spread - spreadSub, 0.0f);
				}
				
				if (chargesUsed > 0 && (double) barrelTravelled / (double) chargesUsed > this.cannonMaterial.squibRatio() && rollSquib(rand)) {
					cbeh.blockEntity.cannonBehavior().loadBlock(foundProjectile);
					CompoundTag tag = cbeh.blockEntity.saveWithFullMetadata();
					tag.remove("x");
					tag.remove("y");
					tag.remove("z");
					StructureBlockInfo squibInfo = new StructureBlockInfo(cbeh.blockInfo.pos, cbeh.blockInfo.state, tag);
					this.getBlocks().put(cbeh.blockInfo.pos, squibInfo);
					iter.set(new CannonBlockEntityHolder<>(cbeh.blockEntity, squibInfo));
					
					Vec3 squibPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
					level.playSound(null, squibPos.x, squibPos.y, squibPos.z, cbeh.blockInfo.state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 10.0f, 0.0f);
					return;
				}
			}	
		}
		if (canFail && failed && failedHolder != null) {
			this.fail(currentPos, level, entity, failedHolder, (int) chargesUsed);
			return;
		}
		
		if (chargesUsed <= 0) {
			chargesUsed = 0.5f;
		}
		
		Vec3 spawnPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 1.0f);
		Vec3 vec = spawnPos.subtract(entity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 1.0f)).normalize();
		
		if (foundProjectile != null && foundProjectile.state.getBlock() instanceof ProjectileBlock projectileBlock) {
			BlockEntity projectileBE = foundProjectile.nbt == null ? null : BlockEntity.loadStatic(foundProjectile.pos, foundProjectile.state, foundProjectile.nbt);
			AbstractCannonProjectile projectile = projectileBlock.getProjectile(level, foundProjectile.state, foundProjectile.pos, projectileBE);
			projectile.setPos(spawnPos);
			projectile.setChargePower(chargesUsed);
			projectile.shoot(vec.x, vec.y, vec.z, chargesUsed, spread);
			projectile.xRotO = projectile.getXRot();
			projectile.yRotO = projectile.getYRot();
			level.addFreshEntity(projectile);
		}
		
		for (ServerPlayer player : level.players()) {
			level.sendParticles(player, new CannonPlumeParticleData(smokeScale), true, spawnPos.x, spawnPos.y, spawnPos.z, 0, vec.x, vec.y, vec.z, 1.0f);
		}
		level.playSound(null, spawnPos.x, spawnPos.y, spawnPos.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 20.0f, 0.0f);
	}
	
	private void consumeBlock(CannonBehavior behavior, CannonBlockEntityHolder<?> cbeh, ListIterator<CannonBlockEntityHolder<?>> iter) {
		behavior.removeBlock();
		CompoundTag tag = cbeh.blockEntity.saveWithFullMetadata();
		tag.remove("x");
		tag.remove("y");
		tag.remove("z");
		StructureBlockInfo consumedInfo = new StructureBlockInfo(cbeh.blockInfo.pos, cbeh.blockInfo.state, tag);
		this.getBlocks().put(cbeh.blockInfo.pos, consumedInfo);
		iter.set(new CannonBlockEntityHolder<>(cbeh.blockEntity, consumedInfo));
	}
	
	private static boolean rollSquib(Random random) {
		float f = CBCConfigs.SERVER.failure.squibChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}
	
	private static boolean rollBarrelBurst(Random random) {
		float f = CBCConfigs.SERVER.failure.barrelChargeBurstChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}
	
	private static boolean rollOverloadBurst(Random random) {
		float f = CBCConfigs.SERVER.failure.overloadBurstChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}
	
	private static boolean rollFailToIgnite(Random random) {
		float f = CBCConfigs.SERVER.failure.interruptedIgnitionChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}
	
	public void fail(BlockPos localPos, Level level, PitchOrientedContraptionEntity entity, CannonBlockEntityHolder<?> cbeh, int charges) {
		Vec3 failurePoint = entity.toGlobalVector(Vec3.atCenterOf(cbeh.blockEntity.getBlockPos()), 1.0f);
		float failScale = CBCConfigs.SERVER.failure.failureExplosionPower.getF();
		if (this.cannonMaterial.failureMode() == FailureMode.RUPTURE) {
			level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, 2 * failScale + 1, Explosion.BlockInteraction.NONE);
			int failInt = Mth.ceil(failScale);
			BlockPos startPos = localPos.relative(this.initialOrientation.getOpposite(), failInt);
			for (int i = 0; i < failInt * 2 + 1; ++i) {
				BlockPos pos = startPos.relative(this.initialOrientation, i);
				this.blocks.remove(pos);
				this.cannonBlockEntities.removeIf(cbeh1 -> cbeh.blockInfo.pos.equals(pos));
			}

			ControlPitchContraption controller = entity.getController();
			if (controller != null) controller.disassemble();
		} else {
			for (Iterator<Map.Entry<BlockPos, StructureBlockInfo>> iter = this.blocks.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry<BlockPos, StructureBlockInfo> entry = iter.next();
				//Vec3 globalPos = entity.toGlobalVector(Vec3.atCenterOf(entry.getKey()), 1.0f);
				this.cannonBlockEntities.removeIf(cbeh1 -> cbeh1.blockInfo.pos.equals(entry.getKey()));
				iter.remove();
			}
			
			float power = (float) charges * failScale;
			level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, power, Explosion.BlockInteraction.DESTROY);
			entity.discard();
		}
	}
	
	@Override
	public CompoundTag writeNBT(boolean clientData) {
		CompoundTag tag = super.writeNBT(clientData);
		tag.putString("CannonMaterial", this.cannonMaterial == null ? CannonMaterial.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
		if (this.initialOrientation != null) {
			tag.putString("InitialOrientation", this.initialOrientation.getSerializedName());
		}
		tag.putLong("LocalStartingPos", this.startPos == null ? 0L : this.startPos.asLong());
		tag.putBoolean("WeakBreech", this.isWeakBreech);
		return tag;
	}
	
	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = CannonMaterial.fromName(new ResourceLocation(tag.getString("CannonMaterial")));
		this.initialOrientation = tag.contains("InitialOrientation", Tag.TAG_STRING) ? Direction.byName(tag.getString("InitialOrientation")) : Direction.NORTH;
		this.startPos = BlockPos.of(tag.getLong("LocalStartingPos"));
		this.isWeakBreech = tag.getBoolean("WeakBreech");
		this.loadBlockEntities();
	}
	
	protected void loadBlockEntities() {
		this.cannonBlockEntities.clear();
		for (StructureBlockInfo blockInfo : this.getBlocks().values()) {
			if (blockInfo.nbt == null) continue;
			BlockEntity be = BlockEntity.loadStatic(blockInfo.pos, blockInfo.state, blockInfo.nbt);
			if (!(be instanceof ICannonBlockEntity)) continue;
			this.cannonBlockEntities.add(new CannonBlockEntityHolder<>((BlockEntity & ICannonBlockEntity) be, blockInfo));
		}
		this.cannonBlockEntities.sort((a, b) -> Integer.compare(a.blockInfo.pos.distManhattan(this.startPos), b.blockInfo.pos.distManhattan(this.startPos)));
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override public ContraptionLighter<?> makeLighter() { return new NonStationaryLighter<>(this); }
	
	private static int getMaxCannonLength() {
		return CBCConfigs.SERVER.cannons.maxCannonLength.get();
	}

	@Override public boolean canBeStabilized(Direction direction, BlockPos pos) { return true; }
	
	@Override protected ContraptionType getType() { return CBCContraptionTypes.MOUNTED_CANNON; }
	
	public static AssemblyException cannonTooLarge() {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonTooLarge", getMaxCannonLength()));
	}
	
	public static AssemblyException invalidCannon() {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.invalidCannon"));
	}
	
	public static AssemblyException cannonLoaderInsideDuringAssembly(BlockPos pos) {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.cannonLoaderInsideDuringAssembly", pos.getX(), pos.getY(), pos.getZ()));
	}
	
	public static AssemblyException hasIncompleteCannonBlocks(BlockPos pos) {
		return new AssemblyException(new TranslatableComponent("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.hasIncompleteCannonBlocks", pos.getX(), pos.getY(), pos.getZ()));
	}
	
	protected static class CannonBlockEntityHolder<T extends BlockEntity & ICannonBlockEntity> {
		protected final T blockEntity;
		protected final StructureBlockInfo blockInfo;
		
		public CannonBlockEntityHolder(T blockEntity, StructureBlockInfo blockInfo) {
			this.blockEntity = blockEntity;
			this.blockInfo = blockInfo;
		}
	}
	
}
