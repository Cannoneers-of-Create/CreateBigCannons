package rbasamoyai.createbigcannons;

import com.tterrag.registrate.util.entry.MenuEntry;

import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeContainer;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeScreen;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeContainer;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeScreen;

public class CBCMenuTypes {

	public static final MenuEntry<TimedFuzeContainer> SET_TIMED_FUZE = CreateBigCannons.registrate()
			.menu("set_timed_fuze", TimedFuzeContainer::getClientMenu, () -> TimedFuzeScreen::new)
			.register();
	
	public static final MenuEntry<ProximityFuzeContainer> SET_PROXIMITY_FUZE = CreateBigCannons.registrate()
			.menu("set_proximity_fuze", ProximityFuzeContainer::getClientMenu, () -> ProximityFuzeScreen::new)
			.register();
	
	public static void register() {}
	
}
