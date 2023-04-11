package rbasamoyai.createbigcannons.crafting.boring;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.contraptions.components.structureMovement.ContraptionCollider;
import com.simibubi.create.content.contraptions.components.structureMovement.ControlledContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.bearing.BearingContraption;
import com.simibubi.create.content.contraptions.components.structureMovement.bearing.MechanicalBearingTileEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.piston.MechanicalPistonBlock.PistonState;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.base.PoleContraption;
import rbasamoyai.createbigcannons.base.PoleMoverBlockEntity;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredBigCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundUpdateContraptionPacket;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractCannonDrillBlockEntity extends PoleMoverBlockEntity {

	protected AbstractContraptionEntity latheEntity;
	protected BlockPos boringPos;
	protected float boreSpeed;
	protected float addedStressImpact;
	protected FailureReason failureReason = FailureReason.NONE;
	private DrillBoringBlockRecipe currentRecipe;
	
	private static final int SYNC_RATE = 8;
	protected boolean queuedSync;
	protected int syncCooldown;
	
	protected AbstractCannonDrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	public void sendData() {
		if (this.syncCooldown > 0) {
			this.queuedSync = true;
			return;
		}
		super.sendData();
		this.queuedSync = false;
		this.syncCooldown = SYNC_RATE;
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		this.boringPos = compound.contains("BoringPos") ? NbtUtils.readBlockPos(compound.getCompound("BoringPos")) : null;
		
		if (!clientPacket) return;
		this.boreSpeed = compound.getFloat("BoreSpeed");
		this.addedStressImpact = compound.getFloat("AddedStress");
		this.failureReason = compound.contains("FailureReason") ? FailureReason.fromId(compound.getString("FailureReason")) : FailureReason.NONE;
	}
	
	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		if (this.boringPos != null) compound.put("BoringPos", NbtUtils.writeBlockPos(this.boringPos));
		
		if (!clientPacket) return;
		if (this.addedStressImpact > 0.0f) compound.putFloat("AddedStress", this.addedStressImpact);
		if (this.boreSpeed != 0.0f) compound.putFloat("BoreSpeed", this.boreSpeed);
		if (this.failureReason != FailureReason.NONE) compound.putString("FailureReason", this.failureReason.getSerializedName());
	}

	@Override
	protected PoleContraption innerAssemble() throws AssemblyException {
		if (!(this.level.getBlockState(this.worldPosition).getBlock() instanceof CannonDrillBlock)) return null;
		this.failureReason = FailureReason.NONE;
		
		Direction facing = this.getBlockState().getValue(CannonDrillBlock.FACING);
		CannonDrillingContraption contraption = new CannonDrillingContraption(facing, this.getMovementSpeed() < 0);
		if (!contraption.assemble(this.level, this.worldPosition)) return null;
		
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, facing.getAxis());
		Direction movementDirection = (this.getSpeed() > 0) ^ facing.getAxis() != Direction.Axis.Z ? positive : positive.getOpposite();
		BlockPos anchor = contraption.anchor.relative(facing, contraption.initialExtensionProgress());
		return this.initialCollide(contraption, anchor, movementDirection) ? null : contraption;
	}
	
	private boolean initialCollide(CannonDrillingContraption contraption, BlockPos anchor, Direction movementDirection) {
		if (ContraptionCollider.isCollidingWithWorld(this.level, contraption, anchor.relative(movementDirection), movementDirection)) return true;
		
		Vec3 pos = Vec3.atLowerCornerOf(anchor.relative(movementDirection));
		
		BlockPos gridPos = new BlockPos(pos);
		AABB bounds = contraption.bounds.move(pos);
		if (movementDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
			gridPos = gridPos.relative(movementDirection);
		}
		gridPos = gridPos.relative(movementDirection.getOpposite());
		
		Direction facing = contraption.orientation();
		Direction positive = Direction.fromAxisAndDirection(facing.getAxis(), Direction.AxisDirection.POSITIVE);
		Vec3 mask = (new Vec3(1, 1, 1)).subtract(positive.getStepX(), positive.getStepY(), positive.getStepZ());
		BlockPos maskedPos = new BlockPos(pos.multiply(mask));
		
		AABB newBounds = bounds.expandTowards(new Vec3(facing.step()).scale(128)).inflate(2);
		
		for (ControlledContraptionEntity other : this.level.getEntitiesOfClass(
				ControlledContraptionEntity.class, newBounds, e -> !e.equals(this.movedContraption))) {
			if (!(other.getContraption() instanceof BearingContraption lathe)) continue;
			
			AABB otherBounds = other.getBoundingBox();
			Direction otherFacing = lathe.getFacing();
			Vec3 otherPosition = other.position();
			
			if (other.isPassenger() || otherBounds == null || !bounds.intersects(otherBounds)) {
				continue;
			}
			
			BlockPos otherMaskedPos = new BlockPos(other.getAnchorVec().multiply(mask));
			if (!maskedPos.equals(otherMaskedPos) || otherFacing != facing.getOpposite() || movementDirection == otherFacing) continue;
			
			BlockPos bearingPos = new BlockPos(other.getAnchorVec()).relative(facing);
			if (!(this.level.getBlockEntity(bearingPos) instanceof MechanicalBearingTileEntity bearing)) continue;
			
			for (BlockPos colliderPos : contraption.getColliders(this.level, movementDirection)) {
				StructureBlockInfo drillBlockInfo = contraption.getBlocks().get(colliderPos);
				BlockPos globalPos = colliderPos.offset(gridPos);
				BlockPos otherColliderPos = globalPos.subtract(new BlockPos(otherPosition));
				if (!lathe.getBlocks().containsKey(otherColliderPos)) continue;
				if (!CBCBlocks.CANNON_DRILL_BIT.has(drillBlockInfo.state)) continue;
				
				BlockPos boringOffset = globalPos.subtract(new BlockPos(otherPosition));
				if (!lathe.getBlocks().containsKey(boringOffset)) continue;
				StructureBlockInfo latheBlockInfo = lathe.getBlocks().get(boringOffset);

				DrillBoringBlockRecipe recipe = getBlockRecipe(latheBlockInfo.state);
				if (recipe != null) {
					this.currentRecipe = recipe;
					return false;
				} else if (!latheBlockInfo.state.is(CBCTags.BlockCBC.DRILL_CAN_PASS_THROUGH)) {
					return true;
				}
			}
		}
		
		return false;
	}

	public static DrillBoringBlockRecipe getBlockRecipe(BlockState block) {
		return BlockRecipesManager.getRecipesOfType(BlockRecipeType.DRILL_BORING).stream()
				.map(DrillBoringBlockRecipe.class::cast)
				.filter(r -> r.matches(block))
				.findAny().orElse(null);
	}

	@Override
	public void disassemble() {
		if (!this.running && this.movedContraption == null) return;
		if (!this.remove) {
			this.level.setBlock(this.worldPosition, this.getBlockState().setValue(CannonDrillBlock.STATE, PistonState.EXTENDED), 3 | 16);
		}
		super.disassemble();
	}
	
	@Override
	public void tick() {
		if (!this.level.isClientSide && this.running && this.movedContraption != null && this.offset + this.getMovementSpeed() >= this.getExtensionRange()) {
			this.collideWithContraptionToBore(null, false);
		}
		
		super.tick();
		
		if (this.syncCooldown > 0) {
			this.syncCooldown--;
			if (this.syncCooldown == 0 && this.queuedSync) {
				this.sendData();
			}
		}
	}
	
	@Override
	protected boolean moveAndCollideContraption() {
		if (super.moveAndCollideContraption()) {
			this.collideWithContraptionToBore(null, true);
			this.boreSpeed = 0;
			this.addedStressImpact = 0;
			this.latheEntity = null;
			return true;
		}
		return false;
	}
	
	@Override
	public void onSpeedChanged(float prevSpeed) {
		super.onSpeedChanged(prevSpeed);
		this.boreSpeed = 0;
		this.addedStressImpact = 0;
		this.latheEntity = null;
	}
	
	public boolean collideWithContraptionToBore(ControlledContraptionEntity other, boolean collide) {
		if (this.level.isClientSide) return false;
		if (other == null && this.latheEntity == null) return false;
		
		CannonDrillingContraption drill = (CannonDrillingContraption) this.movedContraption.getContraption();
		AABB bounds = this.movedContraption.getBoundingBox();
		Vec3 pos = this.movedContraption.position();
		BlockPos gridPos = new BlockPos(pos);
		Vec3 motion = this.movedContraption.getDeltaMovement();
		
		if (drill == null || bounds == null) return false;
		
		Direction facing = drill.orientation();
		Direction positive = Direction.fromAxisAndDirection(facing.getAxis(), Direction.AxisDirection.POSITIVE);
		Vec3 mask = (new Vec3(1, 1, 1)).subtract(positive.getStepX(), positive.getStepY(), positive.getStepZ());
		BlockPos maskedPos = new BlockPos(pos.multiply(mask));
		
		Direction movementDirection = collide ? drill.orientation() : Direction.getNearest(motion.x, motion.y, motion.z);
		if (!collide && movementDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
			gridPos = gridPos.relative(movementDirection);
		}
		if (collide && movementDirection.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
			gridPos = gridPos.relative(movementDirection.getOpposite());
		}
		
		boolean isBoringBlock = false;
		
		if (this.latheEntity == null) this.latheEntity = other;
		if (!(this.latheEntity.getContraption() instanceof BearingContraption lathe)) {
			this.stopBoringState();			
			return false;
		}
		
		AABB otherBounds = this.latheEntity.getBoundingBox();
		Direction otherFacing = lathe.getFacing();
		Vec3 otherPosition = this.latheEntity.position();
		
		if (this.latheEntity.isPassenger() || otherBounds == null || !bounds.move(motion).intersects(otherBounds)) {
			this.stopBoringState();
			return false;
		}
		
		BlockPos otherMaskedPos = new BlockPos(this.latheEntity.getAnchorVec().multiply(mask));
		if (!maskedPos.equals(otherMaskedPos) || otherFacing != facing.getOpposite() || movementDirection == otherFacing) {
			this.stopBoringState();
			return false;
		}
		
		BlockPos bearingPos = new BlockPos(this.latheEntity.getAnchorVec()).relative(facing);
		if (!(this.level.getBlockEntity(bearingPos) instanceof MechanicalBearingTileEntity bearing)) {
			this.stopBoringState();
			return false;
		}
		
		for (BlockPos colliderPos : drill.getColliders(this.level, movementDirection)) {
			StructureBlockInfo drillBlockInfo = drill.getBlocks().get(colliderPos);
			BlockPos globalPos = colliderPos.offset(gridPos);
			BlockPos otherColliderPos = globalPos.subtract(new BlockPos(otherPosition));
			if (!lathe.getBlocks().containsKey(otherColliderPos)) continue;
			if (!CBCBlocks.CANNON_DRILL_BIT.has(drillBlockInfo.state)) continue;
			
			BlockPos boringOffset = globalPos.subtract(new BlockPos(otherPosition));
			if (!lathe.getBlocks().containsKey(boringOffset)) continue;
			StructureBlockInfo latheBlockInfo = lathe.getBlocks().get(boringOffset);
			
			BlockPos currentPos = collide || this.offset + this.getMovementSpeed() >= this.getExtensionRange() ? globalPos.relative(movementDirection) : globalPos;
			if (this.boringPos == null) {
				this.boringPos = currentPos;
			} else if (!this.boringPos.equals(currentPos)) {
				this.tryFinishingBoring();
				this.boringPos = currentPos;
			}

			DrillBoringBlockRecipe candidate = getBlockRecipe(latheBlockInfo.state);
			if (candidate == null) {
				if (latheBlockInfo.state.is(CBCTags.BlockCBC.DRILL_CAN_PASS_THROUGH)) continue;
				this.stopBoringState();
				this.simulateStop();
				return true;
			}
			this.currentRecipe = candidate;
			
			int drainSpeed = (int) Mth.abs(bearing.getSpeed() * 0.5f);
			if (Math.abs(bearing.getSpeed()) > Math.abs(this.getSpeed())) {
				this.failureReason = FailureReason.TOO_WEAK;
			} else if (this.drainLubricant(drainSpeed)) {
				if (this.level instanceof ServerLevel slevel) {
					Vec3 particlePos = Vec3.atCenterOf(globalPos);
					slevel.sendParticles(ParticleTypes.SMOKE, particlePos.x, particlePos.y, particlePos.z, 10, 0.0d, 1.0d, 0.0d, 0.1d);
				}
				this.level.playSound(null, globalPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 2f);
				this.failureReason = FailureReason.DRY_BORE;
			}
			
			if (this.failureReason != FailureReason.NONE) {
				if (this.boreSpeed != 0) {
					this.offset = (int) this.offset;
					if (!this.boringPos.equals(globalPos)) --this.offset;
				}
				this.stopBoringState();
				return true;
			}
			this.failureReason = FailureReason.NONE;
			
			isBoringBlock = true;
			
			float weight = latheBlockInfo.state.getBlock() instanceof BigCannonBlock cBlock ? cBlock.getCannonMaterial().weight() : 1;
			this.boreSpeed = bearing.getAngularSpeed() / 512f / (weight == 0 ? 1f : weight);
			float fSpeed = Math.abs(this.boreSpeed);
			this.boreSpeed = fSpeed * Math.signum(this.getSpeed());
			this.addedStressImpact = weight;
		
			if (this.level.getGameTime() % 4 == 0) {
				this.level.playSound(null, globalPos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, Math.min(Math.abs(bearing.getSpeed()) / 128f, 2f));
			}
			
			this.notifyUpdate();
			break;
		}
		
		if (!isBoringBlock) this.stopBoringState();
		
		return true;
	}

	protected abstract boolean drainLubricant(int drainSpeed);
	
	protected void stopBoringState() {
		this.boringPos = null;
		this.boreSpeed = 0;
		this.addedStressImpact = 0;
		this.latheEntity = null;
	}
	
	protected void simulateStop() {
		this.movedContraption.setContraptionMotion(Vec3.ZERO);
		this.offset = this.getGridOffset(this.offset);
		this.resetContraptionToOffset();
		this.collided();
	}

	protected void tryFinishingBoring() {
		if (!(this.level instanceof ServerLevel slevel)
			|| this.latheEntity == null
			|| this.movedContraption == null
			|| this.boringPos == null
			|| this.currentRecipe == null) return;
		if (!(this.latheEntity.getContraption() instanceof BearingContraption lathe) || lathe.stalled) return;
		BlockPos boringOffset = this.boringPos.subtract(new BlockPos(this.latheEntity.position()));
		if (!lathe.getBlocks().containsKey(boringOffset)) return;
		
		StructureBlockInfo latheBlockInfo = lathe.getBlocks().get(boringOffset);
		Direction facing = ((CannonDrillingContraption) this.movedContraption.getContraption()).orientation();
		
		BlockState boredState = this.currentRecipe.getResultState(latheBlockInfo.state);

		if (latheBlockInfo.nbt != null && boredState.getBlock() instanceof ITE<?> boredBE) {
			BlockEntity be = boredBE.newBlockEntity(BlockPos.ZERO, boredState);
			latheBlockInfo.nbt.putBoolean("JustBored", true);
			
			if (boredState.getBlock() instanceof BigCannonBlock cBlock && be instanceof LayeredBigCannonBlockEntity layered) {
				CannonCastShape shape = cBlock.getCannonShape();
				CompoundTag layerConnectionsTag = new CompoundTag();
				Direction opp = facing.getOpposite();
				
				StructureBlockInfo nextBlockInfo = lathe.getBlocks().get(boringOffset.relative(facing));
				if (nextBlockInfo != null && nextBlockInfo.nbt != null) {
					BlockEntity be1 = BlockEntity.loadStatic(BlockPos.ZERO, nextBlockInfo.state, nextBlockInfo.nbt);
					if (be1 instanceof LayeredBigCannonBlockEntity layered1 && layered1.isLayerConnectedTo(opp, shape)
						|| be1 instanceof ICannonBlockEntity<?> cbe1 && cbe1.cannonBehavior().isConnectedTo(opp)) {
						ResourceLocation key = CBCRegistries.CANNON_CAST_SHAPES.getKey(cBlock.getCannonShape());
						ListTag list = new ListTag();
						list.add(StringTag.valueOf(key.toString()));
						layerConnectionsTag.put(facing.getSerializedName(), list);
					}
				}
				StructureBlockInfo prevBlockInfo = lathe.getBlocks().get(boringOffset.relative(opp));
				if (prevBlockInfo != null && prevBlockInfo.nbt != null) {
					BlockEntity be2 = BlockEntity.loadStatic(BlockPos.ZERO, prevBlockInfo.state, prevBlockInfo.nbt);
					if (be2 instanceof LayeredBigCannonBlockEntity layered2 && layered2.isLayerConnectedTo(facing, shape)
						|| be2 instanceof ICannonBlockEntity<?> cbe2 && cbe2.cannonBehavior().isConnectedTo(facing)) {
						ResourceLocation key = CBCRegistries.CANNON_CAST_SHAPES.getKey(cBlock.getCannonShape());
						ListTag list = new ListTag();
						list.add(StringTag.valueOf(key.toString()));
						layerConnectionsTag.put(opp.getSerializedName(), list);
					}
				}
				
				latheBlockInfo.nbt.put("LayerConnections", layerConnectionsTag);
			}
		}
		
		BlockPos bearingPos = new BlockPos(this.latheEntity.getAnchorVec()).relative(facing);
		if (!(this.level.getBlockEntity(bearingPos) instanceof MechanicalBearingTileEntity bearing)) return;
		
		StructureBlockInfo newInfo = new StructureBlockInfo(boringOffset, boredState, latheBlockInfo.nbt);
		lathe.getBlocks().put(boringOffset, newInfo);
		bearing.notifyUpdate();
		
		ResourceLocation unboredId = Registry.BLOCK.getKey(latheBlockInfo.state.getBlock());
		LootTable table = slevel.getServer().getLootTables().get(new ResourceLocation(unboredId.getNamespace(), "boring_scrap/" + unboredId.getPath()));
		List<ItemStack> scrap = table.getRandomItems(new LootContext.Builder(slevel)
				.withRandom(slevel.random)
				.withParameter(LootContextParams.BLOCK_STATE, latheBlockInfo.state)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.boringPos))
				.withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
				.create(LootContextParamSets.BLOCK));
		scrap.forEach(s -> Block.popResource(this.level, this.boringPos, s));
		
		this.level.playSound(null, this.boringPos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0f, 1.0f);
		NetworkPlatform.sendToClientTracking(new ClientboundUpdateContraptionPacket(this.latheEntity, boringOffset, newInfo), this.latheEntity);
		this.boringPos = null;
	}
	
	@Override
	public float calculateStressApplied() {
		return this.lastStressApplied = super.calculateStressApplied() + this.addedStressImpact;
	}
	
	@Override
	public float getMovementSpeed() {
		float movementSpeed = this.failureReason != FailureReason.NONE ? 0: Mth.clamp(this.boreSpeed != 0 ? this.boreSpeed : convertToLinear(this.getSpeed()), -0.49f, 0.49f);
		if (this.level.isClientSide) {
			movementSpeed *= ServerSpeedProvider.get();
		}
		Direction facing = this.getBlockState().getValue(CannonDrillBlock.FACING);
		int movementModifier = facing.getAxisDirection().getStep() * (facing.getAxis() == Direction.Axis.Z ? -1 : 1);
		movementSpeed = movementSpeed * -movementModifier + this.clientOffsetDiff * 0.5f;
		
		movementSpeed = Mth.clamp(movementSpeed, 0 - this.offset, this.extensionLength - this.offset);
		return movementSpeed;
	}
	
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		tooltip.add(Component.empty());
		this.addFluidInfoToTooltip(tooltip, isPlayerSneaking);
		if (this.failureReason != FailureReason.NONE) {
			tooltip.add(Component.empty());
			Lang.builder("exception")
				.translate(CreateBigCannons.MOD_ID + ".cannon_drill.tooltip.encounteredProblem")
				.style(ChatFormatting.GOLD)
				.forGoggles(tooltip);
			Component exceptionText = Lang.builder("exception")
					.translate(CreateBigCannons.MOD_ID + ".cannon_drill.tooltip." + this.failureReason.getSerializedName())
					.component();
			tooltip.addAll(TooltipHelper.cutTextComponent(exceptionText, ChatFormatting.GRAY, ChatFormatting.WHITE, 4));
		}
		
		return true;
	}

	protected abstract void addFluidInfoToTooltip(List<Component> tooltip, boolean isPlayerSneaking);
	
	public enum FailureReason implements StringRepresentable {
		DRY_BORE("dryBore"),
		TOO_WEAK("tooWeak"),
		NONE("none");
		
		private static final Map<String, FailureReason> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(FailureReason::getSerializedName, Function.identity()));
		
		private final String id;
		
		private FailureReason(String id) {
			this.id = id;
		}
		
		@Override public String getSerializedName() { return this.id; }
		
		public static FailureReason fromId(String id) {
			return BY_ID.getOrDefault(id, DRY_BORE);
		}
	}

}
