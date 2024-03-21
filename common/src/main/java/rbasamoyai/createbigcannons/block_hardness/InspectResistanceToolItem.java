package rbasamoyai.createbigcannons.block_hardness;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.multiloader.IndexPlatform;

public class InspectResistanceToolItem extends Item {

	public InspectResistanceToolItem(Properties properties) { super(properties); }

	@Override public boolean isFoil(ItemStack stack) { return true; }

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Player player = ctx.getPlayer();
		Level level = ctx.getLevel();
		if (!level.isClientSide && player != null && !IndexPlatform.isFakePlayer(player)) {
			String key = "debug." + CreateBigCannons.MOD_ID + ".block_resistance";
			BlockState state = level.getBlockState(ctx.getClickedPos());
			double hardness = TerminalBallisticsBlockPropertiesHandler.getProperties(state).hardness(level, state, ctx.getClickedPos(), true);
			player.displayClientMessage(new TranslatableComponent(key, String.format("%.2f", hardness)), true);
		}
		return super.useOn(ctx);
	}
}
