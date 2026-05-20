package rbasamoyai.createbigcannons;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllDataComponents;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
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
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCParticleTypes;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeSelectionHandler;
import rbasamoyai.createbigcannons.network.ServerboundFiringActionPacket;
import rbasamoyai.createbigcannons.network.ServerboundSetFireRatePacket;
import rbasamoyai.createbigcannons.ponder.CBCPonderPlugin;
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
        PonderIndex.addPlugin(new CBCPonderPlugin());

		CBCBlockPartials.resolveDeferredModels();

		IndexPlatform.registerClampedItemProperty(CBCItems.PARTIALLY_FORMED_AUTOCANNON_CARTRIDGE.get(), CreateBigCannons.resource("formed"), (stack, level, player, a) -> {
			return stack.has(AllDataComponents.SEQUENCED_ASSEMBLY) ? stack.get(AllDataComponents.SEQUENCED_ASSEMBLY).step() - 1 : 0;
		});

		IndexPlatform.registerClampedItemProperty(CBCItems.PARTIALLY_FORMED_BIG_CARTRIDGE.get(), CreateBigCannons.resource("formed"), (stack, level, player, a) -> {
            return stack.has(AllDataComponents.SEQUENCED_ASSEMBLY) ? stack.get(AllDataComponents.SEQUENCED_ASSEMBLY).step() - 1 : 0;
		});

		IndexPlatform.registerClampedItemProperty(CBCBlocks.BIG_CARTRIDGE.get().asItem(), CreateBigCannons.resource("big_cartridge_filled"),
			(stack, level, player, a) -> {
				return BigCartridgeBlockItem.getPower(stack);
			});

		IndexPlatform.registerGenericClampedItemProperty(CreateBigCannons.resource("fuze_state"), (stack, level, player, a) -> {
			if (!(stack.getItem() instanceof FuzedProjectileBlockItem fuzedItem) || !(fuzedItem.getBlock() instanceof FuzedProjectileBlock<?, ?> fuzedBlock))
				return 0;
            ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
            ItemStack fuze = items.copyOne();
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

	public static void registerOverlays(String type, BiConsumer<String, LayeredDraw.Layer> cons) {
		if (type.equals("hotbar")) {
			cons.accept("entity_goggles_overlay", EntityGoggleOverlayRenderer::renderOverlay);
		}
		if (type.equals("helmet")) {
			cons.accept("gas_mask_overlay", GasMaskOverlay::renderOverlay);
		}
	}

	public static void onClientGameTick(Minecraft mc) {
		if (mc.player == null || mc.level == null) return;

        boolean isFiring = CBCClientCommon.FIRE_CONTROLLED_CANNON.isDown();
		if (mc.player.getRootVehicle() instanceof CannonCarriageEntity carriage) {
			net.minecraft.client.player.Input input = mc.player.input;
			boolean isPitching = CBCClientCommon.PITCH_MODE.isDown();
			carriage.setInput(input.left, input.right, input.up, input.down, isPitching);
			mc.player.handsBusy |= input.left | input.right | input.up | input.down | isFiring;
		}
        if (mc.player.getVehicle() instanceof PitchOrientedContraptionEntity) {
            mc.player.handsBusy = true;
        }
		if (isFiring) {
			NetworkPlatform.sendToServer(ServerboundFiringActionPacket.instance());
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
            Direction dir = poce.getInitialOrientation();
            float pitch = Mth.lerp(partialTicks, player.xRotO, player.getXRot());
            if (dir.getAxis().isVertical()) {
                pitch *= -1;
                pitch += dir == Direction.DOWN ? 90 : -90;
            }

            stack.pushPose();
			Vector3f pitchVec = new Vector3f(Mth.sin(yaw * Mth.DEG_TO_RAD), 0, Mth.cos(yaw * Mth.DEG_TO_RAD));
			stack.mulPose(new Quaternionf(new AxisAngle4f(pitch * Mth.DEG_TO_RAD, pitchVec)));
			stack.translate(0, -1.25, 0);
		}
	}

    public static void onPlayerRenderPost(PoseStack stack, LivingEntity player, float partialTicks) {
        if (player.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(player) != null) {
            stack.popPose();
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

		if (player != null && camera.getEntity() == player && player.getVehicle() instanceof PitchOrientedContraptionEntity poce && poce.getSeatPos(player) != null) {
			Direction dir = poce.getInitialOrientation();
            if (dir.getAxis().isHorizontal()) {
                boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
                boolean flag1 = mc.options.getCameraType() == CameraType.THIRD_PERSON_FRONT;
                float sgn = (flag == flag1) != flag1 ? 1 : -1;
                setPitch.accept(poce.getViewXRot((float) partialTicks) * sgn);
            } else if (dir == Direction.DOWN) {
                setPitch.accept(-poce.getViewXRot((float) partialTicks) + 90);
            } else { // dir == Direction.UP
                setPitch.accept(-poce.getViewXRot((float) partialTicks) - 90);
            }
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
	}

	public static void onPlayerLogIn(LocalPlayer player) {}

	public static void onChangeDimension(Player player) {}

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
