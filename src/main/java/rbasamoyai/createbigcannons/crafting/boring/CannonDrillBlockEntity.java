package rbasamoyai.createbigcannons.crafting.boring;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.simibubi.create.content.contraptions.base.DirectionalAxisKineticBlock;
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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.network.PacketDistributor;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.PoleContraption;
import rbasamoyai.createbigcannons.base.PoleMoverBlockEntity;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.network.CBCNetwork;
import rbasamoyai.createbigcannons.network.ClientboundUpdateContraptionPacket;

public class CannonDrillBlockEntity extends PoleMoverBlockEntity {

	protected BlockPos boringPos;
	protected float boreSpeed;
	protected float addedStressImpact;
	protected FailureReason failureReason = FailureReason.NONE;
	protected FluidTank lubricant;
	private LazyOptional<IFluidHandler> fluidOptional;
	
	public CannonDrillBlockEntity(BlockEntityType<? extends CannonDrillBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.lubricant = new FluidTank(1000, fs -> fs.getFluid() == Fluids.WATER);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
			boolean alongFirst = this.getBlockState().getValue(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
			Direction.Axis pipeAxis = switch (facing.getAxis()) {
				case X -> alongFirst ? Direction.Axis.Z : Direction.Axis.Y;
				case Y -> alongFirst ? Direction.Axis.Z : Direction.Axis.X;
				default -> alongFirst ? Direction.Axis.Y : Direction.Axis.X;
			};
			if (pipeAxis == side.getAxis()) {
				return this.getFluidOptional().cast();
			}
		}
		return super.getCapability(cap, side);
	}
	
	private LazyOptional<IFluidHandler> getFluidOptional() {
		if (this.fluidOptional == null) {
			this.fluidOptional = LazyOptional.of(() -> this.lubricant);
		}
		return this.fluidOptional;
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if (this.fluidOptional != null) this.fluidOptional.invalidate();
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		this.boringPos = compound.contains("BoringPos") ? NbtUtils.readBlockPos(compound.getCompound("BoringPos")) : null;
		this.lubricant.readFromNBT(compound.getCompound("FluidContent"));
		
		if (!clientPacket) return;
		this.boreSpeed = compound.getFloat("BoreSpeed");
		this.addedStressImpact = compound.getFloat("AddedStress");
		this.failureReason = compound.contains("FailureReason") ? FailureReason.fromId(compound.getString("FailureReason")) : FailureReason.NONE;
	}
	
	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		if (this.boringPos != null) compound.put("BoringPos", NbtUtils.writeBlockPos(this.boringPos));
		compound.put("FluidContent", this.lubricant.writeToNBT(new CompoundTag()));
		
		if (!clientPacket) return;
		if (this.addedStressImpact > 0.0f) compound.putFloat("AddedStress", this.addedStressImpact);
		if (this.boreSpeed != 0.0f) compound.putFloat("BoreSpeed", this.boreSpeed);
		if (this.failureReason != FailureReason.NONE) compound.putString("FailureReason", this.failureReason.getSerializedName());
	}

	@Override
	protected PoleContraption innerAssemble() throws AssemblyException {
		if (!(this.level.getBlockState(this.worldPosition).getBlock() instanceof CannonDrillBlock)) return null;
		
		Direction facing = this.getBlockState().getValue(CannonDrillBlock.FACING);
		CannonDrillingContraption contraption = new CannonDrillingContraption(facing, this.getMovementSpeed() < 0);
		if (!contraption.assemble(this.level, this.worldPosition)) return null;
		
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, facing.getAxis());
		Direction movementDirection = (this.getSpeed() > 0) ^ facing.getAxis() != Direction.Axis.Z ? positive : positive.getOpposite();
		BlockPos anchor = contraption.anchor.relative(facing, contraption.initialExtensionProgress());
		return ContraptionCollider.isCollidingWithWorld(this.level, contraption, anchor.relative(movementDirection), movementDirection) ? null : contraption;
	}

	@Override
	public void disassemble() {
		if (!this.running && this.movedContraption == null) return;
		if (!this.remove) {
			this.getLevel().setBlock(this.worldPosition, this.getBlockState().setValue(CannonDrillBlock.STATE, PistonState.EXTENDED), 3 | 16);
		}
		super.disassemble();
	}
	
	@Override
	protected boolean moveAndCollideContraption() {
		if (super.moveAndCollideContraption()) return true;
		
		if (this.level.isClientSide) return false;
		
		CannonDrillingContraption drill = (CannonDrillingContraption) this.movedContraption.getContraption();
		AABB bounds = this.movedContraption.getBoundingBox();
		Vec3 pos = this.movedContraption.position();
		BlockPos gridPos = new BlockPos(pos);
		Vec3 motion = this.movedContraption.getDeltaMovement();
		
		Direction facing = drill.orientation();
		Direction positive = Direction.fromAxisAndDirection(facing.getAxis(), Direction.AxisDirection.POSITIVE);
		Vec3 mask = (new Vec3(1, 1, 1)).subtract(positive.getStepX(), positive.getStepY(), positive.getStepZ());
		BlockPos maskedPos = new BlockPos(pos.multiply(mask));
		
		if (drill == null || bounds == null) return false;
		
		Direction movementDirection = Direction.getNearest(motion.x, motion.y, motion.z);
		if (movementDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
			gridPos = gridPos.relative(movementDirection);
		}
		
		boolean isBoringBlock = false;
		
		for (ControlledContraptionEntity other : this.level.getEntitiesOfClass(
				ControlledContraptionEntity.class, bounds.inflate(1), e -> !e.equals(this.movedContraption))) {
			if (!(other.getContraption() instanceof BearingContraption lathe)) continue;
			
			AABB otherBounds = other.getBoundingBox();
			Direction otherFacing = lathe.getFacing();
			Vec3 otherPosition = other.position();
			
			if (other.isPassenger() || otherBounds == null || !bounds.move(motion).intersects(otherBounds)) {
				continue;
			}
			
			BlockPos otherMaskedPos = new BlockPos(other.getAnchorVec().multiply(mask));
			if (!maskedPos.equals(otherMaskedPos) || otherFacing != facing.getOpposite() || movementDirection == otherFacing) break;
			
			BlockPos bearingPos = new BlockPos(other.getAnchorVec()).relative(facing);
			if (!(this.level.getBlockEntity(bearingPos) instanceof MechanicalBearingTileEntity bearing)) continue;
			
			for (BlockPos colliderPos : drill.getColliders(this.level, movementDirection)) {
				StructureBlockInfo drillBlockInfo = drill.getBlocks().get(colliderPos);
				BlockPos globalPos = colliderPos.offset(gridPos);
				BlockPos otherColliderPos = globalPos.subtract(new BlockPos(otherPosition));
				if (!lathe.getBlocks().containsKey(otherColliderPos)) continue;
				if (!CBCBlocks.CANNON_DRILL_BIT.has(drillBlockInfo.state)) continue;
				
				if (this.boringPos == null) {
					this.boringPos = globalPos;
				}
				BlockPos boringOffset = this.boringPos.subtract(new BlockPos(otherPosition));
				if (!lathe.getBlocks().containsKey(boringOffset)) continue;
				StructureBlockInfo latheBlockInfo = lathe.getBlocks().get(boringOffset);
				
				if (!(latheBlockInfo.state.getBlock() instanceof CannonBlock cBlock) || !cBlock.canInteractWithDrill(latheBlockInfo.state)) {
					this.boringPos = null;
					this.boreSpeed = 0;
					this.addedStressImpact = 0;
					return true;
				}
				if (!(cBlock instanceof TransformableByBoring unbored)) continue;
				
				int drainSpeed = (int) Mth.abs(bearing.getSpeed() * 0.5f);
				if (Math.abs(bearing.getSpeed()) > Math.abs(this.getSpeed())) {
					this.failureReason = FailureReason.TOO_WEAK;
				} else if (this.lubricant.drain(drainSpeed, FluidAction.EXECUTE).getAmount() < drainSpeed) {
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
					this.boringPos = null;
					this.boreSpeed = 0;
					this.addedStressImpact = 0;
					return true;
				}
				this.failureReason = FailureReason.NONE;
				
				isBoringBlock = true;
				
				float weight = cBlock.getCannonMaterial().weight();
				this.boreSpeed = bearing.getAngularSpeed() / 512f / (weight == 0 ? 1f : weight);
				float fSpeed = Math.abs(this.boreSpeed);
				this.boreSpeed = fSpeed * Math.signum(this.getSpeed());
				this.addedStressImpact = weight;
			
				if (this.level.getGameTime() % 4 == 0) {
					this.level.playSound(null, globalPos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0f, Math.min(Math.abs(bearing.getSpeed()) / 128f, 2f));
				}
				
				if (this.farEnoughFromPos(movementDirection, pos)) {
					BlockState boredState = unbored.getBoredBlockState(latheBlockInfo.state);
					if (latheBlockInfo.nbt != null && boredState.getBlock() instanceof ITE<?> boredBE) {
						latheBlockInfo.nbt.putString("id", boredBE.getTileEntityType().getRegistryName().toString());
					}
					
					StructureBlockInfo newInfo = new StructureBlockInfo(boringOffset, boredState, latheBlockInfo.nbt);
					lathe.getBlocks().put(boringOffset, newInfo);
					bearing.notifyUpdate();
					
					this.level.playSound(null, globalPos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0f, 1.0f);
					CBCNetwork.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> other), new ClientboundUpdateContraptionPacket(other, boringOffset, newInfo));
					this.boringPos = null;
				}
				
				this.notifyUpdate();
				break;
			}
			break;
		}
		
		if (!isBoringBlock) {
			this.boringPos = null;
			this.boreSpeed = 0;
			this.addedStressImpact = 0;
		}
		
		return false;
	}
	
	private boolean farEnoughFromPos(Direction movement, Vec3 pos) {
		boolean flag = movement.getAxisDirection() == Direction.AxisDirection.POSITIVE;
		return switch (movement.getAxis()) {
			case X -> flag ? pos.x - this.boringPos.getX() >= 1 : pos.x - this.boringPos.getX() <= 0;
			case Y -> flag ? pos.y - this.boringPos.getY() >= 1 : pos.y - this.boringPos.getY() <= 0;
			default -> flag ? pos.z - this.boringPos.getZ() >= 1 : pos.z - this.boringPos.getZ() <= 0;
		};
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
		tooltip.add(TextComponent.EMPTY);
		this.containedFluidTooltip(tooltip, isPlayerSneaking, this.getFluidOptional());
		if (this.failureReason != FailureReason.NONE) {
			tooltip.add(TextComponent.EMPTY);
			Lang.builder("exception")
				.translate(CreateBigCannons.MOD_ID + ".cannon_drill.tooltip.encounteredProblem")
				.style(ChatFormatting.GOLD)
				.forGoggles(tooltip);
			Component exceptionText = Lang.builder("exception")
					.translate(CreateBigCannons.MOD_ID + ".cannon_drill.tooltip." + this.failureReason.getSerializedName())
					.component();
			tooltip.addAll(TooltipHelper.cutTextComponent(exceptionText, ChatFormatting.GRAY, ChatFormatting.WHITE));
		}
		
		return true;
	}
	
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
