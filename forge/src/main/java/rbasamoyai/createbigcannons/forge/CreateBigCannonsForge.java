package rbasamoyai.createbigcannons.forge;

import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateBigCannons.MOD_ID)
public class CreateBigCannonsForge {
    public CreateBigCannonsForge() {
        // registrate must be given the mod event bus on forge before registration
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CBCBlocks.REGISTRATE.registerEventListeners(eventBus);
        CreateBigCannons.init();
    }
}
