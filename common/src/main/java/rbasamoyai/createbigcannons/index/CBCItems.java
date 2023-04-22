package rbasamoyai.createbigcannons.index;

import com.simibubi.create.content.AllSections;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.ModGroup;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringMechanismItem;
import rbasamoyai.createbigcannons.crafting.CannonCraftingWandItem;
import rbasamoyai.createbigcannons.datagen.assets.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.manualloading.RamRodItem;
import rbasamoyai.createbigcannons.manualloading.WormItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.autocannon.ap_round.APAutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.config.InspectResistanceToolItem;
import rbasamoyai.createbigcannons.munitions.fuzes.ImpactFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeItem;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

public class CBCItems {

	static {
		REGISTRATE.creativeModeTab(() -> ModGroup.GROUP);
	}
	
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

	public static final ItemEntry<Item> SPRING_WIRE = REGISTRATE.item("spring_wire", Item::new).register();
	public static final ItemEntry<SequencedAssemblyItem> PARTIAL_RECOIL_SPRING = REGISTRATE.item("partial_recoil_spring", SequencedAssemblyItem::new).register();
	public static final ItemEntry<Item> RECOIL_SPRING = REGISTRATE.item("recoil_spring", Item::new).register();

	public static final ItemEntry<Item> CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE
			.item("cast_iron_autocannon_breech_extractor", Item::new)
			.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/cast_iron"))
			.register();
	public static final ItemEntry<SequencedAssemblyItem> PARTIAL_CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE
			.item("partial_cast_iron_autocannon_breech_extractor", SequencedAssemblyItem::new)
			.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/cast_iron"))
			.register();

	public static final ItemEntry<Item> BRONZE_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE
			.item("bronze_autocannon_breech_extractor", Item::new)
			.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/bronze"))
			.register();
	public static final ItemEntry<SequencedAssemblyItem> PARTIAL_BRONZE_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE
			.item("partial_bronze_autocannon_breech_extractor", SequencedAssemblyItem::new)
			.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/bronze"))
			.register();

	public static final ItemEntry<Item> STEEL_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE
			.item("steel_autocannon_breech_extractor", Item::new)
			.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/steel"))
			.register();
	public static final ItemEntry<SequencedAssemblyItem> PARTIAL_STEEL_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE
			.item("partial_steel_autocannon_breech_extractor", SequencedAssemblyItem::new)
			.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/steel"))
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

	public static final ItemEntry<AutocannonCartridgeItem> AUTOCANNON_CARTRIDGE = REGISTRATE.item("autocannon_cartridge", AutocannonCartridgeItem::new)
			.model((c, p) -> p.getExistingFile(CreateBigCannons.resource("item/autocannon_cartridge")))
			.register();

	public static final ItemEntry<Item> AUTOCANNON_CARTRIDGE_SHEET = REGISTRATE.item("autocannon_cartridge_sheet", Item::new).register();
	
	public static final ItemEntry<SequencedAssemblyItem> PARTIALLY_FORMED_AUTOCANNON_CARTRIDGE = REGISTRATE
			.item("partially_formed_autocannon_cartridge", SequencedAssemblyItem::new)
			.model((c, p) -> p.getExistingFile(CreateBigCannons.resource("item/partially_formed_autocannon_cartridge")))
			.register();

	public static final ItemEntry<Item> EMPTY_AUTOCANNON_CARTRIDGE = REGISTRATE
			.item("empty_autocannon_cartridge", Item::new)
			.model((c, p) -> p.getExistingFile(CreateBigCannons.resource("item/empty_autocannon_cartridge")))
			.register();

	public static final ItemEntry<SequencedAssemblyItem> PARTIALLY_FILLED_AUTOCANNON_CARTRIDGE = REGISTRATE
			.item("partially_filled_autocannon_cartridge", SequencedAssemblyItem::new)
			.model((c, p) -> p.withExistingParent(c.getName(), CreateBigCannons.resource("item/filled_autocannon_cartridge")))
			.register();

	public static final ItemEntry<Item> FILLED_AUTOCANNON_CARTRIDGE = REGISTRATE
			.item("filled_autocannon_cartridge", Item::new)
			.model((c, p) -> p.getExistingFile(CreateBigCannons.resource("item/filled_autocannon_cartridge")))
			.register();

	public static final ItemEntry<APAutocannonRoundItem> AP_AUTOCANNON_ROUND = REGISTRATE
			.item("ap_autocannon_round", APAutocannonRoundItem::new)
			.lang("Armor Piercing (AP) Autocannon Round")
			.register();

	public static final ItemEntry<FlakAutocannonRoundItem> FLAK_AUTOCANNON_ROUND = REGISTRATE
			.item("flak_autocannon_round", FlakAutocannonRoundItem::new)
			.register();

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

	public static final ItemEntry<Item> PAIR_OF_CANNON_WHEELS = REGISTRATE
			.item("pair_of_cannon_wheels", Item::new)
			.lang("Pair of Cannon Wheels")
			.register();

	public static final ItemEntry<Item> BIG_CANNON_SHEET = REGISTRATE.item("big_cartridge_sheet", Item::new)
			.model((c, p) -> p.getExistingFile(CreateBigCannons.resource("item/big_cartridge_sheet")))
			.register();

	public static final ItemEntry<SequencedAssemblyItem> PARTIALLY_FORMED_BIG_CARTRIDGE = REGISTRATE
			.item("partially_formed_big_cartridge", SequencedAssemblyItem::new)
			.model((c, p) -> p.getExistingFile(CreateBigCannons.resource("item/partially_formed_big_cartridge")))
			.register();

	public static final ItemEntry<Item>
			CONGEALED_NITRO = REGISTRATE.item("congealed_nitro", Item::new).register(),
			HARDENED_NITRO = REGISTRATE.item("hardened_nitro", Item::new).register(),
			NITROPOWDER = REGISTRATE.item("nitropowder", Item::new).tag(CBCTags.ItemCBC.NITROPOWDER).register();

	public static final ItemEntry<QuickfiringMechanismItem> QUICKFIRING_MECHANISM = REGISTRATE
			.item("quickfiring_mechanism", QuickfiringMechanismItem::new).register();
	
	static {
		REGISTRATE.startSection(AllSections.CURIOSITIES);
	}
	
	public static final ItemEntry<CannonCraftingWandItem> CANNON_CRAFTING_WAND = REGISTRATE
			.item("cannon_crafting_wand", CannonCraftingWandItem::new)
			.properties(p -> p.stacksTo(1))
			.properties(p -> p.rarity(Rarity.EPIC))
			.model((c, p) -> {})
			.register();

	public static final ItemEntry<InspectResistanceToolItem> RESISTANCE_INSPECTION_TOOL = REGISTRATE
			.item("resistance_inspection_tool", InspectResistanceToolItem::new)
			.properties(p -> p.stacksTo(1))
			.properties(p -> p.rarity(Rarity.EPIC))
			.register();
	
	public static void register() {}
	
}
