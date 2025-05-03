package rbasamoyai.createbigcannons.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.remix.CBCExplodableBlock;

@Mixin(ProjectileBlock.class)
public class ProjectileBlockMixin extends DirectionalBlock implements CBCExplodableBlock {

	ProjectileBlockMixin(Properties arg) { super(arg); }

	@Override
	public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
		this.createbigcannons$onBlockExplode(level, pos, state, explosion);
		super.onBlockExploded(state, level, pos, explosion);
	}

}
