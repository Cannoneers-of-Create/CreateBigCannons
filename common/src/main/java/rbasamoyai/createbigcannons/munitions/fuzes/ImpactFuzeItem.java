package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class ImpactFuzeItem extends FuzeItem {

	public ImpactFuzeItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult hitResult, AbstractCannonProjectile.ImpactResult impactResult, boolean baseFuze) {
		if (baseFuze) return false;
		CompoundTag tag = stack.getOrCreateTag();
		int damage = tag.contains("Damage") ? tag.getInt("Damage") : this.getFuzeDurability();
		if (damage > 0) {
			--damage;
			tag.putInt("Damage", damage);
		}
		if (damage == 0) return false;
		float f = this.getDetonateChance();
		return f > 0 && projectile.level().getRandom().nextFloat() < f;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CBCTooltip.appendImpactFuzeText(stack, level, tooltip, flag, this.getDetonateChance(), this.getFuzeDurability());
	}

	protected float getDetonateChance() {
		return CBCConfigs.SERVER.munitions.impactFuzeDetonationChance.getF();
	}

	protected int getFuzeDurability() {
		return CBCConfigs.SERVER.munitions.impactFuzeDurability.get();
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".impact_fuze.tooltip.shell_info.chance", (int) (this.getDetonateChance() * 100.0f))
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));
	}

}
