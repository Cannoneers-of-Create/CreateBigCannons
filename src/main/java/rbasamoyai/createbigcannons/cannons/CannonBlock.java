package rbasamoyai.createbigcannons.cannons;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public interface CannonBlock {

	CannonMaterial getCannonMaterial();
	CannonCastShape getCannonShape();
	
	Direction getFacing(BlockState state);
	CannonEnd getOpeningType(Level level, BlockState state, BlockPos pos);
	boolean isComplete(BlockState state);
	
	default CannonMaterial getCannonMaterialInLevel(LevelAccessor level, BlockState state, BlockPos pos) { return this.getCannonMaterial(); }
	default CannonCastShape getShapeInLevel(LevelAccessor level, BlockState state, BlockPos pos) { return this.getCannonShape(); }
	
	default boolean isDoubleSidedCannon(BlockState state) { return true; }
	default boolean isImmovable(BlockState state) { return false; }
	
	default void onRemoveCannon(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() instanceof CannonBlock cBlock) {	
			Direction facing = cBlock.getFacing(state);
			CannonMaterial material = cBlock.getCannonMaterial();				
			
			BlockPos pos1 = pos.relative(facing);
			BlockState state1 = level.getBlockState(pos1);
			BlockEntity be1 = level.getBlockEntity(pos1);
			
			if (state1.getBlock() instanceof CannonBlock cBlock1
			&& cBlock1.getCannonMaterialInLevel(level, state1, pos1) == material
			&& be1 instanceof ICannonBlockEntity cbe1) {
				Direction facing1 = cBlock1.getFacing(state1);
				if (facing == facing1.getOpposite() || cBlock1.isDoubleSidedCannon(state1) && facing.getAxis() == facing1.getAxis()) {
					Direction opposite = facing.getOpposite();
					cbe1.cannonBehavior().setConnectedFace(opposite, false);
					if (cbe1 instanceof LayeredCannonBlockEntity layered) {
						for (CannonCastShape layer : layered.getLayers().keySet()) {
							layered.setLayerConnectedTo(opposite, layer, false);
						}
					}
					be1.setChanged();
				}
			}
			BlockPos pos2 = pos.relative(facing.getOpposite());
			BlockState state2 = level.getBlockState(pos2);
			BlockEntity be2 = level.getBlockEntity(pos2);
			
			if (cBlock.isDoubleSidedCannon(state)
			&& state2.getBlock() instanceof CannonBlock cBlock2
			&& cBlock2.getCannonMaterialInLevel(level, state2, pos2) == material
			&& be2 instanceof ICannonBlockEntity cbe2) {
				Direction facing2 = cBlock2.getFacing(state2);
				if (facing == facing2 || cBlock2.isDoubleSidedCannon(state2) && facing.getAxis() == facing2.getAxis()) {
					cbe2.cannonBehavior().setConnectedFace(facing, false);
					if (cbe2 instanceof LayeredCannonBlockEntity layered) {
						for (CannonCastShape layer : layered.getLayers().keySet()) {
							layered.setLayerConnectedTo(facing, layer, false);
						}
					}
					be2.setChanged();
				}
			}
		}
	}
	
	public static void onPlace(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (!(level instanceof ServerLevel slevel)) return;
		
		if (state.getBlock() instanceof CannonBlock cBlock) {	
			Direction facing = cBlock.getFacing(state);
			Vec3 center = Vec3.atCenterOf(pos);
			Vec3 offset = Vec3.atBottomCenterOf(facing.getNormal()).scale(0.5d);
			CannonMaterial material = cBlock.getCannonMaterial();
			CannonCastShape shape = cBlock.getCannonShape();
			
			if (level.getBlockEntity(pos) instanceof LayeredCannonBlockEntity layered) {
				layered.setBaseMaterial(material);
				layered.setLayer(shape, state.getBlock());
			}
			
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof ICannonBlockEntity cbe) {
				BlockPos pos1 = pos.relative(facing);
				BlockState state1 = level.getBlockState(pos1);
				BlockEntity be1 = level.getBlockEntity(pos1);
				
				if (state1.getBlock() instanceof CannonBlock cBlock1
				&& cBlock1.getCannonMaterialInLevel(level, state1, pos1) == material
				&& level.getBlockEntity(pos1) instanceof ICannonBlockEntity cbe1) {
					Direction facing1 = cBlock1.getFacing(state1);
					if (facing == facing1.getOpposite() || cBlock1.isDoubleSidedCannon(state1) && facing.getAxis() == facing1.getAxis()) {
						cbe.cannonBehavior().setConnectedFace(facing, true);
						cbe1.cannonBehavior().setConnectedFace(facing.getOpposite(), true);
						
						if (cbe instanceof LayeredCannonBlockEntity layered && cbe1 instanceof LayeredCannonBlockEntity layered1) {
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								layered.setLayerConnectedTo(facing, layer, true);
								layered1.setLayerConnectedTo(facing.getOpposite(), layer, true);
							}
						} else if (cbe instanceof LayeredCannonBlockEntity layered) {
							CannonCastShape shape1 = cBlock1.getCannonShape();
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								if (layer.diameter() > shape1.diameter()) continue;
								layered.setLayerConnectedTo(facing, layer, true);
							}
						} else if (cbe1 instanceof LayeredCannonBlockEntity layered) {
							CannonCastShape shape1 = cBlock.getCannonShape();
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								if (layer.diameter() > shape1.diameter()) continue;
								layered.setLayerConnectedTo(facing.getOpposite(), layer, true);
							}
						}
						
						be1.setChanged();
						
						Vec3 particlePos = center.add(offset);
						slevel.sendParticles(ParticleTypes.CRIT, particlePos.x, particlePos.y, particlePos.z, 10, 0.5d, 0.5d, 0.5d, 0.1d);
					}
				}
				
				BlockPos pos2 = pos.relative(facing.getOpposite());
				BlockState state2 = level.getBlockState(pos2);
				BlockEntity be2 = level.getBlockEntity(pos2);
				
				if (cBlock.isDoubleSidedCannon(state)
				&& state2.getBlock() instanceof CannonBlock cBlock2
				&& cBlock2.getCannonMaterialInLevel(level, state2, pos2) == material
				&& level.getBlockEntity(pos2) instanceof ICannonBlockEntity cbe2) {
					Direction facing2 = cBlock2.getFacing(state2);
					if (facing == facing2 || cBlock2.isDoubleSidedCannon(state2) && facing.getAxis() == facing2.getAxis()) {
						cbe.cannonBehavior().setConnectedFace(facing.getOpposite(), true);
						cbe2.cannonBehavior().setConnectedFace(facing, true);
						
						if (cbe instanceof LayeredCannonBlockEntity layered && cbe2 instanceof LayeredCannonBlockEntity layered1) {
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								layered.setLayerConnectedTo(facing.getOpposite(), layer, true);
								layered1.setLayerConnectedTo(facing, layer, true);
							}
						} else if (cbe instanceof LayeredCannonBlockEntity layered) {
							CannonCastShape shape1 = cBlock2.getCannonShape();
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								if (layer.diameter() > shape1.diameter()) continue;
								layered.setLayerConnectedTo(facing.getOpposite(), layer, true);
							}
						} else if (cbe2 instanceof LayeredCannonBlockEntity layered) {
							CannonCastShape shape1 = cBlock.getCannonShape();
							for (CannonCastShape layer : layered.getLayers().keySet()) {
								if (layer.diameter() > shape1.diameter()) continue;
								layered.setLayerConnectedTo(facing, layer, true);
							}
						}
						
						be2.setChanged();
						
						Vec3 particlePos = center.add(offset.reverse());
						slevel.sendParticles(ParticleTypes.CRIT, particlePos.x, particlePos.y, particlePos.z, 10, 0.5d, 0.5d, 0.5d, 0.1d);
					}
				}
				
				be.setChanged();
			}
		}
	}
	
}
