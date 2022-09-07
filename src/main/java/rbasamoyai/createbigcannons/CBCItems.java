package rbasamoyai.createbigcannons;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import rbasamoyai.createbigcannons.crafting.CannonCraftingWandItem;
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
	
	public static final ItemEntry<Item> STEEL_SCREW_LOCK = REGISTRATE
			.item("steel_screw_lock", Item::new)
			.transform(CBCBuilderTransformers.screwLock("screw_breech/steel"))
			.register();
	
	public static final ItemEntry<Item> NETHERSTEEL_SCREW_LOCK = REGISTRATE
			.item("nethersteel_screw_lock", Item::new)
			.transform(CBCBuilderTransformers.screwLock("screw_breech/nethersteel"))
			.register();
	
	static {
		REGISTRATE.startSection(AllSections.CURIOSITIES);
	}
	
	public static final ItemEntry<CannonCraftingWandItem> CANNON_CRAFTING_WAND = REGISTRATE
			.item("cannon_crafting_wand", CannonCraftingWandItem::new)
			.properties(p -> p.stacksTo(1))
			.properties(p -> p.rarity(Rarity.EPIC))
			.model((c, p) -> {})
			.register();
	
	public static void register() {}
	
}
