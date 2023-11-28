package rbasamoyai.createbigcannons.cannonloading;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlockEntity;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundGantryRammerContraptionPacket;

public class GantryRammerContraptionEntity extends AbstractContraptionEntity {

    Direction movementAxis;
    double clientOffsetDiff;
    double axisMotion;

    public GantryRammerContraptionEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public static GantryRammerContraptionEntity create(Level world, Contraption contraption, Direction movementAxis) {
        GantryRammerContraptionEntity entity = new GantryRammerContraptionEntity(CBCEntityTypes.GANTRY_RAMMER_CONTRAPTION.get(), world);
        entity.setContraption(contraption);
        entity.movementAxis = movementAxis;
        return entity;
    }

    @Override
    protected void tickContraption() {
        if (!(contraption instanceof GantryRammerContraption))
            return;

        double prevAxisMotion = axisMotion;
        if (level.isClientSide) {
            clientOffsetDiff *= .75f;
            updateClientMotion();
        }

        checkPinionShaft();
        tickActors();
        Vec3 movementVec = getDeltaMovement();

        if (GantryRammerCollider.collideBlocks(this)) {
            if (!level.isClientSide)
                disassemble();
            return;
        }

        if (!isStalled() && tickCount > 2)
            move(movementVec.x, movementVec.y, movementVec.z);

        if (Math.signum(prevAxisMotion) != Math.signum(axisMotion) && prevAxisMotion != 0)
            contraption.stop(level);
        if (!level.isClientSide && (prevAxisMotion != axisMotion || tickCount % 3 == 0))
            sendPacket();
    }

    protected void checkPinionShaft() {
        Vec3 movementVec;
        Direction facing = ((GantryRammerContraption) contraption).getFacing();
        Vec3 currentPosition = getAnchorVec().add(.5, .5, .5);
        BlockPos gantryShaftPos = new BlockPos(currentPosition).relative(facing.getOpposite());

        BlockEntity te = level.getBlockEntity(gantryShaftPos);
        if (!(te instanceof GantryShaftBlockEntity) || !AllBlocks.GANTRY_SHAFT.has(te.getBlockState())) {
            if (!level.isClientSide) {
                setContraptionMotion(Vec3.ZERO);
                disassemble();
            }
            return;
        }

        BlockState blockState = te.getBlockState();
        Direction direction = blockState.getValue(GantryShaftBlock.FACING);
        GantryShaftBlockEntity GantryShaftBlockEntity = (GantryShaftBlockEntity) te;

        float pinionMovementSpeed = GantryShaftBlockEntity.getPinionMovementSpeed();
        movementVec = Vec3.atLowerCornerOf(direction.getNormal()).scale(pinionMovementSpeed);

        if (blockState.getValue(GantryShaftBlock.POWERED) || pinionMovementSpeed == 0) {
            setContraptionMotion(Vec3.ZERO);
            if (!level.isClientSide)
                disassemble();
            return;
        }

        Vec3 nextPosition = currentPosition.add(movementVec);
        double currentCoord = direction.getAxis()
                .choose(currentPosition.x, currentPosition.y, currentPosition.z);
        double nextCoord = direction.getAxis()
                .choose(nextPosition.x, nextPosition.y, nextPosition.z);

        if ((Mth.floor(currentCoord) + .5f < nextCoord != (pinionMovementSpeed * direction.getAxisDirection()
                .getStep() < 0)))
            if (!GantryShaftBlockEntity.canAssembleOn()) {
                setContraptionMotion(Vec3.ZERO);
                if (!level.isClientSide)
                    disassemble();
                return;
            }

        if (level.isClientSide)
            return;

        axisMotion = pinionMovementSpeed;
        setContraptionMotion(movementVec);
    }

    @Override
    protected void writeAdditional(CompoundTag compound, boolean spawnPacket) {
        NBTHelper.writeEnum(compound, "GantryAxis", movementAxis);
        super.writeAdditional(compound, spawnPacket);
    }

    protected void readAdditional(CompoundTag compound, boolean spawnData) {
        movementAxis = NBTHelper.readEnum(compound, "GantryAxis", Direction.class);
        super.readAdditional(compound, spawnData);
    }

    @Override
    public Vec3 applyRotation(Vec3 localPos, float partialTicks) {
        return localPos;
    }

    @Override
    public Vec3 reverseRotation(Vec3 localPos, float partialTicks) {
        return localPos;
    }

    @Override
    protected StructureTransform makeStructureTransform() {
        return new StructureTransform(new BlockPos(getAnchorVec().add(.5, .5, .5)), 0, 0, 0);
    }

    @Override
    protected float getStalledAngle() {
        return 0;
    }

    @Override
    public void teleportTo(double p_70634_1_, double p_70634_3_, double p_70634_5_) {}

    @Override
	@Environment(EnvType.CLIENT)
    public void lerpTo(double x, double y, double z, float yw, float pt, int inc, boolean t) {}

    @Override
    protected void handleStallInformation(double x, double y, double z, float angle) {
        setPosRaw(x, y, z);
        clientOffsetDiff = 0;
    }

    @Override
    public ContraptionRotationState getRotationState() {
        return ContraptionRotationState.NONE;
    }

    @Override
    public void applyLocalTransforms(PoseStack matrixStack, float partialTicks) { }

    public void updateClientMotion() {
        float modifier = movementAxis.getAxisDirection()
                .getStep();
        setContraptionMotion(Vec3.atLowerCornerOf(movementAxis.getNormal())
                .scale((axisMotion + clientOffsetDiff * modifier / 2f) * ServerSpeedProvider.get()));
    }

    public double getAxisCoord() {
        Vec3 anchorVec = getAnchorVec();
        return movementAxis.getAxis()
                .choose(anchorVec.x, anchorVec.y, anchorVec.z);
    }

    public void sendPacket() {
		NetworkPlatform.sendToClientTracking(new ClientboundGantryRammerContraptionPacket(getId(), getAxisCoord(), axisMotion), this);
    }

	@Environment(EnvType.CLIENT)
    public void handlePacket(ClientboundGantryRammerContraptionPacket packet) {
        this.axisMotion = packet.motion();
		this.clientOffsetDiff = packet.coord() - this.getAxisCoord();
    }
}
