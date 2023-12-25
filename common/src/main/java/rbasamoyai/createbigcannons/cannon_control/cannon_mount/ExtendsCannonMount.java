package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import com.simibubi.create.foundation.utility.Components;

import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import javax.annotation.Nullable;

import java.util.List;

public interface ExtendsCannonMount {

	@Nullable CannonMountBlockEntity getCannonMount();

	MutableComponent noCannonPresent = Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.no_cannon_present");
	MutableComponent cannonYawComponent = Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.cannon_yaw");
	MutableComponent cannonPitchComponent = Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.cannon_pitch");
	MutableComponent bigCannonStrengthComponent = Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.cannon_strength");
	String bigCannonStrengthValueKey = CreateBigCannons.MOD_ID + ".display_source.cannon_mount.cannon_strength.value";
	String degreesKey = CreateBigCannons.MOD_ID + ".display_source.cannon_mount.degrees";

	static void addCannonInfoToTooltip(List<Component> tooltip, @Nullable PitchOrientedContraptionEntity mountedContraption) {
		if (mountedContraption != null && mountedContraption.getContraption() instanceof AbstractMountedCannonContraption cannon) {
			Direction dir = mountedContraption.getInitialOrientation();
			boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);

			Lang.builder().add(cannonYawComponent.copy().withStyle(ChatFormatting.GRAY)
					.append(Components.translatable(degreesKey, String.format("%.2f", mountedContraption.yaw)).withStyle(ChatFormatting.WHITE)))
				.forGoggles(tooltip);
			Lang.builder().add(cannonPitchComponent.copy().withStyle(ChatFormatting.GRAY)
					.append(Components.translatable(degreesKey, String.format("%.2f", flag ? mountedContraption.pitch : -mountedContraption.pitch)).withStyle(ChatFormatting.WHITE)))
				.forGoggles(tooltip);
			if (cannon instanceof MountedBigCannonContraption bigCannon) {
				Lang.builder().add(bigCannonStrengthComponent.copy().withStyle(ChatFormatting.GRAY)
					.append(Components.translatable(bigCannonStrengthValueKey, bigCannon.getMaxSafeCharges()).withStyle(ChatFormatting.WHITE)))
				.forGoggles(tooltip);
			}
		} else {
			Lang.builder().add(noCannonPresent.copy()).forGoggles(tooltip);
		}
	}

}
