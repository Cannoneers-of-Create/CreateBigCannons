package net.examplemod.fabric;

import net.examplemod.fabriclike.ExampleModFabricLike;
import net.fabricmc.api.ModInitializer;

public class ExampleModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleModFabricLike.init();
    }
}
