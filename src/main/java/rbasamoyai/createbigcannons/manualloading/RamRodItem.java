package rbasamoyai.createbigcannons.manualloading;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.content.contraptions.components.deployer.DeployerFakePlayer;
import com.simibubi.create.foundation.utility.NBTProcessors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ProjectileBlock;

public class RamRodItem extends Item {

	public RamRodItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player instanceof DeployerFakePlayer && !CBCConfigs.SERVER.cannons.deployersCanUseLoadingTools.get()) return InteractionResult.PASS;
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction pushDirection = context.getClickedFace().getOpposite();
		
		int k = 0;
		if (level.getBlockEntity(pos) instanceof ICannonBlockEntity) {
			k = -1;
			for (int i = 0; i < CBCConfigs.SERVER.cannons.ramRodReach.get(); ++i) {
				BlockPos pos1 = pos.relative(pushDirection, i);
				BlockState state1 = level.getBlockState(pos1);
				if (state1.isAir()) continue;
				if (!isValidLoadBlock(state1, level, pos1, pushDirection)) return InteractionResult.FAIL;
				
				if (level.getBlockEntity(pos1) instanceof ICannonBlockEntity cbe) {
					StructureBlockInfo info = cbe.cannonBehavior().block();
					if (info == null || info.state == null || info.state.isAir()) continue;
				}
				k = i;
				break;
			}
			if (k == -1) return InteractionResult.PASS;
		}
		
		List<StructureBlockInfo> toPush = new ArrayList<>();
		boolean encounteredCannon = false;
		int maxCount = CBCConfigs.SERVER.cannons.ramRodStrength.get();
		for (int i = 0; i < maxCount + 1; ++i) {
			BlockPos pos1 = pos.relative(pushDirection, i + k);
			BlockState state1 = level.getBlockState(pos1);
			if (state1.isAir()) break;
			if (!isValidLoadBlock(state1, level, pos1, pushDirection)) return InteractionResult.FAIL;
			
			if (level.getBlockEntity(pos1) instanceof ICannonBlockEntity cbe) {		
				encounteredCannon = true;
				StructureBlockInfo info = cbe.cannonBehavior().block();
				if (info == null || info.state == null || info.state.isAir()) break;
				toPush.add(info);
			} else {
				BlockEntity be = level.getBlockEntity(pos1);
				CompoundTag tag = null;
				if (be != null) {
					tag = be.saveWithFullMetadata();
					tag.remove("x");
					tag.remove("y");
					tag.remove("z");
				}
				toPush.add(new StructureBlockInfo(BlockPos.ZERO, state1, tag));
			}
			if (toPush.size() > maxCount) return InteractionResult.FAIL;
		}
		if (!encounteredCannon || toPush.isEmpty()) return InteractionResult.FAIL;
		if (!level.isClientSide) {
			for (int i = toPush.size() - 1; i >= 0; --i) {
				BlockPos pos1 = pos.relative(pushDirection, i + k);
				
				if (level.getBlockEntity(pos1) instanceof ICannonBlockEntity cbe) cbe.cannonBehavior().removeBlock();
				else level.removeBlock(pos1, false);
				
				StructureBlockInfo info = toPush.get(i);
				BlockPos pos2 = pos1.relative(pushDirection);
				if (level.getBlockEntity(pos2) instanceof ICannonBlockEntity cbe) {
					cbe.cannonBehavior().tryLoadingBlock(info);
				} else {
					level.setBlock(pos2, info.state, Block.UPDATE_MOVE_BY_PISTON | Block.UPDATE_ALL);
					BlockEntity be = level.getBlockEntity(pos2);
					CompoundTag tag = info.nbt;
					if (be != null) tag = NBTProcessors.process(be, tag, false);
					if (be != null && tag != null) {
						tag.putInt("x", pos2.getX());
						tag.putInt("y", pos2.getY());
						tag.putInt("z", pos2.getZ());
						be.load(tag);
					}
				}
			}
			player.causeFoodExhaustion(toPush.size() * CBCConfigs.SERVER.cannons.loadingToolHungerConsumption.getF());
			level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.PLAYERS, 1, 1);
		}
		player.getCooldowns().addCooldown(this, CBCConfigs.SERVER.cannons.loadingToolCooldown.get());
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
	
	public static boolean isValidLoadBlock(BlockState state, Level level, BlockPos pos, Direction dir) {
		if (CBCBlocks.POWDER_CHARGE.has(state))
			return state.getValue(BlockStateProperties.AXIS) == dir.getAxis();
		if (state.getBlock() instanceof ProjectileBlock)
			return state.getValue(BlockStateProperties.FACING).getAxis() == dir.getAxis();
		if (state.getBlock() instanceof CannonBlock cBlock)
			return cBlock.getOpeningType(level, state, pos) == CannonEnd.OPEN && cBlock.getFacing(state).getAxis() == dir.getAxis();
		return false;
	}

}
