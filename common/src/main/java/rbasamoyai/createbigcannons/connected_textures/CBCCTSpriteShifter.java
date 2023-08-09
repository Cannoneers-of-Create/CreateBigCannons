package rbasamoyai.createbigcannons.connected_textures;

import java.util.HashMap;
import java.util.Map;

import com.simibubi.create.foundation.block.connected.CTType;

import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;

public class CBCCTSpriteShifter {

	private static final Map<String, CBCCTSpriteShiftEntry> ENTRY_CACHE = new HashMap<>();

	public static CBCCTSpriteShiftEntry getCT(CTType type, int scale, ResourceLocation blockTexture, ResourceLocation connectedTexture) {
		String key = blockTexture + "->" + connectedTexture + "+" + type.getId();
		if (ENTRY_CACHE.containsKey(key)) return ENTRY_CACHE.get(key);
		CBCCTSpriteShiftEntry entry = new CBCCTSpriteShiftEntry(type, scale);
		EnvExecute.executeOnClient(() -> () -> entry.set(blockTexture, connectedTexture));
		ENTRY_CACHE.put(key, entry);
		return entry;
	}

}
