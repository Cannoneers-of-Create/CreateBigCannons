package rbasamoyai.createbigcannons.mixin.compat.create;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;
import com.simibubi.create.content.contraptions.pulley.PulleyContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Unique;

import rbasamoyai.createbigcannons.cannonloading.CanLoadBigCannon;

import java.util.HashSet;
import java.util.Set;

@Mixin(PulleyContraption.class)
public abstract class PulleyContraptionMixin extends TranslatingContraption implements CanLoadBigCannon {

	@Unique private final Set<BlockPos> fragileBlocks = new HashSet<>();
	@Unique private final Set<BlockPos> colliderBlocks = new HashSet<>();
	@Unique private boolean brokenDisassembly = false;

    @Override public void createbigcannons$setBrokenDisassembly(boolean flag) { this.brokenDisassembly = flag; }
	@Override public boolean createbigcannons$isBrokenDisassembly() { return this.brokenDisassembly; }

	@Override
	@Nullable
	public Direction createbigcannons$getAssemblyMovementDirection(Level level) {
		BlockPos pos1 = this.anchor.above(((PulleyContraption) (Object) this).getInitialOffset() + 1);
		if (level.getBlockEntity(pos1) instanceof PulleyBlockEntity pulleyBE) {
			return pulleyBE.getSpeed() > 0 ? Direction.DOWN : Direction.UP;
		}
		return null;
	}

    @Override public BlockPos createbigcannons$toLocalPos(BlockPos globalPos) { return this.toLocalPos(globalPos); }

    @Override public Set<BlockPos> createbigcannons$getFragileBlockPositions() { return this.fragileBlocks; }

    @Override public Set<BlockPos> createbigcannons$getCannonLoadingColliders() { return this.colliderBlocks; }

}
