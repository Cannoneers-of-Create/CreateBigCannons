package rbasamoyai.createbigcannons.mixin.compat.create;

import javax.annotation.Nonnull;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.munitions.config.PropertiesMimicTypeBlock;

@Mixin(CopycatBlock.class)
public abstract class CopycatBlockMixin implements PropertiesMimicTypeBlock {
	@Override
	@Nonnull
	public Block createbigcannons$getActualBlock(BlockState state, Level level, BlockPos pos) {
		return CopycatBlock.getMaterial(level, pos).getBlock();
	}

}
