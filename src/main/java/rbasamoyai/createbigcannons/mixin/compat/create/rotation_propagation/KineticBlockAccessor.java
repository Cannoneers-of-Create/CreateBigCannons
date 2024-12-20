package rbasamoyai.createbigcannons.mixin.compat.create.rotation_propagation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.kinetics.base.KineticBlock;

import net.minecraft.world.level.block.state.BlockState;

@Mixin(KineticBlock.class)
public interface KineticBlockAccessor {

	@Invoker(remap = false) boolean callAreStatesKineticallyEquivalent(BlockState state, BlockState state1);

}
