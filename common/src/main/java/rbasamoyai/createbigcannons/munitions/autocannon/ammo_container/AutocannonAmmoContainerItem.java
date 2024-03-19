package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;

public class AutocannonAmmoContainerItem extends BlockItem implements MenuProvider {

	public AutocannonAmmoContainerItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override public Component getDisplayName() { return this.getDescription(); }

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return AutocannonAmmoContainerMenu.getServerMenuForItemStack(i, inventory, player.getMainHandItem(), this.isCreative());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			if (player instanceof ServerPlayer splayer) {
				int spacing = getTracerSpacing(stack);
				Component screenName = stack.hasCustomHoverName() ? stack.getHoverName() : this.getDisplayName();

				CBCMenuTypes.AUTOCANNON_AMMO_CONTAINER.open(splayer, screenName, this, buf -> {
					buf.writeBoolean(this.isCreative());
					buf.writeVarInt(spacing);
					buf.writeBoolean(false);
					buf.writeItem(new ItemStack(this));
				});
			}
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
		}
		return super.use(level, player, hand);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult superResult = super.useOn(context);
		if (superResult.consumesAction()) return superResult;
		return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
	}

	public static ItemStack getMainAmmoStack(ItemStack container) {
		CompoundTag tag = container.getOrCreateTag();
		return tag.contains("Ammo") ? ItemStack.of(tag.getCompound("Ammo")) : ItemStack.EMPTY;
	}

	public static ItemStack getTracerAmmoStack(ItemStack container) {
		CompoundTag tag = container.getOrCreateTag();
		return tag.contains("Tracers") ? ItemStack.of(tag.getCompound("Tracers")) : ItemStack.EMPTY;
	}

	public static int getTracerSpacing(ItemStack container) {
		CompoundTag tag = container.getOrCreateTag();
		return tag.contains("TracerSpacing") ? Mth.clamp(tag.getInt("TracerSpacing"), 1, 6) : 1;
	}

	public static boolean shouldPullTracer(ItemStack container) {
		CompoundTag tag = container.getOrCreateTag();
		if (!tag.contains("CurrentIndex", CompoundTag.TAG_INT)) tag.putInt("CurrentIndex", 0);
		int currentCount = Math.max(tag.getInt("CurrentIndex"), 0);
		tag.putInt("CurrentIndex", currentCount >= getTracerSpacing(container) ? 0 : currentCount + 1);
		return currentCount == 0;
	}

	public static AutocannonAmmoType getTypeOfContainer(ItemStack container) {
		AutocannonAmmoType type = AutocannonAmmoType.of(getMainAmmoStack(container));
		if (type != AutocannonAmmoType.NONE) return type;
		return AutocannonAmmoType.of(getTracerAmmoStack(container));
	}

	public static int getTotalAmmoCount(ItemStack container) {
		return getMainAmmoStack(container).getCount() + getTracerAmmoStack(container).getCount();
	}

	public static ItemStack pollItemFromContainer(ItemStack container) {
		if (!(container.getItem() instanceof AutocannonAmmoContainerItem ctItem)) return ItemStack.EMPTY;
		ItemStack mainAmmo = getMainAmmoStack(container);
		ItemStack tracerAmmo = getTracerAmmoStack(container);
		ItemStack ret = ItemStack.EMPTY;
		boolean isCreative = ctItem.isCreative();

		if (isCreative && shouldPullTracer(container) || getTotalAmmoCount(container) % getTracerSpacing(container) == 0) {
			if (!tracerAmmo.isEmpty()) {
				if (isCreative) {
					ret = tracerAmmo.copy();
					ret.setCount(1);
				} else {
					ret = tracerAmmo.split(1);
					container.getOrCreateTag().put("Tracers", tracerAmmo.isEmpty() ? new CompoundTag() : tracerAmmo.save(new CompoundTag()));
				}
			} else if (!mainAmmo.isEmpty()) {
				if (isCreative) {
					ret = mainAmmo.copy();
					ret.setCount(1);
				} else {
					ret = mainAmmo.split(1);
					container.getOrCreateTag().put("Ammo", mainAmmo.isEmpty() ? new CompoundTag() : mainAmmo.save(new CompoundTag()));
				}
			}
		} else {
			if (!mainAmmo.isEmpty()) {
				if (isCreative) {
					ret = mainAmmo.copy();
					ret.setCount(1);
				} else {
					ret = mainAmmo.split(1);
					container.getOrCreateTag().put("Ammo", mainAmmo.isEmpty() ? new CompoundTag() : mainAmmo.save(new CompoundTag()));
				}
			} else if (!tracerAmmo.isEmpty()) {
				if (isCreative) {
					ret = tracerAmmo.copy();
					ret.setCount(1);
				} else {
					ret = tracerAmmo.split(1);
					container.getOrCreateTag().put("Tracers", tracerAmmo.isEmpty() ? new CompoundTag() : tracerAmmo.save(new CompoundTag()));
				}
			}
		}
		return ret;
	}

	public boolean isCreative() { return false; }

}
