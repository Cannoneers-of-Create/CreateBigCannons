package rbasamoyai.createbigcannons.config;

public class CBCCfgServer extends CBCConfigBase {
	
	public final CBCCfgFailure failure = nested(0, CBCCfgFailure::new, Comments.failure);
	public final CBCCfgMunitions munitions = nested(0, CBCCfgMunitions::new, Comments.munitions);
	public final CBCCfgCannons cannons = nested(0, CBCCfgCannons::new, Comments.cannons);
	public final CBCCfgKinetics kinetics = nested(0, CBCCfgKinetics::new, Comments.kinetics);
	public final CBCCfgCrafting crafting = nested(0, CBCCfgCrafting::new, Comments.crafting);
	
	@Override public String getName() { return "server"; }

	private static class Comments {
		static String failure = "These values affect the extent of cannon failure.";
		static String munitions = "These values affect the characteristics of cannon munitions.";
		static String cannons = "These values affect the characteristics of cannon materials and cannon structures";
		static String kinetics = "These values affect various miscellaneous contraptions.";
		static String crafting = "These values affect cannon crafting properties.";
	}
	
}
