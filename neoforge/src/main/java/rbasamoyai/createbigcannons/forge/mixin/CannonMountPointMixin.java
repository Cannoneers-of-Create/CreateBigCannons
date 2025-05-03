package rbasamoyai.createbigcannons.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.ExtendsCannonMount;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.CannonMountPoint;

@Mixin(CannonMountPoint.class)
public abstract class CannonMountPointMixin extends ArmInteractionPoint {

	CannonMountPointMixin(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		super(type, level, pos, state);
	}

	@Override
	public ItemStack insert(ItemStack stack, boolean simulate) {
		CannonMountPoint self = (CannonMountPoint) (Object) this;
		BlockEntity be = this.getLevel().getBlockEntity(this.pos);
		PitchOrientedContraptionEntity poce;
		if (be instanceof ExtendsCannonMount extendsMount) {
			CannonMountBlockEntity mount = extendsMount.getCannonMount();
			if (mount == null)
				return stack;
			poce = mount.getContraption();
		} else if (be instanceof FixedCannonMountBlockEntity mount) {
			poce = mount.getContraption();
		} else {
			return stack;
		}
		if (poce == null || !(poce.getContraption() instanceof AbstractMountedCannonContraption cannon))
			return stack;
		return self.getInsertedResultAndDoSomething(stack, simulate, cannon, poce);
	}

}
