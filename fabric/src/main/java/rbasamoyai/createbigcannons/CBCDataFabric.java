package rbasamoyai.createbigcannons;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import rbasamoyai.createbigcannons.datagen.CBCDatagenRoot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class CBCDataFabric implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        Path cbcResources = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
        ExistingFileHelper helper = new ExistingFileHelper(
                Set.of(cbcResources), Set.of("create"), true, null, null
        );
        CBCDatagenRoot.register(generator, helper, true, true);
        CreateBigCannons.REGISTRATE.setupDatagen(generator, helper);
    }
}
