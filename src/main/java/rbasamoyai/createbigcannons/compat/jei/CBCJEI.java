package rbasamoyai.createbigcannons.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

@JeiPlugin
public class CBCJEI implements IModPlugin {

	private static final ResourceLocation PLUGIN_ID = CreateBigCannons.resource("jei_plugin");	
	@Override public ResourceLocation getPluginUid() { return PLUGIN_ID; }
	
}
