package rbasamoyai.createbigcannons.munitions.fuzes;

import java.util.List;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;

public class DelayedImpactFuzeItem extends FuzeItem implements MenuProvider {

	public DelayedImpactFuzeItem(Properties properties) {
		super(properties);
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

			CBCMenuTypes.SET_DELAYED_IMPACT_FUZE.open((ServerPlayer) player, this.getDisplayName(), this, buf -> {
				buf.writeVarInt(timer);
				buf.writeItem(new ItemStack(this));
			});
		}
		return super.use(level, player, hand);
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
		ItemStack stack = player.getMainHandItem();
		return DelayedImpactFuzeContainer.getServerMenu(windowId, playerInv, stack);
	}

	@Override
	public Component getDisplayName() {
		return this.getDescription();
	}

	@Override
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile<?> projectile, HitResult result, boolean stopped, boolean baseFuze) {
		if (baseFuze) return false;
		CompoundTag tag = stack.getOrCreateTag();
		int damage = tag.contains("Damage") ? tag.getInt("Damage") : CBCConfigs.SERVER.munitions.impactFuzeDurability.get();
		if (damage > 0 && !tag.contains("Activated")) {
			--damage;
			tag.putInt("Damage", damage);
			float f = this.getDetonateChance();
			if (f > 0 && projectile.level.getRandom().nextFloat() < f) {
				tag.putBoolean("Activated", true);
			}
		}
		return false;
	}

	@Override
	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile<?> projectile) {
		CompoundTag tag = stack.getOrCreateTag();
		if (!tag.contains("Activated")) return false;
		if (!tag.contains("FuzeTimer")) return true;
		int timer = tag.getInt("FuzeTimer");
		--timer;
		tag.putInt("FuzeTimer", timer);
		return timer <= 0;
	}

	@Override
	public boolean onProjectileExpiry(ItemStack stack, AbstractCannonProjectile<?> projectile) {
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		CBCTooltip.appendImpactFuzeText(stack, level, tooltip, flag, this.getDetonateChance());

		int time = stack.getOrCreateTag().getInt("FuzeTimer");
		int seconds = time / 20;
		int ticks = time - seconds * 20;
		tooltip.add(Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".timed_fuze.tooltip.shell_info.item", seconds, ticks)
			.component());
	}

	protected float getDetonateChance() {
		return CBCConfigs.SERVER.munitions.impactFuzeDetonationChance.getF();
	}

	@Override
	public boolean canLingerInGround(ItemStack stack, AbstractCannonProjectile<?> projectile) {
		return stack.getOrCreateTag().contains("Activated");
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".impact_fuze.tooltip.shell_info", (int) (this.getDetonateChance() * 100.0f))
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));

		int time = stack.getOrCreateTag().getInt("FuzeTimer");
		int seconds = time / 20;
		int ticks = time - seconds * 20;
		MutableComponent info1 = Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".timed_fuze.tooltip.shell_info", seconds, ticks)
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info1, Style.EMPTY, Style.EMPTY, 6));
	}

	public static ItemStack getCreativeTabItem(int defaultFuze) {
		ItemStack stack = CBCItems.DELAYED_IMPACT_FUZE.asStack();
		stack.getOrCreateTag().putInt("FuzeTimer", defaultFuze);
		return stack;
	}

}
