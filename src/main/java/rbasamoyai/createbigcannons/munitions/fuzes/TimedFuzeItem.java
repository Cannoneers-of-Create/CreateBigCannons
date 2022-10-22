package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.FuzedCannonProjectile;

public class TimedFuzeItem extends FuzeItem implements MenuProvider {

	public TimedFuzeItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public boolean onProjectileTick(ItemStack stack, FuzedCannonProjectile projectile) {
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("FuzeTimer")) return true;
		int timer = tag.getInt("FuzeTimer");
		--timer;
		tag.putInt("FuzeTimer", timer);
		return timer <= 0;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer && player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			CompoundTag tag = stack.getOrCreateTag();
			if (!tag.contains("FuzeTimer")) {
				tag.putInt("FuzeTimer", 20);
			}
			int timer = tag.getInt("FuzeTimer");
			NetworkHooks.openGui((ServerPlayer) player, this, buf -> {
				buf.writeVarInt(timer);
				buf.writeItem(new ItemStack(this));
			});
		}
		return super.use(level, player, hand);
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
		stack.getOrCreateTag().putInt("FuzeTimer", defaultFuze);
		return stack;
	}
	
	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		int time = stack.getOrCreateTag().getInt("FuzeTimer");
		int seconds = time / 20;
		int ticks = time - seconds * 20;
		MutableComponent info = Lang.builder("item")
				.translate(CreateBigCannons.MOD_ID + ".timed_fuze.tooltip.shell_info", seconds, ticks)
				.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, ChatFormatting.GRAY, ChatFormatting.GREEN, 6));
	}
	
}
