package rbasamoyai.createbigcannons.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.CBCDatagenRoot;

public class CBCDataFabric implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
        CBCDatagenRoot.register(generator, helper, true, true);
        CreateBigCannons.REGISTRATE.setupDatagen(generator, helper);
    }
}
