package rbasamoyai.createbigcannons.datagen.values;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.config.MunitionProperties;
import rbasamoyai.createbigcannons.munitions.config.ShrapnelProperties;

import java.util.LinkedHashMap;
import java.util.Map;

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
		standardBigCannonProjectile(CBCEntityTypes.SHOT.get(), 30, 0, 10);
		standardBigCannonProjectile(CBCEntityTypes.HE_SHELL.get(), 30, 8,8);
		standardBigCannonProjectile(CBCEntityTypes.AP_SHOT.get(), 50, 0,30);
		standardBigCannonProjectile(CBCEntityTypes.TRAFFIC_CONE.get(), 100, 0, 36);
		standardBigCannonProjectile(CBCEntityTypes.AP_SHELL.get(), 50, 5, 20);
		standardBigCannonProjectile(CBCEntityTypes.MORTAR_STONE.get(), 50 , 4, 4);
		standardBigCannonProjectile(CBCEntityTypes.FLUID_SHELL.get(), 30, 0, 8);
		standardBigCannonProjectile(CBCEntityTypes.SMOKE_SHELL.get(), 30, 0, 8);

		setPropertyWithSecondary(CBCEntityTypes.SHRAPNEL_SHELL.get(), 30, 0,8, false, true,
				2, 0.25, 50);
		setPropertyWithSecondary(CBCEntityTypes.BAG_OF_GRAPESHOT.get(),0, 0,1, true, false,
				5, 0.05, 25);

		standardAutocannonProjectile(CBCEntityTypes.AP_AUTOCANNON.get(), 5, 6);
		setPropertyWithSecondary(CBCEntityTypes.FLAK_AUTOCANNON.get(),2, 0,1, false, false,
				5, 0.25, 15);

		shrapnel(CBCEntityTypes.SHRAPNEL.get(), 1);
		shrapnel(CBCEntityTypes.GRAPESHOT.get(), 3);
	}

	protected void standardBigCannonProjectile(EntityType<?> type, double entityDamage, double explosivePower, double durabilityMass) {
		setProperty(type, entityDamage, explosivePower, durabilityMass, false, true);
	}

	protected void standardAutocannonProjectile(EntityType<?> type, double entityDamage, double durabilityMass) {
		setProperty(type, entityDamage, 0, durabilityMass, false, false);
	}

	protected void shrapnel(EntityType<?> type, double durabilityMass) {
		setProperty(type, 0, 0, durabilityMass, true, false);
	}

	protected void setProperty(EntityType<?> type, double entityDamage, double explosivePower, double durabilityMass,
							   boolean renderInvulnerable, boolean ignoresEntityArmor) {
		this.projectiles.put(type, new MunitionProperties(entityDamage, explosivePower, durabilityMass, renderInvulnerable,
				ignoresEntityArmor, null));
	}

	protected void setPropertyWithSecondary(EntityType<?> type, double entityDamage, double explosivePower, double durabilityMass,
											boolean renderInvulnerable, boolean ignoresEntityArmor, double secondaryEntityDamage,
											double secondarySpread, int secondaryCount) {
		this.projectiles.put(type, new MunitionProperties(entityDamage, explosivePower, durabilityMass, renderInvulnerable,
				ignoresEntityArmor, new ShrapnelProperties(secondaryEntityDamage, secondarySpread, secondaryCount)));
	}

	@Override
	protected void write(JsonObject obj) {
		for (Map.Entry<EntityType<?>, MunitionProperties> entry : this.projectiles.entrySet()) {
			obj.add(Registry.ENTITY_TYPE.getKey(entry.getKey()).toString(), entry.getValue().serialize());
		}
	}

	@Override public String getName() { return "Munition properties: " + this.modid; }

}
