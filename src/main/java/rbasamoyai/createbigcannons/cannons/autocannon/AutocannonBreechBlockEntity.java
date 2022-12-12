package rbasamoyai.createbigcannons.cannons.autocannon;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.cannonmount.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class AutocannonBreechBlockEntity extends AutocannonBlockEntity {

	protected static final int[] FIRE_RATES = new int[] {
			120, // 10 rpm
			80, // 15 rpm
			60, // 20 rpm
			48, // 25 rpm
			40, // 30 rpm
			30, // 40 rpm
			24, // 50 rpm
			20, // 60 rpm
			15, // 80 rpm
			12, // 100 rpm
			10, // 120 rpm
			8,  // 150 rpm
			6,  // 200 rpm
			5,  // 240 rpm
			4  // 300 rpm
	};

	private int fireRate = 7;
	private int firingCooldown;
	private int animateTicks = 5;

	private final Queue<ItemStack> inputBuffer = new LinkedList<>();
	private ItemStack outputBuffer = ItemStack.EMPTY;

	public AutocannonBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public IItemHandler createItemHandler() { return new BreechItemHandler(this); }

	public int getQueueLimit() { return 5; }

	@Override
	public void tick() {
		super.tick();
		this.allTick(this.level);
	}

	@Override
	public void tickFromContraption(Level level, PitchOrientedContraptionEntity poce, BlockPos localPos) {
		super.tickFromContraption(level, poce, localPos);
		this.allTick(level);
	}

	private void allTick(Level level) {
		if (this.fireRate < 0 || this.fireRate > 15) this.fireRate = 0;
		if (this.firingCooldown < 0) this.firingCooldown = 0;
		if (this.firingCooldown > 0) this.firingCooldown--;

		if (this.animateTicks < 5) ++this.animateTicks;
		if (this.animateTicks < 0) this.animateTicks = 0;
	}

	public void setFireRate(int power) { this.fireRate = Mth.clamp(power, 0, 15); }
	public int getFireRate() { return this.fireRate; }
	public boolean canFire() { return this.getFireRate() > 0 && this.firingCooldown <= 0; }

	public void handleFiring() {
		if (this.fireRate > 0 && this.fireRate <= FIRE_RATES.length) {
			this.firingCooldown = FIRE_RATES[this.fireRate - 1];
			this.animateTicks = 0;
		}
	}

	public float getAnimateOffset(float partialTicks) {
		float t = ((float) this.animateTicks + partialTicks) * 1.2f;
		if (t <= 0 || t >= 4.8f) return 0;
		float f = t < 1 ? t : (4.8f - t) / 3.8f;
		return Mth.sin(f * Mth.HALF_PI);
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		this.fireRate = tag.getInt("FiringRate");
		this.firingCooldown = tag.getInt("Cooldown");
		this.animateTicks = tag.getInt("AnimateTicks");
		if (this.outputBuffer != null && !this.outputBuffer.isEmpty()) tag.put("Output", this.outputBuffer.serializeNBT());

		if (!this.inputBuffer.isEmpty()) {
			tag.put("Input", this.inputBuffer.stream()
					.map(ItemStack::serializeNBT)
					.collect(Collectors.toCollection(ListTag::new)));
		}
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.putInt("FiringRate", this.fireRate);
		tag.putInt("Cooldown", this.firingCooldown);
		tag.putInt("AnimateTicks", this.animateTicks);
		this.outputBuffer = tag.contains("Output") ? ItemStack.of(tag.getCompound("Output")) : ItemStack.EMPTY;

		this.inputBuffer.clear();
		ListTag inputTag = tag.getList("Input", Tag.TAG_COMPOUND);
		for (int i = 0; i < inputTag.size(); ++i) {
			this.inputBuffer.add(ItemStack.of(inputTag.getCompound(i)));
		}
	}

	public boolean isInputFull() { return this.inputBuffer.size() >= this.getQueueLimit(); }
	public boolean isOutputFull() { return !this.outputBuffer.isEmpty(); }

	public ItemStack insertOutput(ItemStack stack) {
		if (stack.isEmpty()) return ItemStack.EMPTY;
		if (!this.isOutputFull()) return stack;
		this.outputBuffer = stack;
		return ItemStack.EMPTY;
	}

	public record BreechItemHandler(AutocannonBreechBlockEntity breech) implements IItemHandler {
		@Override public int getSlots() { return 2; }

		@NotNull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return switch (slot) {
				case 0 -> this.breech.inputBuffer.isEmpty() ? ItemStack.EMPTY : this.breech.inputBuffer.peek();
				case 1 -> this.breech.outputBuffer;
				default -> ItemStack.EMPTY;
			};
		}

		@NotNull
		@Override
		public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
			if (slot != 0 || !this.isItemValid(slot, stack) || this.breech.isInputFull()) return stack;
			this.breech.inputBuffer.add(stack.split(1));
			return stack;
		}

		@NotNull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (amount <= 0) return ItemStack.EMPTY;
			return switch (slot) {
				case 0 -> this.breech.inputBuffer.isEmpty() ? ItemStack.EMPTY : this.breech.inputBuffer.poll();
				case 1 -> this.breech.outputBuffer.split(1);
				default -> ItemStack.EMPTY;
			};
		}

		@Override public int getSlotLimit(int slot) { return 1; }
		@Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return stack.getItem() instanceof AutocannonCartridgeItem; }
	}

}
