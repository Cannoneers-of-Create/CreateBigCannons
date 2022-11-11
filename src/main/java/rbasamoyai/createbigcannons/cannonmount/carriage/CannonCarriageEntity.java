package rbasamoyai.createbigcannons.cannonmount.carriage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;

public class CannonCarriageEntity extends Entity {
	
	private PitchOrientedContraptionEntity cannonContraption;
	
	public CannonCarriageEntity(EntityType<? extends CannonCarriageEntity> type, Level level) {
		super(type, level);
	}

	@Override protected void defineSynchedData() {}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		return super.interact(player, hand);
	}

	public void disassemble() {
		Direction dir = this.getDirection();
		if (this.cannonContraption != null) this.cannonContraption.stopRiding();
		BlockPos placePos = this.blockPosition();
		if (this.level.getBlockState(placePos).getDestroySpeed(this.level, placePos) != -1) {
			this.level.setBlock(placePos, CBCBlocks.CANNON_CARRIAGE.getDefaultState().setValue(BlockStateProperties.HORIZONTAL_FACING, dir), 11);
		}
		this.discard();
	}

	public void setCannonContraption(PitchOrientedContraptionEntity contraption) {
		this.cannonContraption = contraption;
		this.addPassenger(contraption);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {

	}

	@Override public Packet<?> getAddEntityPacket() { return new ClientboundAddEntityPacket(this); }

}
