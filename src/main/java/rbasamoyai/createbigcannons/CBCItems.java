package rbasamoyai.createbigcannons;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import rbasamoyai.createbigcannons.crafting.CannonCraftingWandItem;
import rbasamoyai.createbigcannons.datagen.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.manualloading.RamRodItem;
import rbasamoyai.createbigcannons.manualloading.WormItem;
import rbasamoyai.createbigcannons.munitions.fuzes.ImpactFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeItem;

public class CBCItems {

	private static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate()
			.creativeModeTab(() -> ModGroup.GROUP);
	
	static {
		REGISTRATE.startSection(AllSections.LOGISTICS);
	}
	
	public static final ItemEntry<ImpactFuzeItem> IMPACT_FUZE = REGISTRATE.item("impact_fuze", ImpactFuzeItem::new).register();
	public static final ItemEntry<TimedFuzeItem> TIMED_FUZE = REGISTRATE.item("timed_fuze", TimedFuzeItem::new).register();
	public static final ItemEntry<ProximityFuzeItem> PROXIMITY_FUZE = REGISTRATE.item("proximity_fuze", ProximityFuzeItem::new).register();
	
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
	
	public static final ItemEntry<Item> PACKED_GUNPOWDER = REGISTRATE.item("packed_gunpowder", Item::new).register();
	public static final ItemEntry<Item> EMPTY_POWDER_CHARGE = REGISTRATE.item("empty_powder_charge", Item::new).register();
	
	public static final ItemEntry<Item> CAST_IRON_INGOT = REGISTRATE.item("cast_iron_ingot", Item::new).tag(CBCTags.ItemCBC.INGOT_CAST_IRON).register();
	public static final ItemEntry<Item> CAST_IRON_NUGGET = REGISTRATE.item("cast_iron_nugget", Item::new).tag(CBCTags.ItemCBC.NUGGET_CAST_IRON).register();
	
	public static final ItemEntry<Item> NETHERSTEEL_INGOT = REGISTRATE.item("nethersteel_ingot", Item::new).register();
	public static final ItemEntry<Item> NETHERSTEEL_NUGGET = REGISTRATE.item("nethersteel_nugget", Item::new).register();
	
	public static final ItemEntry<Item> BRONZE_SCRAP = REGISTRATE.item("bronze_scrap", Item::new).tag(CBCTags.ItemCBC.NUGGET_BRONZE).register();
	public static final ItemEntry<Item> STEEL_SCRAP = REGISTRATE.item("steel_scrap", Item::new).tag(CBCTags.ItemCBC.NUGGET_STEEL).register();
	
	public static final ItemEntry<Item> SHOT_BALLS = REGISTRATE.item("shot_balls", Item::new).register();
	
	public static final ItemEntry<RamRodItem> RAM_ROD = REGISTRATE
			.item("ram_rod", RamRodItem::new)
			.properties(p -> p.stacksTo(1))
			.model((c, p) -> p.getExistingFile(c.getId()))
			.register();
	
	public static final ItemEntry<WormItem> WORM = REGISTRATE
			.item("worm", WormItem::new)
			.properties(p -> p.stacksTo(1))
			.model((c, p) -> p.getExistingFile(c.getId()))
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
