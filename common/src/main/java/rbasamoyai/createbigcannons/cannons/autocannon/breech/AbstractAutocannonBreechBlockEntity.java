package rbasamoyai.createbigcannons.cannons.autocannon.breech;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.AnimatedAutocannon;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlockEntity;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractAutocannonBreechBlockEntity extends AutocannonBlockEntity implements AnimatedAutocannon {

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
	private DyeColor seat = null;
	private boolean updateInstance = true;

	private final Deque<ItemStack> inputBuffer = new LinkedList<>();
	private ItemStack outputBuffer = ItemStack.EMPTY;
	private ItemStack magazine = ItemStack.EMPTY;

	protected AbstractAutocannonBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public int getQueueLimit() { return 5; }

	public Deque<ItemStack> getInputBuffer() { return this.inputBuffer; }
	public ItemStack getOutputBuffer() { return this.outputBuffer; }
	public void setOutputBuffer(ItemStack stack) { this.outputBuffer = stack; }

	public void setMagazine(ItemStack stack) {
		this.magazine = stack;
		this.updateInstance = true;
		this.notifyUpdate();
	}

	public ItemStack getMagazine() { return this.magazine; }

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
	public int getActualFireRate() {
		if (this.fireRate < 1 || this.fireRate > 15) return 0;
		int cooldown = FIRE_RATES[this.fireRate - 1];
		return 1200 / cooldown;
	}
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

	@Override public void incrementAnimationTicks() { ++this.animateTicks; }
	@Override public int getAnimationTicks() { return this.animateTicks; }

	public void setSeatColor(DyeColor color) {
		this.seat = color;
		this.updateInstance = true;
		this.notifyUpdate();
	}

	public DyeColor getSeatColor() { return this.seat; }

	public boolean shouldUpdateInstance() {
		if (this.updateInstance) {
			this.updateInstance = false;
			return true;
		}
		return false;
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		this.fireRate = tag.getInt("FiringRate");
		this.firingCooldown = tag.getInt("Cooldown");
		this.animateTicks = tag.getInt("AnimateTicks");
		this.outputBuffer = tag.contains("Output") ? ItemStack.of(tag.getCompound("Output")) : ItemStack.EMPTY;
		this.seat = DyeColor.byName(tag.getString("Seat"), null);

		this.inputBuffer.clear();
		ListTag inputTag = tag.getList("Input", Tag.TAG_COMPOUND);
		for (int i = 0; i < inputTag.size(); ++i) {
			this.inputBuffer.add(ItemStack.of(inputTag.getCompound(i)));
		}
		this.magazine = tag.contains("Magazine") ? ItemStack.of(tag.getCompound("Magazine")) : ItemStack.EMPTY;

		if (!clientPacket) return;
		this.updateInstance = tag.contains("UpdateInstance");
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.putInt("FiringRate", this.fireRate);
		tag.putInt("Cooldown", this.firingCooldown);
		tag.putInt("AnimateTicks", this.animateTicks);
		if (this.outputBuffer != null && !this.outputBuffer.isEmpty()) tag.put("Output", this.outputBuffer.save(new CompoundTag()));
		if (this.seat != null) tag.putString("Seat", this.seat.getSerializedName());

		if (!this.inputBuffer.isEmpty()) {
			tag.put("Input", this.inputBuffer.stream()
					.map(s -> s.save(new CompoundTag()))
					.collect(Collectors.toCollection(ListTag::new)));
		}
		if (this.magazine != null && !this.magazine.isEmpty()) tag.put("Magazine", this.magazine.save(new CompoundTag()));

		if (!clientPacket) return;
		if (this.updateInstance) tag.putBoolean("UpdateInstance", true);
	}

	public boolean isInputFull() { return this.inputBuffer.size() >= this.getQueueLimit() || !this.magazine.isEmpty(); }
	public boolean isOutputFull() { return !this.outputBuffer.isEmpty(); }

	public ItemStack insertOutput(ItemStack stack) {
		if (stack.isEmpty()) return ItemStack.EMPTY;
		if (this.isOutputFull()) return stack;
		this.outputBuffer = stack;
		return ItemStack.EMPTY;
	}

	public ItemStack extractNextInput() {
		if (!this.inputBuffer.isEmpty()) return this.inputBuffer.poll();
		return this.magazine.isEmpty() ? ItemStack.EMPTY : AutocannonAmmoContainerItem.pollItemFromContainer(this.magazine);
	}

	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> list = super.getDrops();
		for (ItemStack s : this.inputBuffer) {
			if (!s.isEmpty()) list.add(s.copy());
		}
		if (!this.outputBuffer.isEmpty()) list.add(this.outputBuffer.copy());
		return list;
	}

}
