package rbasamoyai.createbigcannons;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rbasamoyai.createbigcannons.base.PartialBlockDamageManager;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.*;
import rbasamoyai.createbigcannons.network.CBCRootNetwork;

public class CreateBigCannons {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "createbigcannons";
	
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public static final PartialBlockDamageManager BLOCK_DAMAGE = new PartialBlockDamageManager();
	
	public static void init() {
		ModGroup.register();
		CBCBlocks.register();
		CBCItems.register();
		CBCBlockEntities.register();
		CBCEntityTypes.register();
		CBCMenuTypes.register();
		CBCFluids.register();
		CBCRecipeTypes.register();

		CBCContraptionTypes.prepare();
		CBCChecks.register();
		
		CBCParticleTypes.register();
		
		CBCTags.register();
		
		CBCConfigs.registerConfigs(mlContext);

		CBCRootNetwork.init();
	}
	
	public static ResourceLocation resource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
}
