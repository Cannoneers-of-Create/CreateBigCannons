package rbasamoyai.createbigcannons.mixin.compat.create;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.piston.LinearActuatorBlockEntity;
import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;

@Mixin(PulleyBlockEntity.class)
public abstract class PulleyBlockEntityMixin extends LinearActuatorBlockEntity {

	PulleyBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) { super(typeIn, pos, state); }

	@Inject(method = "assemble",
			at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/BlockEntry;has(Lnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 0, shift = At.Shift.BEFORE))
	private void createbigcannons$assemble(CallbackInfo ci, @Local BlockPos ropePos, @Local LocalRef<BlockState> ropeStateRef) {
		BlockState ropeState = ropeStateRef.get();
		if (!(ropeState.getBlock() instanceof BigCannonBlock cBlock)
			|| !cBlock.getFacing(ropeState).getAxis().isVertical()
			|| !(this.level.getBlockEntity(ropePos) instanceof IBigCannonBlockEntity cbe)) return;
		StructureBlockInfo info = cbe.cannonBehavior().block();
		if (AllBlocks.ROPE.has(info.state()) || AllBlocks.PULLEY_MAGNET.has(info.state()))
			ropeStateRef.set(info.state());
	}

	@WrapOperation(method = "removeRopes",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private boolean createbigcannons$removeRopes(Level instance, BlockPos pos, BlockState newState, int flags,
												 Operation<Boolean> original, @Local BlockState oldState) {
		if (oldState.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(oldState).getAxis().isVertical()
			&& this.level.getBlockEntity(pos) instanceof IBigCannonBlockEntity cbe) {
			cbe.cannonBehavior().removeBlock();
			cbe.cannonBehavior().blockEntity.notifyUpdate();
			return false;
		}
		return original.call(instance, pos, newState, flags);
	}

	@WrapOperation(method = "disassemble", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z", ordinal = 0))
	private boolean createbigcannons$disassemble$0(Level instance, BlockPos pos, boolean dropBlock, Operation<Boolean> original) {
		return !createbigcannons$isLoadingCannon(instance, pos, AllBlocks.PULLEY_MAGNET.getDefaultState()) && original.call(instance, pos, dropBlock);
	}

	@WrapOperation(method = "disassemble", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;destroyBlock(Lnet/minecraft/core/BlockPos;Z)Z", ordinal = 1))
	private boolean createbigcannons$disassemble$1(Level instance, BlockPos pos, boolean dropBlock, Operation<Boolean> original) {
		return !createbigcannons$isLoadingCannon(instance, pos, AllBlocks.ROPE.getDefaultState()) && original.call(instance, pos, dropBlock);
	}

	@WrapOperation(method = "disassemble", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
	private boolean createbigcannons$disassemble$2(Level instance, BlockPos pos, BlockState state, int flag, Operation<Boolean> original) {
		if (createbigcannons$isLoadingCannon(instance, pos, state)) {
			BlockEntity be = instance.getBlockEntity(pos);
			((IBigCannonBlockEntity) be).cannonBehavior().loadBlock(new StructureBlockInfo(BlockPos.ZERO, state, new CompoundTag()));
			((IBigCannonBlockEntity) be).cannonBehavior().blockEntity.notifyUpdate();
			return true;
		}
		return original.call(instance, pos, state, flag);
	}

	@Inject(method = "visitNewPosition",
			at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/pulley/PulleyBlockEntity;disassemble()V", shift = At.Shift.BEFORE),
			remap = false,
			cancellable = true)
	private void createbigcannons$visitNewPosition(CallbackInfo ci, @Local BlockPos posBelow, @Local BlockState state) {
		if (state.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(state).getAxis().isVertical()
			&& this.level.getBlockEntity(posBelow) instanceof IBigCannonBlockEntity cbe
			&& cbe.cannonBehavior().canLoadBlock(new StructureBlockInfo(BlockPos.ZERO, AllBlocks.PULLEY_MAGNET.getDefaultState(), null))
			&& ci.isCancellable()) {
			ci.cancel();
		}
	}

	@Unique
	private static boolean createbigcannons$isLoadingCannon(Level level, BlockPos pos, BlockState loadState) {
		BlockState state = level.getBlockState(pos);
		BlockEntity be = level.getBlockEntity(pos);
		return state.getBlock() instanceof BigCannonBlock cBlock
			&& cBlock.getFacing(state).getAxis().isVertical()
			&& be instanceof IBigCannonBlockEntity cbe
			&& cbe.canLoadBlock(new StructureBlockInfo(BlockPos.ZERO, loadState, null));
	}

}
