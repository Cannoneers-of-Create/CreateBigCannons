package rbasamoyai.createbigcannons.forge.mixin;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import javax.annotation.Nonnull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import rbasamoyai.createbigcannons.index.fluid_utils.CBCLiquidBlock;

import java.util.ArrayList;
import java.util.List;

@Mixin(CBCLiquidBlock.class)
public class CBCLiquidBlockMixin extends LiquidBlock {

	@Unique private List<FluidState> cbc$stateCache = null;

	public CBCLiquidBlockMixin(FlowingFluid arg, Properties arg2) { super(arg, arg2); }

	// Taken from Bumblezone with help from TelepathicGrunt - thanks! --ritchie
	@Nonnull
	@Override
	public FluidState getFluidState(BlockState arg) {
		int i = arg.getValue(LEVEL);
		if (this.cbc$stateCache == null) {
			this.initFluidStateCache();
		}
		return this.cbc$stateCache.get(Math.min(i, 8));
	}

	protected synchronized void initFluidStateCache() {
		if (this.cbc$stateCache == null) {
			this.cbc$stateCache = new ArrayList<>();
			this.cbc$stateCache.add(this.getFluid().getSource(false));
			for(int i = 1; i < 8; ++i) {
				this.cbc$stateCache.add(this.getFluid().getFlowing(8 - i, false));
			}
			this.cbc$stateCache.add(this.getFluid().getFlowing(8, true));
		}
	}

}
