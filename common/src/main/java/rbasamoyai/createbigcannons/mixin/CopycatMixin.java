package rbasamoyai.createbigcannons.mixin;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import rbasamoyai.createbigcannons.munitions.config.PropertiesMimicTypeBlock;

@Mixin(CopycatBlock.class)
public abstract class CopycatMixin implements PropertiesMimicTypeBlock {
	@Override
	public @NotNull Block createBigCannons$getActualBlock(BlockState state, Level level, BlockPos pos) {
		return CopycatBlock.getMaterial(level, pos).getBlock();
	}
}
