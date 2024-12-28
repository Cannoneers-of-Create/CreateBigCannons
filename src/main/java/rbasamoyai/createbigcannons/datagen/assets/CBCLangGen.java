package rbasamoyai.createbigcannons.datagen.assets;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;
import static rbasamoyai.createbigcannons.index.CBCItems.tag;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.utils.CBCUtils;

public class CBCLangGen {

	public static void prepare() {
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonBlockOutsideOfWorld", "Cannon assembly area at [%s, %s, %s] is out of bounds");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonTooLarge", "Cannon is longer than the maximum length of %s");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "invalidCannon", "Cannon must have exactly one opening and exactly one closed end");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonLoaderInsideDuringAssembly", "Cannon block at [%s, %s, %s] contains a cannon loader part");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "hasIncompleteCannonBlocks", "Cannon block at [%s, %s, %s] has not finished the crafting process");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "noAutocannonBreech", "This cannon requires an autocannon breech to fire");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "incorrectBreechDirection", "Cannon block at [%s, %s, %s] should be reversed");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".fixed_cannon_mount.angle_pitch", "Pitch Adjustment");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".fixed_cannon_mount.angle_yaw", "Yaw Adjustment");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("casting"), "Cannon cast at [%s, %s, %s] does not have a valid recipe for fluid %s and shape %s");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("shrapnel"), "%s was ripped up by shrapnel");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("grapeshot"), "%s was blown out by grapeshot");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("flak"), "%s was downed by flak");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("cannon_projectile"), "%s was hit with artillery fire");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("big_cannon_projectile"), "%s was directly killed by a large caliber round");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("machine_gun_fire"), "%s was punctured by machine gun fire");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("machine_gun_fire"), "in_water", "%s was filled with holes by machine gun fire and the sea round them turned pink");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("traffic_cone"), "%s was struck from heaven above");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("molten_metal"), "%s burned in molten metal");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("molten_metal"), "player", "%s burned in molten metal");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".gui.set_timed_fuze.time", "Fuze Time: %ss %s ticks");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".gui.set_proximity_fuze.distance", "Detonation Distance: %s blocks");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".gui.autocannon_ammo_container.tracer_spacing", "Tracer Spacing: Every %s round(s)");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".gui.autocannon_ammo_container.tracer_slot", "Tracer/Secondary Rounds");

		tooltip(CBCBlocks.SOLID_SHOT)
		.header("SOLID SHOT")
		.summary("_High penetrating force._ Best suited for _soft targets_ such as _wooden structures and thin walls_. _Cannot be fuzed and detonated._");

		tooltip(CBCBlocks.MORTAR_STONE)
		.header("MORTAR STONE")
		.summary("Powerful stone that _explodes on impact._ _Flies further_ than other projectiles. Good for attacking _walls and fortifications._ _Cannot be fuzed and detonated._ Will _break_ if the propellant is too strong.");

		REGISTRATE.addLang("block", CBCBlocks.MORTAR_STONE.getId(), "tooltip.maximumCharges", "Maximum Firing Speed");
		REGISTRATE.addLang("block", CBCBlocks.MORTAR_STONE.getId(), "tooltip.maximumCharges.value", "This mortar stone can be fired at a speed of _%s m/s_ before breaking.");

		tooltip(CBCBlocks.BAG_OF_GRAPESHOT)
		.header("BAG OF GRAPESHOT")
		.summary("Contains _grapeshot_, which can damage soft blocks such as _wood_.")
		.conditionAndBehavior("On Firing", "Releases grapeshot. _Disintegrates upon firing._");

		tooltip(CBCBlocks.HE_SHELL)
		.header("HIGH EXPLOSIVE (HE) SHELL")
		.summary("Delivers _explosive_ force to the battlefield.")
		.conditionAndBehavior("On Detonation", "Explodes with a bit more power than _TNT_.");

		tooltip(CBCBlocks.SHRAPNEL_SHELL)
		.header("SHRAPNEL SHELL")
		.summary("Peppers the battlefield with _shrapnel bullets_ when detonated.")
		.conditionAndBehavior("On Detonation", "Releases _shrapnel_ in its direction. The shrapnel _spreads out over a wide area._");

		tooltip(CBCBlocks.AP_SHOT)
		.header("ARMOR PIERCING (AP) SHOT")
		.summary("Can effectively _pierce through blocks_. Good against _armored targets_. _Cannot be fuzed and detonated._");

		tooltip(CBCBlocks.AP_SHELL)
		.header("ARMOR PIERCING (AP) SHELL")
		.summary("Can effectively _pierce through blocks_, detonating inside protected structures.")
		.conditionAndBehavior("On Detonation", "Explodes.");

		tooltip(CBCBlocks.FLUID_SHELL)
		.header("FLUID SHELL")
		.summary("Spreads the contained fluid all over the targeted area, with _different effects depending on the fluid_.")
		.conditionAndBehavior("On Detonation", "Releases its contents.")
		.conditionAndBehavior("Filling", "The shell can only be filled through _the same face that the fuze is placed on._ If a fuze is present, the shell _cannot be filled._")
		.conditionAndBehavior("Note on Fluid Behavior", "Some fluids may not have any effect on release. Supported fluids include, but may not be limited to, _water, lava, and liquid potions._");

		tooltip(CBCBlocks.SMOKE_SHELL)
		.header("SMOKE SHELL")
		.summary("Covers the battlefield with a _smoke cloud_ that obscures vision.")
		.conditionAndBehavior("On Detonation", "Releases a _temporary smoke cloud_.");

		tooltip(CBCBlocks.DROP_MORTAR_SHELL)
		.header("DROP MORTAR SHELL")
		.summary("Light anti-entity _explosive_ shell that deals a bit of structural damage. Fired by _dropping into a drop mortar-type_ big cannon, although it can also be _conventionally fired._")
		.conditionAndBehavior("On Detonation", "Explodes.");

		tooltip(CBCItems.IMPACT_FUZE)
		.header("IMPACT FUZE")
		.summary("Detonates when the projectile _hits something_. Due to its _simplicity_, it does not always detonate on impact. This must be mounted on the _front_ of a shell; it will not work as a _base fuze._")
		.conditionAndBehavior("Detonation", "The fuze _may_ detonate on _projectile impact_.");

		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.chance", "Impact Chance");
		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.chance.value", "Upon impact this fuze has a _%s%%_ chance to detonate.");
		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.shell_info.chance", "Impact Chance: _%s%%_");
		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.durability", "Durability");
		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.durability.value", "This fuze can break through _%s_ blocks before breaking.");

		tooltip(CBCItems.TIMED_FUZE)
		.header("TIMED FUZE")
		.summary("Detonates after a _set time_ from launch.")
		.conditionAndBehavior("When R-Clicked", "Opens the _Set Timed Fuze_ menu, where the fuze duration can be set.")
		.conditionAndBehavior("Detonation", "The fuze detonates after the projectile has been in the world for the set time.");

		REGISTRATE.addLang("item", CBCItems.TIMED_FUZE.getId(), "tooltip.shell_info", "Time to Detonate: _%ss %s ticks_");
		REGISTRATE.addLang("item", CBCItems.TIMED_FUZE.getId(), "tooltip.shell_info.item", "Time to Detonate: %ss %s ticks");

		tooltip(CBCItems.DELAYED_IMPACT_FUZE)
		.header("DELAYED IMPACT FUZE")
		.summary("Detonates a _short time_ after _hitting_ something. Due to its _simple trigger mechanism_, it does not always trigger the timer. This must be mounted on the _front_ of a shell; it will not work as a _base fuze._")
		.conditionAndBehavior("When R-Clicked", "Opens the _Set Delayed Impact Fuze_ menu, where the fuze duration can be set.")
		.conditionAndBehavior("Detonation", "The fuze detonates after the set time from when the projectile impacts.");

		REGISTRATE.addLang("item", CBCItems.DELAYED_IMPACT_FUZE.getId(), "tooltip.chance", "Impact Chance");
		REGISTRATE.addLang("item", CBCItems.DELAYED_IMPACT_FUZE.getId(), "tooltip.chance.value", "Upon impact this fuze has a _%s%%_ chance to start ticking.");
		REGISTRATE.addLang("item", CBCItems.DELAYED_IMPACT_FUZE.getId(), "tooltip.shell_info.chance", "Impact Chance: _%s%%_");
		REGISTRATE.addLang("item", CBCItems.DELAYED_IMPACT_FUZE.getId(), "tooltip.durability", "Durability");
		REGISTRATE.addLang("item", CBCItems.DELAYED_IMPACT_FUZE.getId(), "tooltip.durability.value", "This fuze can break through _%s_ blocks before breaking.");

		tooltip(CBCItems.PROXIMITY_FUZE)
		.header("PROXIMITY FUZE")
		.summary("Detonates when it _gets close_ to a block. This must be mounted on the _front_ of a shell; it will not work as a _base fuze._")
		.conditionAndBehavior("When R-Clicked", "Opens the _Set Proximity Fuze_ menu, where the detonation distance can be set.")
		.conditionAndBehavior("Detonation", "The fuze detonates after the projectile gets is within the set range of a block.");

		REGISTRATE.addLang("item", CBCItems.PROXIMITY_FUZE.getId(), "tooltip.shell_info", "Detonation Distance: _%s blocks_");
		REGISTRATE.addLang("item", CBCItems.PROXIMITY_FUZE.getId(), "tooltip.shell_info.item", "Detonation Distance: %s blocks");

		tooltip(CBCItems.WIRED_FUZE)
		.header("WIRED FUZE")
		.summary("Detonates when the fuzed shell block is _powered by redstone._ Can be used for _improvised explosives._ _Does not explode in flight._")
		.conditionAndBehavior("Detonation", "The fuze detonates when powered by redstone.");

		tooltip(CBCItems.CANNON_CRAFTING_WAND)
		.header("CANNON CRAFTING WAND")
		.summary("Use on various cannon crafting processes such as _casting, boring, assembly,_ and _heating_ to _instantly finish_ the process.");

		tooltip(CBCItems.RAM_ROD)
		.header("RAM ROD")
		.summary("Used for _manually loading a cannon_ instead of using the Cannon Loader contraption. _Consumes saturation and hunger points_, with more for every block pushed.")
		.controlAndAction("R-Click on Cannon Block", "Pushes munition blocks further into the cannon. Can push munition blocks out if applicable.")
		.controlAndAction("R-Click on a Munition Block", "Pushes munition blocks. A string of munition blocks _must be connected to a valid cannon block_ to be pushed.");

		tooltip(CBCBlocks.CANNON_CARRIAGE)
		.header("CANNON_CARRIAGE")
		.summary("_Mobile_ cannon mount. Can be _moved around_, but _cannot be automated_ like the Cannon Mount.")
		.conditionAndBehavior("When R-Clicked with Wrench", "Assembles or disassembles the carriage. The carriage does not need to have a cannon mounted.")
		.controlAndAction("When Mouse Wheel scrolled", "Changes the rate of fire of a mounted autocannon if present.");

		REGISTRATE.addLang("block", CBCBlocks.CANNON_CARRIAGE.getId(), "tooltip.keyPressed", "When [_%s_] held down");
		REGISTRATE.addLang("block", CBCBlocks.CANNON_CARRIAGE.getId(), "tooltip.fireCannon", "_Fires cannon_ on carriage if present. _Repeatedly fires cannon_ if possible.");
		REGISTRATE.addLang("block", CBCBlocks.CANNON_CARRIAGE.getId(), "tooltip.pitchMode", "The forward/backward keys _set pitch_ instead of moving the cannon.");
		REGISTRATE.addLang("block", CBCBlocks.CANNON_CARRIAGE.getId(), "hotbar.fireRate", "Rate of fire: %s RPM");
		REGISTRATE.addLang("block", CBCBlocks.CANNON_CARRIAGE.getId(), "hotbar.fireRate.createbigcannons.cannon_mount", "Rate of fire: %s RPM (set signal strength on firing side to change)");
		REGISTRATE.addLang("block", CBCBlocks.CANNON_CARRIAGE.getId(), "hotbar.fireRate.createbigcannons.cannon_carriage", "Rate of fire: %s RPM (scroll to change)");

		REGISTRATE.addLang("item", CBCItems.RAM_ROD.getId(), "tooltip.pushStrength", "Push Strength");
		REGISTRATE.addLang("item", CBCItems.RAM_ROD.getId(), "tooltip.pushStrength.value", "Up to _%s blocks_");
		REGISTRATE.addLang("item", CBCItems.RAM_ROD.getId(), "tooltip.reach", "Reach");
		REGISTRATE.addLang("item", CBCItems.RAM_ROD.getId(), "tooltip.reach.value", "Up to _%s blocks_ inside a cannon");
		REGISTRATE.addLang("item", CBCItems.RAM_ROD.getId(), "tooltip.deployerCanUse", "Can Be Used by Deployers");
		REGISTRATE.addLang("item", CBCItems.RAM_ROD.getId(), "tooltip.deployerCanUse.yes", "_Yes_");
		REGISTRATE.addLang("item", CBCItems.RAM_ROD.getId(), "tooltip.deployerCanUse.no", "_No_");

		tooltip(CBCItems.WORM)
		.header("WORM")
		.summary("Used for _manually extracting munitions from a cannon_ instead of using the Cannon Loader Contraption. _Consumes saturation and hunger points._")
		.controlAndAction("R-Click on Cannon Block", "Pulls munition blocks towards the end of the cannon. Can only pull one block at a time.");

		REGISTRATE.addLang("item", CBCItems.WORM.getId(), "tooltip.reach", "Reach");
		REGISTRATE.addLang("item", CBCItems.WORM.getId(), "tooltip.reach.value", "Up to _%s blocks_ inside a cannon");
		REGISTRATE.addLang("item", CBCItems.WORM.getId(), "tooltip.deployerCanUse", "Can Be Used by Deployers");
		REGISTRATE.addLang("item", CBCItems.WORM.getId(), "tooltip.deployerCanUse.yes", "_Yes_");
		REGISTRATE.addLang("item", CBCItems.WORM.getId(), "tooltip.deployerCanUse.no", "_No_");

		tooltip(CBCItems.AP_AUTOCANNON_ROUND)
		.header("ARMOR PIERCING (AP) AUTOCANNON ROUND")
		.summary("_Strong penetrating force._ Best suited for _soft targets_ such as _wooden structures and thin armor._ _Cannot be fuzed and detonated._");

		tooltip(CBCItems.FLAK_AUTOCANNON_ROUND)
		.header("FLAK AUTOCANNON ROUND")
		.summary("Can be used to shoot out _airborne targets._ Peppers targets with _shrapnel._")
		.conditionAndBehavior("On Detonation", "Releases _shrapnel_ in its direction. The shrapnel _spreads out over a wide area._");

		tooltip(CBCItems.MACHINE_GUN_ROUND)
		.header("MACHINE GUN ROUND")
		.summary("_Cheaper_ autocannon ammunition that is more effective against _entities._");

		tooltip(CBCItems.TRACER_TIP)
		.header("TRACER TIP")
		.summary("_Illuminates_ autocannon rounds, making them visible.");

		REGISTRATE.addLang("tooltip", CreateBigCannons.resource("jei_info"), "added_fuze", "+ Fuze");
		REGISTRATE.addLang("tooltip", CreateBigCannons.resource("jei_info"), "added_power", "+ Power");
		REGISTRATE.addLang("tooltip", CreateBigCannons.resource("tracer"), "+ Tracer");

		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.materialProperties", "Cannon Properties");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.strength", "Strength");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.strength.goggles", "_%s Propellant Stress_");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.strength.unlimited", "Unlimited");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.cannonJamming", "Cannon Jamming");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.cannonJamming.goggles", "_At least %s m/s per barrel_ needed to prevent jamming");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.weightImpact", "Weight Impact");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.weightImpact.goggles", "_%sx RPM_");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.onFailure", "On Failure");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.onFailure.rupture", "The cannon _ruptures_ on failure.");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.onFailure.fragment", "The cannon _fragments_ on failure.");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.breechStrength", "Breech Strength");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.breechStrength.desc", "Cannons built with this as the closing can safely handle up to _%s Propellant Stress_.");

		REGISTRATE.addLang("block", CreateBigCannons.resource("autocannon"), "tooltip.materialProperties", "Autocannon Properties");
		REGISTRATE.addLang("block", CreateBigCannons.resource("autocannon"), "tooltip.maxBarrelLength", "Maximum Length");
		REGISTRATE.addLang("block", CreateBigCannons.resource("autocannon"), "tooltip.maxBarrelLength.goggles", "_%s blocks_ (including breech)");
		REGISTRATE.addLang("block", CreateBigCannons.resource("autocannon"), "tooltip.weightImpact", "Weight Impact");
		REGISTRATE.addLang("block", CreateBigCannons.resource("autocannon"), "tooltip.weightImpact.goggles", "_%sx RPM_");

		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_drill"), "tooltip.encounteredProblem", "The cannon drill has encountered a problem:");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_drill"), "tooltip.dryBore", "The cannon drill requires water to operate");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_drill"), "tooltip.tooWeak", "The cannon drill must be operating at the same RPM or faster as the target lathe");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_drill"), "tooltip.none", "The cannon drill did not fail, this should not be displaying");

		REGISTRATE.addLang("block", CreateBigCannons.resource("incomplete_block"), "tooltip.requiredParts", "This block requires the following parts:");

		REGISTRATE.addLang("recipe", CreateBigCannons.resource("melting"), "Basin Melting");
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("cannon_casting"), "Cannon Casting");
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("built_up_heating"), "Cannon Building");
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("drill_boring"), "Drill Boring");
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("incomplete_cannon_blocks"), "Incomplete Cannon Blocks");

		REGISTRATE.addLang("recipe", CreateBigCannons.resource("casting_time"), "Casting time: %ss");

		REGISTRATE.addLang("block", CreateBigCannons.resource("shell"), "tooltip.fuze", "Fuze:");
		REGISTRATE.addLang("block", CreateBigCannons.resource("shell"), "tooltip.fuze.none", "(none)");

		REGISTRATE.addLang("key", CreateBigCannons.resource("category"), "Create Big Cannons");
		REGISTRATE.addLang("key", CreateBigCannons.resource("pitch_mode"), "Change Carriage Mode");
		REGISTRATE.addLang("key", CreateBigCannons.resource("fire_controlled_cannon"), "Fire Controlled Cannon");

		REGISTRATE.addLang("debug", CreateBigCannons.resource("block_armor_info"), "Block Armor Info:");
		REGISTRATE.addLang("debug", CreateBigCannons.resource("block_toughness"), "Block Toughness: ");
		REGISTRATE.addLang("debug", CreateBigCannons.resource("block_hardness"), "Block Hardness: ");

		tooltip(CBCBlocks.POWDER_CHARGE)
		.header("POWDER CHARGE")
		.summary("Standard big cannon propellant.");

		tooltip(CBCBlocks.BIG_CARTRIDGE)
		.header("BIG CARTRIDGE")
		.summary("_Compact_ big cannon propellant that can be filled with _various levels_ of more _powerful propellant_. _Can only be used once_ in a shot, but can be _refilled_ afterwards.");

		REGISTRATE.addLang("block", CreateBigCannons.resource("propellant"), "tooltip.added_muzzle_velocity", "Added Muzzle Velocity");
		REGISTRATE.addLang("block", CreateBigCannons.resource("propellant"), "tooltip.added_muzzle_velocity.value", "_%s_ m/s");
		REGISTRATE.addLang("block", CreateBigCannons.resource("propellant"), "tooltip.added_stress", "Added Stress");
		REGISTRATE.addLang("block", CreateBigCannons.resource("propellant"), "tooltip.added_stress.value", "_%s_");
		REGISTRATE.addLang("block", CreateBigCannons.resource("propellant"), "tooltip.power", "Power");
		REGISTRATE.addLang("block", CreateBigCannons.resource("propellant"), "tooltip.power.value", "_%s_ / _%s_");

		for (Iterator<CannonCastShape> iter = CBCRegistries.cannonCastShapes().iterator(); iter.hasNext(); ) {
			ResourceLocation loc = CBCRegistries.cannonCastShapes().getKey(iter.next());
			if (!loc.getNamespace().equals(CreateBigCannons.MOD_ID)) continue;
			REGISTRATE.addLang("cast_shape", loc, RegistrateLangProvider.toEnglishName(loc.getPath()));
		}

		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".display_source.cannon_mount_source", "From Cannon Mount");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.no_cannon_present", "No Cannon present");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.yaw", "Cannon Yaw");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.pitch", "Cannon Pitch");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".display_source.cannon_mount.display_rotation_axis", "Display Rotation Axis");

		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".cannon_welder.click_to_confirm", "Click again to confirm");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".cannon_welder.invalid_weld", "Cannot weld these blocks");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".cannon_welder.too_far", "Cannot weld more than two blocks at a time");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".cannon_welder.click_to_discard", "Sneak-click to discard selection");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".cannon_welder.abort", "Selection discarded");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".cannon_welder.success", "Welding blocks...");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".cannon_welder.first_pos", "First position selected");

		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.no_cannon_present", "No Cannon present");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.cannon_strength", "Cannon Strength: ");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.cannon_strength.value", "%s Propellant Stress");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.pitch", "Cannon Pitch: ");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.yaw", "Cannon Yaw: ");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.autocannon_rate_of_fire", "Autocannon Rate of Fire: ");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".goggles.cannon_mount.autocannon_rate_of_fire.value", "%s RPM");

		REGISTRATE.addLang("block", CBCBlocks.AUTOCANNON_AMMO_CONTAINER.getId(), "tooltip.main_ammo", "x%1$s %2$s");
		REGISTRATE.addLang("block", CBCBlocks.AUTOCANNON_AMMO_CONTAINER.getId(), "tooltip.tracers", "Tracers: x%1$s %2$s");
		REGISTRATE.addLang("block", CBCBlocks.AUTOCANNON_AMMO_CONTAINER.getId(), "tooltip.tracer_spacing", "Tracer Spacing: 1 tracer every %s round(s)");

		tooltip(CBCItems.GAS_MASK)
		.header("GAS MASK")
		.summary("_Protects against gas clouds_ when worn on the head. _Does not protect_ against _lingering potions_, _dragon's breath_, or _potion fluid blobs_.");

		// TODO: only run if on Fabric env
		createEMITagTranslation(CBCTags.CBCItemTags.AUTOCANNON_AMMO_CONTAINERS);
		createEMITagTranslation(CBCTags.CBCItemTags.AUTOCANNON_CARTRIDGES);
		createEMITagTranslation(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS);
		createEMITagTranslation(CBCTags.CBCItemTags.BIG_CANNON_CARTRIDGES);
		createEMITagTranslation(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES);
		createEMITagTranslation(CBCTags.CBCItemTags.BIG_CANNON_PROPELLANT);
		createEMITagTranslation(CBCTags.CBCItemTags.BIG_CANNON_PROPELLANT_BAGS);
		createEMITagTranslation(CBCTags.CBCItemTags.BLOCK_BRONZE, "Bronze Blocks");
		createEMITagTranslation(CBCTags.CBCItemTags.BLOCK_CAST_IRON, "Cast Iron Blocks");
		createEMITagTranslation(CBCTags.CBCItemTags.BLOCK_NETHERSTEEL, "Nethersteel Blocks");
		createEMITagTranslation(CBCTags.CBCItemTags.BLOCK_STEEL, "Steel Blocks");
		createEMITagTranslation(CBCTags.CBCItemTags.CAN_BE_NITRATED, "Can be Nitrated");
		createEMITagTranslation(CBCTags.CBCItemTags.DUST_GLOWSTONE, "Glowstone Dusts");
		createEMITagTranslation(CBCTags.CBCItemTags.DUSTS_REDSTONE, "Redstone Dusts");
		createEMITagTranslation(CBCTags.CBCItemTags.FUZES);
		createEMITagTranslation(CBCTags.CBCItemTags.GELATINIZERS);
		createEMITagTranslation(CBCTags.CBCItemTags.GEMS_QUARTZ, "Quartz Gems");
		createEMITagTranslation(CBCTags.CBCItemTags.GUNCOTTON);
		createEMITagTranslation(CBCTags.CBCItemTags.GUNPOWDER);
		createEMITagTranslation(CBCTags.CBCItemTags.GUNPOWDER_PINCH, "Pinches of Gunpowder");
		createEMITagTranslation(CBCTags.CBCItemTags.HIGH_EXPLOSIVE_MATERIALS);
		createEMITagTranslation(CBCTags.CBCItemTags.IMPACT_FUZE_HEAD, "Impact Fuze Head Components");
		createEMITagTranslation(CBCTags.CBCItemTags.INEXPENSIVE_BIG_CARTRIDGE_SHEET, "Inexpensive Big Cartridge Sheets");
		createEMITagTranslation(CBCTags.CBCItemTags.INGOT_BRASS, "Brass Ingots");
		createEMITagTranslation(CBCTags.CBCItemTags.INGOT_BRONZE, "Bronze Ingots");
		createEMITagTranslation(CBCTags.CBCItemTags.INGOT_CAST_IRON, "Cast Iron Ingots");
		createEMITagTranslation(CBCTags.CBCItemTags.INGOT_IRON, "Iron Ingots");
		createEMITagTranslation(CBCTags.CBCItemTags.INGOT_NETHERSTEEL, "Nethersteel Ingots");
		createEMITagTranslation(CBCTags.CBCItemTags.INGOT_STEEL, "Steel Ingots");
		createEMITagTranslation(CBCTags.CBCItemTags.NITROPOWDER);
		createEMITagTranslation(CBCTags.CBCItemTags.NITRO_ACIDIFIERS);
		createEMITagTranslation(CBCTags.CBCItemTags.NUGGET_BRONZE, "Bronze Nuggets");
		createEMITagTranslation(CBCTags.CBCItemTags.NUGGET_CAST_IRON, "Cast Iron Nuggets");
		createEMITagTranslation(CBCTags.CBCItemTags.NUGGET_COPPER, "Copper Nuggets");
		createEMITagTranslation(CBCTags.CBCItemTags.NUGGET_IRON, "Iron Nuggets");
		createEMITagTranslation(CBCTags.CBCItemTags.NUGGET_NETHERSTEEL, "Nethersteel Nuggets");
		createEMITagTranslation(CBCTags.CBCItemTags.NUGGET_STEEL, "Steel Nuggets");
		createEMITagTranslation(CBCTags.CBCItemTags.SHEET_BRASS, "Brass Sheets");
		createEMITagTranslation(CBCTags.CBCItemTags.SHEET_COPPER, "Copper Sheets");
		createEMITagTranslation(CBCTags.CBCItemTags.SHEET_IRON, "Iron Sheets");
		createEMITagTranslation(CBCTags.CBCItemTags.SHEET_STEEL, "Steel Sheets");
		createEMITagTranslation(CBCTags.CBCItemTags.SPENT_AUTOCANNON_CASINGS);
		createEMITagTranslation(CBCTags.CBCItemTags.STONE);
		createEMITagTranslation(CBCTags.CBCItemTags.GAS_MASKS);
		createEMITagTranslation(fabricTag("cast_iron_ingots"));
		createEMITagTranslation(fabricTag("nethersteel_ingots"));
		createEMITagTranslation(fabricTag("bronze_nuggets"));
		createEMITagTranslation(fabricTag("cast_iron_nuggets"));
		createEMITagTranslation(fabricTag("nethersteel_nuggets"));
		createEMITagTranslation(fabricTag("steel_nuggets"));
		createEMITagTranslation(fabricTag("ingots/cast_iron"), "Cast Iron Ingots (Forge Format)");
		createEMITagTranslation(fabricTag("ingots/nethersteel"), "Nethersteel Ingots (Forge Format)");
		createEMITagTranslation(fabricTag("nuggets/bronze"), "Bronze Nuggets (Forge Format)");
		createEMITagTranslation(fabricTag("nuggets/cast_iron"), "Cast Iron Nuggets (Forge Format)");
		createEMITagTranslation(fabricTag("nuggets/nethersteel"), "Nethersteel Nuggets (Forge Format)");
		createEMITagTranslation(fabricTag("nuggets/steel"), "Steel Nuggets (Forge Format)");

		createEMICategoryTranslation("melting", "Basin Melting");
		createEMICategoryTranslation("cannon_casting");
		createEMICategoryTranslation("built_up_heating", "Cannon Building");
		createEMICategoryTranslation("drill_boring");
		createEMICategoryTranslation("incomplete_cannon_blocks");
	}

	private static void createEMITagTranslation(TagKey<?> tag, String enUS) {
		ResourceLocation loc = tag.location();
		//REGISTRATE.addRawLang("tag." + tag.registry().location().getPath() + "." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'), enUS);
		REGISTRATE.addRawLang("tag." + loc.getNamespace() + "." + loc.getPath().replace('/', '.'), enUS);
	}

	private static TagKey<Item> fabricTag(String loc) { return tag(CBCUtils.location("c", loc)); }

	private static void createEMITagTranslation(TagKey<?> tag) {
		createEMITagTranslation(tag, capitalizeAll(tag.location().getPath().replace('_', ' ')));
	}

	private static void createEMICategoryTranslation(String id, String enUS) {
		REGISTRATE.addRawLang("emi.category." + CreateBigCannons.MOD_ID + "." + id, enUS);
	}

	private static void createEMICategoryTranslation(String id) {
		createEMICategoryTranslation(id, capitalizeAll(id.replace('_', ' ')));
	}

	private static String capitalizeAll(String str) {
		return Arrays.stream(str.split(" ")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
	}

	private static class TooltipBuilder {
		private final ResourceLocation loc;
		private final String type;
		private int cbCount = 1;
		private int caCount = 1;
		public TooltipBuilder(ItemProviderEntry<?> provider, boolean item) {
			this.loc = provider.getId();
			this.type = item ? "item" : "block";
		}

		public TooltipBuilder header(String enUS) {
			REGISTRATE.addLang(this.type, this.loc, "tooltip", enUS);
			return this;
		}

		public TooltipBuilder summary(String enUS) {
			REGISTRATE.addLang(this.type, this.loc, "tooltip.summary", enUS);
			return this;
		}

		public TooltipBuilder conditionAndBehavior(String enUSCondition, String enUSBehaviour) {
			REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.condition%d", this.cbCount), enUSCondition);
			REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.behaviour%d", this.cbCount), enUSBehaviour);
			this.cbCount++;
			return this;
		}

		public TooltipBuilder controlAndAction(String enUSControl, String enUSAction) {
			REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.control%d", this.caCount), enUSControl);
			REGISTRATE.addLang(this.type, this.loc, String.format("tooltip.action%d", this.caCount), enUSAction);
			this.caCount++;
			return this;
		}
	}

	private static TooltipBuilder tooltip(BlockEntry<?> provider) {
		return new TooltipBuilder(provider, false);
	}

	private static TooltipBuilder tooltip(ItemEntry<?> provider) {
		return new TooltipBuilder(provider, true);
	}

}
