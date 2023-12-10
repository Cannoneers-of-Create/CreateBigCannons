package rbasamoyai.createbigcannons.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import rbasamoyai.createbigcannons.config.CBCConfigs;

public class CBCModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return CBCConfigs::createConfigScreen;
	}
}
