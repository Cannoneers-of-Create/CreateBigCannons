package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import com.simibubi.create.foundation.utility.Components;

import net.minecraft.ChatFormatting;
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
	MutableComponent gogglesPadding = Components.literal("    ");
	String bigCannonStrengthValueKey = CreateBigCannons.MOD_ID + ".display_source.cannon_mount.cannon_strength.value";
	String degreesKey = CreateBigCannons.MOD_ID + ".display_source.cannon_mount.degrees";

	static void addCannonInfoToTooltip(List<Component> tooltip, @Nullable PitchOrientedContraptionEntity mountedContraption) {
		if (mountedContraption != null && mountedContraption.getContraption() instanceof AbstractMountedCannonContraption cannon) {
			tooltip.add(gogglesPadding.copy().append(cannonYawComponent.copy().withStyle(ChatFormatting.GRAY))
				.append(Components.translatable(degreesKey, mountedContraption.yaw).withStyle(ChatFormatting.WHITE)));
			tooltip.add(gogglesPadding.copy().append(cannonPitchComponent.copy().withStyle(ChatFormatting.GRAY))
				.append(Components.translatable(degreesKey, mountedContraption.pitch).withStyle(ChatFormatting.WHITE)));
			if (cannon instanceof MountedBigCannonContraption bigCannon) {
				tooltip.add(gogglesPadding.copy().append(bigCannonStrengthComponent.copy().withStyle(ChatFormatting.GRAY))
					.append(Components.translatable(bigCannonStrengthValueKey, bigCannon.getMaxSafeCharges()).withStyle(ChatFormatting.WHITE)));
			}
		} else {
			tooltip.add(noCannonPresent.copy());
		}
	}

}
