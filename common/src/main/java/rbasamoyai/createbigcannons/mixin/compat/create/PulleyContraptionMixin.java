package rbasamoyai.createbigcannons.mixin.compat.create;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;
import com.simibubi.create.content.contraptions.pulley.PulleyContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_loading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.remix.HasFragileContraption;

@Mixin(PulleyContraption.class)
public abstract class PulleyContraptionMixin extends TranslatingContraption implements CanLoadBigCannon, HasFragileContraption {

	@Unique private final Set<BlockPos> fragileBlocks = new HashSet<>();
	@Unique private final Set<BlockPos> colliderBlocks = new HashSet<>();
	@Unique private final Map<BlockPos, BlockState> encounteredBlocks = new HashMap<>();
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

	@Nullable
	@Override
	public Direction createbigcannons$getOriginalForcedDirection(Level level) {
		return null;
	}

	@Override public BlockPos createbigcannons$toLocalPos(BlockPos globalPos) { return this.toLocalPos(globalPos); }

    @Override public Set<BlockPos> createbigcannons$getFragileBlockPositions() { return this.fragileBlocks; }

    @Override public Set<BlockPos> createbigcannons$getCannonLoadingColliders() { return this.colliderBlocks; }

	@Override public Map<BlockPos, BlockState> createbigcannons$getEncounteredBlocks() { return this.encounteredBlocks; }

	@Override
	public boolean createbigcannons$blockBreaksDisassembly(Level level, BlockPos pos, BlockState newState) {
		BlockPos pos1 = this.anchor.above(((PulleyContraption) (Object) this).getInitialOffset() + 1);
		if (pos.equals(pos1) && AllBlocks.ROPE_PULLEY.has(level.getBlockState(pos1)))
			return false;
		return HasFragileContraption.defaultBlockBreaksAssembly(level, pos, newState, this);
	}

	@Override public boolean createbigcannons$shouldCheckFragility() { return HasFragileContraption.defaultShouldCheck(); }

	@Override
	public void createbigcannons$fragileDisassemble() {
		BlockPos pulleyPos = this.anchor.above(((PulleyContraption) (Object) this).getInitialOffset() + 1);
		if (this.world.getBlockEntity(pulleyPos) instanceof PulleyBlockEntity pulleyBE) {
			pulleyBE.disassemble();
		} else {
			this.entity.disassemble();
		}
	}

}
