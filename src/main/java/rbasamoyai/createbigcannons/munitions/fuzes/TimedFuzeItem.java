package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class TimedFuzeItem extends FuzeItem implements MenuProvider {

	public TimedFuzeItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile projectile) {
		if (!stack.has(CBCDataComponents.FUZE_TIMER)) return true;
		int timer = stack.get(CBCDataComponents.FUZE_TIMER);
		--timer;
		stack.set(CBCDataComponents.FUZE_TIMER, timer);
		return timer <= 0;
	}

	@Override
	public boolean onProjectileExpiry(ItemStack stack, AbstractCannonProjectile projectile) {
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer splayer && player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			if (!stack.has(CBCDataComponents.FUZE_TIMER)) {
				stack.set(CBCDataComponents.FUZE_TIMER, 20);
			}
			int timer = stack.get(CBCDataComponents.FUZE_TIMER);

			CBCMenuTypes.SET_TIMED_FUZE.open(splayer, this.getDisplayName(), this, buf -> {
				buf.writeVarInt(timer);
                ItemStack.STREAM_CODEC.encode(buf, new ItemStack(this));
			});
		}
		return super.use(level, player, hand);
	}

	@Override
	public boolean canLingerInGround(ItemStack stack, AbstractCannonProjectile projectile) {
		return stack.has(CBCDataComponents.FUZE_TIMER);
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
		ItemStack stack = player.getMainHandItem();
		return TimedFuzeContainer.getServerMenu(windowId, playerInv, stack);
	}

	@Override
	public Component getDisplayName() {
		return this.getDescription();
	}

	public static ItemStack getCreativeTabItem(int defaultFuze) {
		ItemStack stack = CBCItems.TIMED_FUZE.asStack();
        stack.set(CBCDataComponents.FUZE_TIMER, defaultFuze);
		return stack;
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		int time = stack.getOrDefault(CBCDataComponents.FUZE_TIMER, 20);
		int seconds = time / 20;
		int ticks = time - seconds * 20;
		MutableComponent info = CreateLang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".timed_fuze.tooltip.shell_info", seconds, ticks)
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
        int time = stack.getOrDefault(CBCDataComponents.FUZE_TIMER, 20);
		int seconds = time / 20;
		int ticks = time - seconds * 20;
		tooltip.add(CreateLang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".timed_fuze.tooltip.shell_info.item", seconds, ticks)
			.component());
	}

}
