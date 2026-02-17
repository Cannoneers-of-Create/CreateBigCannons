package rbasamoyai.createbigcannons.ponder;

import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;


public class CBCPonderTags {
    public static final ResourceLocation

    OPERATING_CANNONS = CreateBigCannons.resource("operating_cannons"),
    MUNITIONS = CreateBigCannons.resource("munitions"),
    CANNON_CRAFTING = CreateBigCannons.resource("cannon_crafting");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        helper.registerTag(OPERATING_CANNONS)
            .addToIndex()
            .item(CBCBlocks.CANNON_MOUNT.get(), true, false)
            .title("Operating Cannons")
            .description("How to use cannons safely and effectively")
            .register();

        helper.registerTag(MUNITIONS)
            .addToIndex()
            .item(CBCBlocks.SOLID_SHOT.get(), true, false)
            .title("Munitions")
            .description("Blocks and items used by cannons, and what they can do")
            .register();

        helper.registerTag(CANNON_CRAFTING)
            .addToIndex()
            .item(CBCBlocks.CASTING_SAND.get(), true, false)
            .title("Cannon Crafting")
            .description("How to manufacture cannons of different sizes and calibers")
            .register();

        HELPER.addToTag(OPERATING_CANNONS)
            .add(CBCBlocks.CANNON_MOUNT)
            .add(CBCBlocks.CANNON_MOUNT_EXTENSION)
            .add(CBCBlocks.FIXED_CANNON_MOUNT)
            .add(CBCBlocks.CANNON_LOADER)
            .add(CBCBlocks.RAM_HEAD)
            .add(CBCBlocks.WORM_HEAD)
            .add(AllBlocks.PISTON_EXTENSION_POLE)
            .add(CBCBlocks.CAST_IRON_SLIDING_BREECH)
            .add(CBCBlocks.CAST_IRON_QUICKFIRING_BREECH)
            .add(CBCBlocks.BRONZE_SLIDING_BREECH)
            .add(CBCBlocks.BRONZE_QUICKFIRING_BREECH)
            .add(CBCBlocks.STEEL_SLIDING_BREECH)
            .add(CBCBlocks.STEEL_QUICKFIRING_BREECH)
            .add(CBCBlocks.STEEL_SCREW_BREECH)
            .add(CBCBlocks.NETHERSTEEL_SCREW_BREECH)
            .add(CBCItems.QUICKFIRING_MECHANISM)
            .add(AllBlocks.MECHANICAL_ARM)
            .add(CBCItems.RAM_ROD)
            .add(CBCItems.WORM)
            .add(AllBlocks.MECHANICAL_PISTON)
            .add(AllBlocks.STICKY_MECHANICAL_PISTON)
            .add(AllBlocks.GANTRY_CARRIAGE)
            .add(AllBlocks.GANTRY_SHAFT)
            .add(AllBlocks.ROPE_PULLEY);

        HELPER.addToTag(MUNITIONS)
            .add(CBCBlocks.POWDER_CHARGE)
            .add(CBCBlocks.BIG_CARTRIDGE)
            .add(CBCBlocks.HE_SHELL)
            .add(CBCBlocks.SHRAPNEL_SHELL)
            .add(CBCBlocks.AP_SHELL)
            .add(CBCBlocks.SMOKE_SHELL)
            .add(CBCBlocks.FLUID_SHELL)
            .add(CBCBlocks.DROP_MORTAR_SHELL)
            .add(CBCItems.IMPACT_FUZE)
            .add(CBCItems.TIMED_FUZE)
            .add(CBCItems.PROXIMITY_FUZE)
            .add(CBCItems.DELAYED_IMPACT_FUZE)
            .add(CBCItems.WIRED_FUZE)
            .add(CBCItems.TRACER_TIP)
            .add(CBCItems.FLAK_AUTOCANNON_ROUND)
            .add(CBCBlocks.AUTOCANNON_AMMO_CONTAINER)
            .add(CBCBlocks.CREATIVE_AUTOCANNON_AMMO_CONTAINER);

        HELPER.addToTag(CANNON_CRAFTING)
            .add(CBCBlocks.BASIN_FOUNDRY_LID)
            .add(CBCBlocks.CASTING_SAND)
            .add(CBCBlocks.CANNON_DRILL)
            .add(CBCBlocks.CANNON_BUILDER)
            .add(AllBlocks.ENCASED_FAN)
            .add(CBCItems.CANNON_WELDER)
            .add(CBCBlocks.INCOMPLETE_CAST_IRON_SLIDING_BREECH)
            .add(CBCBlocks.INCOMPLETE_BRONZE_SLIDING_BREECH)
            .add(CBCBlocks.INCOMPLETE_STEEL_SLIDING_BREECH)
            .add(CBCBlocks.INCOMPLETE_STEEL_SCREW_BREECH)
            .add(CBCBlocks.INCOMPLETE_NETHERSTEEL_SCREW_BREECH)
            .add(CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_BREECH)
            .add(CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_BREECH)
            .add(CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_BREECH)
            .add(CBCBlocks.INCOMPLETE_CAST_IRON_AUTOCANNON_RECOIL_SPRING)
            .add(CBCBlocks.INCOMPLETE_BRONZE_AUTOCANNON_RECOIL_SPRING)
            .add(CBCBlocks.INCOMPLETE_STEEL_AUTOCANNON_RECOIL_SPRING);
    }
}
