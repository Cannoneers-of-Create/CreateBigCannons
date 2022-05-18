package net.examplemod;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class ExampleExpectPlatform {
    /**
     * an example of {@link ExpectPlatform}.
     * <p>
     * This must be a <b>public static</b> method. The platform-implemented solution must be placed under a
     * platform sub-package, with its class suffixed with {@code Impl}.
     * <p>
     * Example:
     * Expect: net.examplemod.ExampleExpectPlatform#makeCreateRegistrate()
     * Actual Fabric: net.examplemod.fabric.ExampleExpectPlatformImpl#makeCreateRegistrate()
     * Actual Forge: net.examplemod.forge.ExampleExpectPlatformImpl#makeCreateRegistrate()
     * <p>
     * <a href="https://plugins.jetbrains.com/plugin/16210-architectury">You should also get the IntelliJ plugin to help with @ExpectPlatform.</a>
     */
    @ExpectPlatform
    public static Path configDir() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }
}
