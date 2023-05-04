package rbasamoyai.createbigcannons.forge.mixin;

import com.simibubi.create.content.logistics.block.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.logistics.block.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechPoint;

import static rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechPoint.loadCartridge;
import static rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechPoint.loadProjectile;

@Mixin(QuickfiringBreechPoint.class)
public abstract class QuickfiringBreechPointMixin extends ArmInteractionPoint {

	QuickfiringBreechPointMixin(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) { super(type, level, pos, state); }

	@Override
	public ItemStack insert(ItemStack stack, boolean simulate) {
		QuickfiringBreechPoint self = (QuickfiringBreechPoint) (Object) this;
		if (!(this.level.getBlockEntity(this.pos) instanceof CannonMountBlockEntity mount)) return stack;
		PitchOrientedContraptionEntity poce = mount.getContraption();
		if (poce == null) return stack;
		MountedBigCannonContraption cannon = (MountedBigCannonContraption) poce.getContraption();
		return self.getInsertedResultAndDoSomething(stack, cannon,
				(s, m) -> loadProjectile(s, m, simulate, poce, cannon), (s, m) -> loadCartridge(s, m, simulate, poce, cannon));
	}

}
