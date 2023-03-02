package rbasamoyai.createbigcannons.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.network.CBCNetwork;
import rbasamoyai.createbigcannons.network.ServerboundFiringActionPacket;
import rbasamoyai.createbigcannons.network.ServerboundSetFireRatePacket;

import static rbasamoyai.createbigcannons.CBCClientCommon.FIRE_CONTROLLED_CANNON;
import static rbasamoyai.createbigcannons.CBCClientCommon.PITCH_MODE;

public class CBCClientForge {

	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		CBCBlockPartials.init();
		modEventBus.addListener(CBCClientForge::onClientSetup);
		modEventBus.addListener(CBCClientForge::onRegisterParticleFactories);

		forgeEventBus.addListener(CBCClientForge::getFogColor);
		forgeEventBus.addListener(CBCClientForge::getFogDensity);
		forgeEventBus.addListener(CBCClientForge::onClientGameTick);
		forgeEventBus.addListener(CBCClientForge::onScrollMouse);
		forgeEventBus.addListener(CBCClientForge::onFovModify);
		forgeEventBus.addListener(CBCClientForge::onPlayerRenderPre);
	}

	public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		CBCClientCommon.onRegisterParticleFactories(mc, mc.particleEngine);
	}

	public static void onClientSetup(FMLClientSetupEvent event) {
		CBCClientCommon.onClientSetup();
		CBCClientCommon.registerKeyMappings(ClientRegistry::registerKeyBinding);
	}

	public static void getFogColor(EntityViewRenderEvent.FogColors event) {
		CBCClientCommon.setFogColor(event.getCamera(), (r, g, b) -> {
			event.setRed(r);
			event.setGreen(g);
			event.setBlue(b);
		});
	}

	public static void getFogDensity(EntityViewRenderEvent.RenderFogEvent event) {
		if (!event.isCancelable()) return;
		float density = CBCClientCommon.getFogDensity(event.getCamera(), event.getFarPlaneDistance());
		if (density != -1) {
			event.setFarPlaneDistance(density);
			event.setCanceled(true);
		}
	}

	public static void onClientGameTick(TickEvent.ClientTickEvent evt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
		if (mc.player.getRootVehicle() instanceof CannonCarriageEntity carriage) {
			net.minecraft.client.player.Input input = mc.player.input;
			boolean isPitching = PITCH_MODE.isDown();
			carriage.setInput(input.left, input.right, input.up, input.down, isPitching);
			mc.player.handsBusy |= input.left | input.right | input.up | input.down;
		}

		if (FIRE_CONTROLLED_CANNON.isDown() && isControllingCannon(mc.player)) {
			mc.player.handsBusy = true;
			CBCNetwork.INSTANCE.sendToServer(new ServerboundFiringActionPacket());
		}
	}

	public static void onScrollMouse(InputEvent.MouseScrollEvent evt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
		if (mc.player.getRootVehicle() instanceof CannonCarriageEntity) {
			double scrollDelta = evt.getScrollDelta();
			int fireRateAdjustment = 0;
			if (scrollDelta > 0) fireRateAdjustment = 1;
			else if (scrollDelta < 0) fireRateAdjustment = -1;
			if (fireRateAdjustment != 0) {
				mc.player.handsBusy = true;
				CBCNetwork.INSTANCE.sendToServer(new ServerboundSetFireRatePacket(fireRateAdjustment));
				if (evt.isCancelable()) evt.setCanceled(true);
			}
		}
	}

	public static void onFovModify(FOVModifierEvent evt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || !mc.options.getCameraType().isFirstPerson()) return;
		if (mc.options.keyUse.isDown() && isControllingCannon(mc.player)) evt.setNewfov(evt.getFov() * 0.5f);
	}

	public static void onPlayerRenderPre(RenderPlayerEvent.Pre evt) {
		PoseStack stack = evt.getPoseStack();
		Player player = evt.getPlayer();
		float pt = evt.getPartialTick();

		if (player.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(player) != null) {
			Vector3f pVec = new Vector3f(player.getPosition(pt));
			stack.translate(-pVec.x(), -pVec.y(), -pVec.z());

			BlockPos seatPos = poce.getSeatPos(player);
			double offs = player.getEyeHeight() + player.getMyRidingOffset() - 0.15;
			Vec3 vec = new Vec3(poce.getInitialOrientation().step()).scale(0.25);
			Vector3f pVec1 = new Vector3f(poce.toGlobalVector(Vec3.atCenterOf(seatPos).subtract(vec).subtract(0, offs, 0), pt));
			stack.translate(pVec1.x(), pVec1.y(), pVec1.z());

			float yr = (-Mth.lerp(pt, player.yRotO, player.getYRot()) + 90) * Mth.DEG_TO_RAD;
			Vector3f vec3 = new Vector3f(Mth.sin(yr), 0, Mth.cos(yr));
			float xr = Mth.lerp(pt, player.xRotO, player.getXRot());
			stack.mulPose(vec3.rotationDegrees(xr));
		}
	}

}
