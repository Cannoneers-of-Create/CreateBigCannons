package rbasamoyai.createbigcannons;

import com.tterrag.registrate.util.entry.MenuEntry;

import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeContainer;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeScreen;

public class CBCMenuTypes {

	public static final MenuEntry<TimedFuzeContainer> SET_TIMED_FUZE = CreateBigCannons.registrate()
			.menu("set_timed_fuze", TimedFuzeContainer::getClientMenu, () -> TimedFuzeScreen::new)
			.register();
	
	public static void register() {}
	
}
