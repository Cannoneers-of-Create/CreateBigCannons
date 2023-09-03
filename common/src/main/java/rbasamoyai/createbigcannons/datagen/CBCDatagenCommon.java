package rbasamoyai.createbigcannons.datagen;

public class CBCDatagenCommon {

	public static final String PLATFORM = System.getProperty("createbigcannons.datagen.platform", "fabric");
	public static final int FLUID_MULTIPLIER = PLATFORM.equals("fabric") ? 81 : 1;

	public static void init() {}

}
