package rbasamoyai.createbigcannons.munitions.fuzes;

import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;

public class FuzeSelectionHandler {

	private static final int HIGHLIGHT = 16777045; // ChatFormatting#YELLOW

	private Object bbOutlineSlot = new Object();

	public void tick() {
		if (!CBCConfigs.CLIENT.highlightFuzeInputOnShellBlocks.get())
			return;
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		ClientLevel level = mc.level;
		BlockPos hovered = null;

		HitResult hitResult = mc.hitResult;
		if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK)
			hovered = ((BlockHitResult) hitResult).getBlockPos();
		if (hovered == null)
			return;
		BlockState hoveredState = level.getBlockState(hovered);
		if (!(hoveredState.getBlock() instanceof FuzedProjectileBlock<?,?> shell))
			return;

		BlockEntity hoveredBE = level.getBlockEntity(hovered);
		if (!(hoveredBE instanceof FuzedBlockEntity shellBE))
			return;
		ItemStack stack = player.getMainHandItem();
		if ((stack.getItem() instanceof FuzeItem && !shellBE.hasFuze()) || (stack.isEmpty() && shellBE.hasFuze())) {
			Direction dir = hoveredState.getValue(FuzedProjectileBlock.FACING);
			if (shell.isBaseFuze())
				dir = dir.getOpposite();
			Direction.Axis axis = dir.getAxis();

			double dx = axis == Direction.Axis.X ? 2 / 16d : 5 / 16d;
			double dy = axis == Direction.Axis.Y ? 2 / 16d : 5 / 16d;
			double dz = axis == Direction.Axis.Z ? 2 / 16d : 5 / 16d;

			Vec3 center = Vec3.atCenterOf(hovered).add(new Vec3(dir.step()).scale(7 / 16f));
			AABB box = new AABB(center, center).inflate(dx, dy, dz);

			CreateClient.OUTLINER.showAABB(this.bbOutlineSlot, box)
				.colored(HIGHLIGHT)
				.withFaceTextures(AllSpecialTextures.BLANK, AllSpecialTextures.BLANK)
				.disableLineNormals()
				.lineWidth(1 / 32f);
		}
	}

}
