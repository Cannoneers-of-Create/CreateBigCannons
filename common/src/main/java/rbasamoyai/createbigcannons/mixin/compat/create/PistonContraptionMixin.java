package rbasamoyai.createbigcannons.mixin.compat.create;

import static com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock.isPiston;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlockEntity;
import com.simibubi.create.content.contraptions.piston.PistonContraption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannon_loading.CanLoadBigCannon;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.remix.ContraptionRemix;
import rbasamoyai.createbigcannons.remix.HasFragileContraption;

@Mixin(PistonContraption.class)
public abstract class PistonContraptionMixin extends TranslatingContraption implements CanLoadBigCannon, HasFragileContraption {

	@Unique private final Set<BlockPos> createbigcannons$fragileBlocks = new HashSet<>();
	@Unique private final Set<BlockPos> createbigcannons$colliderBlocks = new HashSet<>();
	@Unique private final Map<BlockPos, BlockState> createbigcannons$encounteredBlocks = new HashMap<>();
	@Unique private boolean createbigcannons$brokenDisassembly = false;

	@Shadow private boolean retract;
	@Shadow protected Direction orientation;

	@Shadow
	public abstract BlockPos toLocalPos(BlockPos globalPos);

	@Override
	@Nullable
	public Direction createbigcannons$getAssemblyMovementDirection(Level level) {
		return this.orientation != null && this.retract ? this.orientation.getOpposite() : this.orientation;
	}

    @Nullable
    @Override
    public Direction createbigcannons$getOriginalForcedDirection(Level level) {
		return this.orientation != null && this.retract ? this.orientation.getOpposite() : this.orientation;
    }

    @Override public BlockPos createbigcannons$toLocalPos(BlockPos globalPos) { return this.toLocalPos(globalPos); }

	@Override public Set<BlockPos> createbigcannons$getFragileBlockPositions() { return this.createbigcannons$fragileBlocks; }

    @Override public Set<BlockPos> createbigcannons$getCannonLoadingColliders() { return this.createbigcannons$colliderBlocks; }

    @Override public void createbigcannons$setBrokenDisassembly(boolean flag) { this.createbigcannons$brokenDisassembly = flag; }
	@Override public boolean createbigcannons$isBrokenDisassembly() { return this.createbigcannons$brokenDisassembly; }

	@Override public Map<BlockPos, BlockState> createbigcannons$getEncounteredBlocks() { return this.createbigcannons$encounteredBlocks; }

	@Inject(method = "addToInitialFrontier",
			at = @At(value = "INVOKE", target = "Ljava/util/Queue;add(Ljava/lang/Object;)Z", shift = At.Shift.BEFORE),
			remap = false,
			cancellable = true)
	private void createbigcannons$addToInitialFrontier$1(Level level, BlockPos pos, Direction direction, Queue<BlockPos> frontier,
														 CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) boolean retracting,
														 @Local(ordinal = 1) BlockPos currentPos, @Local BlockState state) {
		BlockPos offsetPos = currentPos.relative(direction.getOpposite());
		BlockState offsetState = level.getBlockState(offsetPos);

		if (state.getBlock() instanceof BigCannonBlock && level.getBlockEntity(currentPos) instanceof IBigCannonBlockEntity cbe && !retracting) {
			StructureBlockInfo loadedInfo = cbe.cannonBehavior().block();
			if (!loadedInfo.state().isAir() && CBCBlocks.WORM_HEAD.has(offsetState)
				|| cbe.canPushBlock(new StructureBlockInfo(BlockPos.ZERO, offsetState, null))) {
				cir.setReturnValue(true);
			}
		} else if (IBigCannonBlockEntity.isValidMunitionState(direction.getAxis(), state)
			&& CBCBlocks.WORM_HEAD.has(offsetState) && !retracting) {
			cir.setReturnValue(true);
		}
	}

	@WrapOperation(method = "collectExtensions",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
	private BlockState createbigcannons$collectExtensions$1(Level instance, BlockPos pos, Operation<BlockState> original,
															@Local(argsOnly = true) Direction direction,
															@Local(ordinal = 0, argsOnly = true) BlockPos posArg) {
		BlockState state = original.call(instance, pos);
		return pos.equals(posArg) ? state : ContraptionRemix.getInnerCannonState(instance, state, pos, direction);
	}

	@Override
	public boolean createbigcannons$blockBreaksDisassembly(Level level, BlockPos pos, BlockState newState) {
		BlockPos pistonPos = this.anchor.relative(this.orientation, -1);
		if (pos.equals(pistonPos) && isPiston(level.getBlockState(pos)))
			return false;
		return HasFragileContraption.defaultBlockBreaksAssembly(level, pos, newState, this);
	}

	@Override public boolean createbigcannons$shouldCheckFragility() { return HasFragileContraption.defaultShouldCheck(); }

	@Override
	public void createbigcannons$fragileDisassemble() {
		BlockPos pistonPos = this.anchor.relative(this.orientation, -1);
		if (this.world.getBlockEntity(pistonPos) instanceof MechanicalPistonBlockEntity pistonBE) {
			pistonBE.disassemble();
		} else {
			this.entity.disassemble();
		}
	}

}
