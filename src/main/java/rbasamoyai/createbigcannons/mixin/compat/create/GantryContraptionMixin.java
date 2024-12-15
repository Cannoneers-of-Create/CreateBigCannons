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
import com.simibubi.create.content.contraptions.gantry.GantryCarriageBlock;
import com.simibubi.create.content.contraptions.gantry.GantryContraption;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_loading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.remix.HasFragileContraption;

@Mixin(GantryContraption.class)
public abstract class GantryContraptionMixin extends TranslatingContraption implements CanLoadBigCannon, HasFragileContraption {

	@Unique private final Set<BlockPos> fragileBlocks = new HashSet<>();
	@Unique private final Set<BlockPos> colliderBlocks = new HashSet<>();
	@Unique private final Map<BlockPos, BlockState> encounteredBlocks = new HashMap<>();
	@Unique private boolean brokenDisassembly = false;

    @Override public void createbigcannons$setBrokenDisassembly(boolean flag) { this.brokenDisassembly = flag; }
	@Override public boolean createbigcannons$isBrokenDisassembly() { return this.brokenDisassembly; }

	@Override
	@Nullable
	public Direction createbigcannons$getAssemblyMovementDirection(Level level) {
		BlockState blockState = level.getBlockState(this.anchor);
		if (!(blockState.getBlock() instanceof GantryCarriageBlock)) return null;
		Direction direction = blockState.getValue(GantryCarriageBlock.FACING);

		BlockEntity blockEntity = level.getBlockEntity(this.anchor.relative(direction.getOpposite()));
		if (!(blockEntity instanceof GantryShaftBlockEntity shaftBE)) return null;
		BlockState shaftState = shaftBE.getBlockState();
		if (!AllBlocks.GANTRY_SHAFT.has(shaftState)) return null;

		Direction movementDirection = shaftState.getValue(GantryShaftBlock.FACING);
		return shaftBE.getPinionMovementSpeed() < 0 ? movementDirection.getOpposite() : movementDirection;
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
		return HasFragileContraption.defaultBlockBreaksAssembly(level, pos, newState, this);
	}

	@Override public boolean createbigcannons$shouldCheckFragility() { return HasFragileContraption.defaultShouldCheck(); }

	@Override public void createbigcannons$fragileDisassemble() { this.entity.disassemble(); }

}
