package rbasamoyai.createbigcannons.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() { return CreateBigCannons.MOD_ID; }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        CBCPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        CBCPonderTags.register(helper);
    }
}
