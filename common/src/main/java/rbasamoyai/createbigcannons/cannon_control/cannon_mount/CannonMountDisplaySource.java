package rbasamoyai.createbigcannons.cannon_control.cannon_mount;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.DisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayLayout;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import com.simibubi.create.foundation.utility.Components;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CannonMountDisplaySource extends DisplaySource {

	private static final MutableComponent notEnoughSpace = Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.not_enough_space");
	private static final MutableComponent forCannonStatus = Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.for_cannon_status");
	private static final MutableComponent cannonYaw = Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.cannon_yaw");
	private static final MutableComponent cannonPitch = Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.cannon_pitch");
	private static final String degreesKey = CreateBigCannons.MOD_ID + ".display_source.cannon_mount.degrees";

	private static final List<MutableComponent> notEnoughSpaceSingle = List.of(notEnoughSpace.copy().append(forCannonStatus.copy()));
	private static final List<MutableComponent> notEnoughSpaceDouble = List.of(notEnoughSpace.copy(), forCannonStatus.copy());
	private static final List<List<MutableComponent>> notEnoughSpaceFlap = List.of(List.of(notEnoughSpace.copy()), List.of(forCannonStatus.copy()));

	private static final List<List<MutableComponent>> noCannonPresent = List.of(List.of(Components.translatable(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.no_cannon_present")));

	@Override
	public List<MutableComponent> provideText(DisplayLinkContext context, DisplayTargetStats stats) {
		if (stats.maxRows() < 2) return notEnoughSpaceSingle;
		//if (stats.maxRows() < 4) return notEnoughSpaceDouble;
		Stream<MutableComponent> componentList = this.getComponents(context, false).stream().map(components -> {
			return components.stream().reduce(MutableComponent::append).orElse(EMPTY_LINE);
		});
		if (context.getTargetBlockEntity() instanceof LecternBlockEntity) {
			return List.of(componentList.reduce((comp1, comp2) -> comp1.append(Components.literal("\n"))
					.append(comp2)).orElse(EMPTY_LINE));
		}
		return componentList.toList();
	}

	@Override
	public List<List<MutableComponent>> provideFlapDisplayText(DisplayLinkContext context, DisplayTargetStats stats) {
		if (stats.maxRows() < 2) {
		//if (stats.maxRows() < 4) {
			context.flapDisplayContext = Boolean.FALSE;
			return notEnoughSpaceFlap;
		}
		List<List<MutableComponent>> components = this.getComponents(context, true);
		if (components == noCannonPresent) {
			context.flapDisplayContext = Boolean.FALSE;
			return components;
		}
		if (stats.maxColumns() < 20) {
			context.flapDisplayContext = Boolean.FALSE;
			return notEnoughSpaceFlap;
		}
		return components;
	}

	@Override
	public void loadFlapDisplayLayout(DisplayLinkContext context, FlapDisplayBlockEntity flapDisplay, FlapDisplayLayout layout) {
		if (context.flapDisplayContext instanceof Boolean b && !b) {
			if (!layout.isLayout("Default")) layout.loadDefault(flapDisplay.getMaxCharCount());
			return;
		}

		String layoutKey = "CannonMount";
		if (layout.isLayout(layoutKey)) return;

		FlapDisplaySection label = new FlapDisplaySection(13 * FlapDisplaySection.MONOSPACE, "alphabet", false, true);
		FlapDisplaySection number = new FlapDisplaySection(6 * FlapDisplaySection.MONOSPACE, "number", false, true);

		layout.configure(layoutKey, List.of(label, number));
	}

	private List<List<MutableComponent>> getComponents(DisplayLinkContext context, boolean forFlapDisplay) {
		List<List<MutableComponent>> text = new ArrayList<>();

		BlockEntity be = context.getSourceBlockEntity();
		if (!(be instanceof ExtendsCannonMount ext)) return text;
		CannonMountBlockEntity mount = ext.getCannonMount();
		if (mount == null) return text;
		if (mount.mountedContraption == null) return noCannonPresent;

		if (CBCConfigs.SERVER.cannons.shouldDisplayCannonRotation.get()) {
			text.add(List.of(cannonYaw.copy(), Components.translatable(degreesKey, String.format("%.1f", mount.getYawOffset(0)))));
			text.add(List.of(cannonPitch.copy(), Components.translatable(degreesKey, String.format("%.1f", mount.getDisplayPitch()))));
		}
		return text;
	}

	@Override protected String getTranslationKey() { return "cannon_mount_source"; }

}
