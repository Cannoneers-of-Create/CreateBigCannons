package rbasamoyai.createbigcannons.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> new BaseConfigScreen(parent, CreateBigCannons.MOD_ID);
	}
}
