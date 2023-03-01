package rbasamoyai.createbigcannons;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateBigCannons {
    public static final String MOD_ID = "createbigcannons";
    public static final String NAME = "Example Mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static void init() {
        CBCBlocks.init();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
