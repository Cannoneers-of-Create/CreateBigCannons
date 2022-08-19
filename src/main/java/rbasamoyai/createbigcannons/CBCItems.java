package rbasamoyai.createbigcannons;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import rbasamoyai.createbigcannons.crafting.casting.CastingWandItem;
import rbasamoyai.createbigcannons.datagen.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.munitions.fuzes.ImpactFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeItem;

public class CBCItems {

	private static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate()
			.creativeModeTab(() -> ModGroup.GROUP);
	
	static {
		REGISTRATE.startSection(AllSections.LOGISTICS);
	}
	
	public static final ItemEntry<ImpactFuzeItem> IMPACT_FUZE = REGISTRATE.item("impact_fuze", ImpactFuzeItem::new).register();
	public static final ItemEntry<TimedFuzeItem> TIMED_FUZE = REGISTRATE.item("timed_fuze", TimedFuzeItem::new).register();
	
	public static final ItemEntry<Item> CAST_IRON_SLIDING_BREECHBLOCK = REGISTRATE
			.item("cast_iron_sliding_breechblock", Item::new)
			.transform(CBCBuilderTransformers.slidingBreechblock("sliding_breech/cast_iron"))
			.register();
	
	public static final ItemEntry<Item> BRONZE_SLIDING_BREECHBLOCK = REGISTRATE
			.item("bronze_sliding_breechblock", Item::new)
			.transform(CBCBuilderTransformers.slidingBreechblock("sliding_breech/bronze"))
			.register();
	
	public static final ItemEntry<Item> STEEL_SLIDING_BREECHBLOCK = REGISTRATE
			.item("steel_sliding_breechblock", Item::new)
			.transform(CBCBuilderTransformers.slidingBreechblock("sliding_breech/steel"))
			.register();
	
	static {
		REGISTRATE.startSection(AllSections.CURIOSITIES);
	}
	
	public static final ItemEntry<CastingWandItem> CANNON_CAST_WAND = REGISTRATE
			.item("cannon_cast_wand", CastingWandItem::new)
			.properties(p -> p.stacksTo(1))
			.properties(p -> p.rarity(Rarity.EPIC))
			.model((c, p) -> {})
			.register();
	
	public static void register() {}
	
}
