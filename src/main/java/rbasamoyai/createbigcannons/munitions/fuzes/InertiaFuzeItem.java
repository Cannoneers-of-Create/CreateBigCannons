package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.core.BlockPos;
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

public class InertiaFuzeItem extends FuzeItem {

	public InertiaFuzeItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult hitResult, AbstractCannonProjectile.ImpactResult impactResult, boolean baseFuze) {
		return this.onCommonImpact(stack, projectile.level(), impactResult);
	}

    @Override
    public boolean onBlockImpact(ItemStack stack, Level level, BlockPos pos, BlockState state, HitResult hitResult, AbstractCannonProjectile.ImpactResult impactResult, Vec3 impactPos) {
        return this.onCommonImpact(stack, level, impactResult);
    }

    public boolean onCommonImpact(ItemStack stack, Level level, AbstractCannonProjectile.ImpactResult impactResult) {
        if (impactResult.shouldRemove())
            return false;
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
		return CBCConfigs.server().munitions.inertiaFuzeDetonationChance.getF();
	}

	protected int getFuzeDurability() {
		return CBCConfigs.server().munitions.inertiaFuzeDurability.get();
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = CreateLang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".inertia_fuze.tooltip.shell_info.chance", (int) (this.getDetonateChance() * 100.0f))
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));
	}

}
