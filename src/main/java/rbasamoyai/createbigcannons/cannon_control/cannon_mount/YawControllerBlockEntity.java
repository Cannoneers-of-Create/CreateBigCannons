package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import java.util.List;

import javax.annotation.Nullable;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class YawControllerBlockEntity extends KineticBlockEntity implements ExtendsCannonMount {

	public YawControllerBlockEntity(BlockEntityType<? extends YawControllerBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Nullable
	@Override
	public CannonMountBlockEntity getCannonMount() {
		return this.level.getBlockEntity(this.worldPosition.above()) instanceof CannonMountBlockEntity mount ? mount : null;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		if (!super.addToGoggleTooltip(tooltip, isPlayerSneaking)) return false;
		CannonMountBlockEntity mount = this.getCannonMount();
		if (mount != null) ExtendsCannonMount.addCannonInfoToTooltip(tooltip, mount.mountedContraption);
		return true;
	}

}
