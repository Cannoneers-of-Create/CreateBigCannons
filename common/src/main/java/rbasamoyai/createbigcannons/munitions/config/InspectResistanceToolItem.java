package rbasamoyai.createbigcannons.munitions.config;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CBCExpectPlatform;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class InspectResistanceToolItem extends Item {

	public InspectResistanceToolItem(Properties properties) { super(properties); }

	@Override public boolean isFoil(ItemStack stack) { return true; }

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Player player = ctx.getPlayer();
		Level level = ctx.getLevel();
		if (!level.isClientSide && player != null && !CBCExpectPlatform.isFakePlayer(player)) {
			String key = "debug." + CreateBigCannons.MOD_ID + ".block_resistance";
			double hardness = BlockHardnessHandler.getHardness(level.getBlockState(ctx.getClickedPos()));
			player.displayClientMessage(new TranslatableComponent(key, String.format("%.2f", hardness)), true);
		}
		return super.useOn(ctx);
	}
}
