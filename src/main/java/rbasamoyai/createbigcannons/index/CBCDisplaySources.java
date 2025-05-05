package rbasamoyai.createbigcannons.index;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;

import java.util.function.Supplier;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.tterrag.registrate.util.entry.RegistryEntry;

import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountDisplaySource;

public class CBCDisplaySources {

    public static final RegistryEntry<CannonMountDisplaySource> CANNON_MOUNT = simple("cannon_mount", CannonMountDisplaySource::new);

    public static void register() {}

    private static <T extends DisplaySource> RegistryEntry<T> simple(String name, Supplier<T> sup) {
        return REGISTRATE.displaySource(name, sup).register();
    }

}
