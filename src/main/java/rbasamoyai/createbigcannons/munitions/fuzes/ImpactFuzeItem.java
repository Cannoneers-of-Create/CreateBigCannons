package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.FuzedCannonProjectile;

public class ImpactFuzeItem extends FuzeItem {

	public ImpactFuzeItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean onProjectileImpact(ItemStack stack, FuzedCannonProjectile projectile, HitResult result) {
		return projectile.getBreakthroughPower() > 0 ? false : projectile.level.getRandom().nextFloat() < this.getDetonateChance();
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		ItemDescription.Palette palette = AllSections.of(stack).getTooltipPalette();
		if (Screen.hasShiftDown()) {
			String key = this.getDescriptionId() + ".tooltip.chance";
			tooltip.add(new TextComponent(I18n.get(key)).withStyle(ChatFormatting.GRAY));
			tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", (int)(this.getDetonateChance() * 100.0f)), palette.color, palette.hColor, 1));
		}
	}
	
	protected float getDetonateChance() {
		return CBCConfigs.SERVER.munitions.impactFuzeDetonationChance.getF();
	}
	
}
