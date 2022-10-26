package rbasamoyai.createbigcannons.manualloading;

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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.CannonBlock;
import rbasamoyai.createbigcannons.cannons.ICannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.cannonend.CannonEnd;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class WormItem extends Item {

	public WormItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player instanceof DeployerFakePlayer && !CBCConfigs.SERVER.cannons.deployersCanUseLoadingTools.get()) return InteractionResult.PASS;
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Direction reachDirection = context.getClickedFace().getOpposite();
		
		for (int i = 0; i < CBCConfigs.SERVER.cannons.ramRodReach.get(); ++i) {
			BlockPos pos1 = pos.relative(reachDirection, i);
			BlockState state1 = level.getBlockState(pos1);
			if (!isValidLoadBlock(state1, level, pos1, reachDirection) || !(level.getBlockEntity(pos1) instanceof ICannonBlockEntity cbe)) return InteractionResult.FAIL;
			StructureBlockInfo info = cbe.cannonBehavior().block();
			if (info == null || info.state == null || info.state.isAir()) continue;
			BlockPos pos2 = pos1.relative(context.getClickedFace());
			if (level.getBlockEntity(pos2) instanceof ICannonBlockEntity cbe1 && !cbe1.canLoadBlock(info)
				|| !(level.getBlockEntity(pos2) instanceof ICannonBlockEntity) && !level.getBlockState(pos2).isAir()) {
				return InteractionResult.FAIL;
			}
			
			if (!level.isClientSide) {
				if (level.getBlockEntity(pos2) instanceof ICannonBlockEntity cbe2) {
					cbe2.cannonBehavior().loadBlock(info);
				} else if (level.getBlockState(pos2).isAir()) {
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
				cbe.cannonBehavior().removeBlock();
				level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 1, 1);
			}
			player.causeFoodExhaustion(CBCConfigs.SERVER.cannons.loadingToolHungerConsumption.getF());
			player.getCooldowns().addCooldown(this, CBCConfigs.SERVER.cannons.loadingToolCooldown.get());
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.useOn(context);
	}
	
	public static boolean isValidLoadBlock(BlockState state, Level level, BlockPos pos, Direction dir) {
		return state.getBlock() instanceof CannonBlock cBlock && cBlock.getOpeningType(level, state, pos) == CannonEnd.OPEN;
	}
	
}
