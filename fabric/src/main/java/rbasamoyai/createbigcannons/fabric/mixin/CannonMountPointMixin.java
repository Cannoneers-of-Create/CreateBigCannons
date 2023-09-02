package rbasamoyai.createbigcannons.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.CannonMountPoint;

@Mixin(CannonMountPoint.class)
public abstract class CannonMountPointMixin extends ArmInteractionPoint {

	CannonMountPointMixin(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		super(type, level, pos, state);
	}

	@Override
	public ItemStack insert(ItemStack stack, TransactionContext ctx) {
		CannonMountPoint self = (CannonMountPoint) (Object) this;
		boolean simulate = !stack.getOrCreateTag().contains("DontSimulate");
		if (!simulate) stack.getTag().remove("DontSimulate");
		if (!(this.getLevel().getBlockEntity(this.pos) instanceof CannonMountBlockEntity mount)) return stack;
		PitchOrientedContraptionEntity poce = mount.getContraption();
		if (poce == null || !(poce.getContraption() instanceof AbstractMountedCannonContraption cannon)) return stack;
		return self.getInsertedResultAndDoSomething(stack, simulate, cannon, poce);
	}

}
