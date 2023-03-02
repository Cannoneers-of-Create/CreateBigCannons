package rbasamoyai.createbigcannons.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
        CreateBigCannons.REGISTRATE.setupDatagen(generator, helper);
        CreateBigCannonsFabric.gatherData(generator, helper);
    }
}
