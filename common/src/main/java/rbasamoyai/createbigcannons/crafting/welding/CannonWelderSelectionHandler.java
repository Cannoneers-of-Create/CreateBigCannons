package rbasamoyai.createbigcannons.crafting.welding;

import com.google.common.base.Objects;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ServerboundUseWelderPacket;

public class CannonWelderSelectionHandler {

	private static final int HIGHLIGHT = 0x5C93E8;
	private static final int FAIL = 0xFF5555;

	private Object bbOutlineSlot = new Object();

	private BlockPos firstPos;
	private BlockPos hoveredPos;

	public void tick() {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		ClientLevel level = mc.level;
		BlockPos hovered = null;
		ItemStack stack = player.getMainHandItem();
		if (!(stack.getItem() instanceof CannonWelderItem)) {
			if (this.firstPos != null) this.discard();
			return;
		}
		HitResult hitResult = mc.hitResult;
		if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK)
			hovered = ((BlockHitResult) hitResult).getBlockPos();
		if (hovered == null) {
			this.hoveredPos = null;
			return;
		}
		if (this.firstPos != null && this.firstPos.distManhattan(hovered) > 1) {
			Lang.builder(CreateBigCannons.MOD_ID).translate("cannon_welder.too_far").color(FAIL).sendStatus(player);
			return;
		}
		boolean cancel = player.isSteppingCarefully();
		if (cancel && this.firstPos == null) return;

		if (this.firstPos != null && Objects.equal(hovered, this.hoveredPos) && !this.firstPos.equals(hovered)) {
			int color = HIGHLIGHT;
			String key = "cannon_welder.click_to_confirm";
			if (!CannonWelderItem.weldBlocks(mc.level, this.firstPos, hovered, true)) {
				color = FAIL;
				key = "cannon_welder.invalid_weld";
			} else if (cancel) {
				color = FAIL;
				key = "cannon_welder.click_to_discard";
			}
			Lang.builder(CreateBigCannons.MOD_ID).translate(key).color(color).sendStatus(player);
			if (this.firstPos != null) {
				CreateClient.OUTLINER.showAABB(this.bbOutlineSlot, new AABB(this.firstPos, hovered).expandTowards(1, 1, 1))
					.colored(color)
					.withFaceTextures(AllSpecialTextures.GLUE, AllSpecialTextures.GLUE)
					.disableLineNormals()
					.lineWidth(1 / 16f);
				if (color == HIGHLIGHT && mc.level.getGameTime() % 20 == 0) {
					Direction dir = Direction.getNearest(hovered.getX() - this.firstPos.getX(), hovered.getY() - this.firstPos.getY(), hovered.getZ() - this.firstPos.getZ());
					spawnParticles(mc.level, this.firstPos, dir, true);
				}
			}
			return;
		}
		this.hoveredPos = hovered.immutable();
	}

	public void discard() {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		ClientLevel level = mc.level;
		level.playSound(player, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.75f, 1);
		Lang.builder(CreateBigCannons.MOD_ID).translate("cannon_welder.abort").sendStatus(player);
		this.firstPos = null;
	}

	public void confirm() {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		NetworkPlatform.sendToServer(new ServerboundUseWelderPacket(this.firstPos, this.hoveredPos));
		AllSoundEvents.STEAM.playAt(player.level, this.hoveredPos, 0.5F, 0.95F, false);
		Direction dir = Direction.getNearest(this.hoveredPos.getX() - this.firstPos.getX(), this.hoveredPos.getY() - this.firstPos.getY(), this.hoveredPos.getZ() - this.firstPos.getZ());
		spawnParticles(mc.level, this.firstPos, dir, true);
		Lang.builder(CreateBigCannons.MOD_ID).translate("cannon_welder.success").sendStatus(player);
		this.firstPos = null;
	}

	public boolean onMouseInput() {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		ClientLevel level = mc.level;
		if (!(player.getMainHandItem().getItem() instanceof CannonWelderItem)) return false;
		if (!player.mayBuild() && !player.isSpectator()) return false;

		if (player.isSteppingCarefully()) {
			if (this.firstPos != null) {
				this.discard();
				return true;
			}
			return false;
		}
		if (this.hoveredPos == null) return false;
		Direction face = null;
		if (mc.hitResult instanceof BlockHitResult bhr) {
			BlockState blockState = level.getBlockState(this.hoveredPos);
			if (!(blockState.getBlock() instanceof WeldableBlock wblock) || !wblock.isWeldable(blockState)) {
				Lang.builder(CreateBigCannons.MOD_ID).translate("cannon_welder.invalid_weld").color(FAIL).sendStatus(player);
				return false;
			}
			face = bhr.getDirection();
		}
		player.swing(InteractionHand.MAIN_HAND);
		if (this.firstPos != null) {
			if (!CannonWelderItem.weldBlocks(level, this.firstPos, this.hoveredPos, true)) return false;
			this.confirm();
			return true;
		}
		this.firstPos = this.hoveredPos.immutable();
		if (face != null) spawnParticles(level, this.firstPos, face, false);
		Lang.builder(CreateBigCannons.MOD_ID).translate("cannon_welder.first_pos").sendStatus(player);
		level.playSound(player, this.firstPos, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 0.75f, 1);
		return true;
	}

	public boolean isActive() { return this.firstPos != null; }

	public static void spawnParticles(Level world, BlockPos pos, Direction direction, boolean fullBlock) {
		Vec3 vec = Vec3.atLowerCornerOf(direction.getNormal());
		Vec3 plane = VecHelper.axisAlingedPlaneOf(vec);
		Vec3 facePos = VecHelper.getCenterOf(pos).add(vec.scale(.5f));
		float distance = fullBlock ? 1f : .25f + .25f * (world.random.nextFloat() - .5f);
		plane = plane.scale(distance);
		for (int i = fullBlock ? 40 : 15; i > 0; i--) {
			Vec3 offset = VecHelper.rotate(plane, 360 * world.random.nextFloat(), direction.getAxis());
			Vec3 motion = offset.normalize().scale(1 / 64f);
			if (fullBlock) offset = new Vec3(Mth.clamp(offset.x, -.5, .5), Mth.clamp(offset.y, -.5, .5), Mth.clamp(offset.z, -.5, .5));
			Vec3 particlePos = facePos.add(offset);
			world.addParticle(ParticleTypes.FLAME, particlePos.x, particlePos.y, particlePos.z, motion.x, motion.y, motion.z);
		}
	}

}
