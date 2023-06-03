package rbasamoyai.createbigcannons.multiloader;

import com.tterrag.registrate.AbstractRegistrate;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;

public class CBCLootTableProvider {
	@ExpectPlatform
	public static LootTableProvider create(AbstractRegistrate<?> parent, DataGenerator gen) {
		throw new AssertionError();
	}
}
