package rbasamoyai.createbigcannons.config;

import com.google.common.eventbus.Subscribe;
import com.simibubi.create.foundation.block.BlockStressValues;
import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.api.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import rbasamoyai.createbigcannons.CreateBigCannons;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class CBCConfigs {

	/*
	 * A copy of AllConfigs. Yea, was a bit lazy in making config setup.
	 */
	
	private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);
	
	public static CBCCfgClient CLIENT;
	public static CBCCfgCommon COMMON;
	public static CBCCfgServer SERVER;
	
	public static ConfigBase byType(ModConfig.Type type) { return CONFIGS.get(type); }
	
	private static <T extends CBCConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
		Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
			T config = factory.get();
			config.registerAll(builder);
			return config;
		});

		T config = specPair.getLeft();
		config.specification = specPair.getRight();
		CONFIGS.put(side, config);
		return config;
	}
	
	public static void registerConfigs(ModLoadingContext context) {
		CLIENT = register(CBCCfgClient::new, ModConfig.Type.CLIENT);
		COMMON = register(CBCCfgCommon::new, ModConfig.Type.COMMON);
		SERVER = register(CBCCfgServer::new, ModConfig.Type.SERVER);
		
		for (Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
			context.registerConfig(pair.getKey(), pair.getValue().specification);
		
		BlockStressValues.registerProvider(CreateBigCannons.MOD_ID, SERVER.kinetics.stress);
	}
	
	@Subscribe
	public static void onLoad(ModConfigEvent.Loading event) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == event.getConfig()
				.getSpec())
				config.onLoad();
	}

	@Subscribe
	public static void onReload(ModConfigEvent.Reloading event) {
		for (ConfigBase config : CONFIGS.values())
			if (config.specification == event.getConfig()
				.getSpec())
				config.onReload();
	}

}