package rbasamoyai.createbigcannons.index;

import com.tterrag.registrate.util.entry.MenuEntry;
import rbasamoyai.createbigcannons.munitions.fuzes.*;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

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
	
	public static void register() {}
	
}
