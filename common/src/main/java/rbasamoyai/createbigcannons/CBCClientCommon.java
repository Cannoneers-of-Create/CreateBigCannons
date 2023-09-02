package rbasamoyai.createbigcannons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.mojang.math.Axis;

import org.lwjgl.glfw.GLFW;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Vector3f;

import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlock;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
import rbasamoyai.createbigcannons.network.ServerboundFiringActionPacket;
import rbasamoyai.createbigcannons.network.ServerboundSetFireRatePacket;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;

public class CBCClientCommon {

	private static final String KEY_ROOT = "key." + CreateBigCannons.MOD_ID;
	public static final KeyMapping PITCH_MODE = IndexPlatform.createSafeKeyMapping(KEY_ROOT + ".pitch_mode", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C);
	public static final KeyMapping FIRE_CONTROLLED_CANNON = IndexPlatform.createSafeKeyMapping(KEY_ROOT + ".fire_controlled_cannon", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT);
	public static final List<KeyMapping> KEYS = new ArrayList<>();

	public static void onRegisterParticleFactories(Minecraft mc, ParticleEngine engine) {
		CBCParticleTypes.registerFactories();
	}

	public static void onClientSetup() {
		CBCPonderIndex.register();
		CBCPonderIndex.registerTags();
		CBCBlockPartials.resolveDeferredModels();

		IndexPlatform.registerClampedItemProperty(CBCItems.PARTIALLY_FORMED_AUTOCANNON_CARTRIDGE.get(), CreateBigCannons.resource("formed"), (stack, level, player, a) -> {
			return stack.getOrCreateTag().getCompound("SequencedAssembly").getInt("Step") - 1;
		});

		IndexPlatform.registerClampedItemProperty(CBCItems.PARTIALLY_FORMED_BIG_CARTRIDGE.get(), CreateBigCannons.resource("formed"), (stack, level, player, a) -> {
			return stack.getOrCreateTag().getCompound("SequencedAssembly").getInt("Step") - 1;
		});

		IndexPlatform.registerClampedItemProperty(CBCBlocks.BIG_CARTRIDGE.get().asItem(), CreateBigCannons.resource("big_cartridge_filled"),
			(stack, level, player, a) -> {
				return BigCartridgeBlockItem.getPower(stack);
			});
	}

	public static void registerKeyMappings(Consumer<KeyMapping> cons) {
		cons.accept(PITCH_MODE);
		cons.accept(FIRE_CONTROLLED_CANNON);
		KEYS.add(PITCH_MODE);
		KEYS.add(FIRE_CONTROLLED_CANNON);
	}

	public static void setFogColor(Camera info, SetColorWrapper wrapper) {
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		BlockPos blockPos = info.getBlockPosition();
		FluidState fluidState = level.getFluidState(blockPos);
		if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

		Fluid fluid = fluidState.getType();

		if (CBCFluids.MOLTEN_CAST_IRON.get().isSame(fluid)) {
			wrapper.setFogColor(70 / 255f, 10 / 255f, 11 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_BRONZE.get().isSame(fluid)) {
			wrapper.setFogColor(99 / 255f, 66 / 255f, 22 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_STEEL.get().isSame(fluid)) {
			wrapper.setFogColor(111 / 255f, 110 / 255f, 106 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_NETHERSTEEL.get().isSame(fluid)) {
			wrapper.setFogColor(76 / 255f, 50 / 255f, 58 / 255f);
			return;
		}
	}

	public interface SetColorWrapper {
		void setFogColor(float r, float g, float b);
	}

	public static float getFogDensity(Camera info, float currentDensity) {
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		BlockPos blockPos = info.getBlockPosition();
		FluidState fluidState = level.getFluidState(blockPos);
		if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return -1;

		Fluid fluid = fluidState.getType();

		List<Fluid> moltenMetals = Arrays.asList(
				CBCFluids.MOLTEN_CAST_IRON.get(),
				CBCFluids.MOLTEN_BRONZE.get(),
				CBCFluids.MOLTEN_STEEL.get(),
				CBCFluids.MOLTEN_NETHERSTEEL.get());

		for (Fluid fluid1 : moltenMetals) {
			if (fluid1.isSame(fluid)) {
				return 1f / 32f;
			}
		}
		return -1;
	}

	public static void onClientGameTick(Minecraft mc) {
		if (mc.player == null || mc.level == null) return;
		if (mc.player.getRootVehicle() instanceof CannonCarriageEntity carriage) {
			net.minecraft.client.player.Input input = mc.player.input;
			boolean isPitching = CBCClientCommon.PITCH_MODE.isDown();
			carriage.setInput(input.left, input.right, input.up, input.down, isPitching);
			mc.player.handsBusy |= input.left | input.right | input.up | input.down;
		}

		if (CBCClientCommon.FIRE_CONTROLLED_CANNON.isDown() && isControllingCannon(mc.player)) {
			mc.player.handsBusy = true;
			NetworkPlatform.sendToServer(new ServerboundFiringActionPacket());
		}
	}

	public static boolean onScrollMouse(Minecraft mc, double delta) {
		if (mc.player == null || mc.level == null) return false;
		if (mc.player.getRootVehicle() instanceof CannonCarriageEntity) {
			int fireRateAdjustment = 0;
			if (delta > 0) fireRateAdjustment = 1;
			else if (delta < 0) fireRateAdjustment = -1;
			if (fireRateAdjustment != 0) {
				mc.player.handsBusy = true;
				NetworkPlatform.sendToServer(new ServerboundSetFireRatePacket(fireRateAdjustment));
				return true;
			}
		}
		return false;
	}

	public static float onFovModify(Minecraft mc, float oldFov) {
		if (mc.player == null || !mc.options.getCameraType().isFirstPerson()) return lerpFov(mc, oldFov);
		return lerpFov(mc, mc.options.keyUse.isDown() && isControllingCannon(mc.player) ? oldFov * 0.5f : oldFov);
	}

	private static float lerpFov(Minecraft mc, float fov) {
		return Mth.lerp(mc.options.fovEffectScale().get().floatValue(), 1.0F, fov);
	}

	public static void onPlayerRenderPre(PoseStack stack, LivingEntity player, float partialTicks) {
		if (player.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(player) != null) {
			Vector3f pVec = player.getPosition(partialTicks).toVector3f();
			stack.translate(-pVec.x(), -pVec.y(), -pVec.z());

			BlockPos seatPos = poce.getSeatPos(player);
			double offs = player.getEyeHeight() + player.getMyRidingOffset() - 0.15;
			Vec3 vec = new Vec3(poce.getInitialOrientation().step()).scale(0.25);
			Vector3f pVec1 = poce.toGlobalVector(Vec3.atCenterOf(seatPos).subtract(vec).subtract(0, offs, 0), partialTicks).toVector3f();
			stack.translate(pVec1.x(), pVec1.y(), pVec1.z());

			float yr = (-Mth.lerp(partialTicks, player.yRotO, player.getYRot()) + 90) * Mth.DEG_TO_RAD;
			Vector3f vec3 = new Vector3f(Mth.sin(yr), 0, Mth.cos(yr));
			float xr = Mth.lerp(partialTicks, player.xRotO, player.getXRot());
			stack.mulPose(Axis.of(vec3).rotationDegrees(xr));
		}
	}

	private static boolean isControllingCannon(Entity entity) {
		Entity vehicle = entity.getVehicle();
		return vehicle instanceof CannonCarriageEntity || vehicle instanceof PitchOrientedContraptionEntity;
	}

	public static Direction.Axis getRotationAxis(BlockState state) {
		boolean flag = state.getValue(QuickfiringBreechBlock.AXIS);
		return switch (state.getValue(QuickfiringBreechBlock.FACING).getAxis()) {
			case X -> flag ? Direction.Axis.Y : Direction.Axis.Z;
			case Y -> flag ? Direction.Axis.X : Direction.Axis.Z;
			case Z -> flag ? Direction.Axis.X : Direction.Axis.Y;
		};
	}

	public static PartialModel getBreechblockForState(BlockState state) {
		return state.getBlock() instanceof BigCannonBlock cBlock ? CBCBlockPartials.breechblockFor(cBlock.getCannonMaterial())
			: CBCBlockPartials.CAST_IRON_SLIDING_BREECHBLOCK;
	}

	public static PartialModel getScrewBreechForState(BlockState state) {
		return state.getBlock() instanceof BigCannonBlock cBlock ? CBCBlockPartials.screwLockFor(cBlock.getCannonMaterial())
			: CBCBlockPartials.STEEL_SCREW_LOCK;
	}

	public static void onTextureAtlasStitchPre(Consumer<ResourceLocation> cons) {
		cons.accept(CreateBigCannons.resource("item/tracer_slot"));
	}

}
