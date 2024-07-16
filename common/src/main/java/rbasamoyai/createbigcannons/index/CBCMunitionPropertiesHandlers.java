package rbasamoyai.createbigcannons.index;

import rbasamoyai.createbigcannons.munitions.autocannon.config.InertAutocannonProjectilePropertiesHandler;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectilePropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonCommonShellPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.InertBigCannonProjectilePropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.drop_mortar_shell.DropMortarShellPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.grapeshot.GrapeshotBagPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.mortar_stone.MortarStonePropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.config.BigCartridgePropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.config.PowderChargePropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelShellPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeShellPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.fragment_burst.ProjectileBurstPropertiesHandler;

public class CBCMunitionPropertiesHandlers {

	public static final InertBigCannonProjectilePropertiesHandler INERT_BIG_CANNON_PROJECTILE = new InertBigCannonProjectilePropertiesHandler();
	public static final BigCannonCommonShellPropertiesHandler COMMON_SHELL_BIG_CANNON_PROJECTILE = new BigCannonCommonShellPropertiesHandler();
	public static final ShrapnelShellPropertiesHandler SHRAPNEL_SHELL = new ShrapnelShellPropertiesHandler();
	public static final GrapeshotBagPropertiesHandler BAG_OF_GRAPESHOT = new GrapeshotBagPropertiesHandler();
	public static final FluidShellPropertiesHandler FLUID_SHELL = new FluidShellPropertiesHandler();
	public static final SmokeShellPropertiesHandler SMOKE_SHELL = new SmokeShellPropertiesHandler();
	public static final MortarStonePropertiesHandler MORTAR_STONE = new MortarStonePropertiesHandler();
	public static final DropMortarShellPropertiesHandler DROP_MORTAR_SHELL = new DropMortarShellPropertiesHandler();
	public static final InertAutocannonProjectilePropertiesHandler INERT_AUTOCANNON_PROJECTILE = new InertAutocannonProjectilePropertiesHandler();
	public static final FlakAutocannonProjectilePropertiesHandler FLAK_AUTOCANNON = new FlakAutocannonProjectilePropertiesHandler();
	public static final ProjectileBurstPropertiesHandler PROJECTILE_BURST = new ProjectileBurstPropertiesHandler();

	public static final PowderChargePropertiesHandler POWDER_CHARGE = new PowderChargePropertiesHandler();
	public static final BigCartridgePropertiesHandler BIG_CARTRIDGE = new BigCartridgePropertiesHandler();

	public static void init() {}

}
