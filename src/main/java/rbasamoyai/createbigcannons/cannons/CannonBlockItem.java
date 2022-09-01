package rbasamoyai.createbigcannons.cannons;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.crafting.builtup.LayeredCannonBlockEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CannonBlockItem<T extends Block & CannonBlock> extends BlockItem {

	private final T cannonBlock;
	
	public CannonBlockItem(T block, Properties properties) {
		super(block, properties);
		this.cannonBlock = block;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CannonTooltip.appendText(stack, level, tooltip, flag, this.cannonBlock);		
	}
	
	@Override
	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult result = super.place(context);
		Level level = context.getLevel();
		if (level instanceof ServerLevel slevel) {
			BlockPos pos = context.getClickedPos();
			BlockState state = level.getBlockState(pos);			
			
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
		return result;
	}

}
