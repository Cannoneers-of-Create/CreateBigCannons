package rbasamoyai.createbigcannons.mixin.client;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.NumericSingleLineDisplaySource;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;

import net.minecraft.network.chat.Component;
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
			(si, l) -> si.forOptions(Arrays.asList(Component.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.yaw"),
				Component.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.pitch")))
				.titled(Component.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.display_rotation_axis")),
			"Mode");
	}

}
