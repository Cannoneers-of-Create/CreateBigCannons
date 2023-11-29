package rbasamoyai.createbigcannons.crafting.welding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CannonWelderItem extends Item {

	public CannonWelderItem(Properties properties) {
		super(properties);
	}

	@Override public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) { return false; }

	@Override public boolean canBeDepleted() { return true; }

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return super.useOn(context);
	}

	public static InteractionResult welderItemAlwaysPlacesWhenUsed(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
		if (hitResult != null) {
			BlockState blockState = level.getBlockState(hitResult.getBlockPos());
			if (blockState.getBlock() instanceof WeldableBlock wblock && wblock.isWeldable(blockState)) return InteractionResult.PASS;
		}
		return InteractionResult.FAIL;
	}

	public static boolean validWeldPositions(BlockPos from, BlockPos to) {
		return from.distManhattan(to) == 1;
	}

	public static boolean weldBlocks(Level level, BlockPos from, BlockPos to, boolean simulate) {
		if (!validWeldPositions(from, to)) return false;
		BlockState fstate = level.getBlockState(from);
		if (!(fstate.getBlock() instanceof WeldableBlock wblockf)) return false;
		BlockState tstate = level.getBlockState(to);
		if (!(tstate.getBlock() instanceof WeldableBlock wblockt)) return false;
		Direction dir = Direction.getNearest(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
		if (!wblockf.canWeldSide(level, dir, fstate, tstate, from)
			|| !wblockt.canWeldSide(level, dir.getOpposite(), tstate, fstate, to)) return false;
		if (!simulate) {
			wblockf.weldBlock(level, fstate, from, dir);
			wblockt.weldBlock(level, tstate, to, dir.getOpposite());
		}
		return true;
	}

}
