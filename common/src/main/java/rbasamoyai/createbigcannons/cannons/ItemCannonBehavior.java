package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemCannonBehavior extends CannonBehavior {

	public static final BehaviourType<ItemCannonBehavior> TYPE = new BehaviourType<>();

	private ItemStack containedStack = ItemStack.EMPTY;

	public ItemCannonBehavior(SmartBlockEntity te) {
		super(te);
	}

	public boolean tryLoadingItem(ItemStack stack) {
		if (!this.canLoadItem(stack)) return false;
		this.containedStack = stack;
		return true;
	}

	public boolean canLoadItem(ItemStack stack) {
		return this.containedStack.isEmpty();
	}

	public void removeItem() {
		this.containedStack = ItemStack.EMPTY;
	}

	public ItemStack getItem() {
		return this.containedStack;
	}

	@Override
	public void write(CompoundTag nbt, boolean spawnPacket) {
		nbt.put("ContainedStack", this.containedStack.save(new CompoundTag()));
		super.write(nbt, spawnPacket);
	}

	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		this.containedStack = ItemStack.of(nbt.getCompound("ContainedStack"));
		super.read(nbt, clientPacket);
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

}
