package rbasamoyai.createbigcannons;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class CBCExpectPlatform {

    @ExpectPlatform public static String platformName() { throw new AssertionError(); }

}
