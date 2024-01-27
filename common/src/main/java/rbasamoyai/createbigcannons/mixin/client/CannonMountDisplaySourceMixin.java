package rbasamoyai.createbigcannons.mixin.client;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.NumericSingleLineDisplaySource;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;

import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountDisplaySource;

@Mixin(CannonMountDisplaySource.class)
public abstract class CannonMountDisplaySourceMixin extends NumericSingleLineDisplaySource {

	@Override
	public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
		super.initConfigurationWidgets(context, builder, isFirstLine);
		if (isFirstLine)
			return;
		builder.addSelectionScrollInput(0, 120,
			(si, l) -> si.forOptions(Arrays.asList(Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.yaw"),
				Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.pitch")))
				.titled(Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.display_rotation_axis")),
			"Mode");
	}

}
