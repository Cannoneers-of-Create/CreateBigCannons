package rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount;

import java.util.function.Consumer;

import com.simibubi.create.AllKeys;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBehaviour.ValueSettings;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsScreen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ServerboundSetFixedCannonMountValuePacket;

public class FixedCannonMountValueScreen extends ValueSettingsScreen {

	private final boolean pitch;
	private final BlockPos posCopy;

	public FixedCannonMountValueScreen(BlockPos pos, ValueSettingsBoard board, ValueSettings valueSettings,
                                       Consumer<ValueSettings> onHover, boolean pitch) {
		super(pos, board, valueSettings, onHover);
		this.pitch = pitch;
		this.posCopy = pos;
	}

	@Override
	protected void saveAndClose(double pMouseX, double pMouseY) {
		ValueSettings closest = getClosestCoordinate((int) pMouseX, (int) pMouseY);
		// FIXME: value settings may be face-sensitive on future components - taken from ValueSettingsScreen#saveAndClose
		NetworkPlatform.sendToServer(new ServerboundSetFixedCannonMountValuePacket(this.posCopy, closest.row(), closest.value(),
			null, Direction.UP, AllKeys.ctrlDown(), this.pitch));
		this.onClose();
	}

}
