package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.foundation.ponder.PonderTag;

import rbasamoyai.createbigcannons.CBCBlocks;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCPonderTags {

	public static final PonderTag
	
	OPERATING_CANNONS = create("operating_cannons")
		.item(CBCBlocks.CANNON_MOUNT.get(), true, false)
		.defaultLang("Operating Cannons", "How to use big cannons safely and effectively")
		.addToIndex(),
	
	MUNITIONS = create("munitions")
		.item(CBCBlocks.SOLID_SHOT.get(), true, false)
		.defaultLang("Munitions", "Blocks that make up cannon loads, and what they can do")
		.addToIndex(),
	
	CANNON_CRAFTING = create("cannon_crafting")
		.item(CBCBlocks.CASTING_SAND.get(), true, false)
		.defaultLang("Cannon Crafting", "How to manufacture big cannons")
		.addToIndex();
	
	public static PonderTag create(String id) {
		return new PonderTag(CreateBigCannons.resource(id));
	}
	
	public static void register() {}
	
}
