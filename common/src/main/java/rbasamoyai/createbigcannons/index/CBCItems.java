package rbasamoyai.createbigcannons.index;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.ModGroup;
import rbasamoyai.createbigcannons.crafting.welding.CannonWelderItem;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringMechanismItem;
import rbasamoyai.createbigcannons.crafting.CannonCraftingWandItem;
import rbasamoyai.createbigcannons.datagen.assets.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.manual_loading.RamRodItem;
import rbasamoyai.createbigcannons.manual_loading.WormItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonCartridgeItem;
import rbasamoyai.createbigcannons.munitions.autocannon.ap_round.APAutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.autocannon.bullet.MachineGunRoundItem;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.config.InspectResistanceToolItem;
import rbasamoyai.createbigcannons.munitions.fuzes.DelayedImpactFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.ImpactFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.ProximityFuzeItem;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeItem;

public class CBCItems {

	static {
		REGISTRATE.creativeModeTab(() -> ModGroup.GROUP);
	}

	public static final ItemEntry<ImpactFuzeItem> IMPACT_FUZE = REGISTRATE.item("impact_fuze", ImpactFuzeItem::new)
		.tag(modTag("fuzes"))
		.register();
	public static final ItemEntry<TimedFuzeItem> TIMED_FUZE = REGISTRATE.item("timed_fuze", TimedFuzeItem::new)
		.tag(modTag("fuzes"))
		.register();
	public static final ItemEntry<ProximityFuzeItem> PROXIMITY_FUZE = REGISTRATE.item("proximity_fuze", ProximityFuzeItem::new)
		.tag(modTag("fuzes"))
		.register();
	public static final ItemEntry<DelayedImpactFuzeItem> DELAYED_IMPACT_FUZE = REGISTRATE.item("delayed_impact_fuze", DelayedImpactFuzeItem::new)
		.tag(modTag("fuzes"))
		.register();

	public static final ItemEntry<Item>
		CAST_IRON_SLIDING_BREECHBLOCK = REGISTRATE.item("cast_iron_sliding_breechblock", Item::new)
		.transform(CBCBuilderTransformers.slidingBreechblock("sliding_breech/cast_iron"))
		.register(),

	BRONZE_SLIDING_BREECHBLOCK = REGISTRATE.item("bronze_sliding_breechblock", Item::new)
		.transform(CBCBuilderTransformers.slidingBreechblock("sliding_breech/bronze"))
		.register(),

	STEEL_SLIDING_BREECHBLOCK = REGISTRATE.item("steel_sliding_breechblock", Item::new)
		.transform(CBCBuilderTransformers.slidingBreechblock("sliding_breech/steel"))
		.register(),

	STEEL_SCREW_LOCK = REGISTRATE.item("steel_screw_lock", Item::new)
		.transform(CBCBuilderTransformers.screwLock("screw_breech/steel"))
		.register(),

	NETHERSTEEL_SCREW_LOCK = REGISTRATE.item("nethersteel_screw_lock", Item::new)
		.transform(CBCBuilderTransformers.screwLock("screw_breech/nethersteel"))
		.register(),

	SPRING_WIRE = REGISTRATE.item("spring_wire", Item::new).register(),
		RECOIL_SPRING = REGISTRATE.item("recoil_spring", Item::new).register(),

	CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE.item("cast_iron_autocannon_breech_extractor", Item::new)
		.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/cast_iron"))
		.register(),

	BRONZE_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE.item("bronze_autocannon_breech_extractor", Item::new)
		.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/bronze"))
		.register(),

	STEEL_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE.item("steel_autocannon_breech_extractor", Item::new)
		.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/steel"))
		.register(),

	PACKED_GUNPOWDER = REGISTRATE.item("packed_gunpowder", Item::new).register(),
		EMPTY_POWDER_CHARGE = REGISTRATE.item("empty_powder_charge", Item::new).register(),
		CAST_IRON_INGOT = REGISTRATE.item("cast_iron_ingot", Item::new)
			.tag(forgeTag("ingots/cast_iron"))
			.tag(fabricTag("ingots/cast_iron"))
			.tag(fabricTag("cast_iron_ingots"))
			.register(),
		CAST_IRON_NUGGET = REGISTRATE.item("cast_iron_nugget", Item::new)
			.tag(forgeTag("nuggets/cast_iron"))
			.tag(fabricTag("nuggets/cast_iron"))
			.tag(fabricTag("cast_iron_nuggets"))
			.register(),
		NETHERSTEEL_INGOT = REGISTRATE.item("nethersteel_ingot", Item::new).register(),
		NETHERSTEEL_NUGGET = REGISTRATE.item("nethersteel_nugget", Item::new).register(),
		BRONZE_SCRAP = REGISTRATE.item("bronze_scrap", Item::new)
			.tag(forgeTag("nuggets/bronze"))
			.tag(fabricTag("nuggets/bronze"))
			.tag(fabricTag("bronze_nuggets"))
			.register(),
		STEEL_SCRAP = REGISTRATE.item("steel_scrap", Item::new)
			.tag(forgeTag("nuggets/steel"))
			.tag(fabricTag("nuggets/steel"))
			.tag(fabricTag("steel_nuggets"))
			.register(),
		SHOT_BALLS = REGISTRATE.item("shot_balls", Item::new).register(),
		AUTOCANNON_CARTRIDGE_SHEET = REGISTRATE.item("autocannon_cartridge_sheet", Item::new).register(),

	EMPTY_AUTOCANNON_CARTRIDGE = REGISTRATE.item("empty_autocannon_cartridge", Item::new)
		.tag(modTag("spent_autocannon_casings"))
		.model((c, p) -> {})
		.register(),

	FILLED_AUTOCANNON_CARTRIDGE = REGISTRATE
		.item("filled_autocannon_cartridge", Item::new)
		.model((c, p) -> {})
		.register(),

	PAIR_OF_CANNON_WHEELS = REGISTRATE
		.item("pair_of_cannon_wheels", Item::new)
		.lang("Pair of Cannon Wheels")
		.register(),

	BIG_CARTRIDGE_SHEET = REGISTRATE.item("big_cartridge_sheet", Item::new)
		.model((c, p) -> {})
		.register(),

	CONGEALED_NITRO = REGISTRATE.item("congealed_nitro", Item::new).register(),
	HARDENED_NITRO = REGISTRATE.item("hardened_nitro", Item::new).register(),
	NITROPOWDER = REGISTRATE.item("nitropowder", Item::new).tag(CBCTags.CBCItemTags.NITROPOWDER).register(),

	EMPTY_MACHINE_GUN_ROUND = REGISTRATE.item("empty_machine_gun_round", Item::new)
		.tag(modTag("spent_autocannon_casings"))
		.register(),

	TRACER_TIP = REGISTRATE.item("tracer_tip", Item::new).register();

	public static final ItemEntry<SequencedAssemblyItem>
		PARTIAL_RECOIL_SPRING = REGISTRATE.item("partial_recoil_spring", SequencedAssemblyItem::new).register(),
		PARTIAL_CAST_IRON_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE.item("partial_cast_iron_autocannon_breech_extractor", SequencedAssemblyItem::new)
			.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/cast_iron"))
			.register(),
		PARTIAL_BRONZE_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE.item("partial_bronze_autocannon_breech_extractor", SequencedAssemblyItem::new)
			.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/bronze"))
			.register(),
		PARTIAL_STEEL_AUTOCANNON_BREECH_EXTRACTOR = REGISTRATE.item("partial_steel_autocannon_breech_extractor", SequencedAssemblyItem::new)
			.transform(CBCBuilderTransformers.autocannonBreechExtractor("autocannon/steel"))
			.register(),

	PARTIALLY_FORMED_AUTOCANNON_CARTRIDGE = REGISTRATE.item("partially_formed_autocannon_cartridge", SequencedAssemblyItem::new)
		.model((c, p) -> {})
		.register(),

	PARTIALLY_FILLED_AUTOCANNON_CARTRIDGE = REGISTRATE.item("partially_filled_autocannon_cartridge", SequencedAssemblyItem::new)
		.model((c, p) -> p.withExistingParent(c.getName(), CreateBigCannons.resource("item/filled_autocannon_cartridge")))
		.register(),

	PARTIALLY_FORMED_BIG_CARTRIDGE = REGISTRATE.item("partially_formed_big_cartridge", SequencedAssemblyItem::new)
		.model((c, p) -> {})
		.register(),

	PARTIALLY_ASSEMBLED_MACHINE_GUN_ROUND = REGISTRATE.item("partially_assembled_machine_gun_round", SequencedAssemblyItem::new)
		.model((c, p) -> p.withExistingParent(c.getName(), CreateBigCannons.resource("item/empty_machine_gun_round")))
		.register();

	public static final ItemEntry<AutocannonCartridgeItem> AUTOCANNON_CARTRIDGE = REGISTRATE
		.item("autocannon_cartridge", AutocannonCartridgeItem::new)
		.tag(modTag("autocannon_cartridges"))
		.model((c, p) -> {})
		.register();

	public static final ItemEntry<APAutocannonRoundItem> AP_AUTOCANNON_ROUND = REGISTRATE
		.item("ap_autocannon_round", APAutocannonRoundItem::new)
		.lang("Armor Piercing (AP) Autocannon Round")
		.tag(modTag("autocannon_rounds"))
		.register();

	public static final ItemEntry<FlakAutocannonRoundItem> FLAK_AUTOCANNON_ROUND = REGISTRATE
		.item("flak_autocannon_round", FlakAutocannonRoundItem::new)
		.tag(modTag("autocannon_rounds"))
		.register();

	public static final ItemEntry<MachineGunRoundItem> MACHINE_GUN_ROUND = REGISTRATE
		.item("machine_gun_round", MachineGunRoundItem::new)
		.tag(modTag("autocannon_cartridges"))
		.register();

	public static final ItemEntry<AutocannonAmmoContainerItem> AUTOCANNON_AMMO_CONTAINER = REGISTRATE
		.item("autocannon_ammo_container", AutocannonAmmoContainerItem::new)
		.properties(p -> p.stacksTo(1))
		.tag(modTag("autocannon_ammo_containers"))
		.model((c, p) -> {})
		.register();

	public static final ItemEntry<RamRodItem> RAM_ROD = REGISTRATE
		.item("ram_rod", RamRodItem::new)
		.properties(p -> p.stacksTo(1))
		.model((c, p) -> {})
		.register();

	public static final ItemEntry<WormItem> WORM = REGISTRATE
		.item("worm", WormItem::new)
		.properties(p -> p.stacksTo(1))
		.model((c, p) -> {})
		.register();

	public static final ItemEntry<QuickfiringMechanismItem> QUICKFIRING_MECHANISM = REGISTRATE
		.item("quickfiring_mechanism", QuickfiringMechanismItem::new)
		.lang("Quick-Firing Mechanism")
		.register();

	public static final ItemEntry<CannonCraftingWandItem> CANNON_CRAFTING_WAND = REGISTRATE
		.item("cannon_crafting_wand", CannonCraftingWandItem::new)
		.properties(p -> p.stacksTo(1))
		.properties(p -> p.rarity(Rarity.EPIC))
		.model((c, p) -> {})
		.register();

	public static final ItemEntry<CannonWelderItem> CANNON_WELDER = REGISTRATE
		.item("cannon_welder", CannonWelderItem::new)
		.properties(p -> p.durability(64))
		.model((c, p) -> {})
		.register();

	public static final ItemEntry<InspectResistanceToolItem> RESISTANCE_INSPECTION_TOOL = REGISTRATE
		.item("resistance_inspection_tool", InspectResistanceToolItem::new)
		.properties(p -> p.stacksTo(1))
		.properties(p -> p.rarity(Rarity.EPIC))
		.register();

	public static void register() {
	}

	public static TagKey<Item> tag(ResourceLocation loc) { return TagKey.create(Registry.ITEM_REGISTRY, loc); }
	private static TagKey<Item> forgeTag(String loc) { return tag(new ResourceLocation("forge", loc)); }
	private static TagKey<Item> fabricTag(String loc) { return tag(new ResourceLocation("c", loc)); }
	public static TagKey<Item> modTag(String loc) { return tag(CreateBigCannons.resource(loc)); }

}
