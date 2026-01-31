package rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class SlidingBreechCTBehavior extends ConnectedTextureBehaviour.Base {

	private final CTSpriteShiftEntry side;
	private final CTSpriteShiftEntry sideHole;

	public SlidingBreechCTBehavior(CTSpriteShiftEntry side, CTSpriteShiftEntry sideHole) {
		this.side = side;
		this.sideHole = sideHole;
	}

	@Override
	@Nullable
	public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
		Direction.Axis orientationAxis = state.getValue(FACING).getAxis();
		Direction.Axis faceAxis = direction.getAxis();
		if (orientationAxis == faceAxis) return null;
		boolean firstAxis = state.getValue(SlidingBreechBlock.AXIS_ALONG_FIRST_COORDINATE);
		Direction.Axis shaftAxis = switch (orientationAxis) {
			case X -> firstAxis ? Direction.Axis.Y : Direction.Axis.Z;
			case Y -> firstAxis ? Direction.Axis.X : Direction.Axis.Z;
			case Z -> firstAxis ? Direction.Axis.X : Direction.Axis.Y;
		};
		return faceAxis == shaftAxis ? this.side : this.sideHole;
	}

	@Override
	protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
		return Direction.fromAxisAndDirection(state.getValue(FACING).getAxis(), Direction.AxisDirection.POSITIVE);
	}

	@Override
	protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
		Direction.Axis axis = state.getValue(FACING).getAxis();
		return face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? face.getClockWise(axis) : face.getCounterClockWise(axis);
	}

	@Override
	public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
		if (!(state.getBlock() instanceof BigCannonBlock cBlock) || !(other.getBlock() instanceof BigCannonBlock cBlock1)) return false;
		Direction dir = cBlock.getFacing(state);
		Direction.Axis axis = dir.getAxis();
		if (axis != cBlock1.getFacing(other).getAxis()) return false;
		if (pos.distManhattan(otherPos) != 1) return false;
		BlockPos diff = otherPos.subtract(pos);
		Direction dir1 = Direction.getNearest(diff.getX(), diff.getY(), diff.getZ());
		if (dir1.getAxis() != axis) return false;
		CannonCastShape shape = cBlock.getCannonShape();
		CannonCastShape shape1 = cBlock1.getCannonShape();
		if (shape.diameter() > shape1.diameter() || !shape.texturesCanConnect() || !shape1.texturesCanConnect()) return false;
		BlockEntity be = reader.getBlockEntity(pos);
		BlockEntity be1 = reader.getBlockEntity(otherPos);
		if (!(be instanceof IBigCannonBlockEntity cbe) || !(be1 instanceof IBigCannonBlockEntity cbe1)) return false;
		return cbe.cannonBehavior().isConnectedTo(dir1) || cbe1.cannonBehavior().isConnectedTo(dir1.getOpposite());
	}

	@Override
	public CTContext buildContext(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face, ContextRequirement requirement) {
		CTContext ctx = super.buildContext(reader, pos, state, face, requirement);
		if (state.getBlock() instanceof BigCannonBlock cBlock && cBlock.getFacing(state).getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
			boolean tmp = ctx.up;
			ctx.up = ctx.down;
			ctx.down = tmp;
		}
		return ctx;
	}

}
