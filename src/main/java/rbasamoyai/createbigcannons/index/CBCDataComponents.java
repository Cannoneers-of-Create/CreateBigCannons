package rbasamoyai.createbigcannons.index;

import java.util.function.UnaryOperator;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import rbasamoyai.createbigcannons.CreateBigCannons;

public class CBCDataComponents {
    public static final DataComponentType<Boolean> AUTOCANNON_TRACER = register(
        "autocannon_tracer",
        builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DataComponentType<Integer> TRACER_SPACING = register(
        "tracer_spacing",
        builder -> builder.persistent(ExtraCodecs.intRange(1,6)).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DataComponentType<Integer> CURRENT_INDEX = register(
        "current_index",
        builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DataComponentType<Integer> POWER = register(
        "power",
        builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DataComponentType<Integer> DAMAGE = register(
        "damage",
        builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DataComponentType<Boolean> DAMP = register(
        "damp",
        builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DataComponentType<ItemContainerContents> TRACER = register(
        "tracer",
        builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC)
    );

    public static final DataComponentType<ItemContainerContents> AMMO = register(
        "ammo",
        builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC)
    );

    public static final DataComponentType<ItemContainerContents> TRACERS = register(
        "tracers",
        builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC)
    );

    public static final DataComponentType<ItemContainerContents> FUZE = register(
        "fuze",
        builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC)
    );

    public static final DataComponentType<ItemContainerContents> PROJECTILE = register(
        "projectile",
        builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC)
    );

    public static final DataComponentType<Integer> DETONATION_DISTANCE = register(
        "detonation_distance",
        builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DataComponentType<Integer> FUZE_TIMER = register(
        "fuze_timer",
        builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DataComponentType<Integer> FUZE_DAMAGE = register(
        "fuze_damage",
        builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DataComponentType<Integer> AIR_TIME = register(
        "air_time",
        builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
    );

    public static final DataComponentType<Boolean> ARMED = register(
        "armed",
        builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DataComponentType<Boolean> ACTIVATED = register(
        "activated",
        builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    );

    public static final DataComponentType<CustomData> FLUID_CONTENT = register( // todo: CustomData for now, should be Fluid, hoping for Mojang to implement codecs
        "fluid_content",
        builder -> builder.persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC)
    );

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, CreateBigCannons.resource(name), builder.apply(DataComponentType.builder()).build());
    }

    public static void init() {}

}
