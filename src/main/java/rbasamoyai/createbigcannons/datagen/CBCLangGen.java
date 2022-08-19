package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;

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
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".gui.set_timed_fuze.time", "Fuze Time: %s s %s ticks");
		
		tooltip(CBCBlocks.SOLID_SHOT)
		.header("SOLID SHOT")
		.summary("_High penetrating force._ Best suited for _soft targets_ such as _wooden structures and thin walls_. _Cannot be fuzed and detonated._");
		
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
		
		tooltip(CBCItems.IMPACT_FUZE)
		.header("IMPACT FUZE")
		.summary("Detonates when the projectile _hits something_. Due to its _simplicity_, it does not always detonate on impact.")
		.conditionAndBehavior("Detonation", "The fuze _may_ detonate on _projectile impact_.");
		
		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.chance", "Impact Chance");
		REGISTRATE.addLang("item", CBCItems.IMPACT_FUZE.getId(), "tooltip.chance.value", "Upon impact this fuze has a _%s%%_ chance to detonate.");
		
		tooltip(CBCItems.TIMED_FUZE)
		.header("TIMED FUZE")
		.summary("Detonates after a _set time_ from launch.")
		.conditionAndBehavior("When R-Clicked", "Opens the _Set Timed Fuze_ menu, where the fuze duration can be set.")
		.conditionAndBehavior("Detonation", "The fuze detonates after the projectile has been in the world for the set time.");
		
		tooltip(CBCItems.CANNON_CAST_WAND)
		.header("CANNON CAST WAND")
		.summary("Use on cannon casts to _instantly complete_ the casting process.");
		
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
