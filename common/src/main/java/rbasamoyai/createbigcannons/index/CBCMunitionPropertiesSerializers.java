package rbasamoyai.createbigcannons.index;

import rbasamoyai.createbigcannons.munitions.BaseProjectileProperties;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.CommonShellBigCannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantProperties;
import rbasamoyai.createbigcannons.munitions.fragment_burst.ProjectileBurstProperties;

public class CBCMunitionPropertiesSerializers {

	public static final BaseProjectileProperties.Serializer BASE_PROJECTILE = new BaseProjectileProperties.Serializer();
	public static final BigCannonProjectileProperties.Serializer INERT_BIG_CANNON_PROJECTILE = new BigCannonProjectileProperties.Serializer();
	public static final CommonShellBigCannonProjectileProperties.Serializer COMMON_SHELL_BIG_CANNON_PROJECTILE = new CommonShellBigCannonProjectileProperties.Serializer();
	public static final AutocannonProjectileProperties.Serializer INERT_AUTOCANNON_PROJECTILE = new AutocannonProjectileProperties.Serializer();
	public static final ProjectileBurstProperties.Serializer PROJECTILE_BURST = new ProjectileBurstProperties.Serializer();

	public static final BigCannonPropellantProperties.Serializer BASE_BIG_CANNON_PROPELLANT = new BigCannonPropellantProperties.Serializer();

	public static void init() {}

}
