package rbasamoyai.createbigcannons.datagen.values;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.config.MunitionProperties;
import rbasamoyai.createbigcannons.munitions.config.ShrapnelProperties;

public class MunitionPropertiesProvider extends CBCDataProvider {

	private final Map<EntityType<?>, MunitionProperties> projectiles = new LinkedHashMap<>();

	public MunitionPropertiesProvider(String modid, String name, DataGenerator gen) {
		super(modid, name, gen, "munition_properties");
	}

	public MunitionPropertiesProvider(String modid, DataGenerator gen) {
		this(modid, "default", gen);
	}

	@Override
	protected void generateData() {
		builder(CBCEntityTypes.SHOT.get()).entityDamage(30).durabilityMass(10).build(this);
		builder(CBCEntityTypes.HE_SHELL.get()).entityDamage(30).explosivePower(8).explosivePower(8).build(this);
		builder(CBCEntityTypes.AP_SHOT.get()).entityDamage(50).durabilityMass(30).build(this);
		builder(CBCEntityTypes.TRAFFIC_CONE.get()).entityDamage(100).durabilityMass(36).build(this);
		builder(CBCEntityTypes.AP_SHELL.get()).entityDamage(50).explosivePower(5).durabilityMass(20).baseFuze().build(this);
		builder(CBCEntityTypes.MORTAR_STONE.get()).entityDamage(50).explosivePower(4).durabilityMass(4).build(this);
		builder(CBCEntityTypes.FLUID_SHELL.get()).entityDamage(30).durabilityMass(8).build(this);
		builder(CBCEntityTypes.SMOKE_SHELL.get()).entityDamage(30).durabilityMass(8).build(this);

		builder(CBCEntityTypes.SHRAPNEL_SHELL.get()).entityDamage(30).durabilityMass(8)
			.shrapnel(2, 0.25, 50).build(this);
		builder(CBCEntityTypes.BAG_OF_GRAPESHOT.get()).entityDamage(0).durabilityMass(1).renderInvulnerable()
			.accountForEntityArmor().shrapnel(5, 0.05, 25).build(this);

		builder(CBCEntityTypes.AP_AUTOCANNON.get()).entityDamage(6).durabilityMass(6).accountForEntityArmor().build(this);
		builder(CBCEntityTypes.FLAK_AUTOCANNON.get()).entityDamage(2).durabilityMass(1).accountForEntityArmor()
			.shrapnel(5, 0.25, 15).build(this);
		builder(CBCEntityTypes.MACHINE_GUN_BULLET.get()).entityDamage(4).durabilityMass(0.1).accountForEntityArmor().build(this);

		shrapnel(CBCEntityTypes.SHRAPNEL.get(), 1);
		shrapnel(CBCEntityTypes.GRAPESHOT.get(), 3);
	}

	protected Builder builder(EntityType<?> type) { return new Builder(type); }

	protected void shrapnel(EntityType<?> type, double durabilityMass) {
		builder(type).durabilityMass(durabilityMass).renderInvulnerable().accountForEntityArmor().build(this);
	}

	@Override
	protected void write(JsonObject obj) {
		for (Map.Entry<EntityType<?>, MunitionProperties> entry : this.projectiles.entrySet()) {
			obj.add(Registry.ENTITY_TYPE.getKey(entry.getKey()).toString(), entry.getValue().serialize());
		}
	}

	@Override public String getName() { return "Munition properties: " + this.modid; }

	public static class Builder {
		private final EntityType<?> type;
		private double entityDamage;
		private double explosivePower = 0;
		private double durabilityMass;
		private boolean renderInvulnerable = false;
		private boolean ignoresEntityArmor = true;
		private boolean baseFuze = false;
		private double shrapnelDamage;
		private double shrapnelSpread;
		private int shrapnelCount;
		private boolean buildShrapnel = false;

		public Builder(EntityType<?> type) {
			this.type = type;
		}

		public Builder entityDamage(double value) { this.entityDamage = value; return this; }
		public Builder explosivePower(double value) { this.explosivePower = value; return this; }
		public Builder durabilityMass(double value) { this.durabilityMass = value; return this; }
		public Builder renderInvulnerable() { this.renderInvulnerable = true; return this; }
		public Builder accountForEntityArmor() { this.ignoresEntityArmor = false; return this; }
		public Builder baseFuze() { this.baseFuze = true; return this; }
		public Builder shrapnel(double damage, double spread, int count) {
			this.shrapnelDamage = damage;
			this.shrapnelSpread = spread;
			this.shrapnelCount = count;
			this.buildShrapnel = true;
			return this;
		}

		public void build(MunitionPropertiesProvider cons) {
			cons.projectiles.put(this.type, new MunitionProperties(this.entityDamage, this.explosivePower, this.durabilityMass,
				this.renderInvulnerable, this.ignoresEntityArmor, this.baseFuze, this.buildShrapnel ?
				new ShrapnelProperties(this.shrapnelDamage, this.shrapnelSpread, this.shrapnelCount) : null));
		}
	}

}
