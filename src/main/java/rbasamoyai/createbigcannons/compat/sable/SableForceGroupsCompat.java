package rbasamoyai.createbigcannons.compat.sable;

import dev.ryanhcode.sable.api.physics.force.ForceGroup;
import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class SableForceGroupsCompat {
    public static final DeferredRegister<ForceGroup> FORCE_GROUPS =
        DeferredRegister.create(ForceGroups.REGISTRY_KEY, CreateBigCannons.MOD_ID);

    public static final DeferredHolder<ForceGroup, ForceGroup> RECOIL = FORCE_GROUPS.register(
        "recoil",
        () -> new ForceGroup(
            Component.translatable("force_group.createbigcannons.recoil_force"),
            null,
            0x504550,
            true
        )
    );

    public static final DeferredHolder<ForceGroup, ForceGroup> IMPACT = FORCE_GROUPS.register(
        "impact",
        () -> new ForceGroup(
            Component.translatable("force_group.createbigcannons.impact_force"),
            null,
            0x808080,
            true
        )
    );

    public static void init(IEventBus modEventBus) {
        FORCE_GROUPS.register(modEventBus);
    }
}
