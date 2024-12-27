package rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class FixedCannonMountBoxRenderer {

	/**
	 * Adpated from {@link com.simibubi.create.content.redstone.link.LinkRenderer}
	 */
	public static void tick() {
		Minecraft mc = Minecraft.getInstance();
		HitResult target = mc.hitResult;
		if (target == null || !(target instanceof BlockHitResult))
			return;

		BlockHitResult result = (BlockHitResult) target;
		ClientLevel level = mc.level;
		BlockPos pos = result.getBlockPos();
		Direction face = result.getDirection();

		Component anglePitch = Lang.builder(CreateBigCannons.MOD_ID).translate("fixed_cannon_mount.angle_pitch").component();
		Component angleYaw = Lang.builder(CreateBigCannons.MOD_ID).translate("fixed_cannon_mount.angle_yaw").component();

		for (boolean pitch : Iterate.trueAndFalse) {
			BehaviourType<FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour> type = pitch ? FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour.PITCH_TYPE : FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour.YAW_TYPE;
			FixedCannonMountBlockEntity.FixedCannonMountScrollValueBehaviour behaviour = BlockEntityBehaviour.get(level, pos, type);
			if (behaviour == null)
				continue;
			Pair<BehaviourType<?>, BlockPos> slot = Pair.of(type, pos);
			if (!behaviour.isActive()) {
				CreateClient.OUTLINER.remove(slot);
				continue;
			}

			Component label = pitch ? anglePitch : angleYaw;
			boolean hit = behaviour.testHit(target.getLocation());
			addBox(level, pos, face, behaviour, hit, slot);

			if (!hit)
				continue;

			List<MutableComponent> tip = new ArrayList<>();
			tip.add(label.copy());
			tip.add(Lang.translateDirect("gui.value_settings.hold_to_edit"));
			CreateClient.VALUE_SETTINGS_HANDLER.showHoverTip(tip);
		}
	}

	/**
	 * Copied from {@link com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueRenderer}
	 */
	protected static void addBox(ClientLevel level, BlockPos pos, Direction face, ScrollValueBehaviour behaviour,
								 boolean highlight, Object slot) {
		AABB bb = new AABB(Vec3.ZERO, Vec3.ZERO).inflate(.5f)
			.contract(0, 0, -.5f)
			.move(0, 0, -.125f);
		Component label = behaviour.label;
		ValueBox box = new ValueBox.TextValueBox(label, bb, pos, Components.literal(behaviour.formatValue()));

		box.passive(!highlight)
			.wideOutline();

		CreateClient.OUTLINER.showValueBox(slot, box.transform(behaviour.getSlotPositioning()))
			.highlightFace(face);
	}

}
