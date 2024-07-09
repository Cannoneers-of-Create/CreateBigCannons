package rbasamoyai.createbigcannons.munitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.effects.particles.smoke.GasCloudParticleData;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeEmitterEntity;

public class GasCloudEntity extends SmokeEmitterEntity {

	private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(GasCloudEntity.class, EntityDataSerializers.INT);

	private int waitTime;

	private Potion potion = Potions.EMPTY;
	private final List<MobEffectInstance> effects = new ArrayList<>();
	private final Map<Entity, Integer> victims = Maps.newHashMap();
	private boolean fixedColor;
	private int reapplicationDelay = 20;

	public GasCloudEntity(EntityType<? extends GasCloudEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_COLOR, 0);
	}

	public void setPotion(Potion potion) {
		this.potion = potion;
		if (!this.fixedColor)
			this.updateColor();
	}

	private void updateColor() {
		if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
			this.getEntityData().set(DATA_COLOR, 0);
		} else {
			this.getEntityData().set(DATA_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
		}
	}

	public void addEffect(MobEffectInstance effectInstance) {
		this.effects.add(effectInstance);
		if (!this.fixedColor)
			this.updateColor();
	}

	public int getColor() {
		return this.getEntityData().get(DATA_COLOR);
	}

	public void setFixedColor(int color) {
		this.fixedColor = true;
		this.getEntityData().set(DATA_COLOR, color);
	}

	public void setWaitTime(int waitTime) { this.waitTime = waitTime; }

	@Override
	public void tick() {
		super.tick();
		if (!this.level.isClientSide && this.age >= this.waitTime && this.canDoStuff()) {
			// Adapted from AreaEffectCloud
			this.victims.entrySet().removeIf(entry -> this.tickCount >= entry.getValue());
			List<MobEffectInstance> toApply = new ArrayList<>();

			for (MobEffectInstance potionEffect : this.potion.getEffects()) {
				toApply.add(new MobEffectInstance(
					potionEffect.getEffect(),
					potionEffect.getDuration() / 4,
					potionEffect.getAmplifier(),
					potionEffect.isAmbient(),
					potionEffect.isVisible()
					));
			}
			toApply.addAll(this.effects);
			if (toApply.isEmpty()) {
				this.victims.clear();
			} else {
				for (LivingEntity target : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
					if (this.victims.containsKey(target) || !target.isAffectedByPotions())
						continue;
					this.victims.put(target, this.tickCount + this.reapplicationDelay);
					for (MobEffectInstance appliedEffect : toApply) {
						if (appliedEffect.getEffect().isInstantenous()) {
							appliedEffect.getEffect().applyInstantenousEffect(this, null, target, appliedEffect.getAmplifier(), 0.5);
						} else {
							target.addEffect(new MobEffectInstance(appliedEffect), this);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean canMergeWithOther(SmokeEmitterEntity other) {
		if (!super.canMergeWithOther(other) || !(other instanceof GasCloudEntity otherGas))
			return false;
		return this.potion == otherGas.potion || this.potion == null || otherGas.potion == null;
	}

	@Override
	public void mergeWith(SmokeEmitterEntity other) {
		if (!(other instanceof GasCloudEntity otherGas))
			return;
		boolean flag = this.age >= this.waitTime || otherGas.age >= otherGas.waitTime;
		super.mergeWith(other);
		if (flag)
			this.age = Math.max(this.age, Math.min(this.waitTime, otherGas.waitTime));
		if (this.potion != otherGas.potion && this.potion == null)
			this.potion = otherGas.potion;
		this.effects.addAll(otherGas.effects);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		// Adapted from AreaEffectCloud
		super.addAdditionalSaveData(tag);
		tag.putInt("WaitTime", this.waitTime);
		tag.putInt("ReapplicationDelay", this.reapplicationDelay);
		if (this.fixedColor)
			tag.putInt("Color", this.getColor());
		if (this.potion != Potions.EMPTY)
			tag.putString("Potion", Registry.POTION.getKey(this.potion).toString());

		if (!this.effects.isEmpty()) {
			ListTag listTag = new ListTag();
			for (MobEffectInstance mobEffectInstance : this.effects)
				listTag.add(mobEffectInstance.save(new CompoundTag()));
			tag.put("Effects", listTag);
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		// Adapted from AreaEffectCloud
		super.readAdditionalSaveData(tag);
		this.waitTime = tag.getInt("WaitTime");
		this.reapplicationDelay = tag.getInt("ReapplicationDelay");
		if (tag.contains("Color", Tag.TAG_ANY_NUMERIC))
			this.setFixedColor(tag.getInt("Color"));
		if (tag.contains("Potion", Tag.TAG_STRING))
			this.setPotion(PotionUtils.getPotion(tag));

		if (tag.contains("Effects", Tag.TAG_LIST)) {
			ListTag listTag = tag.getList("Effects", Tag.TAG_COMPOUND);
			this.effects.clear();
			for (int i = 0; i < listTag.size(); ++i) {
				MobEffectInstance mobEffectInstance = MobEffectInstance.load(listTag.getCompound(i));
				if (mobEffectInstance != null)
					this.addEffect(mobEffectInstance);
			}
		}
	}

	@Override
	protected ParticleOptions getParticle() {
		int packed = this.getColor();
		float r = (float)(packed >> 16 & 0xFF) / 255.0F;
		float g = (float)(packed >> 8 & 0xFF) / 255.0F;
		float b = (float)(packed & 0xFF) / 255.0F;
		return new GasCloudParticleData(2, r, g, b);
	}

	@Override protected int getLifetime() { return super.getLifetime() + this.waitTime; }

}
