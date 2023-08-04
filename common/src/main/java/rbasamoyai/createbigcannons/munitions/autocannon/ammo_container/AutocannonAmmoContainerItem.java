package rbasamoyai.createbigcannons.munitions.autocannon.ammo_container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCMenuTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;

import javax.annotation.Nullable;

public class AutocannonAmmoContainerItem extends Item implements MenuProvider {

	public AutocannonAmmoContainerItem(Properties properties) {
		super(properties);
	}

	@Override public Component getDisplayName() { return this.getDescription(); }

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
		return AutocannonAmmoContainerMenu.getServerMenu(i, inventory, player.getMainHandItem());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer splayer && player.mayBuild()) {
			ItemStack stack = player.getItemInHand(hand);
			int spacing = getTracerSpacing(stack);

			CBCMenuTypes.AUTOCANNON_AMMO_CONTAINER.open(splayer, this.getDisplayName(), this, buf -> {
				buf.writeVarInt(spacing);
				buf.writeItem(new ItemStack(this));
			});
		}
		return super.use(level, player, hand);
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

	public static AutocannonAmmoType getTypeOfContainer(ItemStack container) {
		AutocannonAmmoType type = AutocannonAmmoType.of(getMainAmmoStack(container));
		if (type != AutocannonAmmoType.NONE) return type;
		return AutocannonAmmoType.of(getTracerAmmoStack(container));
	}

	public static int getTotalAmmoCount(ItemStack container) {
		return getMainAmmoStack(container).getCount() + getTracerAmmoStack(container).getCount();
	}

	public static ItemStack pollItemFromContainer(ItemStack container) {
		if (!(container.getItem() instanceof AutocannonAmmoContainerItem)) return ItemStack.EMPTY;
		ItemStack mainAmmo = getMainAmmoStack(container);
		ItemStack tracerAmmo = getTracerAmmoStack(container);
		ItemStack ret = ItemStack.EMPTY;
		if (getTotalAmmoCount(container) % getTracerSpacing(container) == 0) {
			if (!tracerAmmo.isEmpty()) {
				ret = tracerAmmo.split(1);
				container.getOrCreateTag().put("Tracers", tracerAmmo.isEmpty() ? new CompoundTag() : tracerAmmo.save(new CompoundTag()));
			} else if (!mainAmmo.isEmpty()) {
				ret = mainAmmo.split(1);
				container.getOrCreateTag().put("Ammo", mainAmmo.isEmpty() ? new CompoundTag() : mainAmmo.save(new CompoundTag()));
			}
		} else {
			if (!mainAmmo.isEmpty()) {
				ret = mainAmmo.split(1);
				container.getOrCreateTag().put("Ammo", mainAmmo.isEmpty() ? new CompoundTag() : mainAmmo.save(new CompoundTag()));
			} else if (!tracerAmmo.isEmpty()) {
				ret = tracerAmmo.split(1);
				container.getOrCreateTag().put("Tracers", tracerAmmo.isEmpty() ? new CompoundTag() : tracerAmmo.save(new CompoundTag()));
			}
		}
		return ret;
	}

}
