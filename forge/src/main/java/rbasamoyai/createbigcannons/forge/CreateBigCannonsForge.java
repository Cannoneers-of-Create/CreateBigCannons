package rbasamoyai.createbigcannons.forge;

import rbasamoyai.createbigcannons.CreateBigCannons;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateBigCannons.MOD_ID)
public class CreateBigCannonsForge {
    public CreateBigCannonsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CBCBlocks.REGISTRATE.registerEventListeners(eventBus);
        CreateBigCannons.init();
    }
}
