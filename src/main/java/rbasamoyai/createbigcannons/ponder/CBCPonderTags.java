package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.foundation.ponder.PonderTag;

import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCPonderTags {

	public static final PonderTag
	
	OPERATING_CANNONS = create("operating_cannons")
		.item(CBCBlocks.SOLID_SHOT.get(), true, false)
		.defaultLang("Operating Cannons", "How to use big cannons safely and effectively")
		.addToIndex();
	
	public static PonderTag create(String id) {
		return new PonderTag(CreateBigCannons.resource(id));
	}
	
	public static void register() {}
	
}
