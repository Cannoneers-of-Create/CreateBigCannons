package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CBCItems;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCLangGen {

	public static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate();
	
	public static void prepare() {
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonBlockOutsideOfWorld", "Cannon assembly area at [%s, %s, %s] is out of bounds");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonTooLarge", "Cannon is longer than the maximum length of %s");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "invalidCannon", "A cannon must have one and only one opening and one and only one closed end");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonLoaderInsideDuringAssembly", "Cannon block at [%s, %s, %s] contains a cannon loader part");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "hasIncompleteCannonBlocks", "Cannon block at [%s, %s, %s] has not finished the crafting process");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("shrapnel"), "%s was ripped up by shrapnel");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("shrapnel"), "player", "%s was ripped up by shrapnel");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("grapeshot"), "%s was blown out by grapeshot");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("grapeshot"), "player", "%s was blown out by grapeshot");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".gui.set_timed_fuze.time", "Fuze Time: %ss %s ticks");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".gui.set_proximity_fuze.distance", "Detonation Distance: %s blocks");
		
		tooltip(CBCBlocks.SOLID_SHOT)
		.header("SOLID SHOT")
		.summary("_High penetrating force._ Best suited for _soft targets_ such as _wooden structures and thin walls_. _Cannot be fuzed and detonated._");

		tooltip(CBCBlocks.MORTAR_STONE)
		.header("MORTAR STONE")
		.summary("Powerful stone that _explodes on impact._ _Flies further_ than other projectiles. Good for attacking _walls and fortifications._ _Cannot be fuzed and detonated._ Will _break_ if too many Powder Charges are used.");

		REGISTRATE.addLang("block", CBCBlocks.MORTAR_STONE.getId(), "tooltip.maximumCharges", "Maximum Powder Charges");
		REGISTRATE.addLang("block", CBCBlocks.MORTAR_STONE.getId(), "tooltip.maximumCharges.value", "This mortar stone can handle up to _%s Powder Charges_ (or equivalent) before breaking.");
		
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
		
		tooltip(CBCBlocks.AP_SHELL)
		.header("ARMOR PIERCING (AP) SHELL")
		.summary("Can effectively _pierce through blocks_, detonating inside protected structures.")
		.conditionAndBehavior("On Detonation", "Explodes.");
		
		tooltip(CBCBlocks.FLUID_SHELL)
		.header("FLUID SHELL")
		.summary("Spreads the contained fluid all over the targeted area, with _different effects depending on the fluid_.")
		.conditionAndBehavior("On Detonation", "Releases its contents.")
		.conditionAndBehavior("Filling", "The shell can only be filled through _the same face that the fuze is placed on._ If a fuze is present, the shell _cannot be filled._")
		.conditionAndBehavior("Note", "Some fluids may not have any effect on release. Supported fluids include, but may not be limited to, _water, lava, and liquid potions._");
		
		tooltip(CBCItems.IMPACT_FUZE)
		.header("IMPACT FUZE")
		.summary("Detonates when the projectile _hits something_. Due to its _simplicity_, it does not always detonate on impact.")
		.conditionAndBehavior("Detonation", "The fuze _may_ detonate on _projectile impact_.");
		
		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.chance", "Impact Chance");
		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.chance.value", "Upon impact this fuze has a _%s%%_ chance to detonate.");
		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.shell_info", "Impact Chance: _%s%%_");
		
		tooltip(CBCItems.TIMED_FUZE)
		.header("TIMED FUZE")
		.summary("Detonates after a _set time_ from launch.")
		.conditionAndBehavior("When R-Clicked", "Opens the _Set Timed Fuze_ menu, where the fuze duration can be set.")
		.conditionAndBehavior("Detonation", "The fuze detonates after the projectile has been in the world for the set time.");
		
		REGISTRATE.addLang("item", CBCItems.TIMED_FUZE.getId(), "tooltip.shell_info", "Time to Detonate: _%ss %s ticks_");
		
		tooltip(CBCItems.PROXIMITY_FUZE)
		.header("PROXIMITY FUZE")
		.summary("Detonates when it _gets close_ to a block.")
		.conditionAndBehavior("When R-Clicked", "Opens the _Set Proximity Fuze_ menu, where the detonation distance can be set.")
		.conditionAndBehavior("Detonation", "The fuze detonates after the projectile gets is within the set range of a block.");
		
		REGISTRATE.addLang("item", CBCItems.PROXIMITY_FUZE.getId(), "tooltip.shell_info", "Detonation Distance: _%s blocks_");
		
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
		.summary("_Mobile_ cannon mount. Can be _moved around_, but _cannot be automated_ like the Cannon Mount.");

		REGISTRATE.addLang("block", CBCBlocks.CANNON_CARRIAGE.getId(), "tooltip.keyPressed", "When [_%s_] held down");
		REGISTRATE.addLang("block", CBCBlocks.CANNON_CARRIAGE.getId(), "tooltip.fireCannon", "_Fires cannon_ on carriage if present. _Repeatedly fires cannon_ if possible.");
		REGISTRATE.addLang("block", CBCBlocks.CANNON_CARRIAGE.getId(), "tooltip.pitchMode", "The forward/backward keys _set pitch_ instead of moving the cannon.");

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
		
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.materialProperties", "Cannon Properties");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.strength", "Strength");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.strength.goggles", "_%s Powder Charge(s)_");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.squibRatio", "Squib Ratio");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.squibRatio.goggles", "_%s barrel(s) travelled_ to _%s Powder Charge(s)_");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.weightImpact", "Weight Impact");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.weightImpact.goggles", "_%sx RPM_");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.onFailure", "On Failure");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.onFailure.rupture", "The cannon _ruptures_ on failure.");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.onFailure.fragment", "The cannon _fragments_ on failure.");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.weakCannonEnd", "Weak Cannon End");
		REGISTRATE.addLang("block", CreateBigCannons.resource("cannon"), "tooltip.weakCannonEnd.desc", "Cannons built with this as the closing can only safely handle up to _%s Powder Charge(s)_.");
		
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_drill"), "tooltip.encounteredProblem", "The cannon drill has encountered a problem:");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_drill"), "tooltip.dryBore", "The cannon drill requires water to operate");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_drill"), "tooltip.tooWeak", "The cannon drill must be operating at the same RPM or faster as the target lathe");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_drill"), "tooltip.none", "The cannon drill did not fail, this should not be displaying");
		
		REGISTRATE.addLang("block", CreateBigCannons.resource("incomplete_block"), "tooltip.requiredParts", "This block requires the following parts:");
		
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("melting"), "Basin Melting");
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("cannon_casting"), "Cannon Casting");
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("built_up_heating"), "Cannon Building");
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("cannon_boring"), "Cannon Boring");
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("incomplete_cannon_blocks"), "Incomplete Cannon Blocks");
		
		REGISTRATE.addLang("recipe", CreateBigCannons.resource("added_casting_time"), "Added casting time: %ss");
		
		REGISTRATE.addLang("block", CreateBigCannons.resource("shell"), "tooltip.fuze", "Fuze:");
		REGISTRATE.addLang("block", CreateBigCannons.resource("shell"), "tooltip.fuze.none", "(none)");

		REGISTRATE.addLang("key", CreateBigCannons.resource("category"), "Create Big Cannons");
		REGISTRATE.addLang("key", CreateBigCannons.resource("pitch_mode"), "Change Carriage Mode");
		REGISTRATE.addLang("key", CreateBigCannons.resource("fire_controlled_cannon"), "Fire Controlled Cannon");
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
