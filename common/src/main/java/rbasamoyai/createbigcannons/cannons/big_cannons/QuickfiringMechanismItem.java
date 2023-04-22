package rbasamoyai.createbigcannons.cannons.big_cannons;

import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

public class QuickfiringMechanismItem extends Item {

	public QuickfiringMechanismItem(Properties properties) { super(properties) ;}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof SlidingBreechBlock sbb) {
			Player player = context.getPlayer();
			BlockState newState = sbb.getConversion(state);
			BlockEntity oldBe = level.getBlockEntity(pos);
			if (!(oldBe instanceof IBigCannonBlockEntity cbe)) return InteractionResult.PASS;
			if (!level.isClientSide) {
				level.setBlock(pos, newState, 11);
				BlockEntity newBe = level.getBlockEntity(pos);

				StructureBlockInfo loaded = cbe.cannonBehavior().block();
				if (loaded != null && player != null) {
					Block block = loaded.state.getBlock();
					player.addItem(block instanceof BigCannonMunitionBlock munition ? munition.getExtractedItem(loaded) : new ItemStack(block));
				}

				if (newBe instanceof IBigCannonBlockEntity cbe1) {
					for (Direction dir : Iterate.directions) {
						boolean flag = cbe.cannonBehavior().isConnectedTo(dir);
						cbe1.cannonBehavior().setConnectedFace(dir, flag);
						if (level.getBlockEntity(pos.relative(dir)) instanceof IBigCannonBlockEntity cbe2) {
							cbe2.cannonBehavior().setConnectedFace(dir.getOpposite(), flag);
						}
					}
					newBe.setChanged();
				}
			}
			SoundType type = newState.getSoundType();
			level.playSound(player, pos, type.getPlaceSound(), SoundSource.BLOCKS, type.getVolume(), type.getPitch());
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.useOn(context);
	}

}
