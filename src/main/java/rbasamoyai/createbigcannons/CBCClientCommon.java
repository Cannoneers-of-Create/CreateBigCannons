package rbasamoyai.createbigcannons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.base.goggles.EntityGoggleOverlayRenderer;
import rbasamoyai.createbigcannons.block_hit_effects.BlockHitEffect;
import rbasamoyai.createbigcannons.block_hit_effects.BlockHitEffectsHandler;
import rbasamoyai.createbigcannons.block_hit_effects.DefaultParticleModifiers;
import rbasamoyai.createbigcannons.block_hit_effects.ProjectileHitEffect;
import rbasamoyai.createbigcannons.block_hit_effects.ProjectileHitEffectsHandler;
import rbasamoyai.createbigcannons.cannon_control.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBoxRenderer;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlock;
import rbasamoyai.createbigcannons.crafting.welding.CannonWelderSelectionHandler;
import rbasamoyai.createbigcannons.effects.CBCScreenShakeHandler;
import rbasamoyai.createbigcannons.effects.particles.ParticleWindHandler;
import rbasamoyai.createbigcannons.effects.sounds.ShellFlyingSoundInstance;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskOverlay;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCFluids;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.mixin.client.CameraAccessor;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeSelectionHandler;
import rbasamoyai.createbigcannons.network.ServerboundFiringActionPacket;
import rbasamoyai.createbigcannons.network.ServerboundSetFireRatePacket;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;
import rbasamoyai.createbigcannons.remix.LightingRemix;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.RPLScreenShakeHandlerClient;
import rbasamoyai.ritchiesprojectilelib.effects.screen_shake.ScreenShakeEffect;

public class CBCClientCommon {

	private static final String KEY_ROOT = "key." + CreateBigCannons.MOD_ID;
	public static final KeyMapping PITCH_MODE = IndexPlatform.createSafeKeyMapping(KEY_ROOT + ".pitch_mode", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C);
	public static final KeyMapping FIRE_CONTROLLED_CANNON = IndexPlatform.createSafeKeyMapping(KEY_ROOT + ".fire_controlled_cannon", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_LEFT);
	public static final List<KeyMapping> KEYS = new ArrayList<>();

	public static final CannonWelderSelectionHandler CANNON_WELDER_HANDLER = new CannonWelderSelectionHandler();
	public static final FuzeSelectionHandler FUZE_GUIDE_HANDLER = new FuzeSelectionHandler();

	private static boolean PARTICLES_REGISTERED = false;

	public static ParticleStatus getParticleStatus() {
		return Minecraft.getInstance().options.particles().get();
	}

	public static void onRegisterParticleFactories(Minecraft mc, ParticleEngine engine) {
		if (PARTICLES_REGISTERED)
			return;
		CBCParticleTypes.registerFactories();
		DefaultParticleModifiers.register();
		PARTICLES_REGISTERED = true;
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

		IndexPlatform.registerGenericClampedItemProperty(CreateBigCannons.resource("fuze_state"), (stack, level, player, a) -> {
			if (!(stack.getItem() instanceof FuzedProjectileBlockItem fuzedItem) || !(fuzedItem.getBlock() instanceof FuzedProjectileBlock<?,?> fuzedBlock))
				return 0;
			CompoundTag tag = stack.getOrCreateTag();
			ItemStack fuze = ItemStack.of(tag.getCompound("BlockEntityTag").getCompound("Fuze"));
			if (fuze.isEmpty())
				return 0;
			return fuzedBlock.isBaseFuze() ? 2 : 1;
		});

		RPLScreenShakeHandlerClient.registerModScreenShakeHandler(CreateBigCannons.SCREEN_SHAKE_HANDLER_ID, new CBCScreenShakeHandler());
	}

	public static void registerKeyMappings(Consumer<KeyMapping> cons) {
		cons.accept(PITCH_MODE);
		cons.accept(FIRE_CONTROLLED_CANNON);
		KEYS.add(PITCH_MODE);
		KEYS.add(FIRE_CONTROLLED_CANNON);
	}

	public static void registerOverlays(String type, BiConsumer<String, CBCGuiOverlay> cons) {
		if (type.equals("hotbar")) {
			cons.accept("entity_goggles_overlay", EntityGoggleOverlayRenderer::renderOverlay);
		}
		if (type.equals("helmet")) {
			cons.accept("gas_mask_overlay", GasMaskOverlay::renderOverlay);
		}
	}

	@FunctionalInterface
	public interface CBCGuiOverlay {
		void renderOverlay(GuiGraphics graphics, float partialTicks, int windowWidth, int windowHeight);
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

		CANNON_WELDER_HANDLER.tick();
		FUZE_GUIDE_HANDLER.tick();
		ParticleWindHandler.updateWind();
		FixedCannonMountBoxRenderer.tick();
	}

	public static boolean onClickMouse(KeyMapping mapping) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen != null) return false;

		boolean isUse = mc.options.keyUse.same(mapping);
		if (isUse) {
			if (CANNON_WELDER_HANDLER.onMouseInput()) return true;
		}
		return false;
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
		float fov = oldFov;
		if (mc.player != null && mc.getCameraEntity() == mc.player && mc.options.getCameraType().isFirstPerson()
			&& mc.options.keyUse.isDown() && isControllingCannon(mc.player)) {
			fov *= 0.5f;
		}
		return Mth.lerp(mc.options.fovEffectScale().get().floatValue(), 1.0F, fov);
	}

	public static void onPlayerRenderPre(PoseStack stack, LivingEntity player, float partialTicks) {
		if (player.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(player) != null) {
			player.yBodyRot = player.yHeadRot;
			player.yBodyRotO = player.yHeadRotO;
			float yaw = 90 - Mth.lerp(partialTicks, player.yHeadRotO, player.yHeadRot);
			float pitch = Mth.lerp(partialTicks, player.xRotO, player.getXRot());

			Vector3f pitchVec = new Vector3f(Mth.sin(yaw * Mth.DEG_TO_RAD), 0, Mth.cos(yaw * Mth.DEG_TO_RAD));
			stack.mulPose(new Quaternionf(new AxisAngle4f(pitch * Mth.DEG_TO_RAD, pitchVec)));
			stack.translate(0, -1.25, 0);
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

	public static boolean onCameraSetup(Camera camera, double partialTicks, Supplier<Float> yaw, Supplier<Float> pitch, Supplier<Float> roll,
										Consumer<Float> setYaw, Consumer<Float> setPitch, Consumer<Float> setRoll) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		CameraAccessor camAccess = (CameraAccessor) camera;

		if (player != null && camera.getEntity() == player && player.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(player) != null) {
			Direction dir = poce.getInitialOrientation();
			Direction up = Direction.UP; // TODO: up and down cases

			Vec3 upNormal = new Vec3(up.step());
			Vec3 localPos = Vec3.atCenterOf(poce.getSeatPos(player));
			if (mc.options.getCameraType() == CameraType.FIRST_PERSON) {
				localPos = localPos.add(upNormal.scale(0.35));
				Vec3 rotationOffset = VecHelper.getCenterOf(BlockPos.ZERO);
				Vec3 anchor = poce.getPrevAnchorVec().lerp(poce.getAnchorVec(), partialTicks);

				Vec3 camPos = localPos.subtract(rotationOffset);
				camPos = poce.applyRotation(camPos, (float) partialTicks);
				camPos = camPos.add(anchor).add(rotationOffset);
				camAccess.callSetPosition(camPos);
			}

			boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
			boolean flag1 = mc.options.getCameraType() == CameraType.THIRD_PERSON_FRONT;
			float sgn = flag == flag1 ? 1 : -1;
			float add = flag1 ? 180 : 0;
			setYaw.accept(-poce.getViewYRot((float) partialTicks) + add);
			setPitch.accept(poce.getViewXRot((float) partialTicks) * sgn);
			setRoll.accept(0f);
		}
		return false;
	}

	public static void onLoadClientLevel(LevelAccessor level) {
		BlockHitEffectsHandler.loadTags();
		ParticleWindHandler.refreshNoise(level.getRandom().nextLong());
	}

	public static void onPlayerLogOut(LocalPlayer player) {
		BlockHitEffectsHandler.cleanUpTags();
		LightingRemix.clearCache();
	}

	public static void onPlayerLogIn(LocalPlayer player) {
		LightingRemix.clearCache();
	}

	public static void onChangeDimension(Player player) {
		LightingRemix.clearCache();
	}

	public static void playCustomSound(BlockHitEffect.HitSound sound, Level level, double x, double y, double z, double dx,
									   double dy, double dz, ProjectileHitEffect projectileEffect) {
		float basePitch = Mth.clamp(projectileEffect.getPitch(sound.basePitch()), 0, 2);
		float pitch = Mth.clamp(basePitch + level.random.nextFloat() * sound.pitchVariation(), 0, 2);
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getSoundManager().play(new SimpleSoundInstance(sound.location(), sound.source(), projectileEffect.volume(),
			pitch, level.getRandom(), false, 0, SoundInstance.Attenuation.LINEAR, x, y, z, false));
	}

	public static void registerClientReloadListeners(BiConsumer<PreparableReloadListener, ResourceLocation> cons) {
		cons.accept(BlockHitEffectsHandler.ReloadListener.BLOCKS_INSTANCE, CreateBigCannons.resource("block_hit_effects_block_handler"));
		cons.accept(BlockHitEffectsHandler.ReloadListener.FLUIDS_INSTANCE, CreateBigCannons.resource("block_hit_effects_fluid_handler"));
		cons.accept(ProjectileHitEffectsHandler.ReloadListener.INSTANCE, CreateBigCannons.resource("projectile_hit_effects_handler"));
	}

	public static void playShellFlyingSoundOnClient(AbstractCannonProjectile projectile, SoundEvent sound, Predicate<Player> playerTest, double radius) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player == null || !playerTest.test(minecraft.player))
			return;
		minecraft.getSoundManager().play(new ShellFlyingSoundInstance(sound, minecraft.player.level().random, minecraft.player, projectile, radius));
		projectile.setLocalSoundCooldown(-1);
	}

	public static void shakeScreenOnClient(ScreenShakeEffect effect) {
		RPLScreenShakeHandlerClient.addShakeEffect(CreateBigCannons.SCREEN_SHAKE_HANDLER_ID, effect);
	}

}
