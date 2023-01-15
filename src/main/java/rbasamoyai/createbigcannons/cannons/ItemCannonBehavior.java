package rbasamoyai.createbigcannons.cannons;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.behaviour.BehaviourType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemCannonBehavior extends CannonBehavior {

    public static final BehaviourType<ItemCannonBehavior> TYPE = new BehaviourType<>();

    private ItemStack containedStack = ItemStack.EMPTY;

    public ItemCannonBehavior(SmartTileEntity te) {
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

    @Override
    public void write(CompoundTag nbt, boolean spawnPacket) {
        nbt.put("ContainedStack", this.containedStack.serializeNBT());
        super.write(nbt, spawnPacket);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        this.containedStack = ItemStack.of(nbt.getCompound("ContainedStack"));
        super.read(nbt, clientPacket);
    }

    @Override public BehaviourType<?> getType() { return TYPE; }

}
