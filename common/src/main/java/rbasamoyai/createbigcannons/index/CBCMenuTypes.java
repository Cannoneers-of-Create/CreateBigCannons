package rbasamoyai.createbigcannons.index;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import com.tterrag.registrate.util.entry.MenuEntry;

import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerMenu;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerScreen;
import rbasamoyai.createbigcannons.munitions.fuzes.DelayedImpactFuzeContainer;
import rbasamoyai.createbigcannons.munitions.fuzes.DelayedImpactFuzeScreen;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeContainer;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeScreen;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeContainer;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeScreen;

public class CBCMenuTypes {

	public static final MenuEntry<TimedFuzeContainer> SET_TIMED_FUZE = REGISTRATE
		.menu("set_timed_fuze", TimedFuzeContainer::getClientMenu, () -> TimedFuzeScreen::new)
		.register();

	public static final MenuEntry<ProximityFuzeContainer> SET_PROXIMITY_FUZE = REGISTRATE
		.menu("set_proximity_fuze", ProximityFuzeContainer::getClientMenu, () -> ProximityFuzeScreen::new)
		.register();

	public static final MenuEntry<DelayedImpactFuzeContainer> SET_DELAYED_IMPACT_FUZE = REGISTRATE
		.menu("set_delayed_fuze", DelayedImpactFuzeContainer::getClientMenu, () -> DelayedImpactFuzeScreen::new)
		.register();

	public static final MenuEntry<AutocannonAmmoContainerMenu> AUTOCANNON_AMMO_CONTAINER = REGISTRATE
		.menu("autocannon_ammo_container", AutocannonAmmoContainerMenu::getClientMenu, () -> AutocannonAmmoContainerScreen::new)
		.register();

	public static void register() {
	}

}
