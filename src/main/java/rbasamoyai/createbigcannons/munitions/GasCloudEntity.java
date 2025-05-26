package rbasamoyai.createbigcannons.munitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.effects.particles.smoke.GasCloudParticleData;
import rbasamoyai.createbigcannons.equipment.gas_mask.GasMaskItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeEmitterEntity;

public class GasCloudEntity extends SmokeEmitterEntity {

	private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(GasCloudEntity.class, EntityDataSerializers.INT);

	private int waitTime;

	private PotionContents potionContents = PotionContents.EMPTY;
	private final Map<Entity, Integer> victims = Maps.newHashMap();
	private boolean fixedColor;
	private int reapplicationDelay = 20;

	public GasCloudEntity(EntityType<? extends GasCloudEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_COLOR, 0);
	}

	public void setPotionContents(PotionContents potion) {
		this.potionContents = potion;
		if (!this.fixedColor)
			this.updateColor();
	}

	private void updateColor() {
        this.getEntityData().set(DATA_COLOR, this.potionContents.getColor()); // todo: playtest 1.21
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
		if (!this.level().isClientSide && this.age >= this.waitTime && this.canDoStuff()) {
			// Adapted from AreaEffectCloud
			this.victims.entrySet().removeIf(entry -> this.tickCount >= entry.getValue());
			List<MobEffectInstance> toApply = new ArrayList<>();

            if (this.potionContents.potion().isPresent()) {
                for (MobEffectInstance potionEffect : this.potionContents.potion().get().value().getEffects()) {
                    toApply.add(new MobEffectInstance(
                        potionEffect.getEffect(),
                        potionEffect.mapDuration(i -> i / 4),
                        potionEffect.getAmplifier(),
                        potionEffect.isAmbient(),
                        potionEffect.isVisible()
                    ));
                }
            }

			toApply.addAll(this.potionContents.customEffects());
			if (toApply.isEmpty()) {
				this.victims.clear();
			} else {
				for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
					if (this.victims.containsKey(target) || !target.isAffectedByPotions() || this.isBlockedBy(target))
						continue;
					this.victims.put(target, this.tickCount + this.reapplicationDelay);
					for (MobEffectInstance appliedEffect : toApply) {
						if (appliedEffect.getEffect().value().isInstantenous()) {
							appliedEffect.getEffect().value().applyInstantenousEffect(this, null, target, appliedEffect.getAmplifier(), 0.5);
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
		return this.potionContents == otherGas.potionContents || this.potionContents == null || otherGas.potionContents == null;
	}

	@Override
	public void mergeWith(SmokeEmitterEntity other) {
		if (!(other instanceof GasCloudEntity otherGas))
			return;
		boolean flag = this.age >= this.waitTime || otherGas.age >= otherGas.waitTime;
		super.mergeWith(other);
		if (flag)
			this.age = Math.max(this.age, Math.min(this.waitTime, otherGas.waitTime));

        Optional<Holder<Potion>> potionOp;
        if (this.potionContents.potion().isPresent()) {
            potionOp = this.potionContents.potion();
        } else if (otherGas.potionContents.potion().isPresent()) {
            potionOp = otherGas.potionContents.potion();
        } else {
            potionOp = Optional.empty();
        }
        List<MobEffectInstance> effects = new ArrayList<>();
        effects.addAll(this.potionContents.customEffects());
        effects.addAll(otherGas.potionContents.customEffects());
        this.potionContents = new PotionContents(potionOp, this.potionContents.customColor(), effects);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		// Adapted from AreaEffectCloud
		super.addAdditionalSaveData(tag);
		tag.putInt("WaitTime", this.waitTime);
		tag.putInt("ReapplicationDelay", this.reapplicationDelay);
		if (this.fixedColor)
			tag.putInt("Color", this.getColor());

        RegistryOps<Tag> registryOps = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        if (!this.potionContents.equals(PotionContents.EMPTY)) {
            Tag potionTag = PotionContents.CODEC.encodeStart(registryOps, this.potionContents).getOrThrow();
            tag.put("potion_contents", potionTag);
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

        RegistryOps<Tag> registryOps = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
		if (tag.contains("potion_contents", Tag.TAG_COMPOUND))
            PotionContents.CODEC
                .parse(registryOps, tag.get("potion_contents"))
                .resultOrPartial(string -> CreateBigCannons.LOGGER.warn("Failed to parse area effect cloud potions: '{}'", string))
                .ifPresent(this::setPotionContents);
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

	public boolean isBlockedBy(LivingEntity target) {
		return GasMaskItem.isWearingWorkingMask(target);
	}

}
