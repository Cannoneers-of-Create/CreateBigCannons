package rbasamoyai.createbigcannons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(CreateBigCannons.MOD_ID)
public class CreateBigCannons {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "createbigcannons";
	
	private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(MOD_ID);
	
	public CreateBigCannons() {
		LangGen.prepare();
		
		ModGroup.register();
		CBCBlocks.register();
		CBCBlockEntities.register();
	}
	
	public static CreateRegistrate registrate() {
		return REGISTRATE.get();
	}
	
	public static ResourceLocation resource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
}
