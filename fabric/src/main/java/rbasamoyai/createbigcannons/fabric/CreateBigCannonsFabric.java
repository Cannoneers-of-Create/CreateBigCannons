package rbasamoyai.createbigcannons.fabric;

import io.github.fabricators_of_create.porting_lib.util.EnvExecutor;
import rbasamoyai.createbigcannons.CreateBigCannons;
import net.fabricmc.api.ModInitializer;

public class CreateBigCannonsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CreateBigCannons.init();
        CreateBigCannons.LOGGER.info(EnvExecutor.unsafeRunForDist(
                () -> () -> "{} is accessing Porting Lib on a Fabric client!",
                () -> () -> "{} is accessing Porting Lib on a Fabric server!"
                ), CreateBigCannons.NAME);
        CBCBlocks.REGISTRATE.register();
    }
}
