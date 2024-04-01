package rbasamoyai.createbigcannons.datagen;

public class CBCDatagenCommon {

	public static final CBCDatagenPlatform PLATFORM = CBCDatagenPlatform.getPlatform(System.getProperty("createbigcannons.datagen.platform"));
	public static final int FLUID_MULTIPLIER = PLATFORM.fluidMultiplier();

	public static void init() {}

}
