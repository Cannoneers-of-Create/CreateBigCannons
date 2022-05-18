package net.examplemod;

import com.simibubi.create.content.contraptions.relays.elementary.CogWheelBlock;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ExampleMod {
    public static final String MOD_ID = "examplemod";
    // registrate is actually busted so uhhhhh
//    public static final ExampleRegistrate REGISTRATE = new ExampleRegistrate(MOD_ID);
    public static final Registries REGISTRIES = Registries.get(MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);

    public static RegistrySupplier<Block> EXAMPLE_COG;

    public static void init() {
//        exampleRegistrate();
        exampleRegistries();
        exampleDeffered();
    }

//    public static void exampleRegistrate() {
//        REGISTRATE.block("example_shaft", ShaftBlock::new)
//                .initialProperties(SharedProperties::stone)
//                .transform(BlockStressDefaults.setNoImpact())
//                .transform(pickaxeOnly())
//                .blockstate(BlockStateGen.axisBlockProvider(false))
//                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
//                .simpleItem()
//                .register();
//        ExampleExpectPlatform.registerRegistrate(REGISTRATE);
//    }

    public static void exampleRegistries() {
       EXAMPLE_COG = REGISTRIES.get(Registry.BLOCK_REGISTRY).register(id("example_cog"), () -> CogWheelBlock.small(BlockBehaviour.Properties.copy(Blocks.STONE)));
    }

    public static void exampleDeffered() {
        ITEMS.register("example_cog_item", () -> new BlockItem(EXAMPLE_COG.get(), new Properties()));
        ITEMS.register();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
