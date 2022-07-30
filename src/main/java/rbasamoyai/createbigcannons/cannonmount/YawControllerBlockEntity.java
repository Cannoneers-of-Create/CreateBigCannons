package rbasamoyai.createbigcannons.cannonmount;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class YawControllerBlockEntity extends KineticTileEntity {

	public YawControllerBlockEntity(BlockEntityType<? extends YawControllerBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
	@Override
	public void tick() {
		super.tick();		
		BlockEntity be = this.level.getBlockEntity(this.worldPosition.above());
		if (be instanceof CannonMountBlockEntity cmbe && cmbe.isRunning()) {
			cmbe.yawSpeed = this.getSpeed();
		}
	}
	
	@Override
	public float calculateStressApplied() {
		BlockEntity be = this.level.getBlockEntity(this.worldPosition.above());
		return be instanceof CannonMountBlockEntity cmbe ? cmbe.calculateStressApplied() : 0.0f;
	}
	
}