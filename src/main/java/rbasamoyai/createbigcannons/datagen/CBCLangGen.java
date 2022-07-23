package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.ponder.PonderLocalization;

import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCLangGen {

	public static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate();
	
	public static void prepare() {
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonBlockOutsideOfWorld", "Cannon assembly area at [%s, %s, %s] is out of bounds");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonTooLarge", "Cannon is longer than the maximum length of %s");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "invalidCannon", "A cannon must have one and only one opening and one and only one closed end");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonLoaderInsideDuringAssembly", "Cannon block at [%s, %s, %s] contains a cannon loader part");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("shrapnel"), "%s was ripped up by shrapnel");
		REGISTRATE.addLang("death.attack", CreateBigCannons.resource("shrapnel"), "player", "%s was ripped up by shrapnel");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".gui.set_timed_fuze.time", "Fuze Time: %s s %s ticks");
		
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/assembly_and_use.header", "Assembly and Use");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/assembly_and_use.text_1", "Big cannons need to be placed on cannon mounts to be assembled.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/assembly_and_use.text_2", "To assemble a cannon, power the hammer face with a redstone signal.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/assembly_and_use.text_3", "Power the cannon mount to aim the cannon up and down.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/assembly_and_use.text_4", "A yaw controller is needed to aim the cannon left and right.");
		PonderLocalization.provideRegistrateLang(REGISTRATE);
	}
	
}
