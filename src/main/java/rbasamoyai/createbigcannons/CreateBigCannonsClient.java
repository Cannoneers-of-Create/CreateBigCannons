
package rbasamoyai.createbigcannons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.FOVModifierEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.effects.CannonPlumeParticle;
import rbasamoyai.createbigcannons.cannon_control.effects.CannonSmokeParticle;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobParticle;
import rbasamoyai.createbigcannons.network.CBCNetwork;
import rbasamoyai.createbigcannons.network.ServerboundFiringActionPacket;
import rbasamoyai.createbigcannons.network.ServerboundSetFireRatePacket;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;

import java.util.Arrays;
import java.util.List;

public class CreateBigCannonsClient {

	private static final String KEY_ROOT = "key." + CreateBigCannons.MOD_ID;
	private static final String KEY_CATEGORY = KEY_ROOT + ".category";
	public static final KeyMapping PITCH_MODE = new KeyMapping(KEY_ROOT + ".pitch_mode", GLFW.GLFW_KEY_C, KEY_CATEGORY);
	public static final KeyMapping FIRE_CONTROLLED_CANNON = new KeyMapping(KEY_ROOT + ".fire_controlled_cannon", GLFW.GLFW_MOUSE_BUTTON_LEFT, KEY_CATEGORY);

	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		CBCBlockPartials.init();
		modEventBus.addListener(CreateBigCannonsClient::onClientSetup);
		modEventBus.addListener(CreateBigCannonsClient::onRegisterParticleFactories);
		
		forgeEventBus.addListener(CreateBigCannonsClient::getFogColor);
		forgeEventBus.addListener(CreateBigCannonsClient::getFogDensity);
		forgeEventBus.addListener(CreateBigCannonsClient::onClientGameTick);
		forgeEventBus.addListener(CreateBigCannonsClient::onScrollMouse);
		forgeEventBus.addListener(CreateBigCannonsClient::onFovModify);
		forgeEventBus.addListener(CreateBigCannonsClient::onPlayerRenderPre);
	}
	
	public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event) {
		Minecraft mc = Minecraft.getInstance();
		ParticleEngine engine = mc.particleEngine;
		
		engine.register(CBCParticleTypes.CANNON_PLUME.get(), new CannonPlumeParticle.Provider());
		engine.register(CBCParticleTypes.FLUID_BLOB.get(), new FluidBlobParticle.Provider());
		engine.register(CBCParticleTypes.CANNON_SMOKE.get(), CannonSmokeParticle.Provider::new);
	}
	
	public static void onClientSetup(FMLClientSetupEvent event) {
		CBCPonderIndex.register();
		CBCPonderIndex.registerTags();
		CBCBlockPartials.resolveDeferredModels();
		ClientRegistry.registerKeyBinding(PITCH_MODE);
		ClientRegistry.registerKeyBinding(FIRE_CONTROLLED_CANNON);

		ItemProperties.register(CBCItems.PARTIALLY_FORMED_AUTOCANNON_CARTRIDGE.get(), CreateBigCannons.resource("formed"), (stack, level, player, a) -> {
			return stack.getOrCreateTag().getCompound("SequencedAssembly").getInt("Step") - 1;
		});
	}
	
	public static void getFogColor(FogColors event) {
		Camera info = event.getCamera();
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		BlockPos blockPos = info.getBlockPosition();
		FluidState fluidState = level.getFluidState(blockPos);
		if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

		Fluid fluid = fluidState.getType();

		if (CBCFluids.MOLTEN_CAST_IRON.get().isSame(fluid)) {
			event.setRed(70 / 255f);
			event.setGreen(10 / 255f);
			event.setBlue(11 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_BRONZE.get().isSame(fluid)) {
			event.setRed(99 / 255f);
			event.setGreen(66 / 255f);
			event.setBlue(22 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_STEEL.get().isSame(fluid)) {
			event.setRed(111 / 255f);
			event.setGreen(110 / 255f);
			event.setBlue(106 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_NETHERSTEEL.get().isSame(fluid)) {
			event.setRed(76 / 255f);
			event.setGreen(50 / 255f);
			event.setBlue(58 / 255f);
			return;
		}
	}
	
	public static void getFogDensity(RenderFogEvent event) {
		if (!event.isCancelable()) return;
		
		Camera info = event.getCamera();
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		BlockPos blockPos = info.getBlockPosition();
		FluidState fluidState = level.getFluidState(blockPos);
		if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

		Fluid fluid = fluidState.getType();
		
		List<Fluid> moltenMetals = Arrays.asList(
				CBCFluids.MOLTEN_CAST_IRON.get(),
				CBCFluids.MOLTEN_BRONZE.get(),
				CBCFluids.MOLTEN_STEEL.get(),
				CBCFluids.MOLTEN_NETHERSTEEL.get());
		
		for (Fluid fluid1 : moltenMetals) {
			if (fluid1.isSame(fluid)) {
				event.scaleFarPlaneDistance(1f / 32f);
				event.setCanceled(true);
				return;
			}
		}
	}

	public static void onClientGameTick(TickEvent.ClientTickEvent evt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
		if (mc.player.getRootVehicle() instanceof CannonCarriageEntity carriage) {
			net.minecraft.client.player.Input input = mc.player.input;
			boolean isPitching = CreateBigCannonsClient.PITCH_MODE.isDown();
			carriage.setInput(input.left, input.right, input.up, input.down, isPitching);
			mc.player.handsBusy |= input.left | input.right | input.up | input.down;
		}

		if (CreateBigCannonsClient.FIRE_CONTROLLED_CANNON.isDown() && isControllingCannon(mc.player)) {
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

	private static boolean isControllingCannon(Entity entity) {
		Entity vehicle = entity.getVehicle();
		return vehicle instanceof CannonCarriageEntity || vehicle instanceof PitchOrientedContraptionEntity;
	}
	
}
