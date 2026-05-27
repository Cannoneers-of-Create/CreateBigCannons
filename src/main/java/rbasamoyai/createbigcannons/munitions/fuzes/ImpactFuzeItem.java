package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedProjectileBlock;

public class ImpactFuzeItem extends FuzeItem {

	public ImpactFuzeItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult hitResult, AbstractCannonProjectile.ImpactResult impactResult, boolean baseFuze) {
		if (baseFuze || impactResult.shouldRemove() || impactResult.kinematics() == AbstractCannonProjectile.ImpactResult.KinematicOutcome.BOUNCE)
            return false;
		return this.onCommonImpact(stack, projectile.level());
	}

    @Override
    public boolean onBlockImpact(ItemStack stack, Level level, BlockPos pos, BlockState state, HitResult hitResult, AbstractCannonProjectile.ImpactResult impactResult, Vec3 impactPos) {
        boolean baseFuze = state.getBlock() instanceof FuzedProjectileBlock<?, ?> fuzedBlock && fuzedBlock.isBaseFuze();
        Direction shellFacing = state.getValue(FuzedProjectileBlock.FACING);
        Direction hitFace = baseFuze ? shellFacing.getOpposite() : shellFacing;
        Vec3 hitDir = impactPos.subtract(pos.getCenter());
        Direction closest = Direction.getNearest(hitDir.x, hitDir.y, hitDir.z);
        if (closest != hitFace)
            return false;

        if (impactResult.shouldRemove() || impactResult.kinematics() == AbstractCannonProjectile.ImpactResult.KinematicOutcome.BOUNCE)
            return false;
        return this.onCommonImpact(stack, level);
    }

    public boolean onCommonImpact(ItemStack stack, Level level) {
        int damage = stack.getOrDefault(CBCDataComponents.FUZE_DAMAGE, this.getFuzeDurability());
        if (damage > 0) {
            --damage;
            stack.set(CBCDataComponents.FUZE_DAMAGE, damage);
        }
        if (damage == 0) return false;
        float f = this.getDetonateChance();
        return f > 0 && level.getRandom().nextFloat() < f;
    }

    @Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
		CBCTooltip.appendImpactFuzeText(stack, ctx, tooltip, flag, this.getDetonateChance(), this.getFuzeDurability());
	}

	protected float getDetonateChance() {
		return CBCConfigs.server().munitions.impactFuzeDetonationChance.getF();
	}

	protected int getFuzeDurability() {
		return CBCConfigs.server().munitions.impactFuzeDurability.get();
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = CreateLang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".impact_fuze.tooltip.shell_info.chance", (int) (this.getDetonateChance() * 100.0f))
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));
	}

}
