package rbasamoyai.createbigcannons.datagen;

import com.simibubi.create.foundation.data.CreateRegistrate;

import rbasamoyai.createbigcannons.CreateBigCannons;

public class LangGen {

	public static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate();
	
	public static void prepare() {
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonBlockOutsideOfWorld", "Cannon assembly area at [%1$s, %2$s, %3$s] is out of bounds");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonTooLarge", "Cannon is longer than the maximum length of %s");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "invalidCannon", "A cannon must have one and only one opening and one and only one closed end");
		REGISTRATE.addLang("exception", CreateBigCannons.resource("cannon_mount"), "cannonLoaderInsideDuringAssembly", "Cannon block at [%1$s, %2$s, %3$s] contains a cannon loader part");
	}
	
}
