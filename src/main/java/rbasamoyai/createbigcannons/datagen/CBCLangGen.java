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
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/assembly_and_use.text_3", "Power the Cannon Mount to aim the cannon up and down.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/assembly_and_use.text_4", "A Yaw Controller is needed to aim the cannon left and right.");
		
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/firing_big_cannons.header", "Firing Big Cannons");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/firing_big_cannons.text_1", "After loading, assembling, and aiming the cannon, big cannons are ready to fire.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/firing_big_cannons.text_2", "Power the lit cannon face on the cannon mount to fire the mounted cannon.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/firing_big_cannons.text_3", "To disassemble the cannon to prepare it for the next shot, unpower the hammer face.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_mount/firing_big_cannons.text_4", "The cannon instantly snaps back to the position it was assembled at when disassembled.");
		
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_loader/loading_big_cannons.header", "Loading Big Cannons");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_loader/loading_big_cannons.text_1", "Cannon Loaders work like Mechanical Pistons, using Piston Extension Poles to extend their range.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_loader/loading_big_cannons.text_2", "They must be outfitted with a head to be useful, such as a Ram Head or a Worm Head.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_loader/loading_big_cannons.text_3", "Under normal circumstances, the Ram Head should be used.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_loader/loading_big_cannons.text_4", "However, the Worm Head can be attached if the cannon needs to be unjammed.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_loader/loading_big_cannons.text_5", "Before reloading a cannon, it must be disassembled.");
		REGISTRATE.addRawLang(CreateBigCannons.MOD_ID + ".ponder.cannon_loader/loading_big_cannons.text_6", "When loading a cannon, the loader mechanism, munitions, and cannon must all be in line on the same axis.");
		PonderLocalization.provideRegistrateLang(REGISTRATE);
	}
	
}
